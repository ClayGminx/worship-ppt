package claygminx.worshipppt.common.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 诗歌实体
 */
public class PoetryEntity implements Serializable {

    private static final long serialVersionUID = 8158226248919803232L;

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

    public void setName(String name) {
        this.name = name;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "PoetryEntity{" +
                "name='" + name + '\'' +
                ", directory=" + directory +
                '}';
    }

}
