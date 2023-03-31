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
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
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
    private static ReplaceData[] cardWords;
    private static ReplaceData[] eventDescriptionWords;
    private static ReplaceData[] eventOptionWords;

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
            Map<String, CardStrings> cardStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "cards");
            if (cardStringsMap != null) {
                for (CardStrings cardStrings : cardStringsMap.values()) {
                    English_UwUifyCardStrings(cardStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "cards", cardStringsMap);
            }
        } catch (Exception e) {
            logger.error("fwickked up making evewything cute :(");
            e.printStackTrace();
        }
    }

    private static void English_UwUifyCardStrings(CardStrings cardStrings) {
        if (cardStrings.DESCRIPTION != null) {
            cardStrings.DESCRIPTION = English_UwUifyString(cardStrings.DESCRIPTION, cardWords);
        }
        if (cardStrings.UPGRADE_DESCRIPTION != null) {
            cardStrings.UPGRADE_DESCRIPTION = English_UwUifyString(cardStrings.UPGRADE_DESCRIPTION, cardWords);
        }
        if (cardStrings.EXTENDED_DESCRIPTION != null) {
            for (int i = 0; i < cardStrings.EXTENDED_DESCRIPTION.length; i++) {
                cardStrings.EXTENDED_DESCRIPTION[i] = English_UwUifyString(cardStrings.EXTENDED_DESCRIPTION[i], cardWords);
            }
        }
    }

    private static String English_UwUifyString(String string, ReplaceData[] replaceDataArray) {
        String result = string;

        // Keyword replacements
        for (ReplaceData replaceData : replaceDataArray) {
            for (String phrase : replaceData.KEYS) {
                if (replaceData.VALUE == null) {
                    replaceData.VALUE = "";
                }

                String replacement = result.replaceAll(phrase, replaceData.VALUE);

                result = replacement;
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
            String cardWordsJson = Gdx.files.internal(localizationPath(getLangString(), "CardWords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            cardWords = gson.fromJson(cardWordsJson, ReplaceData[].class);
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
