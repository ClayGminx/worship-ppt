package claygminx.worshipppt.components;

import claygminx.worshipppt.common.entity.WorshipEntity;
import claygminx.worshipppt.exception.FileServiceException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.List;

/**
 * 敬拜过程服务
 */
public interface WorshipProcedureService {

    /**
     * 生成敬拜流程
     * @param ppt PPT对象
     * @param worshipEntity 敬拜参数实体
     * @return 敬拜流程
     * @throws FileServiceException 若读取定义敬拜流程的XML文件失败，则抛出此异常
     */
    List<WorshipStep> generate(XMLSlideShow ppt, WorshipEntity worshipEntity) throws FileServiceException;

}
