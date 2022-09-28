package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.common.entity.ReleaseEntity;
import claygminx.worshipppt.components.UpgradeService;
import claygminx.worshipppt.exception.SystemException;
import claygminx.worshipppt.common.Dict;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String checkNewRelease() {
        try {
            logger.debug("检查升级服务");
            String owner = SystemConfig.getString(Dict.GithubProperty.OWNER);
            String repo = SystemConfig.getString(Dict.GithubProperty.REPO);
            int connectTimeout = SystemConfig.getInt(Dict.GithubProperty.CONNECT_TIMEOUT);
            int connectRequestTimeout = SystemConfig.getInt(Dict.GithubProperty.CONNECT_REQUEST_TIMEOUT);
            int responseTimeout = SystemConfig.getInt(Dict.GithubProperty.RESPONSE_TIMEOUT);

            CloseableHttpClient client = HttpClients.createDefault();
            String url = String.format("https://api.github.com/repos/%s/%s/releases/latest", owner, repo);
            logger.debug("GET " + url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/vnd.github+json");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .setConnectionRequestTimeout(connectRequestTimeout, TimeUnit.SECONDS)
                    .setResponseTimeout(responseTimeout, TimeUnit.SECONDS)
                    .build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = client.execute(httpGet);
            logger.debug("请求成功");
            if (HttpStatus.SC_NOT_FOUND == response.getCode()) {
                logger.warn("{} 返回404！", url);
            } else if (HttpStatus.SC_OK == response.getCode()) {
                logger.debug("{} 返回200！", url);
                StringBuilder responseBuilder = new StringBuilder();

                try (Scanner scanner = new Scanner(response.getEntity().getContent(), StandardCharsets.UTF_8.name())) {
                    while (scanner.hasNextLine()) {
                        responseBuilder.append(scanner.nextLine());
                    }
                }
                String responseString = responseBuilder.toString();
                // 转码
                responseString = new String(responseString.getBytes(), System.getProperty("file.encoding"));
                logger.debug(responseString);

                ReleaseEntity remoteReleaseEntity = new Gson().fromJson(responseString, ReleaseEntity.class);
                ReleaseEntity thisReleaseEntity = getThisProjectReleaseEntity();
                if (thisReleaseEntity.compareTo(remoteReleaseEntity) < 0) {
                    // 小于远程发行包版本，所以应提示要升级
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String downloadUrl = getDownloadUrlFromGitHubBody(remoteReleaseEntity.getBody());
                    return "有新的版本" + remoteReleaseEntity.getName() + "\n"
                            + "发布于" + sdf.format(remoteReleaseEntity.getPublished_at()) + "\n"
                            + "新版本下载地址：\n"
                            + downloadUrl;
                } else {
                    logger.debug("该发行包应该是最新的，不用升级");
                }
            } else {
                logger.warn("{} 返回{}", url, response.getCode());
            }
        } catch (Exception e) {
            logger.debug("升级服务出现异常！", e);
            logger.error("升级服务出现异常！");
        }
        return null;
    }

    protected ReleaseEntity getThisProjectReleaseEntity() {
        String projectVersion = SystemConfig.getString(Dict.ProjectProperty.VERSION);
        String sProjectTime = SystemConfig.getString(Dict.ProjectProperty.TIME);
        String projectTimeFormat = SystemConfig.getString(Dict.ProjectProperty.TIME_FORMAT);
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

    /**
     * 从GitHub消息返回体中获取下载地址
     * @param body 消息体
     * @return 下载地址
     */
    protected String getDownloadUrlFromGitHubBody(String body) {
        if (body == null) {
            return "";
        }
        String downloadTitle = SystemConfig.getString(Dict.GithubProperty.DOWNLOAD_TITLE);
        String titleLevel = downloadTitle.split(" ")[0];
        Pattern titlePattern = Pattern.compile("^[#]+");

        String[] strArray = body.split("\r\n");
        StringBuilder returnBuilder = new StringBuilder();
        int j = strArray.length;
        for (int i = 0; i < strArray.length; i++) {
            String str = strArray[i];
            if (str.trim().isEmpty()) {
                continue;
            }
            if (i >= j) {// 在“下载地址”的上下文中
                Matcher matcher = titlePattern.matcher(str);
                if (matcher.find()) {// 是个标题，那么再判断是不是“下载地址”的子标题
                    String group = matcher.group();
                    if (group.length() > titleLevel.length()) {// 子标题
                        returnBuilder.append(str).append('\n');
                    } else {
                        break;
                    }
                } else {
                    returnBuilder.append(str).append('\n');
                }
            } else if (downloadTitle.equals(str)) {// 开始“下载地址”
                j = i + 1;
            }
        }

        String result = returnBuilder.toString();
        if (result.length() > 0 && result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
