package claygminx.components.impl;

import claygminx.common.config.SystemConfig;
import claygminx.common.entity.PoetryAlbumEntity;
import claygminx.common.entity.PoetryContentEntity;
import claygminx.common.entity.PoetryEntity;
import claygminx.util.SizeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.util.List;

import static claygminx.common.Dict.*;

/**
 * 诗歌清单阶段
 */
public class PoetryContentStep extends AbstractWorshipStep {

    private final static Logger logger = LoggerFactory.getLogger(PoetryContentStep.class);

    private final PoetryContentEntity poetryContentEntity;

    public PoetryContentStep(XMLSlideShow ppt, String layout, PoetryContentEntity poetryContentEntity) {
        super(ppt, layout);
        this.poetryContentEntity = poetryContentEntity;
    }

    @Override
    public void execute() {
        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout poemManifest = ppt.findLayout(getLayout());
        XSLFSlide slide = ppt.createSlide(poemManifest);

        logger.debug("创建表格");
        XSLFTable table = slide.createTable();

        logger.debug("初始化表格的尺寸");
        double x = SizeUtil.convertToPoints(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_X)),
                y = SizeUtil.convertToPoints(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_Y)),
                w = SizeUtil.convertToPoints(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_W)),
                h = SizeUtil.convertToPoints(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_H));
        table.setAnchor(new Rectangle2D.Double(x, y, w, h));

        int colCount = SystemConfig.getInt(PPTProperty.POETRY_CONTENT_COL_COUNT);
        List<PoetryAlbumEntity> list = poetryContentEntity.export();
        logger.debug(list.size() + "个诗歌集");
        for (int i = 0; i < list.size(); i += colCount) {
            PoetryAlbumEntity left, right = null;
            left = list.get(i);
            if (i < list.size() - 1) {
                right = list.get(i + 1);
            }
            makeTwoRows(table,
                    left.getName(), right == null ? null : right.getName(),
                    left.getPoetryList(), right == null ? null : right.getPoetryList());
        }

        double colWidth = w / colCount;
        for (int i = 0; i < colCount; i++) {
            table.setColumnWidth(i, colWidth);
        }

        logger.info("诗歌清单幻灯片制作完成");
    }

    private void makeTwoRows(XSLFTable table,
                    String leftTitle, String rightTitle,
                    List<PoetryEntity> leftPoetryList, List<PoetryEntity> rightPoetryList) {
        XSLFTableRow row = table.addRow();
        makeTitle(leftTitle, row);
        makeTitle(rightTitle, row);
        row = table.addRow();
        makePoetryList(leftPoetryList, row);
        makePoetryList(rightPoetryList, row);
    }

    private void makeTitle(String title, XSLFTableRow row) {
        XSLFTableCell cell = row.addCell();
        if (title != null) {
            XSLFTextParagraph paragraph = cell.addNewTextParagraph();
            XSLFTextRun span = paragraph.addNewTextRun();
            span.setText(title);
            span.setFontSize(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_TITLE_FONT_SIZE));
            span.setBold(true);
            span.setFontFamily(getFontFamily(), FontGroup.LATIN);
            span.setFontFamily(getFontFamily(), FontGroup.EAST_ASIAN);
        }
    }

    private void makePoetryList(List<PoetryEntity> rightPoetryList, XSLFTableRow row) {
        XSLFTableCell cell = row.addCell();
        if (rightPoetryList != null) {
            cell.setLeftInset(SizeUtil.convertToPoints(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_LEFT_INSET)));
            XSLFTextParagraph paragraph = cell.addNewTextParagraph();
            paragraph.setSpaceBefore(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_SPACE_BEFORE));// 段落前空出9磅的间距
            XSLFTextRun span = paragraph.addNewTextRun();

            StringBuilder poetryBuilder = new StringBuilder();
            for (PoetryEntity poetry : rightPoetryList) {
                poetryBuilder.append(poetry.getName()).append("\n");
            }
            span.setText(poetryBuilder.toString());

            span.setFontSize(SystemConfig.getDouble(PPTProperty.POETRY_CONTENT_FONT_SIZE));
            span.setFontFamily(getFontFamily(), FontGroup.LATIN);
            span.setFontFamily(getFontFamily(), FontGroup.EAST_ASIAN);
        }
    }
}
