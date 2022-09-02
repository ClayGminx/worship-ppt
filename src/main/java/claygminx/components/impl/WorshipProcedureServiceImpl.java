package claygminx.components.impl;

import claygminx.common.entity.CoverEntity;
import claygminx.common.entity.WorshipEntity;
import claygminx.components.FileService;
import claygminx.components.ScriptureService;
import claygminx.components.WorshipProcedureService;
import claygminx.components.WorshipStep;
import claygminx.exception.FileServiceException;
import claygminx.exception.SystemException;
import ognl.Ognl;
import ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.dom4j.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorshipProcedureServiceImpl implements WorshipProcedureService {

    private final static Logger logger = LoggerFactory.getLogger(WorshipProcedureService.class);

    private FileService fileService;
    private ScriptureService scriptureService;

    @Override
    public List<WorshipStep> generate(XMLSlideShow ppt, WorshipEntity worshipEntity) throws FileServiceException {
        String xmlString = fileService.readWorshipProcedureXml();

        Document document;
        try {
            document = DocumentHelper.parseText(xmlString);
        } catch (DocumentException e) {
            throw new FileServiceException("XML读取失败！", e);
        }

        CoverEntity cover = worshipEntity.getCover();
        String model = cover.getModel();
        Element rootElement = document.getRootElement();
        List<?> elements = rootElement.elements();
        Map<String, Object> context = new HashMap<>();
        List<WorshipStep> result = new ArrayList<>();
        for (Object elementObj : elements) {
            Element modelElement = (Element) elementObj;
            String modelName = modelElement.attributeValue("name");
            if (model.equals(modelName)) {
                context.put("ppt", ppt);
                context.put("worshipEntity", worshipEntity);
                context.put("scriptureService", scriptureService);
                Ognl.setRoot(context, worshipEntity);
                List<?> stepElements = modelElement.elements();
                for (Object stepElementObj : stepElements) {
                    Element stepElement = (Element) stepElementObj;
                    WorshipStep worshipStep = getWorshipStep(stepElement, context, worshipEntity);
                    if (worshipStep != null) {
                        result.add(worshipStep);
                    }
                }
                break;
            }
        }

        return result;
    }

    private WorshipStep getWorshipStep(Element stepElement, Map<String, Object> context, WorshipEntity worshipEntity) {
        String ifExpression = stepElement.attributeValue("if");
        if (ifExpression != null) {
            try {
                boolean whether = (boolean) Ognl.getValue(ifExpression, worshipEntity);
                if (!whether) {
                    logger.debug("因[{}]跳过一个阶段", ifExpression);
                    return null;
                }
            } catch (OgnlException e) {
                throw new SystemException("OGNL表达式错误！", e);
            }
        }

        String clazz = stepElement.attributeValue("class");
        String layout = stepElement.attributeValue("layout");
        String data = stepElement.attributeValue("data");
        context.put("layout", layout);
        String expression;
        if (data != null) {
            expression = String.format("new %s(#ppt, #layout, %s)", clazz, data);
        } else {
            expression = String.format("new %s(#ppt, #layout)", clazz);
        }

        try {
            return (WorshipStep) Ognl.getValue(expression, context, worshipEntity);
        } catch (OgnlException e) {
            throw new SystemException("OGNL表达式错误！", e);
        }
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public void setScriptureService(ScriptureService scriptureService) {
        this.scriptureService = scriptureService;
    }
}
