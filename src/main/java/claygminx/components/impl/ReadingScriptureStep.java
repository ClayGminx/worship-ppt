package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.ScriptureEntity;
import claygminx.components.ScriptureService;
import claygminx.exception.ScriptureNumberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;

import static claygminx.common.Dict.General.*;

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
    public void execute() throws ScriptureNumberException {
        logger.info("开始读经" + scriptureNumber);
        ScriptureEntity scriptureEntity = scriptureService.getScriptureWithFormat(
                scriptureNumber, SystemConfig.getString(SCRIPTURE_FORMAT4));
        if (scriptureEntity == null) {
            return;
        }
        String scripture = scriptureEntity.getScripture();
        String[] scriptureArray = scripture.split("\n");
        logger.info("共{}行", scriptureArray.length);

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());

        // 每一行最多31个中文字符，每页最多6行
        XSLFSlide slide = null;
        int lineCount = 0, slideCount = 0;
        for (int i = 0; i < scriptureArray.length; i++) {
            String scriptureItem = scriptureArray[i];
            if (lineCount == 0) {
                slideCount++;
                slide = ppt.createSlide(layout);
                XSLFTextShape titlePlaceholder = slide.getPlaceholder(0);
                String text = titlePlaceholder.getText();
                titlePlaceholder.setText(text.replace(getCustomPlaceholder(), scriptureNumber));

                XSLFTextShape contentPlaceholder = slide.getPlaceholder(1);
                contentPlaceholder.clearText();
                logger.info("读经第{}张幻灯片", slideCount);
            }

            XSLFTextShape contentPlaceholder = slide.getPlaceholder(1);
            XSLFTextParagraph paragraph = contentPlaceholder.addNewTextParagraph();
            XSLFTextRun textRun = paragraph.addNewTextRun();
            String trimScriptureItem = scriptureItem.trim();
            textRun.setText(trimScriptureItem);

            if (trimScriptureItem.startsWith("会众：")) {
                textRun.setFontColor(Color.BLUE);
            } else if (trimScriptureItem.startsWith("主领：")) {
                textRun.setFontColor(Color.BLACK);
            } else {
                textRun.setFontColor(new Color(208, 15, 15));
            }

            /*
             * 控制幻灯片的里经文的数量
             * -------------------
             * 最佳段落数量是4行，文本框高度为206.82265663146973，也就是7.44厘米，
             * 允许再多一些经文，最好不要超过6行，文本框高度最大为312.68603827073，
             * 幻灯片里至少2节经文。
             */
            if (i < scriptureArray.length - 2) {
                int currentHeight = (int) Math.ceil(contentPlaceholder.getTextHeight());
                int n = (int) Math.ceil((double) trimScriptureItem.length() / MAX_CHAR_COUNT);
                lineCount += n;
                if (currentHeight >= BEST_HEIGHT || lineCount >= BEST_LINE_COUNT) {
                    logger.debug("当前幻灯片有{}个段落，应该有{}行", contentPlaceholder.getTextParagraphs().size(), lineCount);
                    lineCount = 0;
                }
            }
        }
    }

}
