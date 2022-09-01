package claygminx.components;

import claygminx.common.entity.ScriptureEntity;
import claygminx.common.entity.ScriptureNumberEntity;
import claygminx.exception.ScriptureNumberException;

import static claygminx.common.Dict.*;

/**
 * 经文服务
 */
public interface ScriptureService {

    /**
     * 根据书卷全称或简称获取序号
     * @param fullName 书卷全称
     * @param shortName 书卷简称
     * @return 书卷序号
     */
    int getIdFromBookName(String fullName, String shortName);

    /**
     * 根据书卷序号获取书卷的全称和简称
     * @param bookId 书卷序号
     * @return 数组元素第一个是书卷全称，第二个是书卷简称
     */
    String[] getBookNameFromId(int bookId);

    /**
     * 验证经文编号的有效性，如果有效，就把书卷编号设置到参数里面去
     * @param scriptureNumberEntity 经文编号实体
     * @return 是否有效
     */
    boolean validateNumber(ScriptureNumberEntity scriptureNumberEntity);

    /**
     * 获取格式化后的经文
     * @param scriptureNumber 经文编号
     * @param formatFileName 格式文件名称
     * @return 经文实体
     */
    ScriptureEntity getScriptureWithFormat(String scriptureNumber, String formatFileName) throws ScriptureNumberException;

    /**
     * 获取格式化后的经文
     * @param scriptureNumber 经文编号
     * @param formatFileName 格式文件名称
     * @return 经文实体
     */
    ScriptureEntity getScriptureWithFormat(ScriptureNumberEntity scriptureNumber, String formatFileName);

}
