package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.common.entity.WorshipEntity;
import claygminx.worshipppt.components.*;
import claygminx.worshipppt.exception.FileServiceException;
import claygminx.worshipppt.exception.InputServiceException;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WorshipPPTServiceImpl implements WorshipPPTService {

    private final static Logger logger = LoggerFactory.getLogger(WorshipPPTService.class);

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
            logger.debug("", e);
            logger.error(e.getMessage());
            return;
        } catch (Exception e) {
            logger.debug("", e);
            logger.error("出现未知错误！");
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
        } catch (Exception e) {
            logger.debug("", e);
            logger.error("出现未知错误！");
            return;
        }

        // 3.按照指定模板制作幻灯片
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
            defRPr.setAltLang(lang);
            // 敬拜过程服务对象
            WorshipProcedureServiceImpl worshipProcedureService = new WorshipProcedureServiceImpl();
            worshipProcedureService.setFileService(fileService);
            worshipProcedureService.setScriptureService(scriptureService);
            List<WorshipStep> worshipProcedure = worshipProcedureService.generate(ppt, worshipEntity);
            // 开始一个个阶段地制作幻灯片
            for (WorshipStep worshipStep : worshipProcedure) {
                worshipStep.execute();
            }
        } catch (FileServiceException | WorshipStepException e) {
            logger.debug("", e);
            logger.error(e.getMessage());
            return;
        } catch (Exception e) {
            logger.debug("", e);
            logger.error("出现未知错误！");
            return;
        }

        // 4.保存
        try (FileOutputStream fos = new FileOutputStream(pptFile)) {
            ppt.write(fos);
            logger.info("\n"
            + "************************************\n"
            + "  PPT制作完成！\n"
            + "  文件位于：{}\n"
            + "  你还需要做一些检查工作：\n"
            + "  1.看看文本框里的文字是否符合排版规范；\n"
            + "  2.宣信和证道内容需要手动制作；\n"
            + "  3.圣餐诗歌需要手动调整以符合圣礼需要；\n"
            + "  4.还有更多需要细心检查的细节。\n"
            + "************************************\n", pptFile.getAbsolutePath());
        } catch (IOException e) {
            logger.debug("", e);
            logger.error("保存PPT文件时出现错误！");
        } catch (Exception e) {
            logger.debug("", e);
            logger.error("出现未知错误！");
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
