package claygminx.util;

import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.common.entity.ScriptureSectionEntity;
import claygminx.exception.ScriptureNumberException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 经文小工具
 */
public class ScriptureUtil {

    public final static String SCRIPTURE_NUMBER_BOOK_SPLITTER = ";";

    private final static Pattern HTML_PATTERN = Pattern.compile("</?[a-zA-Z]+>");

    private final static Pattern GOD_PATTERN = Pattern.compile("　神");

    private final static Pattern SELAH_PATTERN = Pattern.compile("（细拉）");

    private ScriptureUtil() {
    }

    /**
     * 移除经文中的HTML标签
     * @param scripture 经文
     * @return 经文
     */
    public static String removeHtmlTag(String scripture) {
        return HTML_PATTERN.matcher(scripture).replaceAll("");
    }

    /**
     * 解析可能带有多卷的经文编号
     * @param arg 经文编号
     * @return 经文编号实体列表
     * @throws ScriptureNumberException
     */
    public static List<ScriptureNumberEntity> parseNumbers(String arg) throws ScriptureNumberException {
        String[] scriptureNumberArray = arg.split(ScriptureUtil.SCRIPTURE_NUMBER_BOOK_SPLITTER);
        List<ScriptureNumberEntity> publicScriptureNumberList = new ArrayList<>(scriptureNumberArray.length);
        for (String scriptureNumberItem : scriptureNumberArray) {
            ScriptureNumberEntity scriptureNumberEntity = ScriptureUtil.parseNumber(scriptureNumberItem);
            publicScriptureNumberList.add(scriptureNumberEntity);
        }
        return publicScriptureNumberList;
    }

    /**
     *
     * @param arg
     * @return
     * @throws ScriptureNumberException
     */
    public static ScriptureNumberEntity parseNumber(String arg) throws ScriptureNumberException {
        char[] chars = arg.toCharArray();
        int sectionsStartIndex = -1;
        // 应从第二个字符开始
        for (int i  = 1; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '1' && c <= '9') {
                sectionsStartIndex = i;
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

        return result;
    }

    /**
     * 解析章节
     * <p>规则：</p>
     * @param sections 字符串形式的章节
     * @return 章节
     */
    public static List<ScriptureSectionEntity> parseSections(String sections) throws ScriptureNumberException {
        // 章节的第一分隔符是逗号
        String[] splitResult = sections.split(",");
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
     * @param scripture 经文
     * @return 经文
     */
    public static String simplifyScripture(String scripture) {
        String result = removeHtmlTag(scripture);
        result = GOD_PATTERN.matcher(result).replaceAll("神");
        result = SELAH_PATTERN.matcher(result).replaceAll("");
        return result;
    }

    /**
     * 是否中文标点符号
     * @param c 字符
     * @return 是中文标点符号就返回true
     */
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS;
    }

    /**
     *
     * @param sections
     * @param sectionList
     * @throws ScriptureNumberException
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
            throw new ScriptureNumberException(sectionArray[0] + "不是数字！");
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
     *
     * @param digit
     * @param type
     * @param sectionList
     * @throws ScriptureNumberException
     */
    private static void parseDigitSection(String digit, String type, List<ScriptureSectionEntity> sectionList) throws ScriptureNumberException {
        int n;
        try {
            n = Integer.parseInt(digit);
        } catch (NumberFormatException e) {
            throw new ScriptureNumberException(digit + "不是数字！");
        }

        addSection(n, type, sectionList);
    }

    private static void parseSectionWithinDash(String sections, String type, List<ScriptureSectionEntity> sectionList) throws ScriptureNumberException {
        String[] sectionArray = sections.split("-");
        if (sectionArray.length != 2) {
            throw new ScriptureNumberException("短横线（-）用法错误！");
        }

        int start, end;
        try {
            start = Integer.parseInt(sectionArray[0]);
        } catch (NumberFormatException e) {
            throw new ScriptureNumberException(sectionArray[0] + "不是数字");
        }

        try {
            end = Integer.parseInt(sectionArray[1]);
        } catch (NumberFormatException e) {
            throw new ScriptureNumberException(sectionArray[1] + "不是数字");
        }

        for (int i = start; i <= end; i++) {
            addSection(i, type, sectionList);
        }
    }

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
