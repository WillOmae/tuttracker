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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Method;
import java.util.Calendar;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.views.listeners.IUploadListener;

@SuppressWarnings("ConstantConditions")
public class UploadDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Context mContext;
    private IUploadListener mUploadListener;
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

        mUploadListener = (IUploadListener) getParentFragment();

        Bundle args = getArguments();
        if (args != null && args.containsKey("assignment") && args.containsKey("stage")) {
            mAssignment = (Assignment) args.getSerializable("assignment");
            mStage = (AssignmentStage) args.getSerializable("stage");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_upload, null);

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
                String dateAssigned = mDateAssigned.getEditText().getText().toString();
                String dateDue = mDateDue.getEditText().getText().toString();
                String dateSubmitted = mDateSubmitted.getEditText().getText().toString();
                String dateGraded = mDateGraded.getEditText().getText().toString();

                if (verifyInputs(tutorEmail, studentEmail, title, description, gradeMax, gradeActual, dateAssigned, dateDue, dateSubmitted, dateGraded)) {
                    if (!tutorEmail.equals(N_A)) mAssignment.setTutorEmail(tutorEmail);
                    if (!studentEmail.equals(N_A)) mAssignment.setStudentEmail(studentEmail);
                    if (!title.equals(N_A)) mAssignment.setTitle(title);
                    if (!description.equals(N_A)) mAssignment.setDescription(description);
                    if (!gradeMax.equals(N_A))
                        mAssignment.setGradeMax(Double.parseDouble(gradeMax));
                    if (!gradeActual.equals(N_A))
                        mAssignment.setGradeScored(Double.parseDouble(gradeActual));
                    if (!dateAssigned.equals(N_A)) mAssignment.setDateAssigned(dateAssigned);
                    if (!dateDue.equals(N_A)) mAssignment.setDateDue(dateDue);
                    if (!dateSubmitted.equals(N_A)) mAssignment.setDateSubmitted(dateSubmitted);
                    if (!dateGraded.equals(N_A)) mAssignment.setDateGraded(dateGraded);

                    mUploadListener.upload(mAssignment, mContentUri);
                }
            }
        });
        mDateAssigned.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateAssigned, EditingDate.ASSIGN);
            }
        });
        mDateDue.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateDue, EditingDate.DUE);
            }
        });
        mDateSubmitted.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateSubmitted, EditingDate.SUBMIT);
            }
        });
        mDateGraded.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateGraded, EditingDate.GRADE);
            }
        });

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
                mTutorEmail.getEditText().setText(mAssignment.getTutorEmail());
                mTutorEmail.setEnabled(false);
                mTutorEmail.setVisibility(View.GONE);
                mGradeActual.getEditText().setText(N_A);
                mGradeActual.setEnabled(false);
                mGradeActual.setVisibility(View.GONE);
                mDateSubmitted.getEditText().setText(N_A);
                mDateSubmitted.setEnabled(false);
                mDateSubmitted.setVisibility(View.GONE);
                mDateGraded.getEditText().setText(N_A);
                mDateGraded.setEnabled(false);
                mDateGraded.setVisibility(View.GONE);
                break;
            case TO_SUBMIT:
                mTutorEmail.getEditText().setText(mAssignment.getTutorEmail());
                mTutorEmail.setEnabled(false);
                mStudentEmail.getEditText().setText(mAssignment.getStudentEmail());
                mStudentEmail.setEnabled(false);
                mStudentEmail.setVisibility(View.GONE);
                mTitle.getEditText().setText(mAssignment.getTitle());
                mTitle.setEnabled(false);
                mDescription.getEditText().setText(mAssignment.getDescription());
                mDescription.setEnabled(false);
                mGradeActual.getEditText().setText(N_A);
                mGradeActual.setEnabled(false);
                mGradeActual.setVisibility(View.GONE);
                mGradeMax.getEditText().setText(String.valueOf(mAssignment.getGradeMax()));
                mGradeMax.setEnabled(false);
                mDateAssigned.getEditText().setText(mAssignment.getDateAssigned());
                mDateAssigned.setEnabled(false);
                mDateDue.getEditText().setText(mAssignment.getDateDue());
                mDateDue.setEnabled(false);
                mDateSubmitted.getEditText().setText(N_A);
                mDateSubmitted.setEnabled(false);
                mDateSubmitted.setVisibility(View.GONE);
                mDateGraded.getEditText().setText(N_A);
                mDateGraded.setEnabled(false);
                mDateGraded.setVisibility(View.GONE);
                break;
            case TO_GRADE:
                mTutorEmail.getEditText().setText(mAssignment.getTutorEmail());
                mTutorEmail.setEnabled(false);
                mTutorEmail.setVisibility(View.GONE);
                mStudentEmail.getEditText().setText(mAssignment.getStudentEmail());
                mStudentEmail.setEnabled(false);
                mTitle.getEditText().setText(mAssignment.getTitle());
                mTitle.setEnabled(false);
                mDescription.getEditText().setText(mAssignment.getDescription());
                mDescription.setEnabled(false);
                mGradeMax.getEditText().setText(String.valueOf(mAssignment.getGradeMax()));
                mGradeMax.setEnabled(false);
                mDateAssigned.getEditText().setText(mAssignment.getDateAssigned());
                mDateAssigned.setEnabled(false);
                mDateDue.getEditText().setText(mAssignment.getDateDue());
                mDateDue.setEnabled(false);
                mDateSubmitted.getEditText().setText(mAssignment.getDateSubmitted());
                mDateSubmitted.setEnabled(false);
                break;
        }
    }

    private boolean verifyInputs(String tutorEmail, String studentEmail, String title, String description, String gradeMax, String gradeActual, String dateAssigned, String dateDue, String dateSubmitted, String dateGraded) {
        if (mContentUri == null) {
            Toast.makeText(getContext(), "Select a file", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tutorEmail.isEmpty()) {
            mTutorEmail.requestFocus();
            Toast.makeText(getContext(), "Set the tutor's email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (studentEmail.isEmpty()) {
            mStudentEmail.requestFocus();
            Toast.makeText(getContext(), "Set the student's email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (title.isEmpty()) {
            mTitle.requestFocus();
            Toast.makeText(getContext(), "Set a title for this assignment", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.isEmpty()) {
            mDescription.requestFocus();
            Toast.makeText(getContext(), "Briefly describe this assignment", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (gradeMax.isEmpty()) {
            mGradeMax.requestFocus();
            Toast.makeText(getContext(), "Assign a maximum grade to this assignment", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (gradeActual.isEmpty()) {
            mGradeActual.requestFocus();
            Toast.makeText(getContext(), "Set the actual grade", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dateAssigned.isEmpty()) {
            mDateAssigned.requestFocus();
            Toast.makeText(getContext(), "Set the assigned date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dateDue.isEmpty()) {
            mDateDue.requestFocus();
            Toast.makeText(getContext(), "Set the due date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dateSubmitted.isEmpty()) {
            mDateSubmitted.requestFocus();
            Toast.makeText(getContext(), "Set the date of submission", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dateGraded.isEmpty()) {
            mDateGraded.requestFocus();
            Toast.makeText(getContext(), "Set the date of grading", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
