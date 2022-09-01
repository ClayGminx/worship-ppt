package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.ReleaseEntity;
import claygminx.components.UpgradeService;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static claygminx.common.Dict.GitHub.*;

public class UpgradeServiceImpl implements UpgradeService {

    private final static Logger logger = LoggerFactory.getLogger(UpgradeService.class);

    private static UpgradeService upgradeService;

    /**
     * 获取实例对象
     * @return 升级服务实例对象
     */
    public static UpgradeService getInstance() {
        if (upgradeService == null) {
            upgradeService = new UpgradeServiceImpl();
        }
        return upgradeService;
    }

    @Override
    public void checkNewRelease() {
        try {
            String owner = SystemConfig.properties.getProperty(OWNER);
            String repo = SystemConfig.properties.getProperty(REPO);
            CloseableHttpClient client = HttpClients.createDefault();
            String url = String.format("https://api.github.com/repos/%s/%s/releases/latest", owner, repo);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/vnd.github+json");
            CloseableHttpResponse response = client.execute(httpGet);
            if (HttpStatus.SC_NOT_FOUND == response.getCode()) {
                logger.warn("{} 返回404！", url);
            } else if (HttpStatus.SC_OK == response.getCode()) {
                StringBuilder responseBuilder = new StringBuilder();
                try (Scanner scanner = new Scanner(response.getEntity().getContent())) {
                    while (scanner.hasNextLine()) {
                        responseBuilder.append(scanner.nextLine());
                    }
                }
                ReleaseEntity releaseEntity = new Gson().fromJson(responseBuilder.toString(), ReleaseEntity.class);
                System.out.println(releaseEntity);
            }
        } catch (Exception e) {
            logger.warn("升级服务出现异常！");
            logger.debug("", e);
        }
    }
}
