package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.components.*;
import claygminx.worshipppt.exception.FileServiceException;
import claygminx.worshipppt.exception.SystemException;
import claygminx.worshipppt.common.entity.*;
import claygminx.worshipppt.exception.WorshipStepException;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static claygminx.worshipppt.common.Dict.*;

@Slf4j
public class WorshipFormServiceImpl implements WorshipFormService {

    private static WorshipFormServiceImpl instance;
    private final Executor threadPool;
    private final WorshipEntity worshipEntity;
    private final UpgradeService upgradeService;

    // 组件
    private final JFrame frame;
    private ButtonGroup modelRadioGroup;
    private JTextField worshipDateTextField;
    private JTextField churchNameTextField;
    private Map<String, List<JTextField[]>> poetryListMap;
    private Map<String, JTextField> scriptureContentTextFieldMap;
    private Map<String, JTextField> declarationTextFieldMap;
    private Map<String, JTextField> preachTextFieldMap;
    private Map<String, JTextField> holyCommunionTextFieldMap;
    private List<JTextField> familyReportsTextFieldList;

    // 布局用的常量
    public final static String APP_TITLE = "敬拜PPT文件工具";
    public final static Dimension FRAME_SIZE = new Dimension(750, 600);
    public final static Dimension FRAME_MIN_SIZE = new Dimension(650, 450);
    public final static int TABLE_HEADER_HEIGHT = 30;
    public final static int TABLE_ROW_HEIGHT = 36;
    public final static int TEXT_FIELD_HEIGHT = 30;
    public final static int REGULAR_TABLE_LEFT_WIDTH = 70;
    public final static int REGULAR_TABLE_RIGHT_WIDTH = 400;
    public final static int POETRY_TABLE_COLUMN_WIDTH_1 = 180;
    public final static int POETRY_TABLE_COLUMN_WIDTH_2 = 300;
    public final static int POETRY_TABLE_COLUMN_WIDTH_3 = 140;
    public final static int BUTTON_WIDTH = 60;
    public final static int PADDING_LEFT = 6;
    public final static int V_SCROLL_BAR_SPEED = 20;
    public final static int ROW_INDEX_OFFSET = 2;

    // 线程数量
    private final static int THREAD_COUNT = 3;

    private WorshipFormServiceImpl() {
        frame = new JFrame(APP_TITLE);
        threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

        logger.debug("读取缓存...");
        WorshipEntity worshipEntityCache = readWorshipEntity();
        if (worshipEntityCache != null) {
            worshipEntity = worshipEntityCache;
            logger.debug("存在缓存，读取成功");
        } else {
            worshipEntity = new WorshipEntity();
            logger.debug("没有缓存");
        }

        upgradeService = UpgradeServiceImpl.getInstance();
    }

    /**
     * 获取表单服务实例
     * @return 表单服务实例
     */
    public static WorshipFormService getInstance() {
        if (instance == null) {
            instance = new WorshipFormServiceImpl();
        }
        return instance;
    }

    public void showForm() {
        logger.debug("初始化窗体");
        frame.setSize(FRAME_SIZE);
        frame.setMinimumSize(FRAME_MIN_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(getImageIcon().getImage());

        logger.debug("检查新版本");
        checkVersion();

        logger.debug("添加根容器");
        Box rootBox = Box.createVerticalBox();
        JScrollPane rootPanel = new JScrollPane(rootBox);// 使窗体有滚动条
        rootPanel.getVerticalScrollBar().setUnitIncrement(V_SCROLL_BAR_SPEED);
        frame.setContentPane(rootPanel);

        logger.debug("添加组件到窗体中");
        addMenus();
        addWorshipModelPanel(rootBox);
        addCoverPanel(rootBox);
        addAllPoetryAlbumPanel(rootBox);
        addScripturePanel(rootBox);
        addDeclarationPanel(rootBox);
        addPreachPanel(rootBox);
        addHolyCommunionPanel(rootBox);
        addFamilyReportsPanel(rootBox);
        addSubmitPanel(rootBox);

        logger.debug("添加完毕，展示窗体");
        frame.setVisible(true);
    }

    /**
     * 添加菜单
     */
    private void addMenus() {
        JMenuItem menuItem = new JMenuItem("自定义配置");
        menuItem.addActionListener(actionEvent -> {
            String customConfigPath = JOptionPane.showInputDialog(frame,
                    "请输入系统配置文件的完全路径：",
                    "自定义系统配置",
                    JOptionPane.INFORMATION_MESSAGE);
            logger.info("自定义系统配置文件路径：" + customConfigPath);

            if (customConfigPath == null || customConfigPath.trim().isEmpty()) {
                return;
            }

            File file = new File(customConfigPath.trim());
            if (file.exists() && file.isFile() && file.canRead()) {
                logger.debug("继续检查是否合法的配置文件");
                try (InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8)) {
                    new Properties().load(reader);
                    logger.info("文件加载成功！");
                    JOptionPane.showMessageDialog(frame, "文件加载成功，请重启该软件使其生效。", "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    logger.error("自定义配置文件加载失败！");
                    JOptionPane.showMessageDialog(frame, "文件加载失败！", "错误提示", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                logger.warn("文件错误！");
                JOptionPane.showMessageDialog(frame, "文件错误！", "错误提示", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenu menu = new JMenu("选项");
        menu.add(menuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }

    /**
     * 添加敬拜模式单选框
     * @param rootBox 单选框
     */
    private void addWorshipModelPanel(Box rootBox) {
        logger.debug("创建敬拜模式组件...");
        // 默认选择无圣餐
        CoverEntity cover = worshipEntity.getCover();
        String selectedModel = WorshipModel.WITHOUT_HOLY_COMMUNION;
        if (cover != null && !isEmpty(cover.getModel())) {
            selectedModel = cover.getModel();
        }
        JRadioButton withoutHolyCommunionRadio = new JRadioButton(WorshipModel.WITHOUT_HOLY_COMMUNION, selectedModel.equals(WorshipModel.WITHOUT_HOLY_COMMUNION));
        JRadioButton withinHolyCommunionRadio = new JRadioButton(WorshipModel.WITHIN_HOLY_COMMUNION, selectedModel.equals(WorshipModel.WITHIN_HOLY_COMMUNION));
        JRadioButton withinInitiationRadio = new JRadioButton(WorshipModel.WITHIN_INITIATION, selectedModel.equals(WorshipModel.WITHIN_INITIATION));
        withinInitiationRadio.setToolTipText("入会PPT比较复杂，请更细心制作");

        // 放在同一组里
        modelRadioGroup = new ButtonGroup();
        modelRadioGroup.add(withoutHolyCommunionRadio);
        modelRadioGroup.add(withinHolyCommunionRadio);
        modelRadioGroup.add(withinInitiationRadio);

        Box box = Box.createHorizontalBox();
        int strutWidth = 5;
        box.add(new JLabel("请先选择敬拜模式："));
        box.add(Box.createHorizontalStrut(strutWidth));
        box.add(withoutHolyCommunionRadio);
        box.add(Box.createHorizontalStrut(strutWidth));
        box.add(withinHolyCommunionRadio);
        box.add(Box.createHorizontalStrut(strutWidth));
        box.add(withinInitiationRadio);

        JPanel panel = new JPanel();
        panel.add(box);
        rootBox.add(panel);
        logger.debug("创建完毕");
    }

    /**
     * 添加封面
     * @param rootBox 根容器
     */
    private void addCoverPanel(Box rootBox) {
        logger.debug("创建封面组件...");
        Box tableBox = Box.createVerticalBox();
        addTableTitle(tableBox, InputSection.COVER);

        CoverEntity cover = worshipEntity.getCover();

        worshipDateTextField = addRegularTableInputRow(tableBox, CoverKey.WORSHIP_DATE);
        worshipDateTextField.setToolTipText("推荐填写格式：主后某年某月某日");
        if (cover != null && !isEmpty(cover.getWorshipDate())) {
            worshipDateTextField.setText(cover.getWorshipDate());
        }

        churchNameTextField = addRegularTableInputRow(tableBox, CoverKey.CHURCH_NAME);
        if (cover != null && !isEmpty(cover.getChurchName())) {
            churchNameTextField.setText(cover.getChurchName());
        }

        JPanel panel = new JPanel();
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加所有的诗歌集面板
     * @param rootBox 根容器
     */
    private void addAllPoetryAlbumPanel(Box rootBox) {
        String[] albumNames = new String[] {
                PoetryAlbumName.PRAY_POETRY, PoetryAlbumName.PRACTISE_POETRY, PoetryAlbumName.WORSHIP_POETRY,
                PoetryAlbumName.RESPONSE_POETRY, PoetryAlbumName.OFFERTORY_POETRY,
                PoetryAlbumName.HOLY_COMMUNION_POETRY, PoetryAlbumName.INITIATION_POETRY
        };
        poetryListMap = new HashMap<>(albumNames.length, (int) Math.ceil(albumNames.length / 0.75));
        for (String albumName : albumNames) {
            addOnePoetryAlbumPanel(rootBox, albumName);
        }
    }

    /**
     * 添加一个指定诗歌集的面板
     * @param rootBox 根容器
     * @param name 诗歌集的名称
     */
    private void addOnePoetryAlbumPanel(Box rootBox, String name) {
        Box tableBox = Box.createVerticalBox();
        if (!poetryListMap.containsKey(name)) {
            List<JTextField[]> list = new LinkedList<>();
            poetryListMap.put(name, list);
        }

        // 标题
        addTableTitle(tableBox, name);

        // 表头
        addPoetryTableHeader(tableBox);

        // 表体
        PoetryAlbumEntity album = getPoetryAlbumEntity(name);
        if (album != null && album.getPoetryList() != null && !album.getPoetryList().isEmpty()) {
            List<PoetryEntity> poetryList = album.getPoetryList();
            List<JTextField[]> textFieldsList = poetryListMap.get(name);
            for (int i = 0; i < poetryList.size(); i++) {
                addPoetryTableRow(tableBox, name, i);
                JTextField[] textFields = textFieldsList.get(i);
                textFields[0].setText(poetryList.get(i).getName());
                textFields[1].setText(poetryList.get(i).getDirectory().getAbsolutePath());
            }
        } else {
            addPoetryTableRow(tableBox, name, 0);
        }

        JPanel panel = new JPanel();
        panel.setName(name);
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加经文面板
     * @param rootBox 根容器
     */
    private void addScripturePanel(Box rootBox) {
        Box tableBox = Box.createVerticalBox();
        addTableTitle(tableBox, InputSection.SCRIPTURE);

        scriptureContentTextFieldMap = new HashMap<>();

        JTextField summonTextField = addRegularTableInputRow(tableBox, ScriptureContentKey.SUMMON);
        summonTextField.setToolTipText("输入经文编号即可，下面的输入框也一样");
        scriptureContentTextFieldMap.put(ScriptureContentKey.SUMMON, summonTextField);

        JTextField publicPrayTextField = addRegularTableInputRow(tableBox, ScriptureContentKey.PUBLIC_PRAY);
        scriptureContentTextFieldMap.put(ScriptureContentKey.PUBLIC_PRAY, publicPrayTextField);

        JTextField confessTextField = addRegularTableInputRow(tableBox, ScriptureContentKey.CONFESS);
        scriptureContentTextFieldMap.put(ScriptureContentKey.CONFESS, confessTextField);

        JTextField forgiveSinsTextField = addRegularTableInputRow(tableBox, ScriptureContentKey.FORGIVE_SINS);
        scriptureContentTextFieldMap.put(ScriptureContentKey.FORGIVE_SINS, forgiveSinsTextField);

        JTextField readingScriptureTextField = addRegularTableInputRow(tableBox, ScriptureContentKey.READING_SCRIPTURE);
        scriptureContentTextFieldMap.put(ScriptureContentKey.READING_SCRIPTURE, readingScriptureTextField);

        ScriptureContentEntity content = worshipEntity.getScriptureContent();
        if (content != null) {
            if (!isEmpty(content.getSummon())) {
                summonTextField.setText(content.getSummon());
            }
            if (!isEmpty(content.getPublicPray())) {
                publicPrayTextField.setText(content.getPublicPray());
            }
            if (!isEmpty(content.getConfess())) {
                confessTextField.setText(content.getConfess());
            }
            if (!isEmpty(content.getForgiveSins())) {
                forgiveSinsTextField.setText(content.getForgiveSins());
            }
            if (!isEmpty(content.getReadingScripture())) {
                readingScriptureTextField.setText(content.getReadingScripture());
            }
        }


        JPanel panel = new JPanel();
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加宣信面板
     * @param rootBox 根容器
     */
    private void addDeclarationPanel(Box rootBox) {
        Box tableBox = Box.createVerticalBox();
        addTableTitle(tableBox, InputSection.DECLARATION);

        declarationTextFieldMap = new HashMap<>();
        JTextField titleTextField = addRegularTableInputRow(tableBox, DeclarationKey.TITLE);
        titleTextField.setToolTipText("不需要用书名号括起来");
        declarationTextFieldMap.put(DeclarationKey.TITLE, titleTextField);

        JTextField speakerTextField = addRegularTableInputRow(tableBox, DeclarationKey.SPEAKER);
        declarationTextFieldMap.put(DeclarationKey.SPEAKER, speakerTextField);

        DeclarationEntity declaration = worshipEntity.getDeclaration();
        if (declaration != null) {
            if (!isEmpty(declaration.getTitle())) {
                titleTextField.setText(declaration.getTitle());
            }
            if (!isEmpty(declaration.getSpeaker())) {
                speakerTextField.setText(declaration.getSpeaker());
            }
        }

        JPanel panel = new JPanel();
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加证道面板
     * @param rootBox 根容器
     */
    private void addPreachPanel(Box rootBox) {
        Box tableBox = Box.createVerticalBox();
        addTableTitle(tableBox, InputSection.PREACH);

        preachTextFieldMap = new HashMap<>();
        JTextField titleTextField = addRegularTableInputRow(tableBox, PreachKey.TITLE);
        preachTextFieldMap.put(PreachKey.TITLE, titleTextField);

        JTextField scriptureTextField = addRegularTableInputRow(tableBox, PreachKey.SCRIPTURE);
        preachTextFieldMap.put(PreachKey.SCRIPTURE, scriptureTextField);

        PreachEntity preach = worshipEntity.getPreach();
        if (preach != null) {
            if (!isEmpty(preach.getTitle())) {
                titleTextField.setText(preach.getTitle());
            }
            if (!isEmpty(preach.getScriptureNumber())) {
                scriptureTextField.setText(preach.getScriptureNumber());
            }
        }

        JPanel panel = new JPanel();
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加圣餐面板
     * @param rootBox 根容器
     */
    private void addHolyCommunionPanel(Box rootBox) {
        Box tableBox = Box.createVerticalBox();
        addTableTitle(tableBox, InputSection.HOLY_COMMUNION);

        holyCommunionTextFieldMap = new HashMap<>();
        JTextField nameListTextField = addRegularTableInputRow(tableBox, HolyCommunionKey.NAME_LIST);
        nameListTextField.setToolTipText("用英文逗号分隔姓名");
        holyCommunionTextFieldMap.put(HolyCommunionKey.NAME_LIST, nameListTextField);

        HolyCommunionEntity holyCommunion = worshipEntity.getHolyCommunion();
        if (holyCommunion != null) {
            if (holyCommunion.getNameList() != null && !holyCommunion.getNameList().isEmpty()) {
                String join = String.join(",", holyCommunion.getNameList());
                nameListTextField.setText(join);
            }
        }

        JPanel panel = new JPanel();
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加家事报告面板
     * @param rootBox 根容器
     */
    private void addFamilyReportsPanel(Box rootBox) {
        Box tableBox = Box.createVerticalBox();
        addTableTitle(tableBox, InputSection.FAMILY_REPORTS);

        Box header = Box.createHorizontalBox();
        tableBox.add(header);
        addTableColumn(header, "家事报告", REGULAR_TABLE_RIGHT_WIDTH);
        addTableColumn(header, "操作", POETRY_TABLE_COLUMN_WIDTH_3);

        familyReportsTextFieldList = new LinkedList<>();

        // 如果有本地缓存，则加载
        List<String> familyReportList = worshipEntity.getFamilyReports();
        if (familyReportList != null && !familyReportList.isEmpty()) {
            for (int i = 0; i < familyReportList.size(); i++) {
                addFamilyReportsTableInputRow(tableBox, i);
            }
            int i = 0;
            for (JTextField textField : familyReportsTextFieldList) {
                textField.setText(familyReportList.get(i++));
            }
        } else {
            addFamilyReportsTableInputRow(tableBox, 0);
        }

        JPanel panel = new JPanel();
        panel.setName(InputSection.FAMILY_REPORTS);
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加提交按钮
     * @param rootBox 根盒子
     */
    private void addSubmitPanel(Box rootBox) {
        JButton submitButton = new JButton("开始制作");
        JPanel panel = new JPanel();
        panel.add(submitButton);
        rootBox.add(panel);

        submitButton.addActionListener((action) -> run(() -> {
            boolean prepareResult = prepare();
            saveWorshipEntity(worshipEntity);

            if (prepareResult) {
                ProgressMonitor pm = new ProgressMonitor(frame, "制作敬拜PPT", "准备制作", 0, 100);
                threadPool.execute(() -> {
                    WorshipPPTServiceImpl worshipPPTService = new WorshipPPTServiceImpl(worshipEntity);
                    worshipPPTService.setProgressMonitor(pm);
                    try {
                        worshipPPTService.make();
                        File pptFile = worshipPPTService.getFile();
                        String message = "<html>" +
                                "<p>PPT制作完成！</p>" +
                                "<p>文件位于：" +
                                pptFile.getAbsolutePath() +
                                "</p>" +
                                "<p>你还需要做一些检查工作：</p>" +
                                "<ol><li><b>宣信和证道内容需要手动制作；</b></li>" +
                                "<li>圣餐诗歌需要手动调整以符合圣礼需要；</li>" +
                                "<li>还有更多需要细心检查的细节。</li></ol></html>";
                        JTextPane f = createTextPane(message);
                        JOptionPane.showMessageDialog(frame, f, "提示", JOptionPane.INFORMATION_MESSAGE);
                    } catch (FileServiceException | WorshipStepException | SystemException e) {
                        pm.close();
                        logger.error("制作PPT时出现错误！", e);
                        JOptionPane.showMessageDialog(frame, e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        pm.close();
                        logger.error("未捕获的错误！！！", e);
                        JOptionPane.showMessageDialog(frame, "系统错误！", "提示", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }));
    }

///////////////////////////
// Business method
///////////////////////////

    /**
     * 提交之前检查数据
     * @return 是否检查通过
     */
    private boolean prepare() {
        return prepareCover() && preparePoetry() && prepareScriptureContent() && prepareDeclaration()
                && preparePreach() && prepareFamilyReport() && prepareHolyCommunion();
    }
    
    private boolean prepareCover() {
        logger.debug("检查封面信息...");
        
        String selectedModel = null;
        Enumeration<AbstractButton> radioElements = modelRadioGroup.getElements();
        while (radioElements.hasMoreElements()) {
            AbstractButton radioElement = radioElements.nextElement();
            if (radioElement.isSelected()) {
                selectedModel = radioElement.getText();
            }
        }
        if (isEmpty(selectedModel)) {
            warn("请先选择敬拜模式");
            return false;
        }
        String worshipDate = worshipDateTextField.getText();
        if (isEmpty(worshipDate)) {
            warn("请输入敬拜日期！");
            return false;
        }
        String churchName = churchNameTextField.getText();
        if (isEmpty(churchName)) {
            logger.warn("未输入教会名称！");
        }
        
        CoverEntity cover = new CoverEntity();
        cover.setModel(selectedModel);
        cover.setWorshipDate(worshipDate);
        cover.setChurchName(churchName);
        worshipEntity.setCover(cover);
        
        return true;
    }
    
    private boolean preparePoetry() {
        logger.debug("检查诗歌...");
        
        String selectedModel = worshipEntity.getCover().getModel();
        PoetryContentEntity poetryContentEntity = new PoetryContentEntity();
        worshipEntity.setPoetryContent(poetryContentEntity);
        
        List<JTextField[]> prayTextFieldsList = poetryListMap.get(PoetryAlbumName.PRAY_POETRY);
        String message = checkPoetryInfo(PoetryAlbumName.PRAY_POETRY, prayTextFieldsList, 0);
        if (message != null) {
            warn(message);
            return false;
        } else {
            PoetryAlbumEntity album = readPoetryAlbum(PoetryAlbumName.PRAY_POETRY, prayTextFieldsList);
            poetryContentEntity.setPrayPoetryAlbum(album);
        }
        
        List<JTextField[]> practiseTextFieldsList = poetryListMap.get(PoetryAlbumName.PRACTISE_POETRY);
        message = checkPoetryInfo(PoetryAlbumName.PRACTISE_POETRY, practiseTextFieldsList, 0);
        if (message != null) {
            warn(message);
            return false;
        } else {
            PoetryAlbumEntity album = readPoetryAlbum(PoetryAlbumName.PRACTISE_POETRY, practiseTextFieldsList);
            poetryContentEntity.setPractisePoetryAlbum(album);
        }
        
        List<JTextField[]> worshipTextFieldsList = poetryListMap.get(PoetryAlbumName.WORSHIP_POETRY);
        message = checkPoetryInfo(PoetryAlbumName.WORSHIP_POETRY, worshipTextFieldsList, 2);
        if (message != null) {
            warn(message);
            return false;
        } else {
            PoetryAlbumEntity album = readPoetryAlbum(PoetryAlbumName.WORSHIP_POETRY, worshipTextFieldsList);
            poetryContentEntity.setWorshipPoetryAlbum(album);
        }
        
        List<JTextField[]> responseTextFieldsList = poetryListMap.get(PoetryAlbumName.RESPONSE_POETRY);
        message = checkPoetryInfo(PoetryAlbumName.RESPONSE_POETRY, responseTextFieldsList, 1);
        if (message != null) {
            warn(message);
            return false;
        } else {
            PoetryAlbumEntity album = readPoetryAlbum(PoetryAlbumName.RESPONSE_POETRY, responseTextFieldsList);
            poetryContentEntity.setResponsePoetryAlbum(album);
        }
        
        List<JTextField[]> offertoryTextFieldsList = poetryListMap.get(PoetryAlbumName.OFFERTORY_POETRY);
        message = checkPoetryInfo(PoetryAlbumName.OFFERTORY_POETRY, offertoryTextFieldsList, 1);
        if (message != null) {
            warn(message);
            return false;
        } else {
            PoetryAlbumEntity album = readPoetryAlbum(PoetryAlbumName.OFFERTORY_POETRY, offertoryTextFieldsList);
            poetryContentEntity.setOffertoryPoetryAlbum(album);
        }
        
        if (WorshipModel.WITHIN_HOLY_COMMUNION.equals(selectedModel) || WorshipModel.WITHIN_INITIATION.equals(selectedModel)) {
            List<JTextField[]> holyCommunionTextFieldsList = poetryListMap.get(PoetryAlbumName.HOLY_COMMUNION_POETRY);
            message = checkPoetryInfo(PoetryAlbumName.HOLY_COMMUNION_POETRY, holyCommunionTextFieldsList, 1);
            if (message != null) {
                warn(message);
                return false;
            } else {
                PoetryAlbumEntity album = readPoetryAlbum(PoetryAlbumName.HOLY_COMMUNION_POETRY, holyCommunionTextFieldsList);
                poetryContentEntity.setHolyCommunionPoetryAlbum(album);
            }
        }
        
        if (WorshipModel.WITHIN_INITIATION.equals(selectedModel)) {
            List<JTextField[]> initiationTextFieldsList = poetryListMap.get(PoetryAlbumName.INITIATION_POETRY);
            message = checkPoetryInfo(PoetryAlbumName.INITIATION_POETRY, initiationTextFieldsList, 1);
            if (message != null) {
                warn(message);
                return false;
            } else {
                PoetryAlbumEntity album = readPoetryAlbum(PoetryAlbumName.INITIATION_POETRY, initiationTextFieldsList);
                poetryContentEntity.setInitiationPoetryAlbum(album);
            }
        }
        
        return true;
    }
    
    private boolean prepareScriptureContent() {
        logger.debug("检查经文...");
        
        Set<String> keySet = scriptureContentTextFieldMap.keySet();
        for (String key : keySet) {
            JTextField textField = scriptureContentTextFieldMap.get(key);
            String text = textField.getText();
            if (isEmpty(text)) {
                warn("经文部分需要全部填写！");
                return false;
            }
        }

        ScriptureContentEntity scriptureContentEntity = new ScriptureContentEntity();
        scriptureContentEntity.setSummon(scriptureContentTextFieldMap.get(ScriptureContentKey.SUMMON).getText().trim());
        scriptureContentEntity.setConfess(scriptureContentTextFieldMap.get(ScriptureContentKey.CONFESS).getText().trim());
        scriptureContentEntity.setForgiveSins(scriptureContentTextFieldMap.get(ScriptureContentKey.FORGIVE_SINS).getText().trim());
        scriptureContentEntity.setPublicPray(scriptureContentTextFieldMap.get(ScriptureContentKey.PUBLIC_PRAY).getText().trim());
        scriptureContentEntity.setReadingScripture(scriptureContentTextFieldMap.get(ScriptureContentKey.READING_SCRIPTURE).getText().trim());
        worshipEntity.setScriptureContent(scriptureContentEntity);
        
        return true;
    }
    
    private boolean prepareDeclaration() {
        logger.debug("检查宣信...");
        
        JTextField declarationTitleTextField = declarationTextFieldMap.get(DeclarationKey.TITLE);
        JTextField declarationSpeakerTextField = declarationTextFieldMap.get(DeclarationKey.SPEAKER);
        if (isEmpty(declarationTitleTextField.getText())) {
            warn("需要填写宣信主题！");
            return false;
        }
        if (isEmpty(declarationSpeakerTextField.getText())) {
            logger.warn("未输入讲员！");
        }
        
        DeclarationEntity declaration = new DeclarationEntity();
        declaration.setTitle(declarationTitleTextField.getText().trim());
        declaration.setSpeaker(declarationSpeakerTextField.getText().trim());
        worshipEntity.setDeclaration(declaration);
        
        return true;
    }
    
    private boolean preparePreach() {
        logger.debug("检查证道...");
        
        JTextField preachTitleTextField = preachTextFieldMap.get(PreachKey.TITLE);
        JTextField preachScriptureTextField = preachTextFieldMap.get(PreachKey.SCRIPTURE);
        if (isEmpty(preachTitleTextField.getText())) {
            warn("证道主题是什么？");
            return false;
        }
        if (isEmpty(preachScriptureTextField.getText())) {
            logger.warn("未输入证道经文！");
        }
        
        PreachEntity preach = new PreachEntity();
        preach.setTitle(preachTitleTextField.getText().trim());
        preach.setScriptureNumber(preachScriptureTextField.getText().trim());
        worshipEntity.setPreach(preach);
        
        return true;
    }
    
    private boolean prepareFamilyReport() {
        logger.debug("检查家事报告...");
        
        List<String> familyReports = new ArrayList<>();
        for (JTextField textField : familyReportsTextFieldList) {
            String item = textField.getText().trim();
            if (!isEmpty(item)) {
                familyReports.add(item);
            }
        }
        
        if (!familyReports.isEmpty()) {
            worshipEntity.setFamilyReports(familyReports);
        }
        
        return true;
    }
    
    private boolean prepareHolyCommunion() {
        logger.debug("检查圣餐...");

        String selectedModel = worshipEntity.getCover().getModel();
        if (WorshipModel.WITHIN_HOLY_COMMUNION.equals(selectedModel) || WorshipModel.WITHIN_INITIATION.equals(selectedModel)) {
            JTextField nameListTextField = holyCommunionTextFieldMap.get(HolyCommunionKey.NAME_LIST);
            HolyCommunionEntity holyCommunionEntity = new HolyCommunionEntity();
            List<String> nameList;
            String nameListStr = nameListTextField.getText().trim();
            if (!isEmpty(nameListStr)) {
                String[] nameArray = nameListStr.split(",");
                nameList = Arrays.asList(nameArray);
            } else {
                logger.warn("未输入领餐名单，难不成都是会友？");
                nameList = new ArrayList<>();
            }
            holyCommunionEntity.setNameList(nameList);
            worshipEntity.setHolyCommunion(holyCommunionEntity);
        }
        
        return true;
    }

    // 检查诗歌信息
    private String checkPoetryInfo(String albumName, List<JTextField[]> poetryTextFieldList, int minCount) {
        int count = 0;
        for (int i = 0; i < poetryTextFieldList.size(); i++) {
            JTextField[] textFields = poetryTextFieldList.get(i);
            String name = textFields[0].getText(), directory = textFields[1].getText();
            if (!isEmpty(name) && isEmpty(directory) || isEmpty(name) && !isEmpty(directory)) {
                return albumName + "中第" + (i + 1) + "行诗歌输入不完整！";
            }
            if (!isEmpty(name) && !isEmpty(directory)) {
                count++;
            }
        }
        logger.debug("向{}输入了{}首诗歌", albumName, count);
        if (count < minCount) {
            return albumName + "需要至少" + minCount + "首诗歌！";
        }
        return null;
    }

    // 读取诗歌集
    private PoetryAlbumEntity readPoetryAlbum(String albumName, List<JTextField[]> textFieldsList) {
        List<PoetryEntity> poetryEntityList = new ArrayList<>();
        for (JTextField[] textFields : textFieldsList) {
            String name = textFields[0].getText().trim();
            String directoryStr = textFields[1].getText().trim();
            if (!isEmpty(name) && !isEmpty(directoryStr)) {
                poetryEntityList.add(new PoetryEntity(name, new File(directoryStr)));
            }
        }
        if (!poetryEntityList.isEmpty()) {
            PoetryAlbumEntity poetryAlbumEntity = new PoetryAlbumEntity();
            poetryAlbumEntity.setName(albumName);
            poetryAlbumEntity.setPoetryList(poetryEntityList);
            return poetryAlbumEntity;
        }
        return null;
    }

    // 根据名称获取诗歌集
    private PoetryAlbumEntity getPoetryAlbumEntity(String name) {
        PoetryContentEntity content = worshipEntity.getPoetryContent();
        if (content != null) {
            switch (name) {
                case PoetryAlbumName.PRAY_POETRY:
                    return content.getPrayPoetryAlbum();
                case PoetryAlbumName.PRACTISE_POETRY:
                    return content.getPractisePoetryAlbum();
                case PoetryAlbumName.WORSHIP_POETRY:
                    return content.getWorshipPoetryAlbum();
                case PoetryAlbumName.RESPONSE_POETRY:
                    return content.getResponsePoetryAlbum();
                case PoetryAlbumName.OFFERTORY_POETRY:
                    return content.getOffertoryPoetryAlbum();
                case PoetryAlbumName.HOLY_COMMUNION_POETRY:
                    return content.getHolyCommunionPoetryAlbum();
                default:
                    return content.getInitiationPoetryAlbum();
            }
        }
        return null;
    }

    /**
     * 检查新版本
     */
    private void checkVersion() {
        threadPool.execute(() -> {
            String info = upgradeService.checkNewRelease();
            if (info != null) {
                String[] infos = info.split("\n");
                StringBuilder msgBuilder = new StringBuilder("<html>");
                for (String s : infos) {
                    msgBuilder.append("<p>").append(s).append("</p>");
                }
                msgBuilder.append("</html>");

                JTextPane f = createTextPane(msgBuilder.toString());

                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, f, "新版本提示", JOptionPane.INFORMATION_MESSAGE));
            }
        });
    }

///////////////////////////
// Layout method
///////////////////////////

    /**
     * 向表格添加标题
     * @param tableBox 表格
     * @param title 标题
     */
    private void addTableTitle(Box tableBox, String title) {
        // 放置标题的Label，并居中显示
        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // 为不同的操作系统设置不同的字体
        label.setFont(getBoldFont());

        // 创建一行，并加入到表格中
        Box rowBox = Box.createHorizontalBox();
        rowBox.add(label);
        tableBox.add(rowBox);
    }

    /**
     * 向表格添加表头
     * @param tableBox 表格
     */
    private void addPoetryTableHeader(Box tableBox) {
        Box header = Box.createHorizontalBox();
        tableBox.add(header);

        addTableColumn(header, "诗歌名称", POETRY_TABLE_COLUMN_WIDTH_1);
        addTableColumn(header, "歌谱图的路径", POETRY_TABLE_COLUMN_WIDTH_2);
        addTableColumn(header, "操作", POETRY_TABLE_COLUMN_WIDTH_3);
    }

    /**
     * 向诗歌集表格中指定的位置添加一行
     * @param tableBox 表格
     * @param albumName 列名
     * @param poetryIndex 行索引，从0开始
     */
    private void addPoetryTableRow(Box tableBox, String albumName, int poetryIndex) {
        List<JTextField[]> textFieldsList = poetryListMap.get(albumName);
        if (textFieldsList == null) {
            logger.error("无法从poetryListMap中根据{}找到元素", albumName);
            throw new SystemException("系统错误！");
        }
        if (poetryIndex > textFieldsList.size()) {
            logger.error("{}才{}行诗歌，不可在{}处添加一行诗歌", albumName, textFieldsList.size(), poetryIndex);
            throw new SystemException("系统错误！");
        }

        // 添加一行到表格中
        int rowIndex = poetryIndex + ROW_INDEX_OFFSET;
        Box rowBox = Box.createHorizontalBox();
        try {
            tableBox.add(rowBox, rowIndex);
        } catch (Exception e) {
            logger.error("在第{}行处添加一行Box失败", rowIndex);
            throw new SystemException("系统错误！", e);
        }

        // 诗歌名称
        JTextField poetryNameTextField = addTableInputTextCell(rowBox, POETRY_TABLE_COLUMN_WIDTH_1);
        poetryNameTextField.setToolTipText("建议带上书名号《》");

        // 歌谱图的路径
        JTextField poetryDirectoryTextField = addTableInputTextCell(rowBox, POETRY_TABLE_COLUMN_WIDTH_2);
        poetryDirectoryTextField.setToolTipText("该路径文件夹必须只包含此诗歌的歌谱图，歌谱图是用JP-WORD制作的");

        JTextField[] textFieldArray = new JTextField[] {poetryNameTextField, poetryDirectoryTextField};
        textFieldsList.add(poetryIndex, textFieldArray);

        // 操作按钮
        addPoetryTableOperationCell(rowBox, textFieldsList);
    }

    /**
     * 向表头添加列
     * @param headerBox 表头
     * @param columnName 列名
     * @param width 列的宽度
     */
    private void addTableColumn(Box headerBox, String columnName, int width) {
        JLabel label = new JLabel(columnName);
        label.setBorder(new EmptyBorder(0, PADDING_LEFT, 0, 0));

        JPanel column = new JPanel();
        column.setPreferredSize(new Dimension(width, TABLE_HEADER_HEIGHT));
        column.setLayout(new GridBagLayout());// 这种布局可以让文字垂直居中
        column.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));// 上下两线

        // 布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        column.add(label, gbc);
        headerBox.add(column);
    }

    /**
     * 向表格中的一行添加一个输入单元格
     * @param rowBox 表格中的一行
     * @param width 宽度
     * @return 文本框
     */
    private JTextField addTableInputTextCell(Box rowBox, int width) {
        // 输入组件
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(width - PADDING_LEFT, TEXT_FIELD_HEIGHT));

        leftMiddle(rowBox, textField, width);
        return textField;
    }

    /**
     * 向诗歌表格中指定行添加一个操作单元格
     * @param rowBox 表格中的一行
     * @param textFieldsList 诗歌列表
     */
    private void addPoetryTableOperationCell(Box rowBox, List<JTextField[]> textFieldsList) {
        JButton[] buttons = addOperationButtons(rowBox);
        JButton insertButton = buttons[0];
        JButton deleteButton = buttons[1];
        Box tableBox = (Box) rowBox.getParent();

        insertButton.addActionListener(createPoetryInsertButtonActionListener(tableBox, rowBox));
        deleteButton.addActionListener(createPoetryDeleteButtonActionListener(tableBox, rowBox, textFieldsList));
    }

    /**
     * 添加一行输入
     * @param tableBox 根容器箱子
     * @param labelName 标签文本
     * @return 文本框
     */
    private JTextField addRegularTableInputRow(Box tableBox, String labelName) {
        Box rowBox = Box.createHorizontalBox();
        tableBox.add(rowBox);

        JLabel label = addRegularTableInputLabel(rowBox, labelName);
        JTextField textField = addTableInputTextCell(rowBox, REGULAR_TABLE_RIGHT_WIDTH);
        label.setLabelFor(textField);

        return textField;
    }

    /**
     * 向常规表格中指定行添加输入标签
     * @param rowBox 表格中的一行
     * @param labelName 标签文本
     * @return 标签
     */
    private JLabel addRegularTableInputLabel(Box rowBox, String labelName) {
        JLabel label = new JLabel(labelName);
        leftMiddle(rowBox, label, REGULAR_TABLE_LEFT_WIDTH);
        return label;
    }

    /**
     * 添加家事报告面板添加一行
     * @param tableBox 表格箱子
     * @param familyReportIndex 添加未知，从0开始
     */
    private void addFamilyReportsTableInputRow(Box tableBox, int familyReportIndex) {
        Box rowBox = Box.createHorizontalBox();
        tableBox.add(rowBox, familyReportIndex + ROW_INDEX_OFFSET);

        JTextField textField = addTableInputTextCell(rowBox, REGULAR_TABLE_RIGHT_WIDTH);
        familyReportsTextFieldList.add(familyReportIndex, textField);

        addFamilyReportTableOperationCell(rowBox);
    }

    // 添加家事报告操作单元格
    private void addFamilyReportTableOperationCell(Box rowBox) {
        JButton[] buttons = addOperationButtons(rowBox);
        JButton insertButton = buttons[0];
        JButton deleteButton = buttons[1];
        Box tableBox = (Box) rowBox.getParent();

        insertButton.addActionListener((action) -> run(() -> {
            int currentIndex = getIndexOfRowBox(tableBox, rowBox);
            if (currentIndex != -1) {
                int nextIndex = currentIndex - ROW_INDEX_OFFSET + 1;
                addFamilyReportsTableInputRow(tableBox, nextIndex);
                tableBox.getRootPane().revalidate();
            }
        }));
        deleteButton.addActionListener((action) -> run(() -> {
            if (familyReportsTextFieldList.size() == 1) {
                JOptionPane.showMessageDialog(
                        tableBox.getRootPane(),
                        "请保留这一行！",
                        "提示",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                int currentIndex = getIndexOfRowBox(tableBox, rowBox);
                if (currentIndex != -1) {
                    familyReportsTextFieldList.remove(currentIndex - ROW_INDEX_OFFSET);
                    tableBox.remove(rowBox);
                    tableBox.getRootPane().revalidate();
                }
            }
        }));
    }

    // 添加操作按钮
    private JButton[] addOperationButtons(Box rowBox) {
        JButton insertButton = new JButton("插入");
        JButton deleteButton = new JButton("删除");

        Dimension dimension = insertButton.getPreferredSize();
        insertButton.setPreferredSize(new Dimension(BUTTON_WIDTH, (int) dimension.getHeight()));
        deleteButton.setPreferredSize(new Dimension(BUTTON_WIDTH, (int) dimension.getHeight()));

        insertButton.setToolTipText("在这行下面插入一行");
        deleteButton.setToolTipText("删除当前行");

        Box hBox = Box.createHorizontalBox();
        hBox.add(insertButton);
        hBox.add(Box.createHorizontalStrut(PADDING_LEFT));
        hBox.add(deleteButton);

        leftMiddle(rowBox, hBox, POETRY_TABLE_COLUMN_WIDTH_3);

        return new JButton[] {insertButton, deleteButton};
    }

    // 水平居左，垂直居中
    private void leftMiddle(Container container, Component component, int width) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        JPanel cell = new JPanel();
        cell.setPreferredSize(new Dimension(width, TABLE_ROW_HEIGHT));
        cell.setLayout(new GridBagLayout());
        cell.setBorder(new EmptyBorder(0, PADDING_LEFT, 0, 0));
        cell.add(component, gbc);
        container.add(cell);
    }

    /**
     * 创建诗歌集面板中插入按钮的动作监听器
     * @param tableBox 表格箱子布局
     * @param rowBox 水平箱子布局
     * @return 动作监听器
     */
    private ActionListener createPoetryInsertButtonActionListener(Box tableBox, Box rowBox) {
        return (action) -> run(() -> {
            int currentIndex = getIndexOfRowBox(tableBox, rowBox);
            if (currentIndex != -1) {
                int nextIndex = currentIndex - ROW_INDEX_OFFSET + 1;
                String albumName = tableBox.getParent().getName();
                addPoetryTableRow(tableBox, albumName, nextIndex);
                tableBox.getRootPane().revalidate();
            }
        });
    }

    /**
     * 创建诗歌集面板中删除按钮的动作监听器
     * @param tableBox 表格箱子布局
     * @param rowBox 水平箱子布局
     * @param textFieldsList 诗歌输入框列表
     * @return 动作监听器
     */
    private ActionListener createPoetryDeleteButtonActionListener(Box tableBox, Box rowBox, List<JTextField[]> textFieldsList) {
        return (action) -> run(() -> {
            if (textFieldsList.size() == 1) {
                JOptionPane.showMessageDialog(
                        tableBox.getRootPane(),
                        "请保留这一行！",
                        "提示",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                int currentIndex = getIndexOfRowBox(tableBox, rowBox);
                if (currentIndex != -1) {
                    int index = currentIndex - ROW_INDEX_OFFSET;
                    logger.debug("删除第{}行诗歌", index + 1);
                    textFieldsList.remove(index);
                    tableBox.remove(currentIndex);
                    tableBox.getRootPane().revalidate();
                }
            }
        });
    }

    /**
     * 获取{@code rowBox}在{@code tableBox}中的位置
     * @param tableBox 垂直方向的箱子布局
     * @param rowBox 水平方向的箱子布局
     * @return 索引位置，从0开始
     */
    private int getIndexOfRowBox(Box tableBox, Box rowBox) {
        Component[] rows = tableBox.getComponents();
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == rowBox) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 创建一个可以选择复制的文本域
     * @param text 字符串
     * @return 文本域
     */
    private JTextPane createTextPane(String text) {
        JTextPane f = new JTextPane();
        f.setContentType("text/html");
        f.setText(text);
        f.setEditable(false);
        f.setBackground(null);
        f.setBorder(null);
        return f;
    }

///////////////////////////
// Helpful method
///////////////////////////

    /**
     * 字符串是否为空
     * @param str 字符串
     * @return 布尔值
     */
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 弹出警告框
     * @param message 警告信息
     */
    private void warn(String message) {
        JOptionPane.showMessageDialog(frame, message, "提示", JOptionPane.WARNING_MESSAGE);
    }

///////////////////////////
// System method
///////////////////////////

    /**
     * 获取窗体的图标
     * @return 图标
     */
    private ImageIcon getImageIcon() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("icon.jpg");
        if (url == null) {
            throw new SystemException("找不到图标！");
        }
        return new ImageIcon(url);
    }

    /**
     * 获取粗体字体
     * @return 字体
     */
    private Font getBoldFont() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return new Font("微软雅黑", Font.BOLD, 16);
        } else {
            return new Font("PingFang SC", Font.BOLD, 16);
        }
    }

    /**
     * 异步执行GUI事件，将事件放在EDT中执行
     * @param runnable 可执行对象
     */
    private void run(Runnable runnable) {
        threadPool.execute(() -> SwingUtilities.invokeLater(runnable));
    }

    /**
     * 将敬拜实体序列化写入到本地磁盘默认位置
     * @param worshipEntity 敬拜实体
     */
    private void saveWorshipEntity(WorshipEntity worshipEntity) {
        // TODO Mac系统从哪里读取？
        File file = new File(WorshipEntity.class.getSimpleName());
        if (file.exists()) {
            logger.debug("本地磁盘已经存在敬拜实体");
            if (file.delete()) {
                logger.debug("已经删除了");
            }
        }

        try (OutputStream os = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(worshipEntity);
        } catch (Exception e) {
            logger.warn("无法序列化敬拜实体！", e);
        }
    }

    /**
     * 从本地磁盘默认位置读取敬拜实体
     * @return 敬拜实体
     */
    private WorshipEntity readWorshipEntity() {
        File file = new File(WorshipEntity.class.getSimpleName());
        if (!file.exists()) {
            return null;
        }
        try (InputStream is = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(is)) {
            return (WorshipEntity) ois.readObject();
        } catch (Exception e) {
            logger.warn("无法读取敬拜实体！", e);
        }
        return null;
    }

}
