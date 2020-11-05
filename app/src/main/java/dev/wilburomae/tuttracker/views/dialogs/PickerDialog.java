package dev.wilburomae.tuttracker.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.views.listeners.IPickerDialogSelection;

public class PickerDialog extends DialogFragment {
    private CharSequence[] mOptions;
    private Assignment mAssignment;
    private IPickerDialogSelection mPickerDialogSelection;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mPickerDialogSelection = (IPickerDialogSelection) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IPickerDialogSelection");
        }

        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BUNDLE_OPTIONS)) {
            mOptions = args.getStringArray(Constants.BUNDLE_OPTIONS);
        }
        if (args != null && args.containsKey(Constants.BUNDLE_ASSIGNMENT)) {
            mAssignment = (Assignment) args.getSerializable(Constants.BUNDLE_ASSIGNMENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(mOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mPickerDialogSelection.selected(mOptions[which].toString(), mAssignment);
            }
        });
        return builder.create();
    }
}
