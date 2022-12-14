package claygminx.worshipppt.common.config;

import claygminx.worshipppt.common.Dict;
import claygminx.worshipppt.exception.SystemException;
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
            String templatePath = SystemConfig.getString(Dict.ScriptureProperty.PATH);
            configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setClassForTemplateLoading(FreeMarkerConfig.class, templatePath);
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
