package claygminx.worshipppt.common.entity;

import java.util.List;

/**
 * 诗歌集实体
 */
public class PoetryAlbumEntity {

    /**
     * 专辑名称
     */
    private String name;

    /**
     * 诗歌列表
     */
    private List<PoetryEntity> poetryList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PoetryEntity> getPoetryList() {
        return poetryList;
    }

    public void setPoetryList(List<PoetryEntity> poetryList) {
        this.poetryList = poetryList;
    }

    @Override
    public String toString() {
        return "PoetryAlbumEntity{" +
                "name='" + name + '\'' +
                ", poetryList=" + poetryList +
                '}';
    }
}