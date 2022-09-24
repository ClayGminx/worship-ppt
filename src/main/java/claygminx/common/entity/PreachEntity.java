package claygminx.common.entity;

/**
 * 证道实体
 */
public class PreachEntity extends WorshipPropertyBean {

    public final static String TITLE = "title";
    public final static String SCRIPTURE_NUMBER = "scriptureNumber";

    /**
     * 证道主题
     */
    private String title;

    /**
     * 证道经文
     */
    private String scriptureNumber;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        firePropertyChange(TITLE, this.title, title);
        this.title = title;
    }

    public String getScriptureNumber() {
        return scriptureNumber;
    }

    public void setScriptureNumber(String scriptureNumber) {
        firePropertyChange(SCRIPTURE_NUMBER, this.scriptureNumber, scriptureNumber);
        this.scriptureNumber = scriptureNumber;
    }

    @Override
    public String toString() {
        return "PreachEntity{" +
                "title='" + title + '\'' +
                ", scriptureNumber='" + scriptureNumber + '\'' +
                '}';
    }
}
