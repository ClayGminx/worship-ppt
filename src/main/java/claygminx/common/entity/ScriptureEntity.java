package claygminx.common.entity;

/**
 * 经文实体
 */
public class ScriptureEntity {

    /**
     * 译本编号
     */
    private Integer versionId;

    /**
     * 译本名称
     */
    private String versionName;

    /**
     * 经文编号
     */
    private ScriptureNumberEntity scriptureNumber;

    /**
     * 经文
     */
    private String scripture;

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public ScriptureNumberEntity getScriptureNumber() {
        return scriptureNumber;
    }

    public void setScriptureNumber(ScriptureNumberEntity scriptureNumber) {
        this.scriptureNumber = scriptureNumber;
    }

    public String getScripture() {
        return scripture;
    }

    public void setScripture(String scripture) {
        this.scripture = scripture;
    }

    @Override
    public String toString() {
        return "ScriptureEntity{" +
                "versionId=" + versionId +
                ", versionName='" + versionName + '\'' +
                ", scriptureNumber=" + scriptureNumber +
                ", scripture='" + scripture + '\'' +
                '}';
    }
}
