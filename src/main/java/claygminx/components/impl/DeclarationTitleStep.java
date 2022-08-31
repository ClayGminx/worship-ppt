package claygminx.components.impl;

import claygminx.common.entity.DeclarationEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.*;

import java.util.List;

/**
 * 宣信标题阶段
 */
public class DeclarationTitleStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(AbstractWorshipStep.class);

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
                    } else {
                        textRun.setText(textRun.getRawText().replace(getCustomPlaceholder(), declarationEntity.getSpeaker()));
                    }
                }
            }
        }
        logger.info("宣信标题幻灯片制作完成");
    }
}
