package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.common.entity.CoverEntity;
import claygminx.worshipppt.components.FileService;
import claygminx.worshipppt.exception.FileServiceException;
import claygminx.worshipppt.exception.SystemException;
import claygminx.worshipppt.common.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Scanner;

public class FileServiceImpl implements FileService {

    private final static Logger logger = LoggerFactory.getLogger(FileService.class);

    private final static String EXTENSION = ".pptx";

    private static FileService fileService;

    private FileServiceImpl() {
    }

    public static FileService getInstance() {
        if (fileService == null) {
            fileService = new FileServiceImpl();
        }
        return fileService;
    }

    @Override
    public File copyTemplate(CoverEntity coverEntity) throws FileServiceException {
        logger.info("开始根据模板创建PPT文件");

        String templateFilePath = SystemConfig.getString(Dict.PPTProperty.MASTER_WORSHIP_FILE_PATH);
        File targetFile = new File("敬拜-" + coverEntity.getWorshipDate() + EXTENSION);

        try (InputStream inputStream = new FileInputStream(templateFilePath)) {
            logger.debug("开始复制...");
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("PPT文件创建完成，位于：" + targetFile.getAbsolutePath());
        } catch (IOException | NullPointerException e) {
            throw new FileServiceException("PPT文件创建失败！", e);
        }

        return targetFile;
    }

    public void copyTemplate(File targetFile) throws FileServiceException {
        logger.info("开始根据模板创建PPT文件");

        String templateFilePath = SystemConfig.getString(Dict.PPTProperty.MASTER_WORSHIP_FILE_PATH);
        try (InputStream inputStream = new FileInputStream(templateFilePath)) {
            logger.debug("开始复制...");
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("PPT文件创建完成，位于：" + targetFile.getAbsolutePath());
        } catch (IOException | NullPointerException e) {
            throw new FileServiceException("PPT文件创建失败！", e);
        }
    }

    @Override
    public String readWorshipProcedureXml() throws FileServiceException {
        String xmlPath = SystemConfig.getString(Dict.PPTProperty.GENERAL_PROCEDURE_XML_PATH);
        if (xmlPath == null || "".equals(xmlPath)) {
            throw new SystemException(Dict.PPTProperty.GENERAL_PROCEDURE_XML_PATH + "不可为空！");
        }

        if (xmlPath.startsWith("classpath:")) {// 配置的是类路径
            ClassLoader classLoader = FileServiceImpl.class.getClassLoader();
            String path = xmlPath.substring(10);
            try (InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(path));
                 Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                return internalReadWorshipProcedureXml(scanner);
            } catch (IOException | NullPointerException e) {
                throw new FileServiceException("读取" + xmlPath + "失败！", e);
            }
        } else {
            try (InputStream inputStream = new FileInputStream(xmlPath);
                 Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                return internalReadWorshipProcedureXml(scanner);
            } catch (IOException | NullPointerException e) {
                throw new FileServiceException("读取" + xmlPath + "失败！", e);
            }
        }
    }

    private String internalReadWorshipProcedureXml(Scanner scanner) {
        logger.debug("开始一行行地读取XML");
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            stringBuilder.append(str);
        }
        logger.debug("读取完成");
        return stringBuilder.toString();
    }
}
