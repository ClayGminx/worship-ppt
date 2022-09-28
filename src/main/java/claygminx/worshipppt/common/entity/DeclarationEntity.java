package claygminx.worshipppt.common.entity;

import java.io.Serializable;

/**
 * 宣信实体
 */
public class DeclarationEntity implements Serializable {

    private static final long serialVersionUID = 5066922226672640394L;

    /**
     * 宣信主题
     */
    private String title = "";

    /**
     * 讲员
     */
    private String speaker = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    @Override
    public String toString() {
        return "DeclarationEntity{" +
                "title='" + title + '\'' +
                ", speaker='" + speaker + '\'' +
                '}';
    }
}
