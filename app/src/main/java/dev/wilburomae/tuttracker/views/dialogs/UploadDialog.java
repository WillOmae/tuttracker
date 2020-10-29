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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import dev.wilburomae.tuttracker.Constants;
import dev.wilburomae.tuttracker.R;
import dev.wilburomae.tuttracker.models.Assignment;
import dev.wilburomae.tuttracker.models.AssignmentStage;
import dev.wilburomae.tuttracker.views.listeners.IUploadListener;

public class UploadDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private Context mContext;
    private IUploadListener mUploadListener;
    private Dialog mDialog;
    private Uri mContentUri;
    private TextView mTutorEmail;
    private TextView mStudentEmail;
    private TextView mTitle;
    private ImageView mContentSet;
    private TextView mDescription;
    private TextView mGradeMax;
    private TextView mGradeActual;
    private TextView mDateAssigned;
    private TextView mDateDue;
    private TextView mDateSubmitted;
    private TextView mDateGraded;
    private Assignment mAssignment;
    private AssignmentStage mStage;
    private EditingDate mEditingDate;

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
                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();
                String gradeMax = mGradeMax.getText().toString();
                if (verifyInputs(title, description, gradeMax)) {
                    mUploadListener.upload(mAssignment, mContentUri);
                }
            }
        });
        mDateAssigned.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateAssigned, EditingDate.ASSIGN);
            }
        });
        mDateDue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateDue, EditingDate.DUE);
            }
        });
        mDateSubmitted.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateSubmitted, EditingDate.SUBMIT);
            }
        });
        mDateGraded.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCalender(mDateGraded, EditingDate.GRADE);
            }
        });

        setViews();

        mDialog = builder.setView(view).create();
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
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
                mTutorEmail.setText(mAssignment.getTutorEmail());
                mTutorEmail.setEnabled(false);
                mTutorEmail.setVisibility(View.GONE);
                mGradeActual.setText("N/A");
                mGradeActual.setEnabled(false);
                mGradeActual.setVisibility(View.GONE);
                mDateSubmitted.setText("N/A");
                mDateSubmitted.setEnabled(false);
                mDateSubmitted.setVisibility(View.GONE);
                mDateGraded.setText("N/A");
                mDateGraded.setEnabled(false);
                mDateGraded.setVisibility(View.GONE);
                break;
            case TO_SUBMIT:
                mTutorEmail.setText(mAssignment.getTutorEmail());
                mTutorEmail.setEnabled(false);
                mStudentEmail.setText(mAssignment.getStudentEmail());
                mStudentEmail.setEnabled(false);
                mStudentEmail.setVisibility(View.GONE);
                mTitle.setText(mAssignment.getTitle());
                mTitle.setEnabled(false);
                mDescription.setText(mAssignment.getDescription());
                mDescription.setEnabled(false);
                mGradeActual.setText("N/A");
                mGradeActual.setEnabled(false);
                mGradeActual.setVisibility(View.GONE);
                mGradeMax.setText(String.valueOf(mAssignment.getGradeMax()));
                mGradeMax.setEnabled(false);
                mDateAssigned.setText(mAssignment.getDateAssigned());
                mDateAssigned.setEnabled(false);
                mDateDue.setText(mAssignment.getDateDue());
                mDateDue.setEnabled(false);
                mDateSubmitted.setText("N/A");
                mDateSubmitted.setEnabled(false);
                mDateSubmitted.setVisibility(View.GONE);
                mDateGraded.setText("N/A");
                mDateGraded.setEnabled(false);
                mDateGraded.setVisibility(View.GONE);
                break;
            case TO_GRADE:
                mTutorEmail.setText(mAssignment.getTutorEmail());
                mTutorEmail.setEnabled(false);
                mTutorEmail.setVisibility(View.GONE);
                mStudentEmail.setText(mAssignment.getStudentEmail());
                mStudentEmail.setEnabled(false);
                mTitle.setText(mAssignment.getTitle());
                mTitle.setEnabled(false);
                mDescription.setText(mAssignment.getDescription());
                mDescription.setEnabled(false);
                mGradeMax.setText(String.valueOf(mAssignment.getGradeMax()));
                mGradeMax.setEnabled(false);
                mDateAssigned.setText(mAssignment.getDateAssigned());
                mDateAssigned.setEnabled(false);
                mDateDue.setText(mAssignment.getDateDue());
                mDateDue.setEnabled(false);
                mDateSubmitted.setText(mAssignment.getDateSubmitted());
                mDateSubmitted.setEnabled(false);
                break;
        }
    }

    private boolean verifyInputs(String title, String description, String gradeMax) {
        if (mContentUri == null) {
            Toast.makeText(getContext(), "Select a file", Toast.LENGTH_SHORT).show();
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
                mDateAssigned.setText(toShow);
                break;
            case DUE:
                mDateDue.setText(toShow);
                break;
            case SUBMIT:
                mDateSubmitted.setText(toShow);
                break;
            case GRADE:
                mDateGraded.setText(toShow);
                break;
        }
    }
}
