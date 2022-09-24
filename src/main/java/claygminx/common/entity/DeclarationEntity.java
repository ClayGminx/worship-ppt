package claygminx.common.entity;

/**
 * 宣信实体
 */
public class DeclarationEntity extends WorshipPropertyBean {

    public final static String TITLE = "title";
    public final static String SPEAKER = "speaker";

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
        firePropertyChange(TITLE, this.title, title);
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        firePropertyChange(SPEAKER, this.speaker, speaker);
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
