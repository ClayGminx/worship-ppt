package claygminx.components;

import claygminx.common.entity.WorshipEntity;
import claygminx.exception.InputServiceException;

/**
 * 输入服务
 */
public interface InputService {

    /**
     * 读取ini文件
     * @param filePath ini文件位置
     * @return 敬拜参数实体对象
     * @throws InputServiceException 读取时发生的异常
     */
    WorshipEntity readIni(String filePath) throws InputServiceException;

}
