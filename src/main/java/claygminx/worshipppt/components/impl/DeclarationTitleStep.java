package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.entity.DeclarationEntity;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 宣信标题阶段
 */
public class DeclarationTitleStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(DeclarationTitleStep.class);

    private final DeclarationEntity declarationEntity;

    public DeclarationTitleStep(XMLSlideShow ppt, String layout, DeclarationEntity declarationEntity) {
        super(ppt, layout);
        this.declarationEntity = declarationEntity;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        List<XSLFTextParagraph> paragraphs = placeholder.getTextParagraphs();
        int index = 0;
        for (XSLFTextParagraph paragraph : paragraphs) {
            List<XSLFTextRun> textRuns = paragraph.getTextRuns();
            for (XSLFTextRun textRun : textRuns) {
                if (textRun.getRawText().contains(getCustomPlaceholder())) {
                    if (index == 0) {
                        textRun.setText(textRun.getRawText().replace(getCustomPlaceholder(), declarationEntity.getTitle()));
                        index++;
                        logger.debug("填充了标题：" + declarationEntity.getTitle());
                    } else {
                        textRun.setText(textRun.getRawText().replace(getCustomPlaceholder(), declarationEntity.getSpeaker()));
                        logger.debug("填充了讲员：" + declarationEntity.getSpeaker());
                    }
                }
            }
        }
        logger.info("宣信标题幻灯片制作完成");
    }
}
