package claygminx.common.entity;

/**
 * 宣信实体
 */
public class DeclarationEntity {

    /**
     * 宣信主题
     */
    private String title;

    /**
     * 讲员
     */
    private String speaker;

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
