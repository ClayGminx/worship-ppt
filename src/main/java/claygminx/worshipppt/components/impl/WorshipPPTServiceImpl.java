package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.common.entity.WorshipEntity;
import claygminx.worshipppt.components.*;
import claygminx.worshipppt.exception.FileServiceException;
import claygminx.worshipppt.exception.SystemException;
import claygminx.worshipppt.exception.WorshipStepException;
import claygminx.worshipppt.common.Dict;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WorshipPPTServiceImpl implements WorshipPPTService {

    private final static Logger logger = LoggerFactory.getLogger(WorshipPPTService.class);

    private final FileService fileService;
    private final ScriptureService scriptureService;
    private ProgressMonitor progressMonitor;

    private final WorshipEntity worshipEntity;
    private int progress;
    private File file;

    public WorshipPPTServiceImpl(WorshipEntity worshipEntity) {
        fileService = FileServiceImpl.getInstance();
        scriptureService = ScriptureServiceImpl.getInstance();
        this.worshipEntity = worshipEntity;
    }

    @Override
    public void make() throws FileServiceException, WorshipStepException {
        // 1.准备PPT文件
        File pptFile;
        try {
            pptFile = fileService.copyTemplate(worshipEntity.getCover());
            increaseProgress(5, "创建敬拜PPT文件");
        } catch (FileServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemException("出现未知错误！", e);
        }

        // 2.按照指定模板制作幻灯片
        XMLSlideShow ppt;
        try (FileInputStream fis  = new FileInputStream(pptFile)) {
            // 创建PPT对象
            ppt = new XMLSlideShow(fis);
            // 使用自定义语言
            String lang = SystemConfig.getString(Dict.PPTProperty.GENERAL_LANGUAGE);
            if (lang == null || "".equals(lang)) {
                throw new SystemException("PPT语言不可为空！");
            }
            CTPresentation ctPresentation = ppt.getCTPresentation();
            CTTextListStyle defaultTextStyle = ctPresentation.getDefaultTextStyle();
            CTTextParagraphProperties defPPr = defaultTextStyle.getDefPPr();
            CTTextCharacterProperties defRPr = defPPr.getDefRPr();
            defRPr.setLang(lang);
            // 敬拜过程服务对象
            WorshipProcedureServiceImpl worshipProcedureService = new WorshipProcedureServiceImpl();
            worshipProcedureService.setFileService(fileService);
            worshipProcedureService.setScriptureService(scriptureService);
            List<WorshipStep> worshipProcedure = worshipProcedureService.generate(ppt, worshipEntity);
            increaseProgress(1, "读取敬拜流程");
            int perProgress = (int) Math.ceil((99 - progress) / (double) worshipProcedure.size());
            // 开始一个个阶段地制作幻灯片
            for (WorshipStep worshipStep : worshipProcedure) {
                worshipStep.execute();
                if (progress + perProgress < 100) {
                    increaseProgress(perProgress, "制作敬拜阶段幻灯片");
                }
            }
        } catch (FileServiceException | WorshipStepException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemException("出现未知错误！", e);
        }

        // 4.保存
        try (FileOutputStream fos = new FileOutputStream(pptFile)) {
            ppt.write(fos);
            logger.info("保存PPT文件");
            file = pptFile;
            increaseProgress(100, "完成");
            progressMonitor.close();
        } catch (IOException e) {
            throw new FileServiceException("保存PPT文件时出现错误！", e);
        } catch (Exception e) {
            throw new SystemException("出现未知错误！", e);
        }
    }

    public File getFile() {
        return file;
    }

    // 增加进度
    private void increaseProgress(int n, String note) {
        progress += n;
        if (progress > 100) {
            progress = 100;
        }
        logger.debug("进度：{}%", progress);
        progressMonitor.setProgress(progress);
        if (note != null) {
            progressMonitor.setNote(note);
        }
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }
}
