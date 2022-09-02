package claygminx.common.config;

import claygminx.common.Dict;
import claygminx.exception.SystemException;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.nio.charset.StandardCharsets;

/**
 * FreeMarker模板配置类
 */
public class FreeMarkerConfig {

    /**
     * FreeMarker核心配置
     */
    private final static Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

    static {
        try {
            configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setClassForTemplateLoading(FreeMarkerConfig.class, Dict.General.FREEMARKER_PATH);
        } catch (Exception e) {
            throw new SystemException("无法初始化FreeMarker配置！", e);
        }
    }

    private FreeMarkerConfig() {
    }

    /**
     * 获取FreeMarker配置实例对象
     * @return FreeMarker配置实例对象
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

}
