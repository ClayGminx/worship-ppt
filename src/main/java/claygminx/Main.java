package claygminx;

import claygminx.common.config.SystemConfig;
import claygminx.components.FileService;
import claygminx.components.InputService;
import claygminx.components.ScriptureService;
import claygminx.components.UpgradeService;
import claygminx.components.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static claygminx.common.Dict.General.*;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String scene = System.getProperty(RUNNING_SCENE);
        logger.debug(scene);

        if (SystemConfig.getString(RUNNING_SCENE_UPGRADE).equals(scene)) {
            UpgradeService service = UpgradeServiceImpl.getInstance();
            service.checkNewRelease();
        } else if (SystemConfig.getString(RUNNING_SCENE_PPT).equals(scene)) {
            if (args.length == 0) {
                System.err.println("请输入ini文件！");
                return;
            }
            String inputFilePath = args[0];

            InputService inputService = InputServiceImpl.getInstance();
            FileService fileService = FileServiceImpl.getInstance();
            ScriptureService scriptureService = ScriptureServiceImpl.getInstance();

            WorshipPPTServiceImpl worshipPPTService = new WorshipPPTServiceImpl(inputFilePath);
            worshipPPTService.setInputService(inputService);
            worshipPPTService.setFileService(fileService);
            worshipPPTService.setScriptureService(scriptureService);
            worshipPPTService.make();
        } else {
            logger.info("请选择正确的运行场景：升级，或PPT");
            logger.info("命令行如：java -jar -Drunning.scene=PPT worship-ppt.jar D:/input.ini");
        }
    }

}
