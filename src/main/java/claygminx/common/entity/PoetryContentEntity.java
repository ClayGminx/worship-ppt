package claygminx.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 诗歌集实体
 */
public class PoetryContentEntity {

    /**
     * 祷告诗歌
     */
    private PoetryAlbumEntity prayPoetryAlbum;

    /**
     * 练唱诗歌
     */
    private PoetryAlbumEntity practisePoetryAlbum;

    /**
     * 敬拜诗歌
     */
    private PoetryAlbumEntity worshipPoetryAlbum;

    /**
     * 回应诗歌
     */
    private PoetryAlbumEntity responsePoetryAlbum;

    /**
     * 奉献诗歌
     */
    private PoetryAlbumEntity offertoryPoetryAlbum;

    /**
     * 入会诗歌
     */
    private PoetryAlbumEntity initiationPoetryAlbum;

    /**
     * 圣餐诗歌
     */
    private PoetryAlbumEntity holyCommunionPoetryAlbum;

    public PoetryAlbumEntity getPrayPoetryAlbum() {
        return prayPoetryAlbum;
    }

    public void setPrayPoetryAlbum(PoetryAlbumEntity prayPoetryAlbum) {
        this.prayPoetryAlbum = prayPoetryAlbum;
    }

    public PoetryAlbumEntity getPractisePoetryAlbum() {
        return practisePoetryAlbum;
    }

    public void setPractisePoetryAlbum(PoetryAlbumEntity practisePoetryAlbum) {
        this.practisePoetryAlbum = practisePoetryAlbum;
    }

    public PoetryAlbumEntity getWorshipPoetryAlbum() {
        return worshipPoetryAlbum;
    }

    public void setWorshipPoetryAlbum(PoetryAlbumEntity worshipPoetryAlbum) {
        this.worshipPoetryAlbum = worshipPoetryAlbum;
    }

    public PoetryAlbumEntity getResponsePoetryAlbum() {
        return responsePoetryAlbum;
    }

    public void setResponsePoetryAlbum(PoetryAlbumEntity responsePoetryAlbum) {
        this.responsePoetryAlbum = responsePoetryAlbum;
    }

    public PoetryAlbumEntity getOffertoryPoetryAlbum() {
        return offertoryPoetryAlbum;
    }

    public void setOffertoryPoetryAlbum(PoetryAlbumEntity offertoryPoetryAlbum) {
        this.offertoryPoetryAlbum = offertoryPoetryAlbum;
    }

    public PoetryAlbumEntity getInitiationPoetryAlbum() {
        return initiationPoetryAlbum;
    }

    public void setInitiationPoetryAlbum(PoetryAlbumEntity initiationPoetryAlbum) {
        this.initiationPoetryAlbum = initiationPoetryAlbum;
    }

    public PoetryAlbumEntity getHolyCommunionPoetryAlbum() {
        return holyCommunionPoetryAlbum;
    }

    public void setHolyCommunionPoetryAlbum(PoetryAlbumEntity holyCommunionPoetryAlbum) {
        this.holyCommunionPoetryAlbum = holyCommunionPoetryAlbum;
    }

    /**
     * 导出诗歌清单
     * @return 诗歌清单，列表里每个元素不可能是{@code null}
     */
    public List<PoetryAlbumEntity> export() {
        List<PoetryAlbumEntity> list = new ArrayList<>(7);
        if (prayPoetryAlbum != null) {
            list.add(prayPoetryAlbum);
        }
        if (practisePoetryAlbum != null) {
            list.add(practisePoetryAlbum);
        }
        if (worshipPoetryAlbum != null) {
            list.add(worshipPoetryAlbum);
        }
        if (responsePoetryAlbum != null) {
            list.add(responsePoetryAlbum);
        }
        if (offertoryPoetryAlbum != null) {
            list.add(offertoryPoetryAlbum);
        }
        if (initiationPoetryAlbum != null) {
            list.add(initiationPoetryAlbum);
        }
        if (holyCommunionPoetryAlbum != null) {
            list.add(holyCommunionPoetryAlbum);
        }
        return list;
    }

}
