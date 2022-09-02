package claygminx.common.entity;

/**
 * 一节经文实体
 */
public class ScriptureVerseEntity {

    /**
     * 译本编号
     */
    private Integer versionId;

    /**
     * 译本名称
     */
    private String versionName;

    /**
     * 书卷编号
     */
    private Integer bookId;

    /**
     * 书卷全称
     */
    private String bookFullName;

    /**
     * 书卷简称
     */
    private String bookShortName;

    /**
     * 章
     */
    private Integer chapter;

    /**
     * 节
     */
    private Integer verse;

    /**
     * 经文
     */
    private String scripture;

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookFullName() {
        return bookFullName;
    }

    public void setBookFullName(String bookFullName) {
        this.bookFullName = bookFullName;
    }

    public String getBookShortName() {
        return bookShortName;
    }

    public void setBookShortName(String bookShortName) {
        this.bookShortName = bookShortName;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getVerse() {
        return verse;
    }

    public void setVerse(Integer verse) {
        this.verse = verse;
    }

    public String getScripture() {
        return scripture;
    }

    public void setScripture(String scripture) {
        this.scripture = scripture;
    }

    @Override
    public String toString() {
        return "ScriptureVerseEntity{" +
                "versionId=" + versionId +
                ", versionName='" + versionName + '\'' +
                ", bookId=" + bookId +
                ", bookFullName='" + bookFullName + '\'' +
                ", bookShortName='" + bookShortName + '\'' +
                ", chapter=" + chapter +
                ", verse=" + verse +
                ", scripture='" + scripture + '\'' +
                '}';
    }
}
