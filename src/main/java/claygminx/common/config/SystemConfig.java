package claygminx.common.config;

import claygminx.exception.SystemException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * 系统配置
 */
public class SystemConfig {

    public final static String PROPERTIES_FILE_NAME = "worship-ppt.properties";

    public final static Properties properties = new Properties();

    static {
        ClassLoader classLoader = SystemConfig.class.getClassLoader();
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(classLoader.getResourceAsStream(PROPERTIES_FILE_NAME)), StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (Exception e) {
            throw new SystemException(PROPERTIES_FILE_NAME + "加载失败！");
        }
    }

    private SystemConfig() {
    }

    /**
     * 获取字符串值
     * @param key 键
     * @return 系统值
     */
    public static String getString(String key) {
        return properties.getProperty(key);
    }

}
