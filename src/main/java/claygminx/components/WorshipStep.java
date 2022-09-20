package claygminx.components;

import claygminx.exception.WorshipStepException;

/**
 * 敬拜阶段
 */
public interface WorshipStep {

    /**
     * 执行敬拜阶段以制作幻灯片
     * @throws WorshipStepException 出现错误时统一用此异常类
     */
    void execute() throws WorshipStepException;

}
