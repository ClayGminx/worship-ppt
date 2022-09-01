package claygminx.components.impl;

import claygminx.common.entity.PoetryEntity;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.List;

/**
 * 圣餐诗歌阶段
 */
public class HolyCommunionPoetryStep extends RegularPoetryStep {

//    private final static Logger logger = LoggerFactory.getLogger(HolyCommunionPoetryStep.class);

    private final List<PoetryEntity> poetryList;

    public HolyCommunionPoetryStep(XMLSlideShow ppt, String layout, List<PoetryEntity> poetryList) {
        super(ppt, layout, poetryList);
        this.poetryList = poetryList;
    }

    @Override
    public void execute() throws Exception {
        super.execute();
    }

    @Override
    public double getTop() {
        return 2.18;
    }

    @Override
    public List<PoetryEntity> getPoetryList() {
        return poetryList;
    }
}
