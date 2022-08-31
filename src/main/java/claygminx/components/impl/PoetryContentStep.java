package claygminx.components.impl;

import claygminx.common.entity.PoetryAlbumEntity;
import claygminx.common.entity.PoetryContentEntity;
import claygminx.common.entity.PoetryEntity;
import claygminx.util.SizeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * 诗歌清单阶段
 */
public class PoetryContentStep extends AbstractWorshipStep {

    private final static Logger logger = LogManager.getLogger(PoetryContentStep.class);

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
        XSLFTable table = slide.createTable();

        // 水平：0.86厘米，垂直：2.62厘米
        // 宽度：22.58厘米
        table.setAnchor(new Rectangle2D.Double(24.4, 74.3, 640, 500));

        List<PoetryAlbumEntity> list = poetryContentEntity.export();

        for (int i = 0; i < list.size(); i += 2) {
            PoetryAlbumEntity left, right = null;
            left = list.get(i);
            if (i < list.size() - 1) {
                right = list.get(i + 1);
            }
            makeTwoRows(table,
                    left.getName(), right == null ? null : right.getName(),
                    left.getPoetryList(), right == null ? null : right.getPoetryList());
        }

        table.setColumnWidth(0, 320);
        table.setColumnWidth(1, 320);

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
            span.setFontSize(28.0);
            span.setBold(true);
            span.setFontFamily("微软雅黑", FontGroup.LATIN);
            span.setFontFamily("微软雅黑", FontGroup.EAST_ASIAN);
        }
    }

    private void makePoetryList(List<PoetryEntity> rightPoetryList, XSLFTableRow row) {
        XSLFTableCell cell = row.addCell();
        if (rightPoetryList != null) {
            cell.setLeftInset(SizeUtil.convertToPoints(1.2));
            XSLFTextParagraph paragraph = cell.addNewTextParagraph();
            paragraph.setSpaceBefore(-9.0);// 段落前空出9磅的间距
            XSLFTextRun span = paragraph.addNewTextRun();

            StringBuilder poetryBuilder = new StringBuilder();
            for (PoetryEntity poetry : rightPoetryList) {
                poetryBuilder.append(poetry.getName()).append("\n");
            }
            span.setText(poetryBuilder.toString());

            span.setFontSize(18.0);
            span.setFontFamily("微软雅黑", FontGroup.LATIN);
            span.setFontFamily("微软雅黑", FontGroup.EAST_ASIAN);
        }
    }
}
