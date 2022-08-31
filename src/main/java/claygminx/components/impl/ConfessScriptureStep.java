package claygminx.components.impl;

import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.components.ScriptureService;
import claygminx.util.ScriptureUtil;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.List;

/**
 * 认罪经文阶段
 */
public class ConfessScriptureStep extends AbstractWorshipStep {

    private final String scriptureNumber;

    private final ScriptureService scriptureService;

    public ConfessScriptureStep(XMLSlideShow ppt, String layout, ScriptureService scriptureService, String scriptureNumber) {
        super(ppt, layout);
        this.scriptureService = scriptureService;
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public void execute() throws Exception {
        List<ScriptureNumberEntity> scriptureNumberList = ScriptureUtil.parseNumbers(scriptureNumber);
        fillTitleAndScripture("认罪经文", scriptureService, scriptureNumberList);
    }
}
