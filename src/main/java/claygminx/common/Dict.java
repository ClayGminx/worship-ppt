package claygminx.common;

/**
 * 字典
 */
public class Dict {

    private Dict() {
    }

    /**
     * 系统属性
     */
    public static class General {
        // 项目属性
        public final static String PROJECT_VERSION = "project.version";
        public final static String PROJECT_TIME = "project.time";
        public final static String PROJECT_TIME_FORMAT = "project.time.format";

        // 运行场景
        public final static String RUNNING_SCENE = "running.scene";
        public final static String RUNNING_SCENE_UPGRADE = "running.scene.upgrade";
        public final static String RUNNING_SCENE_PPT = "running.scene.ppt";

        // 圣经版本
        public final static String SCRIPTURE_VERSION = "scripture.version";

        // 经文格式
        public final static String SCRIPTURE_PATH = "scripture.path";
        public final static String SCRIPTURE_FORMAT1 = "scripture.format1";
        public final static String SCRIPTURE_FORMAT2 = "scripture.format2";
        public final static String SCRIPTURE_FORMAT3 = "scripture.format3";
        public final static String SCRIPTURE_FORMAT4 = "scripture.format4";

        // GitHub
        public final static String OWNER = "github.owner";
        public final static String REPO = "github.repo";

        // PPT
        public final static String WORSHIP_MODEL_PREFIX = "worship.model.";
        public final static String PPT_PROCEDURE_XML_PATH = "ppt.procedure.path";
        public final static String PPT_FONT_FAMILY = "ppt.fontFamily";
        public final static String PPT_PLACEHOLDER = "ppt.placeholder";
        public final static String PPT_TEMPLATE_GENERAL_PATH = "ppt.template.general.path";
        public final static String PPT_TEMPLATE_INITIATION_PATH = "ppt.template.initiation.path";
        public final static String PPT_TEMPLATE_INITIATION_POETRY_SLIDE_ORDER = "ppt.template.initiation.poetrySlide.order";
        public final static String PPT_TEMPLATE_GENERAL_LAYOUT_NAME = "ppt.template.generalLayout.name";
        public final static String PPT_HOLY_COMMUNION_POETRY_TOP = "ppt.holyCommunion.poetry.top";

        // PPT 诗歌
        public final static String PPT_POETRY_EXTENSION = "ppt.poetry.extension";
        public final static String PPT_POETRY_WIDTH = "ppt.poetry.width";
        public final static String PPT_POETRY_LEFT = "ppt.poetry.left";
        public final static String PPT_POETRY_TOP = "ppt.poetry.top";

        // PPT 诗歌清单表格
        public final static String PPT_POETRY_CONTENT_X = "ppt.poetryContent.x";
        public final static String PPT_POETRY_CONTENT_Y = "ppt.poetryContent.y";
        public final static String PPT_POETRY_CONTENT_W = "ppt.poetryContent.w";
        public final static String PPT_POETRY_CONTENT_H = "ppt.poetryContent.h";
        public final static String PPT_POETRY_CONTENT_COL_COUNT = "ppt.poetryContent.colCount";
        public final static String PPT_POETRY_CONTENT_TITLE_FONT_SIZE = "ppt.poetryContent.titleFontSize";
        public final static String PPT_POETRY_CONTENT_FONT_SIZE = "ppt.poetryContent.fontSize";
        public final static String PPT_POETRY_CONTENT_LEFT_INSET = "ppt.poetryContent.leftInset";
        public final static String PPT_POETRY_CONTENT_SPACE_BEFORE = "ppt.poetryContent.spaceBefore";

        // PPT 非会友领餐名单表格
        public final static String PPT_HOLY_COMMUNION_NAME_LIST_X = "ppt.holyCommunion.nameList.x";
        public final static String PPT_HOLY_COMMUNION_NAME_LIST_Y = "ppt.holyCommunion.nameList.y";
        public final static String PPT_HOLY_COMMUNION_NAME_LIST_W = "ppt.holyCommunion.nameList.w";
        public final static String PPT_HOLY_COMMUNION_NAME_LIST_H = "ppt.holyCommunion.nameList.h";
        public final static String PPT_HOLY_COMMUNION_NAME_LIST_COL_COUNT = "ppt.holyCommunion.nameList.colCount";
        public final static String PPT_HOLY_COMMUNION_NAME_LIST_FONT_SIZE = "ppt.holyCommunion.nameList.fontSize";
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
     * 封面属性
     */
    public static class CoverKey {
        public final static String MODEL = "敬拜模式";
        public final static String WORSHIP_DATE = "敬拜日期";
        public final static String CHURCH_NAME = "教会名称";
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
