package claygminx.worshipppt.common.entity;

import java.io.Serializable;

/**
 * 经文清单实体
 */
public class ScriptureContentEntity implements Serializable {

    private static final long serialVersionUID = 6658036227033027631L;

    /**
     * 宣召
     */
    private String summon;

    /**
     * 公祷
     */
    private String publicPray;

    /**
     * 认罪
     */
    private String confess;

    /**
     * 赦罪
     */
    private String forgiveSins;

    /**
     * 读经
     */
    private String readingScripture;

    public String getSummon() {
        return summon;
    }

    public void setSummon(String summon) {
        this.summon = summon;
    }

    public String getPublicPray() {
        return publicPray;
    }

    public void setPublicPray(String publicPray) {
        this.publicPray = publicPray;
    }

    public String getConfess() {
        return confess;
    }

    public void setConfess(String confess) {
        this.confess = confess;
    }

    public String getForgiveSins() {
        return forgiveSins;
    }

    public void setForgiveSins(String forgiveSins) {
        this.forgiveSins = forgiveSins;
    }

    public String getReadingScripture() {
        return readingScripture;
    }

    public void setReadingScripture(String readingScripture) {
        this.readingScripture = readingScripture;
    }

    @Override
    public String toString() {
        return "ScriptureContentEntity{" +
                "summon='" + summon + '\'' +
                ", publicPray='" + publicPray + '\'' +
                ", confess='" + confess + '\'' +
                ", forgiveSins='" + forgiveSins + '\'' +
                ", readingScripture='" + readingScripture + '\'' +
                '}';
    }

}
