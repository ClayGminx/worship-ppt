package claygminx;

import claygminx.components.FileService;
import claygminx.components.InputService;
import claygminx.components.ScriptureService;
import claygminx.components.impl.FileServiceImpl;
import claygminx.components.impl.InputServiceImpl;
import claygminx.components.impl.ScriptureServiceImpl;
import claygminx.components.impl.WorshipPPTServiceImpl;

public class Main {

//    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
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
