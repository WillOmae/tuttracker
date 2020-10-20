package dev.wilburomae.tuttracker.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.views.Listeners.IAccountTypeDialogListener;

public class AccountTypeDialog extends DialogFragment {
    private Context mContext;
    private IAccountTypeDialogListener mAccountTypeDialogListener;
    private Dialog mDialog;
    private String mAccountType;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;
        try {
            mAccountTypeDialogListener = (IAccountTypeDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IAccountTypeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_account_selector, null);

        RadioButton choiceTutor = view.findViewById(R.id.dialog_account_selector_group_tutor);
        RadioButton choiceStudent = view.findViewById(R.id.dialog_account_selector_group_student);

        choiceTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountType = Constants.ACC_TUTOR;
            }
        });

        choiceStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountType = Constants.ACC_STUDENT;
            }
        });

        mDialog = builder.setTitle("Select account type")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mAccountType == null || mAccountType.length() == 0) {
                            Toast.makeText(mContext, "No account selected", Toast.LENGTH_SHORT).show();
                        } else {
                            mAccountTypeDialogListener.onAccountTypeSet(mAccountType);
                            mDialog.dismiss();
                        }
                    }
                })
                .create();
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }
}
