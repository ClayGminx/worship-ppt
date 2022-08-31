package claygminx.components;

import claygminx.common.entity.WorshipEntity;
import claygminx.exception.FileServiceException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.List;

/**
 * 敬拜过程服务
 */
public interface WorshipProcedureService {

    List<WorshipStep> generate(XMLSlideShow ppt, WorshipEntity worshipEntity) throws FileServiceException;

}
