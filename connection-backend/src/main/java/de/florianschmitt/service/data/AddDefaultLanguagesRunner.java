package de.florianschmitt.service.data;

import com.google.common.collect.Sets;
import de.florianschmitt.model.entities.ELanguage;
import de.florianschmitt.model.entities.ELocalized;
import de.florianschmitt.model.entities.ELocalizedLanguageEnum;
import de.florianschmitt.service.LanguageService;
import de.florianschmitt.system.util.DevPostgresProfile;
import de.florianschmitt.system.util.DevProfile;
import de.florianschmitt.system.util.ProductiveProfile;
import de.florianschmitt.system.util.TestingProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@ProductiveProfile
@TestingProfile
@DevPostgresProfile
@DevProfile
@Component
public class AddDefaultLanguagesRunner implements CommandLineRunner {

    @Autowired
    private LanguageService languageService;

    @Override
    public void run(String... args) throws Exception {
        addLanguages();
    }

    private void addLanguages() {
        if (languageService.hasAtLeastOneLanguage())
            return;

        addArabic();
        addTurkish();
        addSerboCroatic();
    }

    private void addArabic() {
        ELanguage l = new ELanguage("ARABIC", 0);

        ELocalized l1 = new ELocalized(l, ELocalizedLanguageEnum.DE, "Arabisch");
        ELocalized l2 = new ELocalized(l, ELocalizedLanguageEnum.AR, "العربية");
        ELocalized l3 = new ELocalized(l, ELocalizedLanguageEnum.EN, "Arabic");

        l.setLocalized(Sets.newHashSet(l1, l2, l3));
        languageService.save(l);
    }

    private void addTurkish() {
        ELanguage l = new ELanguage("TURKISH", 1);

        ELocalized l1 = new ELocalized(l, ELocalizedLanguageEnum.DE, "Türkisch");
        ELocalized l2 = new ELocalized(l, ELocalizedLanguageEnum.AR, "اللغة التركية");
        ELocalized l3 = new ELocalized(l, ELocalizedLanguageEnum.EN, "Turkish");


        l.setLocalized(Sets.newHashSet(l1, l2, l3));
        languageService.save(l);
    }

    private void addSerboCroatic() {
        ELanguage l = new ELanguage("SERBOCROATIAN", 2);

        ELocalized l1 = new ELocalized(l, ELocalizedLanguageEnum.DE, "Serbo-Kroatisch");
        ELocalized l2 = new ELocalized(l, ELocalizedLanguageEnum.AR, "اللغة التركية");
        ELocalized l3 = new ELocalized(l, ELocalizedLanguageEnum.EN, "Serbo-Croatian");

        l.setLocalized(Sets.newHashSet(l1, l2, l3));
        languageService.save(l);
    }
}
