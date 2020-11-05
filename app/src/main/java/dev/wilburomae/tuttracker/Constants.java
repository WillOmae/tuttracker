package dev.wilburomae.tuttracker;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.views.dialogs.PickerDialog;
import dev.wilburomae.tuttracker.views.dialogs.UploadDialog;

public class Constants {
    public static final short RC_FB_SIGNIN = 1234;
    public static final short RC_CONTENT_PICKER = 2345;
    public static final short RC_PERMISSIONS = 3456;

    public static final String OPT_OPEN = "Open";
    public static final String OPT_GRADE = "Grade";
    public static final String OPT_TURNIN = "Turn in";
    public static final String OPT_ARCHIVE = "Archive";

    public static final String BUNDLE_ASSIGNMENT = "assignment";
    public static final String BUNDLE_STAGE = "stage";
    public static final String BUNDLE_OPTIONS = "options";

    public static String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    public static boolean isDateSet(String date) {
        return date != null && !date.isEmpty();
    }

    public static String getFormattedDate() {
        return getFormattedDate(Calendar.getInstance().getTime());
    }

    public static String getFormattedDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(date);
    }

    public static void showUploadDialog(FragmentManager fragmentManager, Assignment assignment, AssignmentStage stage) {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_ASSIGNMENT, assignment);
        args.putSerializable(BUNDLE_STAGE, stage);

        UploadDialog dialogFragment = new UploadDialog();
        dialogFragment.setArguments(args);
        dialogFragment.show(fragmentManager, UploadDialog.class.getName());
    }

    public static void showPickerDialog(FragmentManager fragmentManager, String[] options, Assignment assignment) {
        Bundle args = new Bundle();
        args.putStringArray(BUNDLE_OPTIONS, options);
        args.putSerializable(BUNDLE_ASSIGNMENT, assignment);

        PickerDialog dialogFragment = new PickerDialog();
        dialogFragment.setArguments(args);
        dialogFragment.show(fragmentManager, PickerDialog.class.getName());
    }
}
