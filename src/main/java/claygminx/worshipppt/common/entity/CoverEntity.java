package claygminx.worshipppt.common.entity;

import java.io.Serializable;

/**
 * 封面实体
 */
public class CoverEntity implements Serializable {

    private static final long serialVersionUID = 7538889491071987595L;

    /**
     * 敬拜模式
     */
    private String model = "";

    /**
     * 敬拜日期
     */
    private String worshipDate = "";

    /**
     * 教会名称
     */
    private String churchName = "";

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWorshipDate() {
        return worshipDate;
    }

    public void setWorshipDate(String worshipDate) {
        this.worshipDate = worshipDate;
    }

    public String getChurchName() {
        return churchName;
    }

    public void setChurchName(String churchName) {
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
