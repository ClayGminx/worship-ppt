package claygminx.worshipppt.common.entity;

import java.io.File;

/**
 * 诗歌实体
 */
public class PoetryEntity extends WorshipPropertyBean {

    public final static String NAME = "name";
    public final static String DIRECTORY = "directory";

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
        firePropertyChange(NAME, this.name, name);
        this.name = name;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        firePropertyChange(DIRECTORY, this.directory, directory);
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
