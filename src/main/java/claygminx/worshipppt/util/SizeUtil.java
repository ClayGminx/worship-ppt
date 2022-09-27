package claygminx.worshipppt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 尺寸工具类
 */
public class SizeUtil {

    private final static Logger logger = LoggerFactory.getLogger(SizeUtil.class);

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
