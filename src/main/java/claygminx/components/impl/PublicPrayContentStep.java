package claygminx.components.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * 公祷内容阶段
 */
public class PublicPrayContentStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(PublicPrayContentStep.class);

    public static String example = "1.为教会的复兴祷告；\n2.为教会中圣约婚姻家庭的圣洁合一祷告；\n3.为教会中单身弟兄姐妹的婚姻预备祷告；\n4.为教会中生病软弱的肢体祷告。";

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
