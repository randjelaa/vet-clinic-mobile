package org.unibl.etf.vetclinic.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public static String formatDate(Date date) {
        return date != null ? sdf.format(date) : null;
    }
}
