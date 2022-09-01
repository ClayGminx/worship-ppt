package claygminx.components.impl;

import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.components.ScriptureService;
import claygminx.exception.ScriptureNumberException;
import claygminx.util.ScriptureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.*;

import java.util.List;

/**
 * 赦罪经文阶段
 */
public class ForgiveSinsScriptureStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(ForgiveSinsScriptureStep.class);

    private final String scriptureNumber;

    private final ScriptureService scriptureService;

    public ForgiveSinsScriptureStep(XMLSlideShow ppt, String layout, ScriptureService scriptureService, String scriptureNumber) {
        super(ppt, layout);
        this.scriptureService = scriptureService;
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public void execute() throws ScriptureNumberException {
        List<ScriptureNumberEntity> scriptureNumberList = ScriptureUtil.parseNumbers(scriptureNumber);
        String[] titleAndScripture = getTitleAndScripture(scriptureService, scriptureNumberList);

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(titleAndScripture[0]);

        placeholder = slide.getPlaceholder(1);
        List<XSLFTextParagraph> paragraphs = placeholder.getTextParagraphs();
        for (XSLFTextParagraph paragraph : paragraphs) {
            List<XSLFTextRun> textRuns = paragraph.getTextRuns();
            for (XSLFTextRun textRun : textRuns) {
                String rawText = textRun.getRawText();
                if (rawText != null && rawText.contains(getCustomPlaceholder())) {
                    textRun.setText(rawText.replace(getCustomPlaceholder(), titleAndScripture[1]));
                    break;
                }
            }
        }

        logger.info("认罪经文幻灯片制作完成");
    }
}
