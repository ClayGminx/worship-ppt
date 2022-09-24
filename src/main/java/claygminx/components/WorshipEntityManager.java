package claygminx.components;

import claygminx.common.entity.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static claygminx.common.Dict.*;

/**
 * 敬拜实体对象管理器
 */
public class WorshipEntityManager {

    private final static WorshipEntity worshipEntity = new WorshipEntity();
    private final static CoverEntity coverEntity;
    private final static PoetryContentEntity poetryContentEntity;
    private final static ScriptureContentEntity scriptureContentEntity;
    private final static DeclarationEntity declarationEntity;
    private final static PreachEntity preachEntity;
    private final static List<String> familyReports;
    private final static HolyCommunionEntity holyCommunionEntity;

    static {
        coverEntity = new CoverEntity();
        coverEntity.setModel(WorshipModel.WITHOUT_HOLY_COMMUNION);

        poetryContentEntity = new PoetryContentEntity();
        String[] albumNames = new String[] {
                POETRY_ALBUM_NAME.PRAY_POETRY, POETRY_ALBUM_NAME.PRACTISE_POETRY, POETRY_ALBUM_NAME.WORSHIP_POETRY,
                POETRY_ALBUM_NAME.RESPONSE_POETRY, POETRY_ALBUM_NAME.OFFERTORY_POETRY,
                POETRY_ALBUM_NAME.HOLY_COMMUNION_POETRY, POETRY_ALBUM_NAME.INITIATION_POETRY
        };
        for (String albumName : albumNames) {
            List<PoetryEntity> poetryList = new LinkedList<>();
            PoetryAlbumEntity poetryAlbumEntity = new PoetryAlbumEntity();
            poetryAlbumEntity.setName(albumName);
            poetryAlbumEntity.setPoetryList(poetryList);
            switch (albumName) {
                case POETRY_ALBUM_NAME.PRAY_POETRY:
                    poetryContentEntity.setPrayPoetryAlbum(poetryAlbumEntity);
                    break;
                case POETRY_ALBUM_NAME.PRACTISE_POETRY:
                    poetryContentEntity.setPractisePoetryAlbum(poetryAlbumEntity);
                    break;
                case POETRY_ALBUM_NAME.WORSHIP_POETRY:
                    poetryContentEntity.setWorshipPoetryAlbum(poetryAlbumEntity);
                    break;
                case POETRY_ALBUM_NAME.RESPONSE_POETRY:
                    poetryContentEntity.setResponsePoetryAlbum(poetryAlbumEntity);
                    break;
                case POETRY_ALBUM_NAME.OFFERTORY_POETRY:
                    poetryContentEntity.setOffertoryPoetryAlbum(poetryAlbumEntity);
                    break;
                case POETRY_ALBUM_NAME.HOLY_COMMUNION_POETRY:
                    poetryContentEntity.setHolyCommunionPoetryAlbum(poetryAlbumEntity);
                    break;
                default:
                    poetryContentEntity.setInitiationPoetryAlbum(poetryAlbumEntity);
                    break;
            }
        }

        scriptureContentEntity = new ScriptureContentEntity();

        declarationEntity = new DeclarationEntity();

        preachEntity = new PreachEntity();

        familyReports = new ArrayList<>(10);

        holyCommunionEntity = new HolyCommunionEntity();
        List<String> nameList = new ArrayList<>(100);
        holyCommunionEntity.setNameList(nameList);

        worshipEntity.setCover(coverEntity);
        worshipEntity.setPoetryContent(poetryContentEntity);
        worshipEntity.setScriptureContent(scriptureContentEntity);
        worshipEntity.setDeclaration(declarationEntity);
        worshipEntity.setPreach(preachEntity);
        worshipEntity.setFamilyReports(familyReports);
        worshipEntity.setHolyCommunion(holyCommunionEntity);
    }

    private WorshipEntityManager() {
    }

    public static WorshipEntity getWorshipEntity() {
        return worshipEntity;
    }

    public static CoverEntity getCoverEntity() {
        return coverEntity;
    }

    public static PoetryContentEntity getPoetryContentEntity() {
        return poetryContentEntity;
    }

    public static List<PoetryEntity> getPrayPoetryList() {
        return poetryContentEntity.getPrayPoetryAlbum().getPoetryList();
    }

    public static List<PoetryEntity> getPractisePoetryList() {
        return poetryContentEntity.getPractisePoetryAlbum().getPoetryList();
    }

    public static List<PoetryEntity> getWorshipPoetryList() {
        return poetryContentEntity.getWorshipPoetryAlbum().getPoetryList();
    }

    public static List<PoetryEntity> getResponsePoetryList() {
        return poetryContentEntity.getResponsePoetryAlbum().getPoetryList();
    }

    public static List<PoetryEntity> getOffertoryPoetryList() {
        return poetryContentEntity.getOffertoryPoetryAlbum().getPoetryList();
    }

    public static List<PoetryEntity> getHolyCommunionPoetryList() {
        return poetryContentEntity.getHolyCommunionPoetryAlbum().getPoetryList();
    }

    public static List<PoetryEntity> getInitiationPoetryList() {
        return poetryContentEntity.getInitiationPoetryAlbum().getPoetryList();
    }

    public static ScriptureContentEntity getScriptureContentEntity() {
        return scriptureContentEntity;
    }

    public static DeclarationEntity getDeclarationEntity() {
        return declarationEntity;
    }

    public static PreachEntity getPreachEntity() {
        return preachEntity;
    }

    public static List<String> getFamilyReports() {
        return familyReports;
    }

    public static HolyCommunionEntity getHolyCommunionEntity() {
        return holyCommunionEntity;
    }
}
