package claygminx.components;

import claygminx.common.entity.CoverEntity;
import claygminx.exception.FileServiceException;

import java.io.File;

/**
 * 文件服务
 */
public interface FileService {

    File copyTemplate(CoverEntity coverEntity) throws FileServiceException;

    /**
     * 读取敬拜流程XML文件内容跟
     * @return XML内容
     */
    String readWorshipProcedureXml() throws FileServiceException;
}
