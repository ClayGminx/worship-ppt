package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.entity.PreachEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * 证道摘要阶段
 */
public class PreachSummaryStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(PreachSummaryStep.class);

    private final PreachEntity preachEntity;

    public PreachSummaryStep(XMLSlideShow ppt, String layout, PreachEntity preachEntity) {
        super(ppt, layout);
        this.preachEntity = preachEntity;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        String text = placeholder.getText();
        placeholder.setText(text.replace(getCustomPlaceholder(), preachEntity.getTitle()));

        placeholder = slide.getPlaceholder(1);
        text = placeholder.getText();
        placeholder.setText(text.replace(getCustomPlaceholder(), preachEntity.getScriptureNumber()));

        logger.info("证道摘要幻灯片制作完成");
    }
}
