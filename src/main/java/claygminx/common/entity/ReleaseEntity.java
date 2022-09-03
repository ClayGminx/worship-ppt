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
     * fa'xing
     */
    private String body;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ReleaseEntity{" +
                "published_at=" + published_at +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public int compareTo(ReleaseEntity another) {
        if (another == null || another.getPublished_at() == null || another.getName() == null) {
            throw new IllegalArgumentException("非法的比较参数！");
        }
        if (this != another) {
            int[] thisVersion = parseVersion(this.getName());
            int[] anotherVersion = parseVersion(another.getName());
            for (int i = 0; i < thisVersion.length; i++) {
                int r = thisVersion[i] - anotherVersion[i];
                if (r != 0) {
                    return r;
                }
            }
        }
        return 0;
    }

    private int[] parseVersion(String version) {
        int[] result = new int[3];
        char[] chars = version.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0, n; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '0' && c <= '9') {
                sb.append(c);
            } else if (j < 3) {
                n = Integer.parseInt(sb.toString());
                result[j] = n;
                sb.setLength(0);
                j++;
            } else {
                break;
            }
        }
        return result;
    }
}
