package claygminx.components.impl;

import claygminx.util.SizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * 非会友领餐名单阶段
 */
public class HolyCommunionNameListStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(HolyCommunionNameListStep.class);

    private final static int COLUMN_COUNT = 5;
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

        /*
         * 用表格来展示名单
         * -------------
         * 每行5个单元格，每个单元格4.7厘米宽，1.64厘米高
         * 字体都是微软雅黑，28，单行距
         */
        if (!nameList.isEmpty()) {
            XSLFTable table = slide.createTable();
            double x = SizeUtil.convertToPoints(0.82),
                    y = SizeUtil.convertToPoints(3.75),
                    w = SizeUtil.convertToPoints(23.52),
                    h = SizeUtil.convertToPoints(1.64);
            table.setAnchor(new Rectangle2D.Double(x, y, w, h));

            XSLFTableRow row = null;
            for (int i = 0; i < nameList.size(); i++) {
                if (i % COLUMN_COUNT == 0) {
                    row = table.addRow();
                }
                XSLFTableCell cell = row.addCell();
                XSLFTextParagraph paragraph = cell.addNewTextParagraph();
                paragraph.setLineSpacing(100.0);
                XSLFTextRun span = paragraph.addNewTextRun();
                span.setText(nameList.get(i));
                span.setFontFamily(getFontFamily(), FontGroup.LATIN);
                span.setFontFamily(getFontFamily(), FontGroup.EAST_ASIAN);
                span.setFontSize(28.0);
            }

            // 补齐单元格
            int m = nameList.size() % COLUMN_COUNT;
            if (m > 0) {
                m = COLUMN_COUNT - m;
                for (int i = 0; i < m; i++) {
                    row.addCell();
                }
            }

            double columnWidth = w / COLUMN_COUNT;
            for (int i = 0; i < COLUMN_COUNT; i++) {
                table.setColumnWidth(i, columnWidth);
            }
        }

        logger.info("非会友领餐名单幻灯片制作完成");
    }
}
