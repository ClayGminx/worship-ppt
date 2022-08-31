package claygminx.common.entity;

import java.io.File;

/**
 * 诗歌实体
 */
public class PoetryEntity {

    /**
     * 诗歌名称
     */
    private String name;

    /**
     * 诗歌文件所在的文件夹
     */
    private File directory;

    public PoetryEntity(String name, File directory) {
        this.name = name;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }
}
