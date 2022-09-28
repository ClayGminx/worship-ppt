package claygminx.worshipppt.common.entity;

import java.io.Serializable;

/**
 * 证道实体
 */
public class PreachEntity implements Serializable {

    private static final long serialVersionUID = -6185731342799198015L;

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
        this.title = title;
    }

    public String getScriptureNumber() {
        return scriptureNumber;
    }

    public void setScriptureNumber(String scriptureNumber) {
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
