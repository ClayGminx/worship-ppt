package claygminx.worshipppt.common.entity;

import java.util.List;

/**
 * 经文章节实体
 */
public class ScriptureSectionEntity {

    /**
     * 第几章
     */
    private Integer chapter;

    /**
     * 哪些节，若不指定，就使用整章
     */
    private List<Integer> verses;

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public List<Integer> getVerses() {
        return verses;
    }

    public void setVerses(List<Integer> verses) {
        this.verses = verses;
    }

    @Override
    public String toString() {
        return "ScriptureSectionEntity{" +
                "chapter=" + chapter +
                ", verses=" + verses +
                '}';
    }
}
