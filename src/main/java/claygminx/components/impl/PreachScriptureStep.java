package claygminx.components.impl;

import claygminx.common.entity.PreachEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * 证道经文阶段
 */
public class PreachScriptureStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(PreachScriptureStep.class);

    private final PreachEntity preachEntity;

    public PreachScriptureStep(XMLSlideShow ppt, String layout, PreachEntity preachEntity) {
        super(ppt, layout);
        this.preachEntity = preachEntity;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(preachEntity.getScriptureNumber());

        logger.info("证道经文幻灯片制作完成");
    }
}
