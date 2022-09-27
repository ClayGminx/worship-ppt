package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.entity.ScriptureNumberEntity;
import claygminx.worshipppt.components.ScriptureService;
import claygminx.worshipppt.exception.ScriptureNumberException;
import claygminx.worshipppt.exception.WorshipStepException;
import claygminx.worshipppt.util.ScriptureUtil;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 认罪经文阶段
 */
public class ConfessScriptureStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(ConfessScriptureStep.class);

    private final String scriptureNumber;
    private final ScriptureService scriptureService;

    public ConfessScriptureStep(XMLSlideShow ppt, String layout, ScriptureService scriptureService, String scriptureNumber) {
        super(ppt, layout);
        this.scriptureService = scriptureService;
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public void execute() throws WorshipStepException {
        try {
            List<ScriptureNumberEntity> scriptureNumberList = ScriptureUtil.parseNumbers(scriptureNumber);
            fillTitleAndScripture("认罪经文", scriptureService, scriptureNumberList);
            logger.info("认罪经文幻灯片制作完成");
        } catch (ScriptureNumberException e) {
            throw new WorshipStepException("解析认罪经文 [" + scriptureNumber + "] 编号时出错！", e);
        }
    }
}
