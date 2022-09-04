package claygminx.components.impl;

import claygminx.common.config.FreeMarkerConfig;
import claygminx.common.config.SystemConfig;
import claygminx.common.entity.*;
import claygminx.components.ScriptureService;
import claygminx.exception.ScriptureNumberException;
import claygminx.exception.ScriptureServiceException;
import claygminx.exception.SystemException;
import claygminx.util.ScriptureUtil;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static claygminx.common.Dict.*;

/**
 * 经文服务实体
 */
public class ScriptureServiceImpl implements ScriptureService {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SystemException("无法加载org.sqlite.JDBC！", e);
        }

        String bibleVersion = SystemConfig.getString(General.SCRIPTURE_VERSION);
        if (bibleVersion == null) {
            throw new SystemException("未设置圣经版本！");
        }
        BIBLE_VERSION = bibleVersion;
    }

    private final static Logger logger = LoggerFactory.getLogger(ScriptureService.class);

    private static ScriptureService scriptureService;

    private final static String BIBLE_VERSION;

    private ScriptureServiceImpl() {
    }

    /**
     * 获取经文服务实例对象
     * @return 经文服务实例对象
     */
    public static ScriptureService getInstance() {
        if (scriptureService == null) {
            logger.debug("实例化经文服务");
            scriptureService = new ScriptureServiceImpl();
        }
        return scriptureService;
    }

    @Override
    public int getIdFromBookName(String fullName, String shortName) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT BookId FROM BookNames WHERE FullName=? OR AbbrName=?");
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, shortName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int bookId = resultSet.getInt(1);
                logger.debug("Query by {} and {}, BookId is {}", fullName, shortName, bookId);
                return bookId;
            }
        } catch (SQLException e) {
            throw new SystemException("数据库异常！", e);
        }
        return 0;
    }

    @Override
    public String[] getBookNameFromId(int bookId) {
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT FullName,AbbrName FROM BookNames WHERE BookId=?");
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String[] result = new String[2];
                result[0] = resultSet.getString(1);
                result[1] = resultSet.getString(2);
                logger.debug("BookId={}, FullName is {}, ShortName is {}", bookId, result[0], result[1]);
                return result;
            }
        } catch (SQLException e) {
            throw new SystemException("数据库异常！", e);
        }
        return null;
    }

    @Override
    public boolean validateNumber(ScriptureNumberEntity scriptureNumberEntity) {
        String bookFullName = Optional.ofNullable(scriptureNumberEntity.getBookFullName()).orElse("");
        String bookShortName = Optional.ofNullable(scriptureNumberEntity.getBookShortName()).orElse("");
        int bookId = getIdFromBookName(bookFullName, bookShortName);
        if (bookId == 0) {
            logger.debug("{} or {} doesn't exist!", bookFullName, bookShortName);
            return false;
        }

        String[] bookNames = getBookNameFromId(bookId);
        scriptureNumberEntity.setBookId(bookId);
        scriptureNumberEntity.setBookFullName(bookNames[0]);
        scriptureNumberEntity.setBookShortName(bookNames[1]);
        return true;
    }

    @Override
    public ScriptureEntity getScriptureWithFormat(String scriptureNumber, String format) throws ScriptureNumberException {
        ScriptureNumberEntity scriptureNumberEntity = ScriptureUtil.parseNumber(scriptureNumber);
        boolean flag = validateNumber(scriptureNumberEntity);
        if (flag) {
            return getScriptureWithFormat(scriptureNumberEntity, format);
        } else {
            throw new ScriptureServiceException("经文编号格式错误！");
        }
    }

    @Override
    public ScriptureEntity getScriptureWithFormat(ScriptureNumberEntity scriptureNumber, String format) {
        checkScriptureNumber(scriptureNumber);

        List<ScriptureSectionEntity> scriptureSections = scriptureNumber.getScriptureSections();
        List<ScriptureVerseEntity> scriptureVerseEntityList = new ArrayList<>();
        try (Connection connection = getConnection()) {
            for (ScriptureSectionEntity scriptureSection : scriptureSections) {
                List<Integer> verses = scriptureSection.getVerses();
                if (verses == null || verses.isEmpty()) {
                    logger.debug("没有写节，那么直接按章来查询经文");
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT Verse,Scripture FROM Bible WHERE Book=? AND Chapter=?");
                    preparedStatement.setInt(1, scriptureNumber.getBookId());
                    preparedStatement.setInt(2, scriptureSection.getChapter());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String scripture = resultSet.getString(2);
                        scripture = ScriptureUtil.simplifyScripture(scripture);
                        ScriptureVerseEntity scriptureVerseEntity = new ScriptureVerseEntity();
                        scriptureVerseEntity.setBookId(scriptureNumber.getBookId());
                        scriptureVerseEntity.setChapter(scriptureSection.getChapter());
                        scriptureVerseEntity.setVerse(resultSet.getInt(1));
                        scriptureVerseEntity.setScripture(scripture);
                        scriptureVerseEntityList.add(scriptureVerseEntity);
                    }
                } else {
                    for (int i = 0; i < verses.size(); i++) {
                        int verseNumber = verses.get(i);
                        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Scripture FROM Bible WHERE Book=? AND Chapter=? AND Verse=?");
                        preparedStatement.setInt(1, scriptureNumber.getBookId());
                        preparedStatement.setInt(2, scriptureSection.getChapter());
                        preparedStatement.setInt(3, verseNumber);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            String scripture = resultSet.getString(1);
                            scripture = ScriptureUtil.simplifyScripture(scripture);
                            ScriptureVerseEntity scriptureVerseEntity = new ScriptureVerseEntity();
                            scriptureVerseEntity.setBookId(scriptureNumber.getBookId());
                            scriptureVerseEntity.setChapter(scriptureSection.getChapter());
                            scriptureVerseEntity.setVerse(verseNumber);
                            scriptureVerseEntity.setScripture(scripture);
                            scriptureVerseEntityList.add(scriptureVerseEntity);
                        }
                    }
                }
            }

            if (scriptureVerseEntityList.isEmpty()) {
                logger.warn("查无经文！");
                return null;
            } else {
                ScriptureBookEntity scriptureBookEntity = new ScriptureBookEntity();
                scriptureBookEntity.setBookId(scriptureNumber.getBookId());
                scriptureBookEntity.setBookFullName(scriptureNumber.getBookFullName());
                scriptureBookEntity.setBookShortName(scriptureNumber.getBookShortName());
                scriptureBookEntity.setScriptureVerseList(scriptureVerseEntityList);

                Configuration configuration = FreeMarkerConfig.getConfiguration();
                Template template = configuration.getTemplate(format);
                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     Writer out = new OutputStreamWriter(byteArrayOutputStream)) {
                    template.process(scriptureBookEntity, out);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    String scripture = new String(bytes);
                    ScriptureEntity scriptureEntity = new ScriptureEntity();
                    scriptureEntity.setScriptureNumber(scriptureNumber);
                    scriptureEntity.setScripture(scripture);
                    return scriptureEntity;
                }
            }
        } catch (SQLException e) {
            logger.error("查询经文时发生异常！", e);
            throw new SystemException("查询经文时发生异常！");
        } catch (IOException | TemplateException e) {
            logger.error("格式化经文时发生异常！", e);
            throw new SystemException("格式化经文时发生异常!");
        }
    }

    /**
     * 检查必备参数
     * @param scriptureNumber 经文编号
     */
    private void checkScriptureNumber(ScriptureNumberEntity scriptureNumber) {
        logger.debug("开始检查经文实体参数的有效性");
        if (scriptureNumber.getBookId() == null) {
            throw new ScriptureServiceException("未提供书卷序号！");
        }
        List<ScriptureSectionEntity> scriptureSections = scriptureNumber.getScriptureSections();
        if (scriptureSections == null || scriptureSections.isEmpty()) {
            throw new ScriptureServiceException("未提供章节！");
        }
        logger.debug("检查通过");
    }

    /**
     * 获取SQLite获取连接
     * @return 数据库连接，记得要关闭该连接
     */
    private Connection getConnection() {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("assets/sqlite/" + BIBLE_VERSION + ".db");
            return DriverManager.getConnection("jdbc:sqlite::resource:" + url);
        } catch (SQLException e) {
            throw new SystemException("从" + BIBLE_VERSION + "获取连接失败！", e);
        }
    }

}
