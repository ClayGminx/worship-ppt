package claygminx.util;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.common.entity.ScriptureSectionEntity;
import claygminx.exception.ScriptureNumberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static claygminx.common.Dict.*;

/**
 * 经文小工具
 */
public class ScriptureUtil {

    private final static Logger logger = LoggerFactory.getLogger(ScriptureUtil.class);

    private ScriptureUtil() {
    }

    /**
     * 解析可能带有多卷的经文编号
     * @param arg 经文编号
     * @return 经文编号实体列表
     * @throws IllegalArgumentException 若参数为空，抛出此异常
     * @throws ScriptureNumberException 若给定参数不符合经文编号格式，抛出此异常
     */
    public static List<ScriptureNumberEntity> parseNumbers(String arg) throws ScriptureNumberException {
        String[] scriptureNumberArray = arg.split(";");
        List<ScriptureNumberEntity> publicScriptureNumberList = new ArrayList<>(scriptureNumberArray.length);
        for (String scriptureNumberItem : scriptureNumberArray) {
            ScriptureNumberEntity scriptureNumberEntity = ScriptureUtil.parseNumber(scriptureNumberItem);
            publicScriptureNumberList.add(scriptureNumberEntity);
        }
        return publicScriptureNumberList;
    }

    /**
     * 解析经文编号
     *
     * <p>
     *     <ol>
     *         <li>有且仅有一个书卷，书卷名可以是全称，也可以是简称；</li>
     *         <li>书卷名称后面跟着章或节，可以只有章，但不可以只有节；</li>
     *         <li>如果后面仅跟着章，章和章用英文逗号隔开，若章和章是连续的，可以用英文短横线连接；</li>
     *         <li>章和节开始用英文冒号隔开；</li>
     *         <li>节和节的分隔符，跟章和章一样，用英文逗号连接连续节，或者用英文逗号分隔节和节。</li>
     *     </ol>
     * </p>
     *
     * <p>例如：</p>
     * <ol>
     *     <li>创世记1:1</li>
     *     <li>创1:1</li>
     *     <li>诗42-43</li>
     *     <li>诗42,43</li>
     *     <li>创1:1-5,7,9,11-15,2:1-3,5:1-5</li>
     * </ol>
     *
     * @param arg 字符串形式的经文编号
     * @return 经文编号实体。注意，经文编号里的章和节是否存在，还没有经过验证。
     * @throws IllegalArgumentException 若参数为空，抛出此异常
     * @throws ScriptureNumberException 若给定参数不符合经文编号格式，抛出此异常
     */
    public static ScriptureNumberEntity parseNumber(String arg) throws ScriptureNumberException {
        if (arg == null || arg.isEmpty()) {
            throw new IllegalArgumentException("参数不可为空！");
        }

        logger.debug("开始解析经文编号");
        char[] chars = arg.toCharArray();
        int sectionsStartIndex = -1;
        // 应从第二个字符开始，因为通常而言，第一个字符是书卷名称的一部分
        for (int i  = 1; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '1' && c <= '9') {
                sectionsStartIndex = i;
                logger.debug("章的起始索引是" + sectionsStartIndex);
                break;
            }
        }
        if (sectionsStartIndex == -1) {
            throw new ScriptureNumberException("没有章节！");
        }

        ScriptureNumberEntity result = new ScriptureNumberEntity(arg);
        String bookName = arg.substring(0, sectionsStartIndex).trim();
        String sections = arg.substring(sectionsStartIndex).trim();
        List<ScriptureSectionEntity> sectionList = parseSections(sections);

        result.setBookFullName(bookName);
        result.setBookShortName(bookName);
        result.setScriptureSections(sectionList);
        logger.debug("经文编号解析完成");

        return result;
    }

    /**
     * 解析章节
     * <p>规则：</p>
     * <ol>
     *     <li>章节的第一分隔符是逗号（,）；</li>
     *     <li>短横线（-）既可以连接章和章，也可以连接节和节，不可以连接章和节；</li>
     *     <li>冒号（:）仅能连接章和节，左边是章，右边是节。</li>
     * </ol>
     * @param sections 字符串形式的章节
     * @return 章节列表
     * @throws ScriptureNumberException 若给定参数不符合经文编号格式，抛出此异常
     */
    public static List<ScriptureSectionEntity> parseSections(String sections) throws ScriptureNumberException {
        logger.debug("开始章节");
        // 章节的第一分隔符是逗号
        String[] splitResult = sections.split(",");
        logger.debug("逗号分割后有{}个部分", splitResult.length);
        // 返回结果
        List<ScriptureSectionEntity> sectionList = new ArrayList<>(splitResult.length);
        // 当前解析类型，要么是chapter，要么是verse
        String currentType = "chapter";
        // 遍历分割结果
        for (String splitItem : splitResult) {
            if (splitItem.contains(":")) {
                parseSectionWithColon(splitItem, sectionList);
                currentType = "verse";
            } else if (splitItem.contains("-")) {
                parseSectionWithinDash(splitItem, currentType, sectionList);
            } else {
                parseDigitSection(splitItem, currentType, sectionList);
            }
        }
        return sectionList;
    }

    /**
     * 精简经文
     * @param scripture 未精简的经文
     * @return 精简后的经文
     */
    public static String simplifyScripture(String scripture) {
        String pattern = SystemConfig.getString(General.SCRIPTURE_REGEX);
        return scripture.replaceAll(pattern, "");
    }

    /**
     * 解析由冒号（:）连接的章节
     * @param sections 章节
     * @param sectionList 章节列表
     * @throws ScriptureNumberException 若章节格式不是“章:节”或"章:节-节"，则抛出此异常
     */
    private static void parseSectionWithColon(String sections, List<ScriptureSectionEntity> sectionList) throws ScriptureNumberException {
        String[] sectionArray = sections.split(":");
        if (sectionArray.length != 2) {
            throw new ScriptureNumberException("冒号（:）用法错误！");
        }

        int chapter;
        try {
            chapter = Integer.parseInt(sectionArray[0]);
        } catch (NumberFormatException e) {
            throw new ScriptureNumberException(sectionArray[0] + "不是数字！", e);
        }

        List<Integer> verses = new ArrayList<>();
        ScriptureSectionEntity entity = new ScriptureSectionEntity();
        entity.setChapter(chapter);
        entity.setVerses(verses);
        sectionList.add(entity);

        if (sectionArray[1].contains("-")) {
            parseSectionWithinDash(sectionArray[1], "verse", sectionList);
        } else {
            parseDigitSection(sectionArray[1], "verse", sectionList);
        }
    }

    /**
     * 将章号或节号添加到列表
     * @param digit 字符串形式的章号，或节号
     * @param type chapter，或verse
     * @param sectionList 章节列表
     * @throws ScriptureNumberException 若{@code digit}不是整型数字，则抛出此异常
     */
    private static void parseDigitSection(String digit, String type, List<ScriptureSectionEntity> sectionList) throws ScriptureNumberException {
        try {
            addSection(Integer.parseInt(digit), type, sectionList);
        } catch (NumberFormatException e) {
            throw new ScriptureNumberException(digit + "不是数字！", e);
        }
    }

    /**
     * 解析由短横线（-）连接的章节部分
     * @param sections 章节
     * @param type chapter，或verse
     * @param sectionList 章节列表
     * @throws ScriptureNumberException 若给定章节参数的格式不是“数字-数字”，则抛出此异常
     */
    private static void parseSectionWithinDash(String sections, String type, List<ScriptureSectionEntity> sectionList) throws ScriptureNumberException {
        String[] sectionArray = sections.split("-");
        if (sectionArray.length != 2) {
            throw new ScriptureNumberException("短横线（-）用法错误！");
        }

        int start, end;
        try {
            start = Integer.parseInt(sectionArray[0]);
        } catch (NumberFormatException e) {
            throw new ScriptureNumberException(sectionArray[0] + "不是数字", e);
        }

        try {
            end = Integer.parseInt(sectionArray[1]);
        } catch (NumberFormatException e) {
            throw new ScriptureNumberException(sectionArray[1] + "不是数字", e);
        }

        for (int i = start; i <= end; i++) {
            addSection(i, type, sectionList);
        }
    }

    /**
     * 将章节添加到列表中
     * @param n 章号，或节号
     * @param type chapter，或verse
     * @param sectionList 章节列表
     */
    private static void addSection(int n, String type, List<ScriptureSectionEntity> sectionList) {
        if ("chapter".equals(type)) {
            ScriptureSectionEntity section = new ScriptureSectionEntity();
            section.setChapter(n);
            sectionList.add(section);
        } else {
            ScriptureSectionEntity section = sectionList.get(sectionList.size() - 1);
            List<Integer> verses;
            if (section.getVerses() == null) {
                verses = new ArrayList<>();
                section.setVerses(verses);
            } else {
                verses = section.getVerses();
            }
            verses.add(n);
        }
    }

}
