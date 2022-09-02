package claygminx.components.impl;

import claygminx.common.entity.CoverEntity;
import claygminx.components.FileService;
import claygminx.exception.FileServiceException;
import claygminx.exception.SystemException;
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

    private final static String WORSHIP_PROCEDURE_XML_PATH = "worship-procedure.xml";

    private final static String PPT_TEMPLATE_PATH = "assets/ppt/";

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
        String model = coverEntity.getModel();// 这里该参数不应该是null或""
        ClassLoader classLoader = FileServiceImpl.class.getClassLoader();
        String classPath = PPT_TEMPLATE_PATH + model + EXTENSION;
        logger.debug(classPath);

        File targetFile = new File("敬拜-" + coverEntity.getWorshipDate() + EXTENSION);
        try (InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(classPath))) {
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileServiceException("PPT文件创建失败！", e);
        }

        return targetFile;
    }

    @Override
    public String readWorshipProcedureXml() throws FileServiceException {
        ClassLoader classLoader = FileServiceImpl.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(WORSHIP_PROCEDURE_XML_PATH)) {
            if (inputStream  == null) {
                throw new SystemException("读取" + WORSHIP_PROCEDURE_XML_PATH + "失败！");
            }
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                logger.debug(str);
                stringBuilder.append(str);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new FileServiceException("读取" + WORSHIP_PROCEDURE_XML_PATH + "失败！", e);
        }
    }
}
