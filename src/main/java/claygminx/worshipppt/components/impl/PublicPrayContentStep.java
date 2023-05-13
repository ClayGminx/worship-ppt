package claygminx.worshipppt.components.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * 公祷内容阶段
 */
public class PublicPrayContentStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(PublicPrayContentStep.class);

    public static String example = "1、";

    private final String content;

    public PublicPrayContentStep(XMLSlideShow ppt, String layout, String content) {
        super(ppt, layout);
        this.content = content;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide coverSlide = ppt.createSlide(layout);
        XSLFTextShape placeholder = coverSlide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(content == null || content.isEmpty() ? example : content);
        logger.info("公祷内容幻灯片制作完成");
    }
}
