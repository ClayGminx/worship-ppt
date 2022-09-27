package claygminx.worshipppt.common.entity;

import java.util.List;

/**
 * 圣餐实体
 */
public class HolyCommunionEntity extends WorshipPropertyBean {

    public final static String NAME_LIST = "nameList";

    /**
     * 非会友领餐名单
     */
    private List<String> nameList;

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        firePropertyChange(NAME_LIST, this.nameList, nameList);
        this.nameList = nameList;
    }

    @Override
    public String toString() {
        return "HolyCommunionEntity{" +
                "nameList=" + nameList +
                '}';
    }
}
