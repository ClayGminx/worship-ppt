package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.common.entity.ScriptureEntity;
import claygminx.worshipppt.components.ScriptureService;
import claygminx.worshipppt.exception.ScriptureNumberException;
import claygminx.worshipppt.exception.WorshipStepException;
import claygminx.worshipppt.common.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;

/**
 * 读经阶段
 */
public class ReadingScriptureStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(ReadingScriptureStep.class);

    private final static int BEST_LINE_COUNT = 4;
    private final static int BEST_HEIGHT = 207;
    private final static int MAX_CHAR_COUNT = 32;

    private final ScriptureService scriptureService;
    private final String scriptureNumber;

    public ReadingScriptureStep(XMLSlideShow ppt, String layout, ScriptureService scriptureService, String scriptureNumber) {
        super(ppt, layout);
        this.scriptureService = scriptureService;
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public void execute() throws WorshipStepException {
        logger.info("开始读经" + scriptureNumber);
        ScriptureEntity scriptureEntity;
        try {
            scriptureEntity = scriptureService.getScriptureWithFormat(scriptureNumber, SystemConfig.getString(Dict.ScriptureProperty.FORMAT4));
        } catch (ScriptureNumberException e) {
            throw new WorshipStepException("解析经文编号 [" + scriptureNumber + "] 时出错！", e);
        }
        if (scriptureEntity == null) {
            return;
        }
        String scripture = scriptureEntity.getScripture();
        String[] scriptureArray = scripture.replaceAll("\r", "").split("\n");
        logger.info("共{}段经文", scriptureArray.length);

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());

        // 按预设的读经模板，每一行最多32个中文字符，每一页行数最佳是4行，尽量控制最多6行
        XSLFSlide slide = null;
        XSLFTextShape placeholder;
        int lineCount = 0, slideCount = 0;
        for (int i = 0; i < scriptureArray.length; i++) {
            String scriptureItem = scriptureArray[i];
            if (lineCount == 0) {
                slideCount++;
                slide = ppt.createSlide(layout);
                placeholder = slide.getPlaceholder(0);
                String text = placeholder.getText();
                placeholder.setText(text.replace(getCustomPlaceholder(), scriptureNumber));

                placeholder = slide.getPlaceholder(1);
                placeholder.clearText();
                logger.info("开始第{}张读经幻灯片...", slideCount);
            }

            // 往正文添加一段经文，注意，可能因为经文字数较多，一行容不下
            placeholder = slide.getPlaceholder(1);
            XSLFTextParagraph paragraph = placeholder.addNewTextParagraph();
            useCustomLanguage(paragraph);
            XSLFTextRun textRun = paragraph.addNewTextRun();
            String trimScriptureItem = scriptureItem.trim();
            textRun.setText(trimScriptureItem);

            // 不同的朗读者字体颜色不一样
            if (trimScriptureItem.startsWith("会众：")) {
                textRun.setFontColor(Color.BLUE);
            } else if (trimScriptureItem.startsWith("主领：")) {
                textRun.setFontColor(Color.BLACK);
            } else {
                textRun.setFontColor(new Color(208, 15, 15));
            }

            // 控制幻灯片里经文的数量和幻灯片的数量
            int currentHeight = (int) Math.ceil(placeholder.getTextHeight());// 当前文本框的高度
            int n = (int) Math.ceil((double) trimScriptureItem.length() / MAX_CHAR_COUNT);// 此节经文可能展示为多少行
            lineCount += n;
            if (currentHeight >= BEST_HEIGHT || lineCount >= BEST_LINE_COUNT) {
                if (i < scriptureArray.length - 2) {
                    logger.debug("当前幻灯片有{}个段落，应该有{}行", placeholder.getTextParagraphs().size(), lineCount);
                    lineCount = 0;
                }
            }
        }
    }

}
