package claygminx.components.impl;

import claygminx.common.entity.CoverEntity;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封面阶段
 */
public class CoverStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(CoverStep.class);

    private final CoverEntity coverEntity;

    public CoverStep(XMLSlideShow ppt, String layout, CoverEntity coverEntity) {
        super(ppt, layout);
        this.coverEntity = coverEntity;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout coverLayout = ppt.findLayout(getLayout());
        XSLFSlide coverSlide = ppt.createSlide(coverLayout);

        XSLFTextShape placeholder = coverSlide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(coverEntity.getWorshipDate());

        placeholder = coverSlide.getPlaceholder(1);
        placeholder.clearText();
        placeholder.setText(coverEntity.getChurchName());

        logger.info("封面幻灯片制作完成");
    }
}
