package claygminx.common.entity;

/**
 * 封面实体
 */
public class CoverEntity extends WorshipPropertyBean {

    public final static String MODEL = "model";
    public final static String WORSHIP_DATE = "worshipDate";
    public final static String CHURCH_NAME = "churchName";

    /**
     * 敬拜模式
     */
    private String model = "";

    /**
     * 敬拜日期
     */
    private String worshipDate;

    /**
     * 教会名称
     */
    private String churchName;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        firePropertyChange(MODEL, this.model, model);
        this.model = model;
    }

    public String getWorshipDate() {
        return worshipDate;
    }

    public void setWorshipDate(String worshipDate) {
        firePropertyChange(WORSHIP_DATE, this.worshipDate, worshipDate);
        this.worshipDate = worshipDate;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
        firePropertyChange(CHURCH_NAME, this.churchName, churchName);
        this.churchName = churchName;
    }

    @Override
    public String toString() {
        return "CoverEntity{" +
                "model='" + model + '\'' +
                ", worshipDate='" + worshipDate + '\'' +
                ", churchName='" + churchName + '\'' +
                '}';
    }
}
