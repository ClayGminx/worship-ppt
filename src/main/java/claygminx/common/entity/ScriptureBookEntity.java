package claygminx.common.entity;

import java.util.List;

/**
 * 一卷经文实体
 */
public class ScriptureBookEntity {

    private Integer versionId;

    private String versionName;

    private Integer bookId;

    private String bookFullName;

    private String bookShortName;

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
}
