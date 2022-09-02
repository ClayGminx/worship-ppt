package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.ReleaseEntity;
import claygminx.components.UpgradeService;
import claygminx.exception.SystemException;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static claygminx.common.Dict.General.*;

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
            logger.debug("检查升级服务");
            String owner = SystemConfig.getString(OWNER);
            String repo = SystemConfig.getString(REPO);
            CloseableHttpClient client = HttpClients.createDefault();
            String url = String.format("https://api.github.com/repos/%s/%s/releases/latest", owner, repo);
            logger.debug("GET " + url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/vnd.github+json");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5, TimeUnit.SECONDS)
                    .setConnectionRequestTimeout(30, TimeUnit.SECONDS)
                    .setResponseTimeout(30, TimeUnit.SECONDS)
                    .build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = client.execute(httpGet);
            logger.debug("请求成功");
            if (HttpStatus.SC_NOT_FOUND == response.getCode()) {
                logger.warn("{} 返回404！", url);
            } else if (HttpStatus.SC_OK == response.getCode()) {
                logger.debug("{} 返回200！", url);
                StringBuilder responseBuilder = new StringBuilder();
                try (Scanner scanner = new Scanner(response.getEntity().getContent())) {
                    while (scanner.hasNextLine()) {
                        responseBuilder.append(scanner.nextLine());
                    }
                }
                String responseString = responseBuilder.toString();
                logger.debug(responseString);

                ReleaseEntity remoteReleaseEntity = new Gson().fromJson(responseString, ReleaseEntity.class);
                ReleaseEntity thisReleaseEntity = getThisProjectReleaseEntity();
                if (thisReleaseEntity.compareTo(remoteReleaseEntity) < 0) {
                    // 小于远程发行包版本，所以应提示要升级
                    logger.info("温馨提示");
                    logger.info("有新的版本" + remoteReleaseEntity.getName());
                    logger.info("发布于" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(remoteReleaseEntity.getPublished_at()));
                    logger.info("新版本下载地址：" + remoteReleaseEntity.getZipball_url());
                } else {
                    logger.debug("该发行包应该是最新的，不用升级");
                }
            } else {
                logger.warn("{} 返回{}", url, response.getCode());
            }
        } catch (Exception e) {
            logger.warn("升级服务出现异常！");
            logger.debug("", e);
        }
    }

    protected ReleaseEntity getThisProjectReleaseEntity() {
        String projectVersion = SystemConfig.getString(PROJECT_VERSION);
        String sProjectTime = SystemConfig.getString(PROJECT_TIME);
        String projectTimeFormat = SystemConfig.getString(PROJECT_TIME_FORMAT);
        try {
            Date oProjectTime = new SimpleDateFormat(projectTimeFormat).parse(sProjectTime);
            ReleaseEntity releaseEntity = new ReleaseEntity();
            releaseEntity.setPublished_at(oProjectTime);
            releaseEntity.setName(projectVersion);
            return releaseEntity;
        } catch (ParseException e) {
            String message = String.format("格式化发行包的构建时间时出错，构建时间是%s，时间格式是%s", sProjectTime, projectTimeFormat);
            throw new SystemException(message, e);
        }
    }
}
