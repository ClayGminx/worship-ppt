package claygminx.worshipppt.components.impl;

import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FamilyReportStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(FamilyReportStep.class);

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

        if (familyReports.size() > 0) {
            XSLFTextShape placeholder = slide.getPlaceholder(0);
            placeholder.clearText();

            logger.debug(familyReports.size() + "项家事报告");
            for (int i = 0; i < familyReports.size(); i++) {
                String item = familyReports.get(i);
                XSLFTextParagraph paragraph = placeholder.addNewTextParagraph();
                XSLFTextRun textRun = paragraph.addNewTextRun();
                textRun.setText((i + 1) + "、" + item);
            }
        }

        logger.info("家事报告幻灯片制作完成");
    }
}
