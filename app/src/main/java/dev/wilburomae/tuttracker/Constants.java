package dev.wilburomae.tuttracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constants {
    public static final short RC_FB_SIGNIN = 1234;
    public static final short RC_CONTENT_PICKER = 2345;

    public static boolean isDateSet(String date) {
        return date != null && !date.isEmpty();
    }

    public static String getFormattedDate() {
        return getFormattedDate(Calendar.getInstance().getTime());
    }

    public static String getFormattedDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(date);
    }
}
