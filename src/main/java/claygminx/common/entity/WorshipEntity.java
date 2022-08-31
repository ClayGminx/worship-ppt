package claygminx.common.entity;

import java.util.List;

/**
 * 敬拜参数实体
 */
public class WorshipEntity {

    /**
     * 封面
     */
    private CoverEntity cover;

    /**
     * 诗歌清单
     */
    private PoetryContentEntity poetryContent;

    /**
     * 经文编号集
     */
    private ScriptureContentEntity scriptureContent;

    /**
     * 宣信
     */
    private DeclarationEntity declaration;

    /**
     * 证道
     */
    private PreachEntity preach;

    /**
     * 家事报告
     */
    private List<String> familyReports;

    public CoverEntity getCover() {
        return cover;
    }

    public void setCover(CoverEntity cover) {
        this.cover = cover;
    }

    public PoetryContentEntity getPoetryContent() {
        return poetryContent;
    }

    public void setPoetryContent(PoetryContentEntity poetryContent) {
        this.poetryContent = poetryContent;
    }

    public ScriptureContentEntity getScriptureContent() {
        return scriptureContent;
    }

    public void setScriptureContent(ScriptureContentEntity scriptureContent) {
        this.scriptureContent = scriptureContent;
    }

    public DeclarationEntity getDeclaration() {
        return declaration;
    }

    public void setDeclaration(DeclarationEntity declaration) {
        this.declaration = declaration;
    }

    public PreachEntity getPreach() {
        return preach;
    }

    public void setPreach(PreachEntity preach) {
        this.preach = preach;
    }

    public List<String> getFamilyReports() {
        return familyReports;
    }

    public void setFamilyReports(List<String> familyReports) {
        this.familyReports = familyReports;
    }
}
