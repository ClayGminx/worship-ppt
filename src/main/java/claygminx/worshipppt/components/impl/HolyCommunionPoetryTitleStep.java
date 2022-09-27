package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.entity.PoetryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.*;

import java.util.List;

/**
 * 圣餐诗歌标题阶段
 */
public class HolyCommunionPoetryTitleStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(HolyCommunionPoetryTitleStep.class);

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

        logger.info("圣餐诗歌标题幻灯片制作完成");
    }
}
