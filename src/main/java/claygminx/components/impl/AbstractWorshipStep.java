package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.ScriptureEntity;
import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.components.ScriptureService;
import claygminx.components.WorshipStep;
import claygminx.exception.ScriptureServiceException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.util.List;

import static claygminx.common.Dict.General.*;

public abstract class AbstractWorshipStep implements WorshipStep {

    private final XMLSlideShow ppt;
    private final String layout;

    public AbstractWorshipStep(XMLSlideShow ppt, String layout) {
        this.ppt = ppt;
        this.layout = layout;
    }

    public XMLSlideShow getPpt() {
        return ppt;
    }

    public String getLayout() {
        return layout;
    }

    public String getCustomPlaceholder() {
        return "{}";
    }

    protected String[] getTitleAndScripture(ScriptureService scriptureService, List<ScriptureNumberEntity> scriptureNumberList) {
        StringBuilder titleBuilder = new StringBuilder();
        StringBuilder scriptureBuilder = new StringBuilder();
        for (ScriptureNumberEntity scriptureNumberEntity : scriptureNumberList) {
            boolean validateResult = scriptureService.validateNumber(scriptureNumberEntity);
            if (!validateResult) {
                throw new ScriptureServiceException("经文编号格式错误！");
            }
            titleBuilder.append('【').append(scriptureNumberEntity).append('】');
            ScriptureEntity scriptureEntity = scriptureService.getScriptureWithFormat(
                    scriptureNumberEntity, SystemConfig.getString(SCRIPTURE_FORMAT1));
            scriptureBuilder.append("　　").append(scriptureEntity.getScripture()).append('\n');
        }
        scriptureBuilder.setLength(scriptureBuilder.length() - 1);
        return new String[] {titleBuilder.toString(), scriptureBuilder.toString()};
    }

    protected void fillTitleAndScripture(String layoutName, ScriptureService scriptureService, List<ScriptureNumberEntity> scriptureNumberList) {
        String[] titleAndScripture = getTitleAndScripture(scriptureService, scriptureNumberList);

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(layoutName);
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(titleAndScripture[0]);

        placeholder = slide.getPlaceholder(1);
        placeholder.clearText();
        placeholder.setText(titleAndScripture[1]);
    }
}
