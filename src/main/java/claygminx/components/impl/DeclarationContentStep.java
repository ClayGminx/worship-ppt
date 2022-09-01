package claygminx.components.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 宣信内容阶段
 */
public class DeclarationContentStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(DeclarationContentStep.class);

    public DeclarationContentStep(XMLSlideShow ppt, String layout) {
        super(ppt, layout);
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        ppt.createSlide(layout);
        logger.info("宣信内容幻灯片制作完成");
    }
}
