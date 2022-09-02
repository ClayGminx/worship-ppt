package claygminx.components.impl;

import claygminx.common.Dict;
import claygminx.common.config.SystemConfig;
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
        return SystemConfig.getDouble(Dict.General.PPT_HOLY_COMMUNION_POETRY_TOP);
    }

    @Override
    public List<PoetryEntity> getPoetryList() {
        return poetryList;
    }
}
