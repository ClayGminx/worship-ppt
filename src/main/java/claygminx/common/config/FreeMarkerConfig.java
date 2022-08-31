package claygminx.common.config;

import claygminx.exception.SystemException;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.nio.charset.StandardCharsets;

/**
 * FreeMarker模板配置类
 */
public class FreeMarkerConfig {

    private final static Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

    static {
        try {
            configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setClassForTemplateLoading(FreeMarkerConfig.class, "/templates");
        } catch (Exception e) {
            throw new SystemException("无法初始化FreeMarker配置！");
        }
    }

    private FreeMarkerConfig() {
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}
