package claygminx.worshipppt.components.impl;

import claygminx.worshipppt.common.config.SystemConfig;
import claygminx.common.entity.*;
import claygminx.worshipppt.common.entity.*;
import claygminx.worshipppt.components.InputService;
import claygminx.worshipppt.exception.InputServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static claygminx.worshipppt.common.Dict.*;

/**
 * 输入服务
 */
public class InputServiceImpl implements InputService {

    private final static Logger logger = LoggerFactory.getLogger(InputService.class);

    // 单实例模式
    private static InputService instance;

    private InputServiceImpl() {
    }

    @Override
    public WorshipEntity readIni(String filePath) throws InputServiceException {
        logger.info("读取输入文件：" + filePath);
        Wini wini;
        try {
            wini = new Wini(new File(filePath));
        } catch (IOException e) {
            String message = String.format("读取文件[%s]时发生异常！", filePath);
            throw new InputServiceException(message, e);
        }

        CoverEntity coverEntity = readCover(wini);
        PoetryContentEntity poetryContentEntity = readPoetry(wini);
        ScriptureContentEntity scriptureContentEntity = readScripture(wini);
        DeclarationEntity declarationEntity = readDeclaration(wini);
        PreachEntity preachEntity = readPreach(wini);
        List<String> familyReports = readFamilyReport(wini);
        HolyCommunionEntity holyCommunion = readHolyCommunion(wini);

        WorshipEntity worshipEntity = new WorshipEntity();
        worshipEntity.setCover(coverEntity);
        worshipEntity.setPoetryContent(poetryContentEntity);
        worshipEntity.setScriptureContent(scriptureContentEntity);
        worshipEntity.setDeclaration(declarationEntity);
        worshipEntity.setPreach(preachEntity);
        worshipEntity.setFamilyReports(familyReports);
        worshipEntity.setHolyCommunion(holyCommunion);

        logger.info("读取完成");
        return worshipEntity;
    }

    /**
     * 获取实例
     * @return 输入服务实例对象
     */
    public synchronized static InputService getInstance() {
        if (instance == null) {
            logger.debug("实例化" + InputService.class);
            instance = new InputServiceImpl();
        }
        return instance;
    }

    private CoverEntity readCover(Wini wini) throws InputServiceException {
        logger.debug("开始读取封面信息");
        Profile.Section section = wini.get(InputSection.COVER);
        if (section != null) {
            String worshipModel = section.get(CoverKey.MODEL);
            String worshipDate = section.get(CoverKey.WORSHIP_DATE);
            String churchName = section.get(CoverKey.CHURCH_NAME);

            if (worshipModel == null || worshipModel.isEmpty()) {
                throw new InputServiceException("没有敬拜模式！");
            }
            if (worshipDate == null || worshipDate.isEmpty()) {
                throw new InputServiceException("没有主日敬拜日期！");
            }
//            if (churchName == null || churchName.isEmpty()) {
//                throw new InputServiceException("没有教会名称！");
//            }

            logger.debug("用户选择的敬拜模式是" + worshipModel);
            String modelKey = PPTProperty.GENERAL_WORSHIP_MODEL_PREFIX + worshipModel;
            String pptTemplateName = SystemConfig.getString(modelKey);
            if (pptTemplateName == null) {
                throw new InputServiceException("不支持" + worshipModel + "！");
            }
            worshipModel = pptTemplateName;

            CoverEntity coverEntity = new CoverEntity();
            coverEntity.setModel(worshipModel);
            coverEntity.setWorshipDate(worshipDate);
            coverEntity.setChurchName(churchName);
            logger.debug("封面信息：" + coverEntity);
            return coverEntity;
        }
        throw new InputServiceException("没有封面参数！");
    }

    private PoetryContentEntity readPoetry(Wini wini) {
        logger.debug("开始读取诗歌信息");
        PoetryContentEntity result = new PoetryContentEntity();
        PoetryAlbumEntity poetryAlbumEntity;
        poetryAlbumEntity = readPoetry(wini, InputSection.PRAY_POETRY);
        result.setPrayPoetryAlbum(poetryAlbumEntity);
        poetryAlbumEntity = readPoetry(wini, InputSection.PRACTISE_POETRY);
        result.setPractisePoetryAlbum(poetryAlbumEntity);
        poetryAlbumEntity = readPoetry(wini, InputSection.WORSHIP_POETRY);
        result.setWorshipPoetryAlbum(poetryAlbumEntity);
        poetryAlbumEntity = readPoetry(wini, InputSection.RESPONSE_POETRY);
        result.setResponsePoetryAlbum(poetryAlbumEntity);
        poetryAlbumEntity = readPoetry(wini, InputSection.OFFERTORY_POETRY);
        result.setOffertoryPoetryAlbum(poetryAlbumEntity);
        poetryAlbumEntity = readPoetry(wini, InputSection.HOLY_COMMUNION_POETRY);
        result.setHolyCommunionPoetryAlbum(poetryAlbumEntity);
        poetryAlbumEntity = readPoetry(wini, InputSection.INITIATION_POETRY);
        result.setInitiationPoetryAlbum(poetryAlbumEntity);
        logger.debug("诗歌信息读取完成");
        return result;
    }

    private PoetryAlbumEntity readPoetry(Wini wini, String sectionName) {
        logger.debug("读取{}的信息", sectionName);
        Profile.Section section = wini.get(sectionName);
        if (section != null && !section.isEmpty()) {
            Set<String> poetryNames = section.keySet();
            List<PoetryEntity> poetryList = new ArrayList<>(poetryNames.size());
            PoetryAlbumEntity poetryAlbumEntity = new PoetryAlbumEntity();
            poetryAlbumEntity.setName(sectionName);
            poetryAlbumEntity.setPoetryList(poetryList);
            for (String poetryName : poetryNames) {
                String path = section.get(poetryName);
                logger.debug("{}={}", poetryName, path);
                PoetryEntity poetryEntity = new PoetryEntity(poetryName, new File(path));
                poetryList.add(poetryEntity);
            }
            return poetryAlbumEntity;
        }
        return null;
    }

    private ScriptureContentEntity readScripture(Wini wini) throws InputServiceException {
        logger.debug("开始读取经文信息");
        ScriptureContentEntity result = new ScriptureContentEntity();

        Profile.Section section = wini.get(InputSection.SCRIPTURE);

        if (section == null || section.isEmpty()) {
            throw new InputServiceException("没有经文编号！");
        }

        if (section.containsKey(ScriptureScene.SUMMON)) {
            result.setSummon(section.get(ScriptureScene.SUMMON));
        }

        if (section.containsKey(ScriptureScene.PUBLIC_PRAY)) {
            result.setPublicPray(section.get(ScriptureScene.PUBLIC_PRAY));
        }

        if (section.containsKey(ScriptureScene.CONFESS)) {
            result.setConfess(section.get(ScriptureScene.CONFESS));
        }

        if (section.containsKey(ScriptureScene.FORGIVE_SINS)) {
            result.setForgiveSins(section.get(ScriptureScene.FORGIVE_SINS));
        }

        if (section.containsKey(ScriptureScene.READING_SCRIPTURE)) {
            result.setReadingScripture(section.get(ScriptureScene.READING_SCRIPTURE));
        }

        logger.debug("经文信息：" + result);
        return result;
    }

    private DeclarationEntity readDeclaration(Wini wini) throws InputServiceException {
        logger.debug("开始读取宣信信息");
        Profile.Section section = wini.get(InputSection.DECLARATION);
        if (section == null || section.isEmpty()) {
            throw new InputServiceException("没有宣信内容吗？");
        }

        DeclarationEntity result = new DeclarationEntity();

        if (section.containsKey(DeclarationKey.TITLE)) {
            result.setTitle(section.get(DeclarationKey.TITLE));
        } else {
            throw new InputServiceException("没有宣信主题");
        }

        result.setSpeaker(Optional.ofNullable(section.get(DeclarationKey.SPEAKER)).orElse(""));

        logger.debug("宣信信息：" + result);
        return result;
    }

    private PreachEntity readPreach(Wini wini) throws InputServiceException {
        logger.debug("开始读取证道信息");
        Profile.Section section = wini.get(InputSection.PREACH);
        if (section == null || section.isEmpty()) {
            throw new InputServiceException("怎么可以没有证道信息呢？");
        }

        PreachEntity result = new PreachEntity();

        if (section.containsKey(PreachKey.TITLE)) {
            result.setTitle(section.get(PreachKey.TITLE));
        } else {
            throw new InputServiceException("没有证道主题");
        }

        if (section.containsKey(PreachKey.SCRIPTURE)) {
            result.setScriptureNumber(section.get(PreachKey.SCRIPTURE));
        } else {
            throw new InputServiceException("没有证道经文");
        }

        logger.debug("证道信息：" + result);

        return result;
    }

    private List<String> readFamilyReport(Wini wini) {
        logger.debug("开始读取家事报告信息");
        Profile.Section section = wini.get(InputSection.FAMILY_REPORT);
        if (section != null && !section.isEmpty()) {
            Set<String> keySet = section.keySet();
            List<String> familyReports = new ArrayList<>(keySet.size());
            for (String key : keySet) {
                String item = section.get(key);
                logger.debug(item);
                familyReports.add(item);
            }
            return familyReports;
        }
        return null;
    }

    private HolyCommunionEntity readHolyCommunion(Wini wini) {
        logger.debug("开始读取圣餐信息");
        Profile.Section section = wini.get(InputSection.HOLY_COMMUNION);
        if (section != null && !section.isEmpty()) {
            HolyCommunionEntity result = new HolyCommunionEntity();
            String nameList = section.get(HolyCommunionKey.NAME_LIST);
            if (nameList != null && !nameList.isEmpty()) {
                String[] split = nameList.split(",");
                result.setNameList(Arrays.asList(split));
            }
            return result;
        }
        return null;
    }

}
