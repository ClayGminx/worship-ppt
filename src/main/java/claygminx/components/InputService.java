package claygminx.components;

import claygminx.common.entity.WorshipEntity;
import claygminx.exception.InputServiceException;

public interface InputService {

    WorshipEntity readIni(String filePath) throws InputServiceException;

}
