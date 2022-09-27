package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.entity.ScriptureNumberEntity;
import claygminx.worshipppt.components.ScriptureService;
import claygminx.worshipppt.exception.ScriptureNumberException;
import claygminx.worshipppt.exception.WorshipStepException;
import claygminx.worshipppt.util.ScriptureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.List;

/**
 * 公祷经文阶段
 */
public class PublicPrayScriptureStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(PublicPrayScriptureStep.class);

    private final String scriptureNumber;
    private final ScriptureService scriptureService;

    public PublicPrayScriptureStep(XMLSlideShow ppt, String layout, ScriptureService scriptureService, String scriptureNumber) {
        super(ppt, layout);
        this.scriptureService = scriptureService;
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public void execute() throws WorshipStepException {
        try {
            List<ScriptureNumberEntity> publicScriptureNumberList = ScriptureUtil.parseNumbers(scriptureNumber);
            fillTitleAndScripture(getLayout(), scriptureService, publicScriptureNumberList);
            logger.info("公祷经文幻灯片制作完成");
        } catch (ScriptureNumberException e) {
            throw new WorshipStepException("解析公祷经文编号 [" + scriptureNumber + "] 时出错！", e);
        }
    }
}
