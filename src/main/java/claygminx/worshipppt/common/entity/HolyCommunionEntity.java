package claygminx.worshipppt.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 圣餐实体
 */
public class HolyCommunionEntity implements Serializable {

    private static final long serialVersionUID = -4072834576315140171L;

    /**
     * 非会友领餐名单
     */
    private List<String> nameList;

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    @Override
    public String toString() {
        return "HolyCommunionEntity{" +
                "nameList=" + nameList +
                '}';
    }
}
