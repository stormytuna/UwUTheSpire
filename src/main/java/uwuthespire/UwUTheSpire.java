package uwuthespire;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;
import uwuthespire.util.GeneralUtils;
import uwuthespire.util.KeywordInfo;
import uwuthespire.util.ReplaceData;
import uwuthespire.util.TextureLoader;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.megacrit.cardcrawl.core.Settings.GameLanguage.ENG;

@SpireInitializer
public class UwUTheSpire implements
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber {
    private static final String resourcesFolder = "uwuthespire";
    private static final String defaultLanguage = "eng";
    private static final Gson gson = new Gson();
    public static ModInfo info;
    public static String modID;
    public static final Logger logger = LogManager.getLogger(modID);
    public static Settings.GameLanguage[] SupportedLanguages = {ENG};
    private static ReplaceData[] replaceDataArray;
    private static ReplaceData[] globalReplaceDataArray;

    static {
        loadModInfo();
    }

    public UwUTheSpire() {
        BaseMod.subscribe(this);
        logger.info(modID + " subscwibed to BaseMod :D");
    }

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    public static void initialize() {
        new UwUTheSpire();
    }

    /*----------Localization----------*/

    private static String getLangString() {
        return Settings.language.name().toLowerCase();
    }

    public static void PostLoadLocalizationStrings(LocalizedStrings localizedStrings) {
        logger.info("makin evewything cyoot :3");

        if (Objects.requireNonNull(Settings.language) == ENG) {
            English_UwUifyStrings(localizedStrings);
        }
    }

    private static void English_UwUifyStrings(LocalizedStrings localizedStrings) {
        try {
            Map<String, MonsterStrings> monsterStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "monsters");
            if (monsterStringsMap != null) {
                for (MonsterStrings monsterStrings : monsterStringsMap.values()) {
                    English_UwUifyMonsterStrings(monsterStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "monsters", monsterStringsMap);
            }

            Map<String, PowerStrings> powerStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "powers");
            if (powerStringsMap != null) {
                for (PowerStrings powerStrings : powerStringsMap.values()) {
                    English_UwUifyPowerStrings(powerStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "powers", powerStringsMap);
            }

            Map<String, CardStrings> cardStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "cards");
            if (cardStringsMap != null) {
                for (CardStrings cardStrings : cardStringsMap.values()) {
                    English_UwUifyCardStrings(cardStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "cards", cardStringsMap);
            }

            Map<String, RelicStrings> relicStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "relics");
            if (relicStringsMap != null) {
                for (RelicStrings relicStrings : relicStringsMap.values()) {
                    English_UwUifyRelicStrings(relicStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "relics", relicStringsMap);
            }

            Map<String, EventStrings> eventStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "events");
            if (eventStringsMap != null) {
                for (EventStrings eventStrings : eventStringsMap.values()) {
                    English_UwUifyEventStrings(eventStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "events", eventStringsMap);
            }

            Map<String, PotionStrings> potionStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "potions");
            if (potionStringsMap != null) {
                for (PotionStrings potionStrings : potionStringsMap.values()) {
                    English_UwUifyPotionStrings(potionStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "potions", potionStringsMap);
            }

            Map<String, CreditStrings> creditStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "credits");
            if (creditStringsMap != null) {
                for (CreditStrings creditStrings : creditStringsMap.values()) {
                    English_UwUifyCreditStrings(creditStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "credits", creditStringsMap);
            }

            Map<String, TutorialStrings> tutorialStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "tutorials");
            if (tutorialStringsMap != null) {
                for (TutorialStrings tutorialStrings : tutorialStringsMap.values()) {
                    English_UwUifyTutorialStrings(tutorialStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "tutorials", tutorialStringsMap);
            }

            Map<String, CharacterStrings> characterStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "characters");
            if (characterStringsMap != null) {
                for (CharacterStrings characterStrings : characterStringsMap.values()) {
                    English_UwUifyCharacterStrings(characterStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "characters", characterStringsMap);
            }
        } catch (Exception e) {
            logger.error("fwickked up making evewything cute :(");
            e.printStackTrace();
        }
    }

    private static void English_UwUifyMonsterStrings(MonsterStrings monsterStrings) {
        if (monsterStrings.NAME != null) {
            monsterStrings.NAME = English_UwUifyString(monsterStrings.NAME, false);
        }
        if (monsterStrings.DIALOG != null) {
            for (int i = 0; i < monsterStrings.DIALOG.length; i++) {
                monsterStrings.DIALOG[i] = English_UwUifyString(monsterStrings.DIALOG[i], true);
            }
        }
        if (monsterStrings.MOVES != null) {
            for (int i = 0; i < monsterStrings.MOVES.length; i++) {
                monsterStrings.MOVES[i] = English_UwUifyString(monsterStrings.MOVES[i], true);
            }
        }
    }

    private static void English_UwUifyPowerStrings(PowerStrings powerStrings) {
        if (powerStrings.NAME != null) {
            powerStrings.NAME = English_UwUifyString(powerStrings.NAME, false);
        }
        if (powerStrings.DESCRIPTIONS != null) {
            for (int i = 0; i < powerStrings.DESCRIPTIONS.length; i++) {
                powerStrings.DESCRIPTIONS[i] = English_UwUifyString(powerStrings.DESCRIPTIONS[i], true);
            }
        }
    }

    private static void English_UwUifyCardStrings(CardStrings cardStrings) {
        if (cardStrings.NAME != null) {
            cardStrings.NAME = English_UwUifyString(cardStrings.NAME, false);
        }
        if (cardStrings.DESCRIPTION != null) {
            cardStrings.DESCRIPTION = English_UwUifyString(cardStrings.DESCRIPTION, true);
        }
        if (cardStrings.UPGRADE_DESCRIPTION != null) {
            cardStrings.UPGRADE_DESCRIPTION = English_UwUifyString(cardStrings.UPGRADE_DESCRIPTION, true);
        }
        if (cardStrings.EXTENDED_DESCRIPTION != null) {
            for (int i = 0; i < cardStrings.EXTENDED_DESCRIPTION.length; i++) {
                cardStrings.EXTENDED_DESCRIPTION[i] = English_UwUifyString(cardStrings.EXTENDED_DESCRIPTION[i], true);
            }
        }
    }

    private static void English_UwUifyRelicStrings(RelicStrings relicStrings) {
        if (relicStrings.NAME != null) {
            relicStrings.NAME = English_UwUifyString(relicStrings.NAME, false);
        }
        if (relicStrings.FLAVOR != null) {
            relicStrings.FLAVOR = English_UwUifyString(relicStrings.FLAVOR, true);
        }
        if (relicStrings.DESCRIPTIONS != null) {
            for (int i = 0; i < relicStrings.DESCRIPTIONS.length; i++) {
                relicStrings.DESCRIPTIONS[i] = English_UwUifyString(relicStrings.DESCRIPTIONS[i], true);
            }
        }
    }

    private static void English_UwUifyEventStrings(EventStrings eventStrings) {
        if (eventStrings.NAME != null) {
            eventStrings.NAME = English_UwUifyString(eventStrings.NAME, false);
        }
        if (eventStrings.DESCRIPTIONS != null) {
            for (int i = 0; i < eventStrings.DESCRIPTIONS.length; i++) {
                eventStrings.DESCRIPTIONS[i] = English_UwUifyString(eventStrings.DESCRIPTIONS[i], true);
            }
        }
        if (eventStrings.OPTIONS != null) {
            for (int i = 0; i < eventStrings.OPTIONS.length; i++) {
                eventStrings.OPTIONS[i] = English_UwUifyString(eventStrings.OPTIONS[i], true);
            }
        }
    }

    private static void English_UwUifyPotionStrings(PotionStrings potionStrings) {
        if (potionStrings.NAME != null) {
            potionStrings.NAME = English_UwUifyString(potionStrings.NAME, false);
        }
        if (potionStrings.DESCRIPTIONS != null) {
            for (int i = 0; i < potionStrings.DESCRIPTIONS.length; i++) {
                potionStrings.DESCRIPTIONS[i] = English_UwUifyString(potionStrings.DESCRIPTIONS[i], true);
            }
        }
    }

    private static void English_UwUifyCreditStrings(CreditStrings creditStrings) {
        if (creditStrings.HEADER != null) {
            creditStrings.HEADER = English_UwUifyString(creditStrings.HEADER, false);
        }
        if (creditStrings.NAMES != null) {
            for (int i = 0; i < creditStrings.NAMES.length; i++) {
                creditStrings.NAMES[i] = English_UwUifyString(creditStrings.NAMES[i], false);
            }
        }
    }

    private static void English_UwUifyTutorialStrings(TutorialStrings tutorialStrings) {
        if (tutorialStrings.LABEL != null) {
            for (int i = 0; i < tutorialStrings.LABEL.length; i++) {
                tutorialStrings.LABEL[i] = English_UwUifyString(tutorialStrings.LABEL[i], false);
            }
        }
        if (tutorialStrings.TEXT != null) {
            for (int i = 0; i < tutorialStrings.TEXT.length; i++) {
                tutorialStrings.TEXT[i] = English_UwUifyString(tutorialStrings.TEXT[i], false);
            }
        }
    }

    private static void English_UwUifyCharacterStrings(CharacterStrings characterStrings) {
        if (characterStrings.NAMES != null) {
            for (int i = 0; i < characterStrings.NAMES.length; i++) {
                characterStrings.NAMES[i] = English_UwUifyString(characterStrings.NAMES[i], false);
            }
        }
        if (characterStrings.OPTIONS != null) {
            for (int i = 0; i < characterStrings.OPTIONS.length; i++) {
                characterStrings.OPTIONS[i] = English_UwUifyString(characterStrings.OPTIONS[i], false);
            }
        }
        if (characterStrings.TEXT != null) {
            for (int i = 0; i < characterStrings.TEXT.length; i++) {
                characterStrings.TEXT[i] = English_UwUifyString(characterStrings.TEXT[i], false);
            }
        }
        if (characterStrings.UNIQUE_REWARDS != null) {
            for (int i = 0; i < characterStrings.UNIQUE_REWARDS.length; i++) {
                characterStrings.UNIQUE_REWARDS[i] = English_UwUifyString(characterStrings.UNIQUE_REWARDS[i], false);
            }
        }
    }

    private static boolean English_SkipWord(String word, ReplaceData[] replaceDataArray) {
        if (replaceDataArray == null) {
            return false;
        }

        for (ReplaceData replaceData : replaceDataArray) {
            if (word.contains(replaceData.VALUE)) {
                return true;
            }
        }

        return false;
    }

    private static String English_UwUifyString(String string, boolean careAboutReplaceData) {
        String result = string;

        // Replace data replacements
        // These generally don't happen for names
        if (careAboutReplaceData) {
            for (ReplaceData replaceData : replaceDataArray) {
                for (String phrase : replaceData.KEYS) {
                    if (replaceData.VALUE == null) {
                        replaceData.VALUE = "";
                    }

                    result = result.replaceAll(phrase, replaceData.VALUE);
                }
            }
        }

        // Global replace data replacements
        for (ReplaceData globalReplaceData : globalReplaceDataArray) {
            for (String phrase : globalReplaceData.KEYS) {
                if (globalReplaceData.VALUE == null) {
                    globalReplaceData.VALUE = "";
                }

                result = result.replaceAll(phrase, globalReplaceData.VALUE);
            }
        }

        // Make letters smol
        result = result.toLowerCase();

        // Make big letters that are supposed be big, big again
        result = result.replaceAll("!d!", "!D!");
        result = result.replaceAll("!b!", "!B!");
        result = result.replaceAll("!m!", "!M!");
        result = result.replaceAll(" nl ", " NL ");
        result = result.replaceAll("\\[e]", "[E]");
        result = result.replaceAll("\\[r]", "[R]");
        result = result.replaceAll("\\[g]", "[G]");
        result = result.replaceAll("\\[b]", "[B]");
        result = result.replaceAll("\\[w]", "[W]");

        // Replace some common letter patterns
        String[] words = result.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String lastTwoChars = GeneralUtils.lastChars(word, 2);
            String lastThreeChars = GeneralUtils.lastChars(word, 3);
            String allButLastTwoChars = GeneralUtils.allButLastChars(word, 2);
            String allButLastThreeChars = GeneralUtils.allButLastChars(word, 3);

            if (careAboutReplaceData && English_SkipWord(word, replaceDataArray)) {
                continue;
            }

            if (word.contains("l")) {
                if (lastTwoChars.equals("le") || lastTwoChars.equals("ll")) {
                    word = allButLastTwoChars.replaceAll("(l|r)", "w") + lastTwoChars;
                } else if (lastThreeChars.equals("les") || lastThreeChars.equals("lls")) {
                    word = allButLastThreeChars.replaceAll("l", "w") + lastThreeChars;
                } else {
                    word = word.replaceAll("(l|r)", "w");
                }
            } else if (word.contains("r")) {
                if (lastTwoChars.equals("er") || lastTwoChars.equals("re")) {
                    word = allButLastTwoChars.replaceAll("r", "w") + lastTwoChars;
                } else if (lastThreeChars.equals("ers") || lastThreeChars.equals("res")) {
                    word = allButLastThreeChars.replaceAll("r", "w") + lastThreeChars;
                } else {
                    word = word.replaceAll("r", "w");
                }
            }

            words[i] = word;
        }
        result = String.join(" ", words);

        return result;
    }

    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String resourcePath(String file) {
        return resourcesFolder + "/" + file;
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(UwUTheSpire.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receiveEditStrings() {
        try {
            String replaceDataJson = Gdx.files.internal(localizationPath(getLangString(), "ReplaceData.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            replaceDataArray = gson.fromJson(replaceDataJson, ReplaceData[].class);

            String globalReplaceDataJson = Gdx.files.internal(localizationPath(getLangString(), "GlobalReplaceData.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            globalReplaceDataArray = gson.fromJson(globalReplaceDataJson, ReplaceData[].class);
        } catch (Exception e) {
            logger.error("fwickked up woading some impowtant stuffies (っ◞‸◟ c)");
            e.printStackTrace();
        }

    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    @Override
    public void receiveEditKeywords() {
        try {
            String keywordsJson = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordInfo[] keywords = gson.fromJson(keywordsJson, KeywordInfo[].class);

            if (keywords != null) {
                for (KeywordInfo keyword : keywords) {
                    BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        } catch (Exception e) {
            logger.error("fwickked up woading some impowtant stuffies (っ◞‸◟ c)");
            e.printStackTrace();
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
    }
}
