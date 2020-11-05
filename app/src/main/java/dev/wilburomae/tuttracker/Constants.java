package dev.wilburomae.tuttracker;

public class Constants {
    public static final short RC_FB_SIGNIN = 1234;
    public static final short RC_CONTENT_PICKER = 2345;

    public static boolean isDateSet(String date) {
        return date != null && !date.isEmpty();
    }
}
