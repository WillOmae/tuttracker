package dev.wilburomae.tuttracker;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.views.dialogs.UploadDialog;

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

    public static void showUploadDialog(FragmentManager fragmentManager, Assignment assignment, AssignmentStage stage) {
        Bundle args = new Bundle();
        args.putSerializable("assignment", assignment);
        args.putSerializable("stage", stage);

        UploadDialog dialogFragment = new UploadDialog();
        dialogFragment.setArguments(args);
        dialogFragment.show(fragmentManager, UploadDialog.class.getName());
    }
}
