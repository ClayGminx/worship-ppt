package claygminx.components.impl;

import claygminx.common.entity.PreachEntity;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class PreachStep extends AbstractWorshipStep {

    private final PreachEntity preachEntity;

    public PreachStep(XMLSlideShow ppt, String layout, PreachEntity preachEntity) {
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
    }
}
