package claygminx.worshipppt;

import claygminx.worshipppt.common.Dict;
import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.worshipppt.components.impl.WorshipFormServiceImpl;
import com.formdev.flatlaf.FlatDarkLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 * 主程序
 */
public class Main extends JFrame {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println(SystemConfig.getString(Dict.PPTProperty.GENERAL_LANGUAGE));

        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            logger.error("初始化swing Look And Feel失败！", e);
            JOptionPane.showMessageDialog(null, "系统初始化失败！", "错误", ERROR_MESSAGE);
            return;
        }

        try {
            WorshipFormServiceImpl.getInstance().showForm();
        } catch (Exception e) {
            logger.error("未知的错误！", e);
            JOptionPane.showMessageDialog(null, "未知的错误！", "错误", ERROR_MESSAGE);
        }
    }

}
