package claygminx.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.sl.usermodel.PictureData;

/**
 * 图片工具
 */
public class PictureUtil {

    private final static Logger logger = LogManager.getLogger(PictureUtil.class);

    private PictureUtil() {
    }

    /**
     * 根据文件扩展名获取POI库提供的图片类型枚举值
     * @param extension 扩展名
     * @return 图片类型
     */
    public static PictureData.PictureType getPictureType(String extension) {
        if (logger.isDebugEnabled()) {
            logger.debug(extension + " extension");
        }
        if (".png".equals(extension)) {
            return PictureData.PictureType.PNG;
        } else if (".jpg".equals(extension)) {
            return PictureData.PictureType.JPEG;
        } else if (".gif".equals(extension)) {
            return PictureData.PictureType.GIF;
        } else if (".bmp".equals(extension)) {
            return PictureData.PictureType.BMP;
        }
        return null;
    }

}
