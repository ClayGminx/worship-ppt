package claygminx.components.impl;

import claygminx.common.entity.PoetryEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.*;

import java.util.List;

/**
 * 圣餐诗歌标题阶段
 */
public class HolyCommunionPoetryTitleStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(HolyCommunionPoetryTitleStep.class);

    private final List<PoetryEntity> poetryList;

    public HolyCommunionPoetryTitleStep(XMLSlideShow ppt, String layout, List<PoetryEntity> poetryList) {
        super(ppt, layout);
        this.poetryList = poetryList;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());

        label1:
        for (PoetryEntity poetryEntity : poetryList) {
            XSLFSlide slide = ppt.createSlide(layout);
            XSLFTextShape placeholder = slide.getPlaceholder(0);
            List<XSLFTextParagraph> paragraphs = placeholder.getTextParagraphs();
            for (XSLFTextParagraph paragraph : paragraphs) {
                List<XSLFTextRun> textRuns = paragraph.getTextRuns();
                for (XSLFTextRun textRun : textRuns) {
                    String rawText = textRun.getRawText();
                    if (rawText.contains(getCustomPlaceholder())) {
                        textRun.setText(rawText.replace(getCustomPlaceholder(), poetryEntity.getName()));
                        continue label1;
                    }
                }
            }
        }

    }
}
