package claygminx.components.impl;

import claygminx.common.entity.WorshipEntity;
import claygminx.components.*;
import claygminx.exception.FileServiceException;
import claygminx.exception.InputServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WorshipPPTServiceImpl implements WorshipPPTService {

    private final static Logger logger = LogManager.getLogger(WorshipPPTService.class);

    private final String inputFilePath;

    private InputService inputService;

    private FileService fileService;

    private ScriptureService scriptureService;

    /**
     * 构造器
     * @param inputFilePath 输入文件的完全路径，现在仅支持ini文件
     */
    public WorshipPPTServiceImpl(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    @Override
    public void make() {
        // 1.解析输入参数
        WorshipEntity worshipEntity;
        try {
            worshipEntity = inputService.readIni(inputFilePath);
        } catch (InputServiceException e) {
            logger.error(e.getMessage());
            return;
        }

        // 2.准备PPT文件
        File pptFile;
        try {
            pptFile = fileService.copyTemplate(worshipEntity.getCover());
        } catch (FileServiceException e) {
            logger.debug("", e);
            logger.error(e.getMessage());
            return;
        }

        // 3.按照指定模板制作幻灯片
        XMLSlideShow ppt;
        try (FileInputStream fis  = new FileInputStream(pptFile)) {
            ppt = new XMLSlideShow(fis);
            WorshipProcedureServiceImpl worshipProcedureService = new WorshipProcedureServiceImpl();
            worshipProcedureService.setFileService(fileService);
            worshipProcedureService.setScriptureService(scriptureService);
            List<WorshipStep> worshipProcedure = worshipProcedureService.generate(ppt, worshipEntity);
            for (WorshipStep worshipStep : worshipProcedure) {
                worshipStep.execute();
            }
        } catch (Exception e) {
            logger.error(e);
            return;
        }

        // 4.保存
        try (FileOutputStream fos = new FileOutputStream(pptFile)) {
            ppt.write(fos);
        } catch (IOException e) {
        }
    }

    public void setInputService(InputService inputService) {
        this.inputService = inputService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public void setScriptureService(ScriptureService scriptureService) {
        this.scriptureService = scriptureService;
    }
}
