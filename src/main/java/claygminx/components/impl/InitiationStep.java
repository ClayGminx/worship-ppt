package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.PoetryEntity;
import claygminx.exception.SystemException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

import static claygminx.common.Dict.*;

/**
 * 入会阶段
 * <p>注：该阶段比较特殊，不好通过模板的方式来做，所以这里采取的方式是，除了诗歌要通过模板的方式来做，其余的只要把已经做好的幻灯片复制进来就行了。</p>
 */
public class InitiationStep extends RegularPoetryStep {

    private final static Logger logger = LoggerFactory.getLogger(InitiationStep.class);

    private final List<PoetryEntity> poetryList;

    public InitiationStep(XMLSlideShow ppt, String layout, List<PoetryEntity> poetryList) {
        super(ppt, layout, poetryList);
        this.poetryList = poetryList;
    }

    @Override
    public void execute() throws Exception {
        XMLSlideShow sourcePpt = getSourcePpt();
        XMLSlideShow targetPpt = getPpt();
        int poetrySlideOrder = SystemConfig.getInt(General.WORSHIP_PPT_INITIATION_POETRY_SLIDE_ORDER);

        createInitiationSlide(targetPpt, sourcePpt, 0, poetrySlideOrder);

        // 制作入会诗歌歌谱图
        super.execute();

        createInitiationSlide(targetPpt, sourcePpt, poetrySlideOrder, -1);

        logger.info("入会幻灯片制作完成");
    }

    @Override
    public double getTop() {
        return 2.18;
    }

    @Override
    public List<PoetryEntity> getPoetryList() {
        return poetryList;
    }

    private void createInitiationSlide(XMLSlideShow targetPpt, XMLSlideShow sourcePpt, int from, int to) {
        String layoutName = SystemConfig.getString(General.WORSHIP_PPT_GENERAL_CROSS_LAYOUT_NAME);
        XSLFSlideLayout layout = targetPpt.findLayout(layoutName);
        List<XSLFSlide> sourceSlides = sourcePpt.getSlides();

        to = to == -1 ? sourceSlides.size() : to;
        for (int i = from; i < to; i++) {
            XSLFSlide sourceSlide = sourceSlides.get(i);
            XmlObject copy = sourceSlide.getXmlObject().copy();
            XSLFSlide newSlide = targetPpt.createSlide(layout);
            newSlide.getXmlObject().set(copy);
        }
    }

    private XMLSlideShow getSourcePpt() {
        ClassLoader classLoader = InitiationStep.class.getClassLoader();
        String classPath = "assets/ppt/initiation.pptx";
        logger.debug(classPath);
        try (InputStream is = classLoader.getResourceAsStream(classPath)) {
            return new XMLSlideShow(is);
        } catch (Exception e) {
            throw new SystemException("读取入会模板文件失败！", e);
        }
    }

}
