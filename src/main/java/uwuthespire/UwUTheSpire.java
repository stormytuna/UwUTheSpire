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
        logger.info(modID + " subscribed to BaseMod.");
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
        logger.info("making evewything c-cute :3");

        if (Objects.requireNonNull(Settings.language) == ENG) {
            English_UwUifyStrings(localizedStrings);
        }
    }

    private static void English_UwUifyStrings(LocalizedStrings localizedStrings) {
        try {
            Map<String, CardStrings> cardStringsMap = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "cards");
            if (cardStringsMap != null) {
                for (CardStrings cardStrings : cardStringsMap.values()) {
                    English_UwUifyStrings(cardStrings);
                }
                ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, "cards", cardStringsMap);
            }
        } catch (Exception e) {
            logger.error("fwickked up making evewything cute :(");
        }
    }

    private static void English_UwUifyStrings(CardStrings cardStrings) {

    }

    // Stringy string meaning the actual 'String' class
    private static String English_UwUifyStringyStrings(String string) {
        String textInput = string.toLowerCase();
        String[] words = string.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = English_UwUifyWord(words[i]);
        }
        textInput = String.join(" ", words);
        return textInput;
    }

    private static String English_UwUifyWord(String word) {
        return word;
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
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    @Override
    public void receiveEditKeywords() {
        try {
            String lang = getLangString();

            String importantKeywordWords = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordInfo[] keywords = gson.fromJson(importantKeywordWords, KeywordInfo[].class);

            if (keywords != null) {
                for (KeywordInfo keyword : keywords) {
                    BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        } catch (Exception e) {
            logger.error("fwickked up woading some impowtant stuffies (っ◞‸◟ c)");
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
    }
}
