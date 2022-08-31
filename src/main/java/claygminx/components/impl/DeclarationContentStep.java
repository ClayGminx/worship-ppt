package claygminx.components.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;

/**
 * 宣信内容阶段
 */
public class DeclarationContentStep extends AbstractWorshipStep {

    public DeclarationContentStep(XMLSlideShow ppt, String layout) {
        super(ppt, layout);
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        ppt.createSlide(layout);
    }
}
