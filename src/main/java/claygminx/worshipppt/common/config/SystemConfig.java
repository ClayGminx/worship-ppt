package claygminx.worshipppt.common.config;

import claygminx.worshipppt.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * 系统配置
 * <p>使用用户目录。此软件的配置文件夹的名称是.worship-ppt，在里面放配置数据。</p>
 */
@Slf4j
public class SystemConfig {

    /**
     * 应用配置文件夹
     */
    public final static String APP_CONFIG_DIR_PATH = ".worship-ppt";

    /**
     * 配置文件的名称
     */
    public final static String APP_CONFIG_NAME = "system.config";

    /**
     * 核心配置
     */
    public final static String CORE_PROPERTIES = "core.properties";

    /**
     * 禁止用户自定义，只能是jar包内定义的配置参数
     */
    private final static String[] EXCLUDE_NAMESPACE = new String[] {
            "github", "gitee", "project"
    };

    /**
     * 系统属性实例对象
     */
    public final static Properties properties = new Properties();

    static {
        // 1.先去用户目录看是否已经有配置
        File appDir = new File(System.getProperty("user.home"), APP_CONFIG_DIR_PATH);
        if (!appDir.exists()) {
            logger.info("创建目录{}", APP_CONFIG_DIR_PATH);
            boolean flag = appDir.mkdirs();
            if (!flag) {
                logger.error("目录{}创建失败，系统退出！", appDir.getAbsolutePath());
                System.exit(1);
            }
        }

        File systemConfigFile = new File(appDir, APP_CONFIG_NAME);
        ClassLoader classLoader = SystemConfig.class.getClassLoader();
        if (systemConfigFile.exists()) {
            logger.info("配置文件已经存在，直接使用。");
        } else {
            logger.info("用户目录不存在配置文件，拷贝一份过去。");
            try (InputStream inputStream = classLoader.getResourceAsStream(APP_CONFIG_NAME)) {
                if (inputStream != null) {
                    FileUtils.copyToFile(inputStream, systemConfigFile);
                } else {
                    logger.error("配置文件初始化失败，系统退出！");
                    System.exit(1);
                }
            } catch (Exception e) {
                logger.error("配置文件初始化失败，系统退出！", e);
                System.exit(1);
            }
        }

        // 2.读取配置文件的路径
        String systemPropertiesPath = null;
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(systemConfigFile), StandardCharsets.UTF_8)) {
            Properties cacheSystemConfig = new Properties();
            cacheSystemConfig.load(reader);
            systemPropertiesPath = cacheSystemConfig.getProperty("SystemConfigPath");
            logger.info("SystemConfigPath={}", systemPropertiesPath);
        } catch (Exception e) {
            logger.error("读取SystemConfigPath失败！", e);
            System.exit(1);
        }

        // 3.加载用户配置
        Properties userProperties = null;
        try {
            userProperties = loadUserProperties(systemPropertiesPath);
        } catch (Exception e) {
            logger.error("用户配置加载失败！", e);
            System.exit(1);
        }

        // 4.加载核心配置
        Properties coreProperties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(classLoader.getResourceAsStream(CORE_PROPERTIES)),
                StandardCharsets.UTF_8)) {
            coreProperties.load(reader);
            logger.info("核心配置加载成功");

            Set<Object> keySet = coreProperties.keySet();
            for (Object key : keySet) {
                logger.info("{}={}", key, coreProperties.get(key));
            }
        } catch (Exception e) {
            logger.error("{}加载失败！", CORE_PROPERTIES, e);
            System.exit(1);
        }

        // 5.合并配置
        try {
            properties.putAll(coreProperties);
            logger.info("合并了核心配置");

            mergeUserProperties(userProperties);
        } catch (Exception e) {
            logger.error("合并配置失败！", e);
            System.exit(1);
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

    /**
     * 获取int值
     * @param key 键
     * @return 系统值
     */
    public static int getInt(String key) {
        String strValue = properties.getProperty(key);
        try {
            return Integer.parseInt(strValue);
        } catch (Exception e) {
            throw new SystemException("根据" + key + "获取int值失败！", e);
        }
    }

    /**
     * 获取double值
     * @param key 键
     * @return 系统值
     */
    public static double getDouble(String key) {
        String strValue = properties.getProperty(key);
        try {
            return Double.parseDouble(strValue);
        } catch (Exception e) {
            throw new SystemException("根据" + key + "获取double值失败！", e);
        }
    }

    public static void update(String propFilePath) throws IOException {
        String conf = "SystemConfigPath=" + propFilePath;
        File appDir = new File(System.getProperty("user.home"), APP_CONFIG_DIR_PATH);
        File systemConfigFile = new File(appDir, APP_CONFIG_NAME);
        FileUtils.writeStringToFile(systemConfigFile, conf, StandardCharsets.UTF_8);
        Properties userProperties = loadUserProperties(propFilePath);
        mergeUserProperties(userProperties);
    }

    private static Properties loadUserProperties(String propFilePath) throws IOException {
        Properties userProperties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(propFilePath), StandardCharsets.UTF_8)) {
            userProperties.load(reader);
            logger.info("用户配置加载成功");

            Set<Object> keySet = userProperties.keySet();
            for (Object key : keySet) {
                logger.info("{}={}", key, userProperties.get(key));
            }
        }
        return userProperties;
    }

    private static void mergeUserProperties(Properties userProperties) {
        Set<Object> keySet = userProperties.keySet();
        for (Object keyObj : keySet) {
            String key = (String) keyObj;
            String[] split = key.split("[.]");
            String ns = split[0];
            boolean found = false;
            for (int i = 0; i < EXCLUDE_NAMESPACE.length; i++) {
                if (EXCLUDE_NAMESPACE[i].equals(ns)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                logger.info("跳过{}", key);
            } else {
                properties.put(key, userProperties.get(key));
            }
        }
        logger.info("合并了用户配置");
    }

}
