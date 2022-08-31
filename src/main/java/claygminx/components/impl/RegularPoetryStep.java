package claygminx.components.impl;

import claygminx.common.entity.PoetryEntity;
import claygminx.exception.SystemException;
import claygminx.util.PictureUtil;
import claygminx.util.SizeUtil;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 常规诗歌阶段
 */
public class RegularPoetryStep extends AbstractWorshipStep {

    private final List<PoetryEntity> poetryList;

    private String fileExtensionName = ".png";

    private double pictureLength = 24.3;

    private double top = 0.0;

    private double left = 0.0;

    public RegularPoetryStep(XMLSlideShow ppt, String layout, List<PoetryEntity> poetryList) {
        super(ppt, layout);
        this.poetryList = poetryList;
    }

    @Override
    public void execute() throws Exception {
        for (PoetryEntity poetry : poetryList) {
            File directory = poetry.getDirectory();
            checkDirectory(directory);
            File[] files = directory.listFiles((dir, name) -> name.endsWith(fileExtensionName));
            if (files == null || files.length == 0) {
                return;
            }
            sortFiles(files);

            // 下面开始一张张地制作幻灯片
            makeSlides(files);
        }
    }

    public void setFileExtensionName(String fileExtensionName) {
        this.fileExtensionName = fileExtensionName;
    }

    /**
     * 制作幻灯片
     * @param files 简谱图片
     * @throws IOException 添加图片到幻灯片时可能发生的异常
     */
    private void makeSlides(File[] files) throws IOException {
        XSLFPictureData.PictureType pictureType = PictureUtil.getPictureType(fileExtensionName);
        if (pictureType == null) {
            throw new SystemException("文件扩展名[" + fileExtensionName + "]错误！");
        }

        XMLSlideShow ppt = getPpt();
        XSLFSlideLayout layout = ppt.findLayout(getLayout());
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            XSLFSlide slide = ppt.createSlide(layout);
            XSLFPictureData picData = ppt.addPicture(file, pictureType);
            XSLFPictureShape picture = slide.createPicture(picData);
            resizePicture(picture);
            setPageNumber(slide, files.length, i + 1, picture.getAnchor().getHeight());
        }
    }


    /**
     * 检查诗歌所在的文件夹
     */
    private void checkDirectory(File directory) throws Exception {
        if (directory == null) {
            throw new IllegalArgumentException("未指定简谱所在的文件夹！");
        }
        if (!directory.exists()) {
            throw new FileNotFoundException("简谱文件夹[" + directory.getAbsolutePath() + "]不存在！");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("[" + directory.getAbsolutePath() + "]不是文件夹！");
        }
    }

    /**
     * 排序文件
     */
    private void sortFiles(File[] files) {
        Arrays.sort(files, (o1, o2) -> {
            int i1 = getFileIndex(o1.getName());
            int i2 = getFileIndex(o2.getName());
            return i1 - i2;
        });
    }

    /**
     * 获取文件序号
     * <p>文件名称的格式是：[诗歌名称]_Page[序号].png</p>
     * @param filename 文件名称
     * @return 文件序号
     */
    private int getFileIndex(String filename) {
        String[] split = filename.split("_");
        String last = split[split.length - 1];
        split = last.split("[.]");
        String page = split[0];
        return Integer.parseInt(page.substring(4));
    }

    /**
     * 调整图片地尺寸
     * <p>预设图片宽度是24.3厘米，但是程序的长度单位是像素，转换公式是 1px=0.035275</p>
     * @param picture 简谱图片
     */
    private void resizePicture(XSLFPictureShape picture) {
        double width = SizeUtil.convertToPoints(pictureLength);
        Rectangle2D anchor = picture.getAnchor();
        double ratio = anchor.getHeight() / anchor.getWidth();// 保持宽高比
        picture.setAnchor(new Rectangle2D.Double(left, top, width, width * ratio));
    }

    private void setPageNumber(XSLFSlide slide, int totalCount, int current, double top) {
        XSLFTextShape placeholder = slide.getPlaceholder(0);
        placeholder.clearText();
        placeholder.setText(current + "/" + totalCount);
        Rectangle2D anchor = placeholder.getAnchor();
        placeholder.setAnchor(new Rectangle2D.Double(anchor.getX(), top, anchor.getWidth(), anchor.getHeight()));
    }

}
