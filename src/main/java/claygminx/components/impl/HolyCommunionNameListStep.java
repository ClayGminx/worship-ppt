package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.util.SizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.util.List;

import static claygminx.common.Dict.*;

/**
 * 非会友领餐名单阶段
 */
public class HolyCommunionNameListStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(HolyCommunionNameListStep.class);

    private final List<String> nameList;

    public HolyCommunionNameListStep(XMLSlideShow ppt, String layout, List<String> nameList) {
        super(ppt, layout);
        this.nameList = nameList;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(layout);

        if (nameList == null || nameList.isEmpty()) {
            logger.warn("没有非会友领餐名单！");
            return;
        }

        logger.debug("创建表格");
        XSLFTable table = slide.createTable();

        logger.debug("初始化表格的尺寸");
        double x = SizeUtil.convertToPoints(SystemConfig.getDouble(General.PPT_HOLY_COMMUNION_NAME_LIST_X)),
                y = SizeUtil.convertToPoints(SystemConfig.getDouble(General.PPT_HOLY_COMMUNION_NAME_LIST_Y)),
                w = SizeUtil.convertToPoints(SystemConfig.getDouble(General.PPT_HOLY_COMMUNION_NAME_LIST_W)),
                h = SizeUtil.convertToPoints(SystemConfig.getDouble(General.PPT_HOLY_COMMUNION_NAME_LIST_H));
        table.setAnchor(new Rectangle2D.Double(x, y, w, h));

        int colCount = SystemConfig.getInt(General.PPT_HOLY_COMMUNION_NAME_LIST_COL_COUNT);
        XSLFTableRow row = null;
        logger.debug("总共有{}个人", nameList.size());
        for (int i = 0; i < nameList.size(); i++) {
            if (i % colCount == 0) {
                logger.debug("加一行");
                row = table.addRow();
            }
            XSLFTableCell cell = row.addCell();
            XSLFTextParagraph paragraph = cell.addNewTextParagraph();
            paragraph.setLineSpacing(100.0);
            XSLFTextRun span = paragraph.addNewTextRun();
            span.setText(nameList.get(i));
            span.setFontFamily(getFontFamily(), FontGroup.LATIN);
            span.setFontFamily(getFontFamily(), FontGroup.EAST_ASIAN);
            span.setFontSize(SystemConfig.getDouble(General.PPT_HOLY_COMMUNION_NAME_LIST_FONT_SIZE));
        }

        // 补齐单元格
        int m = nameList.size() % colCount;
        if (m > 0) {
            m = colCount - m;
            logger.debug("还需要再补{}个单元格", m);
            for (int i = 0; i < m; i++) {
                row.addCell();
            }
        }

        logger.debug("设置每一列的宽度");
        double columnWidth = w / colCount;
        for (int i = 0; i < colCount; i++) {
            table.setColumnWidth(i, columnWidth);
        }

        logger.info("非会友领餐名单幻灯片制作完成");
    }
}
