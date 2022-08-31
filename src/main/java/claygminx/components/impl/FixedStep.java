package claygminx.components.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;

/**
 * 固定的阶段，不用填入参数，比如敬拜提示、静默、三一颂
 */
public class FixedStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(FixedStep.class);

    public FixedStep(XMLSlideShow ppt, String layout) {
        super(ppt, layout);
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        ppt.createSlide(layout);
        logger.info(getLayout() + "幻灯片制作完成");
    }
}
