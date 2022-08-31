package claygminx.components.impl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;

import java.util.List;

public class FamilyReportStep extends AbstractWorshipStep {

    private final List<String> familyReports;

    public FamilyReportStep(XMLSlideShow ppt, String layout, List<String> familyReports) {
        super(ppt, layout);
        this.familyReports = familyReports;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);
    }
}
