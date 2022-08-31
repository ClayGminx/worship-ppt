package claygminx.components.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * 三一颂阶段
 */
public class OdeToTheTrinityStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(OdeToTheTrinityStep.class);

    private final String title;

    public OdeToTheTrinityStep(XMLSlideShow ppt, String layout, String title) {
        super(ppt, layout);
        this.title = title;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(title);

        logger.info("三一颂幻灯片制作完成");
    }
}
