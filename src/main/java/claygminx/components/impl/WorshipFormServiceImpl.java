package claygminx.components.impl;

import claygminx.Main;
import claygminx.common.entity.*;
import claygminx.components.WorshipEntityManager;
import claygminx.components.WorshipFormService;
import claygminx.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static claygminx.common.Dict.*;

public class WorshipFormServiceImpl implements WorshipFormService {

    private final static Logger logger = LoggerFactory.getLogger(WorshipFormService.class);

    public final static String APP_TITLE = "敬拜PPT文件工具";
    public final static Dimension FRAME_SIZE = new Dimension(750, 600);
    public final static Dimension FRAME_MIN_SIZE = new Dimension(650, 450);
    public final static int TABLE_HEADER_HEIGHT = 30;
    public final static int TABLE_ROW_HEIGHT = 36;
    public final static int PADDING_LEFT = 6;
    public final static int REGULAR_TABLE_LEFT_WIDTH = 70;
    public final static int REGULAR_TABLE_RIGHT_WIDTH = 400;
    public final static int TEXT_FIELD_HEIGHT = 30;
    public final static int POETRY_TABLE_COLUMN_WIDTH_1 = 180;
    public final static int POETRY_TABLE_COLUMN_WIDTH_2 = 300;
    public final static int POETRY_TABLE_COLUMN_WIDTH_3 = 130;
    public final static int ROW_INDEX_OFFSET = 2;
    public final static int V_SCROLL_BAR_SPEED = 20;

    private static WorshipFormServiceImpl instance;
    private final JFrame frame;
    private final Executor threadPool;

    private WorshipFormServiceImpl() {
        frame = new JFrame(APP_TITLE);
        threadPool = Executors.newCachedThreadPool();
    }

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

        logger.debug("添加根容器");
        Box rootBox = Box.createVerticalBox();
        JScrollPane rootPanel = new JScrollPane(rootBox);// 使窗体有滚动条
        rootPanel.getVerticalScrollBar().setUnitIncrement(V_SCROLL_BAR_SPEED);
        frame.setContentPane(rootPanel);

        logger.debug("添加组件到窗体中");
        addWorshipModelPanel(rootBox);
        addCoverPanel(rootBox);
        addAllPoetryAlbumPanel(rootBox);
        addScripturePanel(rootBox);
        addDeclarationPanel(rootBox);
        addPreachPanel(rootBox);
        addHolyCommunionPanel(rootBox);
        addSubmitPanel(rootBox);

        logger.debug("添加完毕，展示窗体");
        frame.setVisible(true);
    }

    /**
     * 添加敬拜模式单选框
     * @param rootBox 单选框
     */
    private void addWorshipModelPanel(Box rootBox) {
        logger.debug("创建敬拜模式组件...");
        // 默认选择无圣餐
        JRadioButton withoutHolyCommunionRadio = new JRadioButton(WorshipModel.WITHOUT_HOLY_COMMUNION, true);
        JRadioButton withinHolyCommunionRadio = new JRadioButton(WorshipModel.WITHIN_HOLY_COMMUNION);
        JRadioButton withinInitiationRadio = new JRadioButton(WorshipModel.WITHIN_INITIATION);
        withinInitiationRadio.setToolTipText("入会PPT比较复杂，请更细心制作");

        // 放在同一组里
        ButtonGroup group = new ButtonGroup();
        group.add(withoutHolyCommunionRadio);
        group.add(withinHolyCommunionRadio);
        group.add(withinInitiationRadio);

        // 监听器
        PropertyChangeListener listener1 = createRadioPropertyChangeListener(withoutHolyCommunionRadio);
        PropertyChangeListener listener2 = createRadioPropertyChangeListener(withinHolyCommunionRadio);
        PropertyChangeListener listener3 = createRadioPropertyChangeListener(withinInitiationRadio);

        withoutHolyCommunionRadio.addPropertyChangeListener(CoverEntity.MODEL, listener1);
        withinHolyCommunionRadio.addPropertyChangeListener(CoverEntity.MODEL, listener2);
        withinInitiationRadio.addPropertyChangeListener(CoverEntity.MODEL, listener3);

        // 绑定到JavaBean
        CoverEntity bean = WorshipEntityManager.getCoverEntity();
        bean.addPropertyChangeListener(listener1);
        bean.addPropertyChangeListener(listener2);
        bean.addPropertyChangeListener(listener3);

        // 添加事件
        withoutHolyCommunionRadio.addActionListener((action) -> bean.setModel(withoutHolyCommunionRadio.getText()));
        withinHolyCommunionRadio.addActionListener((action) -> bean.setModel(withinHolyCommunionRadio.getText()));
        withinInitiationRadio.addActionListener((action) -> bean.setModel(withinInitiationRadio.getText()));

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

        CoverEntity coverEntity = WorshipEntityManager.getCoverEntity();
        JTextField textField = addRegularTableInputRow(tableBox, CoverKey.WORSHIP_DATE, coverEntity, CoverEntity.WORSHIP_DATE, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                coverEntity.setWorshipDate(localTextField.getText());
            }
        });
        textField.setToolTipText("推荐填写格式：主后某年某月某日");

        addRegularTableInputRow(tableBox, CoverKey.CHURCH_NAME, coverEntity, CoverEntity.CHURCH_NAME, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                coverEntity.setChurchName(localTextField.getText());
            }
        });

        JPanel panel = new JPanel();
        panel.setName("cover");
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加所有的诗歌集面板
     * @param rootBox 根容器
     */
    private void addAllPoetryAlbumPanel(Box rootBox) {
        String[] albumNames = new String[] {
                POETRY_ALBUM_NAME.PRAY_POETRY, POETRY_ALBUM_NAME.PRACTISE_POETRY, POETRY_ALBUM_NAME.WORSHIP_POETRY,
                POETRY_ALBUM_NAME.RESPONSE_POETRY, POETRY_ALBUM_NAME.OFFERTORY_POETRY,
                POETRY_ALBUM_NAME.HOLY_COMMUNION_POETRY, POETRY_ALBUM_NAME.INITIATION_POETRY
        };
        for (String albumName : albumNames) {
            addOnePoetryAlbumPanel(rootBox, albumName);
        }
    }

    /**
     * 添加一个指定诗歌集的面板
     * @param rootBox 根容器
     * @param name 诗歌集得名称
     */
    private void addOnePoetryAlbumPanel(Box rootBox, String name) {
        Box tableBox = Box.createVerticalBox();

        // 标题
        addTableTitle(tableBox, name);

        // 表头
        addPoetryTableHeader(tableBox);

        // 表体
        addPoetryTableRow(tableBox, name, 0);

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

        ScriptureContentEntity scriptureContentEntity = WorshipEntityManager.getScriptureContentEntity();
        addRegularTableInputRow(tableBox, ScriptureScene.SUMMON, scriptureContentEntity, ScriptureContentEntity.SUMMON, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                scriptureContentEntity.setSummon(localTextField.getText());
            }
        });
        addRegularTableInputRow(tableBox, ScriptureScene.PUBLIC_PRAY, scriptureContentEntity, ScriptureContentEntity.PUBLIC_PRAY, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                scriptureContentEntity.setPublicPray(localTextField.getText());
            }
        });
        addRegularTableInputRow(tableBox, ScriptureScene.CONFESS, scriptureContentEntity, ScriptureContentEntity.CONFESS, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                scriptureContentEntity.setConfess(localTextField.getText());
            }
        });
        addRegularTableInputRow(tableBox, ScriptureScene.FORGIVE_SINS, scriptureContentEntity, ScriptureContentEntity.FORGIVE_SINS, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                scriptureContentEntity.setForgiveSins(localTextField.getText());
            }
        });
        addRegularTableInputRow(tableBox, ScriptureScene.READING_SCRIPTURE, scriptureContentEntity, ScriptureContentEntity.READING_SCRIPTURE, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                scriptureContentEntity.setReadingScripture(localTextField.getText());
            }
        });

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

        DeclarationEntity declarationEntity = WorshipEntityManager.getDeclarationEntity();
        addRegularTableInputRow(tableBox, DeclarationKey.TITLE, declarationEntity, DeclarationEntity.TITLE, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                declarationEntity.setTitle(localTextField.getText());
            }
        });
        addRegularTableInputRow(tableBox, DeclarationKey.SPEAKER, declarationEntity, DeclarationEntity.SPEAKER, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                declarationEntity.setSpeaker(localTextField.getText());
            }
        });

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

        PreachEntity preachEntity = WorshipEntityManager.getPreachEntity();
        addRegularTableInputRow(tableBox, PreachKey.TITLE, preachEntity, PreachEntity.TITLE, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                preachEntity.setTitle(localTextField.getText());
            }
        });
        addRegularTableInputRow(tableBox, PreachKey.SCRIPTURE, preachEntity, PreachEntity.SCRIPTURE_NUMBER, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                preachEntity.setScriptureNumber(localTextField.getText());
            }
        });

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

        HolyCommunionEntity holyCommunionEntity = WorshipEntityManager.getHolyCommunionEntity();
        addRegularTableInputRow(tableBox, HolyCommunionKey.NAME_LIST, holyCommunionEntity, HolyCommunionEntity.NAME_LIST, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextField localTextField = (JTextField) e.getSource();
                String inputText = localTextField.getText();
                if (inputText == null || inputText.trim().isEmpty()) {
                    holyCommunionEntity.setNameList(null);
                } else {
                    String[] nameArray = inputText.split(",");
                    List<String> nameList = new ArrayList<>(nameArray.length);
                    for (String name : nameArray) {
                        nameList.add(name.trim());
                    }
                    holyCommunionEntity.setNameList(nameList);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(tableBox);
        rootBox.add(panel);
    }

    /**
     * 添加提交按钮
     * @param rootBox 根盒子
     */
    private void addSubmitPanel(Box rootBox) {
        JButton button = new JButton("提交");
        JPanel panel = new JPanel();
        panel.add(button);
        rootBox.add(panel);

        button.addActionListener((action) -> {
            final WorshipEntity worshipEntity = WorshipEntityManager.getWorshipEntity();
            run(() -> {
                logger.debug("检查输入数据...");
                logger.debug("检查封面信息...");
                CoverEntity cover = worshipEntity.getCover();
                if (isEmpty(cover.getModel())) {
                    warn("请先选择敬拜模式");
                    return;
                }
                if (isEmpty(cover.getWorshipDate())) {
                    warn("请输入敬拜日期！");
                    return;
                }
                if (isEmpty(cover.getChurchName())) {
                    logger.warn("未输入教会名称！");
                }

                logger.debug("检查诗歌...");
                PoetryContentEntity poetryContent = worshipEntity.getPoetryContent();
                PoetryAlbumEntity prayPoetryAlbum = poetryContent.getPrayPoetryAlbum();
                PoetryAlbumEntity practisePoetryAlbum = poetryContent.getPractisePoetryAlbum();
                PoetryAlbumEntity worshipPoetryAlbum = poetryContent.getWorshipPoetryAlbum();
                PoetryAlbumEntity responsePoetryAlbum = poetryContent.getResponsePoetryAlbum();
                PoetryAlbumEntity offertoryPoetryAlbum = poetryContent.getOffertoryPoetryAlbum();
                String message = checkPoetryInfo(InputSection.PRAY_POETRY, prayPoetryAlbum.getPoetryList(), 0);
                if (message != null) {
                    warn(message);
                    return;
                }
                message = checkPoetryInfo(InputSection.PRACTISE_POETRY, practisePoetryAlbum.getPoetryList(), 0);
                if (message != null) {
                    warn(message);
                    return;
                }
                message = checkPoetryInfo(InputSection.WORSHIP_POETRY, worshipPoetryAlbum.getPoetryList(), 2);
                if (message != null) {
                    warn(message);
                    return;
                }
                message = checkPoetryInfo(InputSection.RESPONSE_POETRY, responsePoetryAlbum.getPoetryList(), 1);
                if (message != null) {
                    warn(message);
                    return;
                }
                message = checkPoetryInfo(InputSection.OFFERTORY_POETRY, offertoryPoetryAlbum.getPoetryList(), 1);
                if (message != null) {
                    warn(message);
                    return;
                }
                PoetryAlbumEntity holyCommunionPoetryAlbum = poetryContent.getHolyCommunionPoetryAlbum();
                if (WorshipModel.WITHIN_HOLY_COMMUNION.equals(cover.getModel()) || WorshipModel.WITHIN_INITIATION.equals(cover.getModel())) {
                    message = checkPoetryInfo(InputSection.HOLY_COMMUNION_POETRY, holyCommunionPoetryAlbum.getPoetryList(), 1);
                    if (message != null) {
                        warn(message);
                        return;
                    }
                }
                if (WorshipModel.WITHIN_INITIATION.equals(cover.getModel())) {
                    PoetryAlbumEntity initiationPoetryAlbum = poetryContent.getInitiationPoetryAlbum();
                    message = checkPoetryInfo(InputSection.INITIATION_POETRY, initiationPoetryAlbum.getPoetryList(), 1);
                    if (message != null) {
                        warn(message);
                        return;
                    }
                }

                logger.debug("检查经文...");
                ScriptureContentEntity scriptureContent = worshipEntity.getScriptureContent();
                if (isEmpty(scriptureContent.getSummon())
                        || isEmpty(scriptureContent.getConfess())
                        || isEmpty(scriptureContent.getForgiveSins())
                        || isEmpty(scriptureContent.getPublicPray())
                        || isEmpty(scriptureContent.getReadingScripture())) {
                    warn("经文部分需要全部填写！");
                    return;
                }

                logger.debug("检查宣信...");
                DeclarationEntity declaration = worshipEntity.getDeclaration();
                if (isEmpty(declaration.getTitle())) {
                    warn("需要填写宣信主题！");
                    return;
                }
                if (isEmpty(declaration.getSpeaker())) {
                    logger.warn("未输入讲员！");
                }

                logger.debug("检查证道...");
                PreachEntity preach = worshipEntity.getPreach();
                if (isEmpty(preach.getTitle())) {
                    warn("证道主题是什么？");
                    return;
                }
                if (isEmpty(preach.getScriptureNumber())) {
                    logger.warn("未输入证道经文！");
                }

                // TODO 开始
                System.out.println(worshipEntity);
            });
        });
    }

///////////////////////////
// 帮助创建表单组件地私有方法
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
        // 诗歌列表
        List<PoetryEntity> poetryEntityList;
        switch (albumName) {
            case POETRY_ALBUM_NAME.PRAY_POETRY:
                poetryEntityList = WorshipEntityManager.getPrayPoetryList();
                break;
            case POETRY_ALBUM_NAME.PRACTISE_POETRY:
                poetryEntityList = WorshipEntityManager.getPractisePoetryList();
                break;
            case POETRY_ALBUM_NAME.WORSHIP_POETRY:
                poetryEntityList = WorshipEntityManager.getWorshipPoetryList();
                break;
            case POETRY_ALBUM_NAME.RESPONSE_POETRY:
                poetryEntityList = WorshipEntityManager.getResponsePoetryList();
                break;
            case POETRY_ALBUM_NAME.OFFERTORY_POETRY:
                poetryEntityList = WorshipEntityManager.getOffertoryPoetryList();
                break;
            case POETRY_ALBUM_NAME.HOLY_COMMUNION_POETRY:
                poetryEntityList = WorshipEntityManager.getHolyCommunionPoetryList();
                break;
            default:
                poetryEntityList = WorshipEntityManager.getInitiationPoetryList();
        }

        // 每添加一行，就要相应地创建一个诗歌实体
        PoetryEntity poetryEntity = new PoetryEntity(null, null);
        try {
            poetryEntityList.add(poetryIndex, poetryEntity);
        } catch (IndexOutOfBoundsException e) {
            throw new SystemException("无法在第" + (poetryIndex + 1) + "添加输入控件", e);
        }

        // 添加一行到表格中
        int rowIndex = poetryIndex + ROW_INDEX_OFFSET;
        Box rowBox = Box.createHorizontalBox();
        tableBox.add(rowBox, rowIndex);

        // 诗歌名称
        JTextField textField = addTableInputTextCell(rowBox, poetryEntity, PoetryEntity.NAME, POETRY_TABLE_COLUMN_WIDTH_1, new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                run(() -> {
                    JTextField localTextField = (JTextField) e.getSource();
                    String poetryName = localTextField.getText();
                    logger.debug(poetryName);
                    poetryEntity.setName(poetryName);
                });
            }
        });
        textField.setToolTipText("建议带上书名号《》");

        // 歌谱图的路径
        textField = addTableInputTextCell(rowBox, poetryEntity, PoetryEntity.DIRECTORY, POETRY_TABLE_COLUMN_WIDTH_2, new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                run(() -> {
                    JTextField localTextField = (JTextField) e.getSource();
                    String directory = localTextField.getText();
                    logger.debug(directory);
                    if (isEmpty(directory)) {
                        poetryEntity.setDirectory(null);
                    } else {
                        poetryEntity.setDirectory(new File(directory));
                    }
                });
            }
        });
        textField.setToolTipText("该路径文件夹必须只包含此诗歌的歌谱图，歌谱图是用JP-WORD制作的");

        // 操作按钮
        addPoetryTableOperationCell(rowBox, poetryEntityList);
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
     * 向表格指定行添加一个输入组件
     * @param rowBox 表格行
     * @param bean 与输入组件绑定的JavaBean
     * @param propertyName JavaBean中的一个属性的名称
     * @param width 输入组件的宽度
     * @param keyAdapter 与输入组件绑定的事件适配器
     * @return 输入组件
     */
    private JTextField addTableInputTextCell(Box rowBox, WorshipPropertyBean bean, String propertyName,
                                             int width, KeyAdapter keyAdapter) {
        // 输入组件
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(width - PADDING_LEFT, TEXT_FIELD_HEIGHT));

        // 放置输入组件的单元格容器
        JPanel cell = new JPanel();
        cell.setPreferredSize(new Dimension(width, TABLE_ROW_HEIGHT));
        cell.setLayout(new GridBagLayout());
        cell.setBorder(new EmptyBorder(0, PADDING_LEFT, 0, 0));

        // 布局
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        cell.add(textField);

        // 双向数据绑定
        PropertyChangeListener listener = (evt) -> {
            if (Objects.equals(propertyName, evt.getPropertyName())) {
                textField.setText(String.valueOf(evt.getNewValue()));
            }
        };
        bean.addPropertyChangeListener(listener);
        textField.addPropertyChangeListener(propertyName, listener);
        textField.addKeyListener(keyAdapter);

        rowBox.add(cell);
        return textField;
    }

    /**
     * 向诗歌表格中指定行添加一个操作单元格
     * @param rowBox 表格中的一行
     * @param poetryEntityList 诗歌列表
     */
    private void addPoetryTableOperationCell(Box rowBox, List<PoetryEntity> poetryEntityList) {
        JButton insertButton = new JButton("插入");
        JButton deleteButton = new JButton("删除");

        Dimension dimension = insertButton.getPreferredSize();
        insertButton.setPreferredSize(new Dimension(55, (int) dimension.getHeight()));
        deleteButton.setPreferredSize(new Dimension(55, (int) dimension.getHeight()));

        insertButton.setToolTipText("在这行下面插入一行");
        deleteButton.setToolTipText("删除当前行");

        Box hBox = Box.createHorizontalBox();
        hBox.add(insertButton);
        hBox.add(Box.createHorizontalStrut(PADDING_LEFT));
        hBox.add(deleteButton);

        JPanel cell = new JPanel();
        cell.setPreferredSize(new Dimension(POETRY_TABLE_COLUMN_WIDTH_3, TABLE_ROW_HEIGHT));
        cell.setLayout(new GridBagLayout());
        cell.setBorder(new EmptyBorder(0, PADDING_LEFT, 0, 0));
        cell.add(hBox);
        rowBox.add(cell);

        Box tableBox = (Box) rowBox.getParent();
        insertButton.addActionListener(createPoetryInsertButtonActionListener(tableBox, rowBox));
        deleteButton.addActionListener(createPoetryDeleteButtonActionListener(tableBox, rowBox, poetryEntityList));
    }

    /**
     * 向常规表格中添加输入行，此输入行只有两列，左边是标签，右边是文本框
     * @param tableBox 表格
     * @param labelName 标签文本
     * @param bean 于文本框绑定的JavaBean
     * @param propertyName JavaBean的属性名
     * @param keyAdapter 键盘适配器
     * @return 文本框
     */
    private JTextField addRegularTableInputRow(Box tableBox, String labelName, WorshipPropertyBean bean,
                                               String propertyName, KeyAdapter keyAdapter) {
        Box rowBox = Box.createHorizontalBox();
        tableBox.add(rowBox);

        JLabel label = addRegularTableInputLabel(rowBox, labelName);
        JTextField textField = addTableInputTextCell(rowBox, bean, propertyName, REGULAR_TABLE_RIGHT_WIDTH, keyAdapter);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        JPanel cell = new JPanel();
        cell.setPreferredSize(new Dimension(REGULAR_TABLE_LEFT_WIDTH, TABLE_ROW_HEIGHT));
        cell.setLayout(new GridBagLayout());
        cell.setBorder(new EmptyBorder(0, PADDING_LEFT, 0, 0));
        cell.add(label, gbc);
        rowBox.add(cell);
        return label;
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
                int nextIndex = currentIndex + 1 - ROW_INDEX_OFFSET;
                String albumName = tableBox.getParent().getName();
                addPoetryTableRow(tableBox, albumName, nextIndex);
            }
        });
    }

    /**
     * 创建诗歌集面板中删除按钮的动作监听器
     * @param tableBox 表格箱子布局
     * @param rowBox 水平箱子布局
     * @param poetryEntityList 诗歌列表
     * @return 动作监听器
     */
    private ActionListener createPoetryDeleteButtonActionListener(Box tableBox, Box rowBox, List<PoetryEntity> poetryEntityList) {
        return (action) -> run(() -> {
            if (tableBox.getComponentCount() == ROW_INDEX_OFFSET + 1) {
                JOptionPane.showMessageDialog(
                        tableBox.getRootPane(),
                        "请保留这一行！",
                        "提示",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                int currentIndex = getIndexOfRowBox(tableBox, rowBox);
                if (currentIndex != -1) {
                    // 是否要删除文本框监听器、按钮监听器？
                    int index = currentIndex - ROW_INDEX_OFFSET;
                    logger.debug("删除第{}行诗歌", index + 1);
                    poetryEntityList.remove(index);
                    tableBox.remove(rowBox);
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
     * 创建一个用于单选框的属性更改监听器
     * @param radio 单选框
     * @return 监听器
     */
    private PropertyChangeListener createRadioPropertyChangeListener(final JRadioButton radio) {
        return (evt) -> {
            if (Objects.equals(evt.getNewValue(), radio.getText())) {
                radio.setSelected(true);
            }
        };
    }

    /**
     * 检查诗歌信息
     * @param albumName 诗歌集名称
     * @param poetryList 诗歌列表
     * @param minCount 诗歌集最少要有多少首诗歌
     * @return 检查结果
     */
    private String checkPoetryInfo(String albumName, List<PoetryEntity> poetryList, int minCount) {
        int count = 0;
        for (int i = 0; i < poetryList.size(); i++) {
            PoetryEntity poetryEntity = poetryList.get(i);
            if (!isEmpty(poetryEntity.getName()) && poetryEntity.getDirectory() == null
                    || isEmpty(poetryEntity.getName()) && poetryEntity.getDirectory() != null) {
                return albumName + "中第" + (i + 1) + "行诗歌输入不完整！";
            }
            if (!isEmpty(poetryEntity.getName()) && poetryEntity.getDirectory() != null) {
                count++;
            }
        }
        if (count < minCount) {
            return albumName + "需要至少" + minCount + "首诗歌！";
        }
        return null;
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
        ClassLoader classLoader = Main.class.getClassLoader();
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

}
