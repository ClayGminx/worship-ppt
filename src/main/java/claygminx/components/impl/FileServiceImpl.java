package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.CoverEntity;
import claygminx.components.FileService;
import claygminx.exception.FileServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Scanner;

import static claygminx.common.Dict.*;

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

        ClassLoader classLoader = FileServiceImpl.class.getClassLoader();
        String templateFilePath = SystemConfig.getString(General.PPT_TEMPLATE_GENERAL_PATH);
        File targetFile = new File("敬拜-" + coverEntity.getWorshipDate() + EXTENSION);

        try (InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(templateFilePath))) {
            logger.debug("开始复制...");
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("PPT文件创建完成，位于：" + targetFile.getAbsolutePath());
        } catch (IOException | NullPointerException e) {
            throw new FileServiceException("PPT文件创建失败！", e);
        }

        return targetFile;
    }

    @Override
    public String readWorshipProcedureXml() throws FileServiceException {
        String xmlPath = SystemConfig.getString(General.PPT_PROCEDURE_XML_PATH);
        ClassLoader classLoader = FileServiceImpl.class.getClassLoader();
        try (InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(xmlPath));
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            logger.debug("开始一行行地读取XML");
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                stringBuilder.append(str);
            }
            logger.debug("读取完成");
            return stringBuilder.toString();
        } catch (IOException | NullPointerException e) {
            throw new FileServiceException("读取" + xmlPath + "失败！", e);
        }
    }
}
