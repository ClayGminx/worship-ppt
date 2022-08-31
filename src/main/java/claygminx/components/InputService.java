package claygminx.components;

import claygminx.common.entity.WorshipEntity;
import claygminx.exception.InputServiceException;

/**
 * 输入服务
 */
public interface InputService {

    WorshipEntity readIni(String filePath) throws InputServiceException;

}
