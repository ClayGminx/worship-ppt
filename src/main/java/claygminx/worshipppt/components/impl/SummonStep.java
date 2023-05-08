package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.common.entity.ScriptureEntity;
import claygminx.worshipppt.common.entity.ScriptureNumberEntity;
import claygminx.worshipppt.components.ScriptureService;
import claygminx.worshipppt.exception.ScriptureNumberException;
import claygminx.worshipppt.exception.ScriptureServiceException;
import claygminx.worshipppt.exception.WorshipStepException;
import claygminx.worshipppt.util.ScriptureUtil;
import claygminx.worshipppt.common.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;

/**
 * 宣召阶段
 */
public class SummonStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(SummonStep.class);

    private final String scriptureNumber;
    private final ScriptureService scriptureService;

    public SummonStep(XMLSlideShow ppt, String layout, ScriptureService scriptureService, String scriptureNumber) {
        super(ppt, layout);
        this.scriptureService = scriptureService;
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public void execute() throws WorshipStepException {
        ScriptureNumberEntity scriptureNumberEntity;
        try {
            scriptureNumberEntity = ScriptureUtil.parseNumber(scriptureNumber);
        } catch (ScriptureNumberException e) {
            throw new WorshipStepException("解析经文编号 [" + scriptureNumber + "] 时出错！", e);
        }
        boolean validateResult = scriptureService.validateNumber(scriptureNumberEntity);
        if (!validateResult) {
            throw new ScriptureServiceException("经文编号格式错误！");
        }

        ScriptureEntity scriptureEntity = scriptureService.getScriptureWithFormat(
                scriptureNumberEntity, SystemConfig.getString(Dict.ScriptureProperty.FORMAT1));

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText("【" + scriptureNumber + "】");

        placeholder = slide.getPlaceholder(1);
        placeholder.clearText();
        XSLFTextParagraph paragraph = placeholder.addNewTextParagraph();
        useCustomLanguage(paragraph);
        XSLFTextRun span = paragraph.addNewTextRun();
        span.setText("　　");
        span = paragraph.addNewTextRun();
        span.setText(scriptureEntity.getScripture());
        paragraph = placeholder.addNewTextParagraph();
        span = paragraph.addNewTextRun();
        span.setText("　　");
        span = paragraph.addNewTextRun();
        span.setText("我们当赞美耶和华");
        span.setBold(true);
        span.setUnderlined(true);
        span.setFontColor(Color.BLUE);

        logger.info("宣召幻灯片制作完成");
    }
}
