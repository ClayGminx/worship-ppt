package claygminx.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * GitHub发行包实体
 */
public class ReleaseEntity implements Serializable, Comparable<ReleaseEntity> {

    /**
     * 发行时间
     */
    private Date published_at;

    /**
     * 版本号
     */
    private String name;

    /**
     * ZIP包地址
     */
    private String zipball_url;

    public Date getPublished_at() {
        return published_at;
    }

    public void setPublished_at(Date published_at) {
        this.published_at = published_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipball_url() {
        return zipball_url;
    }

    public void setZipball_url(String zipball_url) {
        this.zipball_url = zipball_url;
    }

    @Override
    public String toString() {
        return "ReleaseEntity{" +
                "published_at=" + published_at +
                ", name='" + name + '\'' +
                ", zipball_url='" + zipball_url + '\'' +
                '}';
    }

    @Override
    public int compareTo(ReleaseEntity another) {
        if (another == null || another.getPublished_at() == null || another.getName() == null) {
            throw new IllegalArgumentException("非法的比较参数！");
        }
        if (this == another) {
            return 0;
        } else {
            if (this.getPublished_at().equals(another.getPublished_at())) {
                if (!this.getName().equals(another.getName())) {
                    String splitter = "[.]";
                    String[] thisVersion = this.getName().split(splitter);
                    String[] anotherVersion = another.getName().split(splitter);
                    if (thisVersion.length != anotherVersion.length) {
                        throw new IllegalArgumentException("非法的比较参数！");
                    }
                    try {
                        for (int i = 0; i < thisVersion.length; i++) {
                            int iThisVersion = Integer.parseInt(thisVersion[i]);
                            int iAnotherVersion = Integer.parseInt(anotherVersion[i]);
                            if (iThisVersion != iAnotherVersion) {
                                return iThisVersion - iAnotherVersion;
                            }
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("非法的比较参数！");
                    }
                }
                return 0;
            } else {
                return this.getPublished_at().compareTo(another.getPublished_at());
            }
        }
    }
}
