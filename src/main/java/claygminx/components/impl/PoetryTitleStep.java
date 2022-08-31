package claygminx.components.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * 诗歌标题阶段
 */
public class PoetryTitleStep extends AbstractWorshipStep {

    private final String slideName;

    private final String poetryName;

    public PoetryTitleStep(XMLSlideShow ppt, String layout, String slideName, String poetryName) {
        super(ppt, layout);
        this.slideName = slideName;
        this.poetryName = poetryName;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        fillPlaceholder(slide, 0, slideName);
        fillPlaceholder(slide, 1, poetryName);
    }

    private void fillPlaceholder(XSLFSlide slide, int idx, String text) {
        XSLFTextShape placeholder = slide.getPlaceholder(idx);
        placeholder.clearText();
        placeholder.setText(text);
    }
}
