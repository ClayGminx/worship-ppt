package claygminx.components.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * 三一颂阶段
 */
public class OdeToTheTrinityStep extends AbstractWorshipStep {

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
    }
}
