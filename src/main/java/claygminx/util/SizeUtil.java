package claygminx.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 尺寸工具类
 */
public class SizeUtil {

    private final static Logger logger = LogManager.getLogger(SizeUtil.class);

    private SizeUtil() {
    }

    /**
     * 将厘米长度值转换为{@code point}数
     * @param centimetre 厘米长度
     * @return point数
     */
    public static double convertToPoints(double centimetre) {
        double points = centimetre / 0.035275;
        if (logger.isDebugEnabled()) {
            logger.debug("{}cm => {} points", centimetre, points);
        }
        return points;
    }
}
