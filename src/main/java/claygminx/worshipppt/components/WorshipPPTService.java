package claygminx.worshipppt.components;

import claygminx.worshipppt.exception.FileServiceException;
import claygminx.worshipppt.exception.WorshipStepException;

/**
 * 敬拜PPT服务
 */
public interface WorshipPPTService {

    /**
     * 制作敬拜PPT
     */
    void make() throws FileServiceException, WorshipStepException;

}
