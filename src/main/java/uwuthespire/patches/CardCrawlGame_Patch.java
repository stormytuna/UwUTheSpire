package uwuthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import uwuthespire.UwUTheSpire;

public class CardCrawlGame_Patch {
    @SpirePatch(clz = CardCrawlGame.class, method = "create")
    public static class CardCrawlGame_PostLocalizationLoaded {
        @SpireInsertPatch(locator = Locator.class, localvars = {"languagePack"})
        public static void patch(CardCrawlGame __instance, LocalizedStrings languagePack) {
            for (Settings.GameLanguage language : UwUTheSpire.SupportedLanguages) {
                if (language.equals(Settings.language)) {
                    UwUTheSpire.PostLoadLocalizationStrings(languagePack);
                    return;
                }
            }
        }
    }
 
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws PatchingException, CannotCompileException {
            Matcher matcher = new Matcher.FieldAccessMatcher(Settings.class, "IS_FULLSCREEN");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}
