package de.florianschmitt.service.util;

import java.util.Comparator;

import de.florianschmitt.model.rest.ELocalizedDTO;

public class CustomELocalizedDTOComparator implements Comparator<ELocalizedDTO> {

    public static CustomELocalizedDTOComparator INSTANCE = new CustomELocalizedDTOComparator();

    @Override
    public int compare(ELocalizedDTO left, ELocalizedDTO right) {
        String leftLocale = left.getLocale();
        String rightLocale = right.getLocale();

        Comparator<String> comparator = Comparator.comparingInt(CustomELocalizedDTOComparator::localeOrder);

        return comparator.compare(leftLocale, rightLocale);
    }

    public static int localeOrder(String locale) {
        switch (locale) {
            case "de":
                return 0;
            case "en":
                return 1;
            case "ar":
                return 2;
            default:
                return 3;
        }
    }

}
