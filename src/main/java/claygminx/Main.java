package claygminx;

import claygminx.components.FileService;
import claygminx.components.InputService;
import claygminx.components.ScriptureService;
import claygminx.components.UpgradeService;
import claygminx.components.impl.*;

public class Main {

//    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
//        UpgradeService upgradeService = UpgradeServiceImpl.getInstance();
//        upgradeService.checkNewRelease();
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
    }

}
