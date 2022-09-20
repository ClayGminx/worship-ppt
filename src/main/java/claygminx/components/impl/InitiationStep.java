package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.PoetryEntity;
import claygminx.exception.SystemException;
import claygminx.exception.WorshipStepException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
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
    public void execute() throws WorshipStepException {
        XMLSlideShow sourcePpt = getSourcePpt();
        XMLSlideShow targetPpt = getPpt();
        int poetrySlideOrder = SystemConfig.getInt(PPTProperty.MASTER_INITIATION_POETRY_SLIDE_ORDER);

        copyInitiationSlide(targetPpt, sourcePpt, 0, poetrySlideOrder);
        super.execute();
        copyInitiationSlide(targetPpt, sourcePpt, poetrySlideOrder, -1);

        logger.info("入会幻灯片制作完成");
    }

    @Override
    public double getTop() {
        return SystemConfig.getDouble(PPTProperty.HOLY_COMMUNION_POETRY_TOP);
    }

    @Override
    public List<PoetryEntity> getPoetryList() {
        return poetryList;
    }

    /**
     * 将入会模板中的一些幻灯片拷贝到目标PPT中
     * @param targetPpt 目标PPT
     * @param sourcePpt 源PPT
     * @param from 幻灯片起始位置
     * @param to 幻灯片结束位置，注意，拷贝时不包含此位置的幻灯片，若传-1，则表示源PPT中的最后位置
     */
    private void copyInitiationSlide(XMLSlideShow targetPpt, XMLSlideShow sourcePpt, int from, int to) {
        // 拷贝时，目标PPT应先创建幻灯片，并且使用统一的版式
        String layoutName = SystemConfig.getString(PPTProperty.MASTER_GENERAL_LAYOUT_NAME);
        XSLFSlideLayout layout = targetPpt.findLayout(layoutName);
        List<XSLFSlide> sourceSlides = sourcePpt.getSlides();

        to = to == -1 ? sourceSlides.size() : to;
        logger.debug("准备拷贝幻灯片，起始索引为{}, 结束索引为{}", from ,to);
        for (int i = from; i < to; i++) {
            XSLFSlide sourceSlide = sourceSlides.get(i);
            XmlObject copy = sourceSlide.getXmlObject().copy();
            XSLFSlide newSlide = targetPpt.createSlide(layout);
            newSlide.getXmlObject().set(copy);
        }
        logger.debug("拷贝完成");
    }

    /**
     * 获取入会PPT对象
     * @return PPT对象
     */
    private XMLSlideShow getSourcePpt() {
        String filePath = SystemConfig.getString(PPTProperty.MASTER_INITIATION_FILE_PATH);
        logger.debug("准备读取入会PPT模板文件流");
        try (InputStream is = new FileInputStream(filePath)) {
            logger.debug("入会PPT模板文件流读取完成");
            return new XMLSlideShow(is);
        } catch (Exception e) {
            throw new SystemException("读取入会模板文件失败！", e);
        }
    }

}
