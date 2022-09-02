package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.ScriptureEntity;
import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.components.ScriptureService;
import claygminx.exception.ScriptureServiceException;
import claygminx.util.ScriptureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;

import static claygminx.common.Dict.General.*;

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
    public void execute() throws Exception {
        ScriptureNumberEntity scriptureNumberEntity = ScriptureUtil.parseNumber(scriptureNumber);
        boolean validateResult = scriptureService.validateNumber(scriptureNumberEntity);
        if (!validateResult) {
            throw new ScriptureServiceException("经文编号格式错误！");
        }

        ScriptureEntity scriptureEntity = scriptureService.getScriptureWithFormat(
                scriptureNumberEntity, SystemConfig.getString(SCRIPTURE_FORMAT1));

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText("【" + scriptureNumber + "】");

        placeholder = slide.getPlaceholder(1);
        placeholder.clearText();
        XSLFTextParagraph paragraph = placeholder.addNewTextParagraph();
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