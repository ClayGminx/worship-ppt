package claygminx.worshipppt.common.entity;

import java.util.List;

/**
 * 一卷经文实体
 */
public class ScriptureBookEntity {

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
     * 经节列表
     */
    private List<ScriptureVerseEntity> scriptureVerseList;

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

    public List<ScriptureVerseEntity> getScriptureVerseList() {
        return scriptureVerseList;
    }

    public void setScriptureVerseList(List<ScriptureVerseEntity> scriptureVerseList) {
        this.scriptureVerseList = scriptureVerseList;
    }

    @Override
    public String toString() {
        return "ScriptureBookEntity{" +
                "versionId=" + versionId +
                ", versionName='" + versionName + '\'' +
                ", bookId=" + bookId +
                ", bookFullName='" + bookFullName + '\'' +
                ", bookShortName='" + bookShortName + '\'' +
                ", scriptureVerseList=" + scriptureVerseList +
                '}';
    }
}
