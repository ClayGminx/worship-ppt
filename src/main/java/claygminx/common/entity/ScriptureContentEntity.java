package claygminx.common.entity;

/**
 * 经文清单实体
 */
public class ScriptureContentEntity extends WorshipPropertyBean {

    public final static String SUMMON = "summon";
    public final static String PUBLIC_PRAY = "publicPray";
    public final static String CONFESS = "confess";
    public final static String FORGIVE_SINS = "forgiveSins";
    public final static String READING_SCRIPTURE = "readingScripture";

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
        firePropertyChange(SUMMON, this.summon, summon);
        this.summon = summon;
    }

    public String getPublicPray() {
        return publicPray;
    }

    public void setPublicPray(String publicPray) {
        firePropertyChange(PUBLIC_PRAY, this.publicPray, publicPray);
        this.publicPray = publicPray;
    }

    public String getConfess() {
        return confess;
    }

    public void setConfess(String confess) {
        firePropertyChange(CONFESS, this.confess, confess);
        this.confess = confess;
    }

    public String getForgiveSins() {
        return forgiveSins;
    }

    public void setForgiveSins(String forgiveSins) {
        firePropertyChange(FORGIVE_SINS, this.forgiveSins, forgiveSins);
        this.forgiveSins = forgiveSins;
    }

    public String getReadingScripture() {
        return readingScripture;
    }

    public void setReadingScripture(String readingScripture) {
        firePropertyChange(READING_SCRIPTURE, this.readingScripture, readingScripture);
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
