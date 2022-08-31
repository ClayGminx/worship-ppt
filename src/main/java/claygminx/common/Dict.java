package claygminx.common;

/**
 * 字典
 */
public class Dict {

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
     * 经文格式
     */
    public enum ScriptureFormat {
        FORMAT_1("scripture-format1.ftl"),
        FORMAT_2("scripture-format2.ftl"),
        FORMAT_3("scripture-format3.ftl"),
        FORMAT_4("scripture-format4.ftl");

        private String value;

        ScriptureFormat(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 圣餐属性
     */
    public static class HolyCommunionKey {
    }

}
