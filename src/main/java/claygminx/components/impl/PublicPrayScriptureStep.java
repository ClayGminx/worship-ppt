package claygminx.components.impl;

import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.components.ScriptureService;
import claygminx.exception.ScriptureNumberException;
import claygminx.util.ScriptureUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.List;

/**
 * 公祷经文阶段
 */
public class PublicPrayScriptureStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(PublicPrayScriptureStep.class);

    private final String scriptureNumber;

    private final ScriptureService scriptureService;

    public PublicPrayScriptureStep(XMLSlideShow ppt, String layout, ScriptureService scriptureService, String scriptureNumber) {
        super(ppt, layout);
        this.scriptureService = scriptureService;
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public void execute() throws ScriptureNumberException {
        List<ScriptureNumberEntity> publicScriptureNumberList = ScriptureUtil.parseNumbers(scriptureNumber);
        fillTitleAndScripture(getLayout(), scriptureService, publicScriptureNumberList);
        logger.info("公祷经文幻灯片制作完成");
    }
}
