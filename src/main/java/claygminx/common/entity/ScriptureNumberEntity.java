package claygminx.common.entity;

import java.util.List;

/**
 * 经文编号实体
 */
public class ScriptureNumberEntity {

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
     * 章节
     */
    private List<ScriptureSectionEntity> scriptureSections;

    private final String value;

    public ScriptureNumberEntity(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("参数不可为空！");
        }
        this.value = value;
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

    public List<ScriptureSectionEntity> getScriptureSections() {
        return scriptureSections;
    }

    public void setScriptureSections(List<ScriptureSectionEntity> scriptureSections) {
        this.scriptureSections = scriptureSections;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
