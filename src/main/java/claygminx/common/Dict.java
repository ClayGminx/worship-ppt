package claygminx.common;

/**
 * 字典
 */
public class Dict {

    private Dict() {
    }

    /**
     * PPT制作属性
     */
    public static class PPTProperty {
        // 全局制作参数
        public final static String GENERAL_WORSHIP_MODEL_PREFIX = "worship.model.";
        public final static String GENERAL_PROCEDURE_XML_PATH = "ppt.general.procedurePath";
        public final static String GENERAL_FONT_FAMILY = "ppt.general.fontFamily";
        public final static String GENERAL_PLACEHOLDER = "ppt.general.placeholder";
        public final static String GENERAL_LANGUAGE = "ppt.general.language";
        // 幻灯片母版
        public final static String MASTER_WORSHIP_FILE_PATH = "ppt.master.worshipFilePath";
        public final static String MASTER_INITIATION_FILE_PATH = "ppt.master.initiationFilePath";
        public final static String MASTER_GENERAL_LAYOUT_NAME = "ppt.master.generalLayout.name";
        public final static String MASTER_INITIATION_POETRY_SLIDE_ORDER = "ppt.master.initiation.poetrySlide.order";
        // 赦罪
        public final static String FORGIVE_SINS_SCRIPTURE_LINE_SPACING = "ppt.forgiveSinsScripture.lineSpacing";
        // 诗歌：歌谱图
        public final static String POETRY_EXTENSION = "ppt.poetry.extension";
        public final static String POETRY_WIDTH = "ppt.poetry.width";
        public final static String POETRY_LEFT = "ppt.poetry.left";
        public final static String POETRY_TOP = "ppt.poetry.top";
        // 诗歌：清单
        public final static String POETRY_CONTENT_X = "ppt.poetryContent.x";
        public final static String POETRY_CONTENT_Y = "ppt.poetryContent.y";
        public final static String POETRY_CONTENT_W = "ppt.poetryContent.w";
        public final static String POETRY_CONTENT_H = "ppt.poetryContent.h";
        public final static String POETRY_CONTENT_COL_COUNT = "ppt.poetryContent.colCount";
        public final static String POETRY_CONTENT_TITLE_FONT_SIZE = "ppt.poetryContent.titleFontSize";
        public final static String POETRY_CONTENT_FONT_SIZE = "ppt.poetryContent.fontSize";
        public final static String POETRY_CONTENT_LEFT_INSET = "ppt.poetryContent.leftInset";
        public final static String POETRY_CONTENT_SPACE_BEFORE = "ppt.poetryContent.spaceBefore";
        // 圣餐：诗歌
        public final static String HOLY_COMMUNION_POETRY_TOP = "ppt.holyCommunion.poetry.top";
        // 圣餐：非会友领餐名单表格
        public final static String HOLY_COMMUNION_NAME_LIST_X = "ppt.holyCommunion.nameList.x";
        public final static String HOLY_COMMUNION_NAME_LIST_Y = "ppt.holyCommunion.nameList.y";
        public final static String HOLY_COMMUNION_NAME_LIST_W = "ppt.holyCommunion.nameList.w";
        public final static String HOLY_COMMUNION_NAME_LIST_H = "ppt.holyCommunion.nameList.h";
        public final static String HOLY_COMMUNION_NAME_LIST_COL_COUNT = "ppt.holyCommunion.nameList.colCount";
        public final static String HOLY_COMMUNION_NAME_LIST_FONT_SIZE = "ppt.holyCommunion.nameList.fontSize";
    }

    /**
     * 数据库属性
     */
    public static class DatabaseProperty {
        public final static String SQLITE_PATH = "SQLite.path";
    }

    /**
     * 经文属性
     */
    public static class ScriptureProperty {
        public final static String PATH = "scripture.path";
        public final static String FORMAT1 = "scripture.format1";
//        public final static String FORMAT2 = "scripture.format2";
//        public final static String FORMAT3 = "scripture.format3";
        public final static String FORMAT4 = "scripture.format4";
        public final static String REGEX = "scripture.regex";
    }

    /**
     * 项目属性
     */
    public static class ProjectProperty {
        public final static String VERSION = "project.version";
        public final static String TIME = "project.time";
        public final static String TIME_FORMAT = "project.time.format";
    }

    /**
     * GitHub属性
     */
    public static class GithubProperty {
        public final static String OWNER = "github.owner";
        public final static String REPO = "github.repo";
        public final static String CONNECT_TIMEOUT = "github.connectTimeout";
        public final static String CONNECT_REQUEST_TIMEOUT = "github.connectRequestTimeout";
        public final static String RESPONSE_TIMEOUT = "github.responseTimeout";
        public final static String DOWNLOAD_TITLE = "github.downloadTitle";
    }

    /**
     * 运行属性
     */
    public static class RunningProperty {
        public final static String SCENE = "running.scene";
        public final static String SCENE_UPGRADE = "running.scene.upgrade";
        public final static String SCENE_PPT = "running.scene.ppt";
    }

    /**
     * 输入参数中的部分
     */
    public static class InputSection {
        public final static String COVER = "封面";
        public final static String PRAY_POETRY = "祷告诗歌";
        public final static String PRACTISE_POETRY = "练唱诗歌";
        public final static String WORSHIP_POETRY = "敬拜诗歌";
        public final static String RESPONSE_POETRY = "回应诗歌";
        public final static String OFFERTORY_POETRY = "奉献诗歌";
        public final static String HOLY_COMMUNION_POETRY = "圣餐诗歌";
        public final static String INITIATION_POETRY = "入会诗歌";
        public final static String SCRIPTURE = "经文";
        public final static String DECLARATION = "宣信";
        public final static String PREACH = "证道";
        public final static String FAMILY_REPORT = "家事报告";
        public final static String HOLY_COMMUNION = "圣餐";
    }

    /**
     * 敬拜模式
     */
    public static class WorshipModel {
        public final static String WITHOUT_HOLY_COMMUNION = "无圣餐";
        public final static String WITHIN_HOLY_COMMUNION = "有圣餐";
        public final static String WITHIN_INITIATION = "有入会";
    }

    /**
     * 封面属性
     */
    public static class CoverKey {
        public final static String MODEL = "敬拜模式";
        public final static String WORSHIP_DATE = "敬拜日期";
        public final static String CHURCH_NAME = "教会名称";
    }

    /**
     * 诗歌集的名称
     */
    public static class POETRY_ALBUM_NAME {
        public final static String PRAY_POETRY = "祷告诗歌";
        public final static String PRACTISE_POETRY = "练唱诗歌";
        public final static String WORSHIP_POETRY = "敬拜诗歌";
        public final static String RESPONSE_POETRY = "回应诗歌";
        public final static String OFFERTORY_POETRY = "奉献诗歌";
        public final static String HOLY_COMMUNION_POETRY = "圣餐诗歌";
        public final static String INITIATION_POETRY = "入会诗歌";
    }

    /**
     * 经文使用场景
     */
    public static class ScriptureScene {
        public final static String SUMMON = "宣召";
        public final static String PUBLIC_PRAY = "公祷";
        public final static String CONFESS = "认罪";
        public final static String FORGIVE_SINS = "赦罪";
        public final static String READING_SCRIPTURE = "读经";
    }

    /**
     * 宣信属性
     */
    public static class DeclarationKey {
        public final static String TITLE = "主题";
        public final static String SPEAKER = "讲员";
    }

    /**
     * 证道属性
     */
    public static class PreachKey {
        public final static String TITLE = "主题";
        public final static String SCRIPTURE = "经文";
    }

    /**
     * 圣餐属性
     */
    public static class HolyCommunionKey {
        public final static String NAME_LIST = "名单";
    }

}
