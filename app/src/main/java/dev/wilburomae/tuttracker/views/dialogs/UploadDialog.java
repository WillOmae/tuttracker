package dev.wilburomae.tuttracker.views.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Method;
import java.util.Calendar;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.managers.AssignmentManager;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;

public class UploadDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Context mContext;
    private Dialog mDialog;
    private Uri mContentUri;
    private TextInputLayout mTutorEmail;
    private TextInputLayout mStudentEmail;
    private TextInputLayout mTitle;
    private ImageView mContentSet;
    private TextInputLayout mDescription;
    private TextInputLayout mGradeMax;
    private TextInputLayout mGradeActual;
    private TextInputLayout mDateAssigned;
    private TextInputLayout mDateDue;
    private TextInputLayout mDateSubmitted;
    private TextInputLayout mDateGraded;
    private Assignment mAssignment;
    private AssignmentStage mStage;
    private EditingDate mEditingDate;
    private static final String N_A = "N/A";

    private enum EditingDate {
        ASSIGN, DUE, SUBMIT, GRADE
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;

        Bundle args = getArguments();
        if (args != null && args.containsKey(Constants.BUNDLE_ASSIGNMENT) && args.containsKey(Constants.BUNDLE_STAGE)) {
            mAssignment = (Assignment) args.getSerializable(Constants.BUNDLE_ASSIGNMENT);
            mStage = (AssignmentStage) args.getSerializable(Constants.BUNDLE_STAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_upload, null);

        TextView dialogHeader = view.findViewById(R.id.dialog_upload_header);
        Button fetchContent = view.findViewById(R.id.dialog_upload_fetch_content);
        mContentSet = view.findViewById(R.id.dialog_upload_fetch_content_set);
        mTutorEmail = view.findViewById(R.id.dialog_upload_tutor);
        mStudentEmail = view.findViewById(R.id.dialog_upload_student);
        mTitle = view.findViewById(R.id.dialog_upload_title);
        mDescription = view.findViewById(R.id.dialog_upload_description);
        mGradeMax = view.findViewById(R.id.dialog_upload_grade_max);
        mGradeActual = view.findViewById(R.id.dialog_upload_grade_actual);
        mDateAssigned = view.findViewById(R.id.dialog_upload_date_assigned);
        mDateDue = view.findViewById(R.id.dialog_upload_date_due);
        mDateSubmitted = view.findViewById(R.id.dialog_upload_date_submitted);
        mDateGraded = view.findViewById(R.id.dialog_upload_date_graded);
        Button cancel = view.findViewById(R.id.dialog_upload_cancel);
        Button accept = view.findViewById(R.id.dialog_upload_accept);

        switch (mStage) {
            case TO_ASSIGN:
                dialogHeader.setText(R.string.dialog_upload_header_new);
                break;
            case TO_SUBMIT:
                dialogHeader.setText(R.string.dialog_upload_header_turn_in);
                break;
            case TO_GRADE:
                dialogHeader.setText(R.string.dialog_upload_header_grade);
                break;
        }

        fetchContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tutorEmail = mTutorEmail.getEditText().getText().toString();
                String studentEmail = mStudentEmail.getEditText().getText().toString();
                String title = mTitle.getEditText().getText().toString();
                String description = mDescription.getEditText().getText().toString();
                String gradeMax = mGradeMax.getEditText().getText().toString();
                String gradeActual = mGradeActual.getEditText().getText().toString();
                String dateDue = mDateDue.getEditText().getText().toString();

                if (verifyInputs()) {
                    if (!tutorEmail.equals(N_A)) mAssignment.setTutorEmail(tutorEmail);
                    if (!studentEmail.equals(N_A)) mAssignment.setStudentEmail(studentEmail);
                    if (!title.equals(N_A)) mAssignment.setTitle(title);
                    if (!description.equals(N_A)) mAssignment.setDescription(description);
                    if (!gradeMax.equals(N_A))
                        mAssignment.setGradeMax(Double.parseDouble(gradeMax));
                    if (!gradeActual.equals(N_A))
                        mAssignment.setGradeScored(Double.parseDouble(gradeActual));
                    if (!dateDue.equals(N_A)) mAssignment.setDateDue(dateDue);

                    upload();
                }
            }
        });

        setCalendarFocus(mDateAssigned, EditingDate.ASSIGN);
        setCalendarFocus(mDateDue, EditingDate.DUE);
        setCalendarFocus(mDateSubmitted, EditingDate.SUBMIT);
        setCalendarFocus(mDateGraded, EditingDate.GRADE);

        preventKeyboard(mDateAssigned.getEditText());
        preventKeyboard(mDateDue.getEditText());
        preventKeyboard(mDateSubmitted.getEditText());
        preventKeyboard(mDateGraded.getEditText());

        setViews();

        mDialog = builder.setView(view).create();
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }

    private void preventKeyboard(EditText view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus"
                        , boolean.class);
                method.setAccessible(true);
                method.invoke(view, false);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private void setCalendarFocus(final TextInputLayout textInputLayout, final EditingDate editingDate) {
        textInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(textInputLayout, editingDate);
            }
        });
    }

    private void showCalender(View view, EditingDate which) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        mEditingDate = which;

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(mContext,
                UploadDialog.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void setViews() {
        switch (mStage) {
            case TO_ASSIGN:
                modifyTextInput(mTutorEmail, mAssignment.getTutorEmail(), false);
                modifyTextInput(mGradeActual, N_A, false);
                modifyTextInput(mDateAssigned, mAssignment.getDateAssigned(), true);
                modifyTextInput(mDateSubmitted, N_A, false);
                modifyTextInput(mDateGraded, N_A, false);
                break;
            case TO_SUBMIT:
                modifyTextInput(mTutorEmail, mAssignment.getTutorEmail(), true);
                modifyTextInput(mStudentEmail, mAssignment.getStudentEmail(), false);
                modifyTextInput(mTitle, mAssignment.getTitle(), true);
                modifyTextInput(mDescription, mAssignment.getDescription(), true);
                modifyTextInput(mGradeActual, N_A, false);
                modifyTextInput(mGradeMax, mAssignment.getGradeMax(), true);
                modifyTextInput(mDateAssigned, mAssignment.getDateAssigned(), true);
                modifyTextInput(mDateDue, mAssignment.getDateDue(), true);
                modifyTextInput(mDateSubmitted, N_A, false);
                modifyTextInput(mDateGraded, N_A, false);
                break;
            case TO_GRADE:
                modifyTextInput(mTutorEmail, mAssignment.getTutorEmail(), false);
                modifyTextInput(mStudentEmail, mAssignment.getStudentEmail(), true);
                modifyTextInput(mTitle, mAssignment.getTitle(), true);
                modifyTextInput(mDescription, mAssignment.getDescription(), true);
                modifyTextInput(mGradeMax, mAssignment.getGradeMax(), true);
                modifyTextInput(mDateAssigned, mAssignment.getDateAssigned(), true);
                modifyTextInput(mDateDue, mAssignment.getDateDue(), true);
                modifyTextInput(mDateSubmitted, mAssignment.getDateSubmitted(), true);
                modifyTextInput(mDateGraded, N_A, false);
                break;
        }
    }

    private void modifyTextInput(TextInputLayout textInputLayout, Object text, boolean visible) {
        textInputLayout.getEditText().setText(String.valueOf(text));
        textInputLayout.getEditText().setEnabled(false);
        textInputLayout.getEditText().setFocusable(false);
        textInputLayout.getEditText().setFocusableInTouchMode(false);
        if (!visible) textInputLayout.setVisibility(View.GONE);
    }

    private boolean verifyInputs() {
        if (verifyInputFails(mTutorEmail, "Set the tutor's email")) return false;
        if (verifyInputFails(mStudentEmail, "Set the students's email")) return false;
        if (verifyInputFails(mTitle, "Set a title for this assignment")) return false;
        if (verifyInputFails(mDescription, "Briefly describe this assignment")) return false;
        if (verifyInputFails(mGradeMax, "Assign a maximum grade to this assignment")) return false;
        if (verifyInputFails(mGradeActual, "Set the actual grade")) return false;
        if (verifyInputFails(mDateAssigned, "Set the assigned date")) return false;
        if (verifyInputFails(mDateDue, "Set the due date")) return false;
        if (verifyInputFails(mDateSubmitted, "Set the date of submission")) return false;
        if (verifyInputFails(mDateGraded, "Set the date of grading")) return false;
        if (mContentUri == null) {
            Toast.makeText(getContext(), "Select a file", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean verifyInputFails(TextInputLayout textInputLayout, String message) {
        String input = textInputLayout.getEditText().getText().toString();
        if (input.isEmpty()) {
            textInputLayout.requestFocus();
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private void openFilePicker() {
        String[] mimeTypes = mContext.getResources().getStringArray(R.array.mime_types);
        Intent pickerIntent = new Intent(Intent.ACTION_GET_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pickerIntent.setType("*/*");
            pickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else {
            StringBuilder builder = new StringBuilder();
            for (String mimeType : mimeTypes) {
                builder.append(mimeType).append("|");
            }
            String mimeTypesStr = builder.toString();
            pickerIntent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        startActivityForResult(Intent.createChooser(pickerIntent, "Choose assignment file"), Constants.RC_CONTENT_PICKER);
    }

    private void upload() {
        Toast.makeText(getContext(), "Upload started...", Toast.LENGTH_SHORT).show();

        UploadTask task = AssignmentManager.upload(mAssignment, mContentUri, AssignmentStage.TO_ASSIGN);
        if (task != null) {
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload complete!", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RC_CONTENT_PICKER) {
            if (resultCode == -1 && data != null) {
                mContentUri = data.getData();
                mContentSet.setImageResource(R.drawable.ic_check);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String toShow = dayOfMonth + "-" + month + "-" + year;
        switch (mEditingDate) {
            case ASSIGN:
                mDateAssigned.getEditText().setText(toShow);
                break;
            case DUE:
                mDateDue.getEditText().setText(toShow);
                break;
            case SUBMIT:
                mDateSubmitted.getEditText().setText(toShow);
                break;
            case GRADE:
                mDateGraded.getEditText().setText(toShow);
                break;
        }
    }
}
