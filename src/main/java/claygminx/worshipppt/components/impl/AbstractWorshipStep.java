package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.common.entity.ScriptureEntity;
import claygminx.worshipppt.common.entity.ScriptureNumberEntity;
import claygminx.worshipppt.components.ScriptureService;
import claygminx.worshipppt.components.WorshipStep;
import claygminx.worshipppt.exception.ScriptureServiceException;
import claygminx.worshipppt.common.Dict;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 通用的敬拜阶段抽象类
 */
public abstract class AbstractWorshipStep implements WorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(AbstractWorshipStep.class);

    private final XMLSlideShow ppt;
    private final String layout;

    /**
     * 通用的构造器
     * @param ppt PPT对象
     * @param layout 幻灯片母版中版式的布局名称
     */
    public AbstractWorshipStep(XMLSlideShow ppt, String layout) {
        this.ppt = ppt;
        this.layout = layout;
    }

    public XMLSlideShow getPpt() {
        return ppt;
    }

    public String getLayout() {
        return layout;
    }

    /**
     * 获取自定义的占位符
     * <p>制作幻灯片时，你可以将此占位符替换为其它字符串。</p>
     * @return 文本框中的占位符
     */
    public String getCustomPlaceholder() {
        return SystemConfig.getString(Dict.PPTProperty.GENERAL_PLACEHOLDER);
    }

    public String getFontFamily() {
        return SystemConfig.getString(Dict.PPTProperty.GENERAL_FONT_FAMILY);
    }

    /**
     * 获取标题和经文
     * @param scriptureService 经文服务对象
     * @param scriptureNumberList 经文编号实体对象列表
     * @return 数组。第一个元素时经文编号，作为标题；第二个元素是经文，作为内容。
     */
    protected String[] getTitleAndScripture(ScriptureService scriptureService, List<ScriptureNumberEntity> scriptureNumberList) {
        StringBuilder titleBuilder = new StringBuilder();
        StringBuilder scriptureBuilder = new StringBuilder();
        if (logger.isDebugEnabled()) {
            logger.debug("{}个经文编号实体对象", scriptureNumberList.size());
        }
        for (ScriptureNumberEntity scriptureNumberEntity : scriptureNumberList) {
            if (logger.isDebugEnabled()) {
                logger.debug(scriptureNumberEntity.toString());
            }
            boolean validateResult = scriptureService.validateNumber(scriptureNumberEntity);
            if (!validateResult) {
                throw new ScriptureServiceException("经文编号格式错误！");
            }
            titleBuilder.append('【').append(scriptureNumberEntity.getValue()).append('】');
            ScriptureEntity scriptureEntity = scriptureService.getScriptureWithFormat(scriptureNumberEntity, SystemConfig.getString(Dict.ScriptureProperty.FORMAT1));
            if (logger.isDebugEnabled()) {
                logger.debug(scriptureEntity.toString());
            }
            scriptureBuilder.append("　　").append(scriptureEntity.getScripture()).append('\n');
        }
        scriptureBuilder.setLength(scriptureBuilder.length() - 1);
        char lastChar = scriptureBuilder.charAt(scriptureBuilder.length() - 1);
        if (lastChar == '，' || lastChar == '；') {
            scriptureBuilder.setCharAt(scriptureBuilder.length() - 1, '。');
        }

        return new String[] {titleBuilder.toString(), scriptureBuilder.toString()};
    }

    /**
     * 填充标题和经文
     * @param layoutName 幻灯片母版中版式的布局名称
     * @param scriptureService 经文服务对象
     * @param scriptureNumberList 经文编号对象列表
     */
    protected void fillTitleAndScripture(String layoutName, ScriptureService scriptureService, List<ScriptureNumberEntity> scriptureNumberList) {
        String[] titleAndScripture = getTitleAndScripture(scriptureService, scriptureNumberList);

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(layoutName);
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(titleAndScripture[0]);
        if (logger.isDebugEnabled()) {
            logger.debug("填充标题：" + titleAndScripture[0]);
        }

        placeholder = slide.getPlaceholder(1);
        placeholder.clearText();
        placeholder.setText(titleAndScripture[1]);
        List<XSLFTextParagraph> paragraphs = placeholder.getTextParagraphs();
        for (XSLFTextParagraph paragraph : paragraphs) {
            useCustomLanguage(paragraph);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("填充经文：" + titleAndScripture[1]);
        }
    }

    /**
     * 使用自定义语言
     * @param paragraph 段落
     */
    protected void useCustomLanguage(XSLFTextParagraph paragraph) {
        CTTextParagraph xmlObject = paragraph.getXmlObject();
        CTTextParagraphProperties pPr = xmlObject.getPPr();
        if (pPr == null) {
            pPr = xmlObject.addNewPPr();
        }

        // 按中文习惯控制首尾字符
        pPr.setEaLnBrk(true);

        // 允许标点溢出边界
        pPr.setHangingPunct(true);

        CTTextCharacterProperties defRPr = pPr.getDefRPr();
        if (defRPr == null) {
            defRPr = pPr.addNewDefRPr();
        }
        // 设置语言
        String lang = SystemConfig.getString(Dict.PPTProperty.GENERAL_LANGUAGE);
        defRPr.setLang(lang);
        defRPr.setAltLang(lang);

        CTTextCharacterProperties endParaRPr = xmlObject.getEndParaRPr();
        if (endParaRPr == null) {
            endParaRPr = xmlObject.addNewEndParaRPr();
        }
        // 设置语言
        endParaRPr.setLang(lang);
        endParaRPr.setAltLang(lang);
    }
}
