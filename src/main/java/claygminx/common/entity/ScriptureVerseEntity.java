package claygminx.common.entity;

/**
 * 一节经文实体
 */
public class ScriptureVerseEntity {

    private Integer versionId;

    private String versionName;

    private Integer bookId;

    private String bookFullName;

    private String bookShortName;

    private Integer chapter;

    private Integer verse;

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
}
