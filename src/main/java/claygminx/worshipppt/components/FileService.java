package claygminx.worshipppt.components;

import claygminx.worshipppt.common.entity.CoverEntity;
import claygminx.worshipppt.exception.FileServiceException;

import java.io.File;

/**
 * 文件服务
 */
public interface FileService {

    /**
     * 将发行包内的模板文件复制到目标位置
     * @param coverEntity 封面实体。该方法会实体此参数的敬拜日期属性值用来给目标文件起名字
     * @return 复制后的目标文件对象
     * @throws FileServiceException 复制文件时抛出的异常
     */
    File copyTemplate(CoverEntity coverEntity) throws FileServiceException;

    /**
     * 将发行包内的模板文件复制到目标位置
     * @param targetFile 目标文件。注意，若目标文件已经存在，则会被覆盖。
     * @throws FileServiceException 复制文件时抛出的异常
     */
    void copyTemplate(File targetFile) throws FileServiceException;

    /**
     * 读取敬拜流程XML文件内容
     * @return XML内容
     */
    String readWorshipProcedureXml() throws FileServiceException;
}
