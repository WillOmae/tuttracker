package dev.wilburomae.tuttracker.models;

import java.io.Serializable;

public class Assignment implements Serializable {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mTutorId;
    private String mTutorName;
    private String mTutorEmail;
    private String mStudentId;
    private String mStudentName;
    private String mStudentEmail;
    private String mFileAssignedId;
    private String mFileSubmittedId;
    private String mFileGradedId;
    private double mGradeScored;
    private double mGradeMax;
    private String mDateAssigned;
    private String mDateDue;
    private String mDateSubmitted;
    private String mDateGraded;

    public Assignment() {
    }

    public Assignment(String id, String title, String description, String tutorId, String tutorName, String tutorEmail, String studentId, String studentName, String studentEmail, String fileAssignedId, String fileSubmittedId, String fileGradedId, double gradeScored, double gradeMax, String dateAssigned, String dateDue, String dateSubmitted, String dateGraded) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mTutorId = tutorId;
        mTutorName = tutorName;
        mTutorEmail = tutorEmail;
        mStudentId = studentId;
        mStudentName = studentName;
        mStudentEmail = studentEmail;
        mFileAssignedId = fileAssignedId;
        mFileSubmittedId = fileSubmittedId;
        mFileGradedId = fileGradedId;
        mGradeScored = gradeScored;
        mGradeMax = gradeMax;
        mDateAssigned = dateAssigned;
        mDateDue = dateDue;
        mDateSubmitted = dateSubmitted;
        mDateGraded = dateGraded;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTutorId() {
        return mTutorId;
    }

    public void setTutorId(String tutorId) {
        mTutorId = tutorId;
    }

    public String getTutorName() {
        return mTutorName;
    }

    public void setTutorName(String tutorName) {
        mTutorName = tutorName;
    }

    public String getTutorEmail() {
        return mTutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        mTutorEmail = tutorEmail;
    }

    public String getStudentId() {
        return mStudentId;
    }

    public void setStudentId(String studentId) {
        mStudentId = studentId;
    }

    public String getStudentName() {
        return mStudentName;
    }

    public void setStudentName(String studentName) {
        mStudentName = studentName;
    }

    public String getStudentEmail() {
        return mStudentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        mStudentEmail = studentEmail;
    }

    public String getFileAssignedId() {
        return mFileAssignedId;
    }

    public void setFileAssignedId(String fileAssignedId) {
        mFileAssignedId = fileAssignedId;
    }

    public String getFileSubmittedId() {
        return mFileSubmittedId;
    }

    public void setFileSubmittedId(String fileSubmittedId) {
        mFileSubmittedId = fileSubmittedId;
    }

    public String getFileGradedId() {
        return mFileGradedId;
    }

    public void setFileGradedId(String fileGradedId) {
        mFileGradedId = fileGradedId;
    }

    public double getGradeScored() {
        return mGradeScored;
    }

    public void setGradeScored(double gradeScored) {
        mGradeScored = gradeScored;
    }

    public double getGradeMax() {
        return mGradeMax;
    }

    public void setGradeMax(double gradeMax) {
        mGradeMax = gradeMax;
    }

    public String getDateAssigned() {
        return mDateAssigned;
    }

    public void setDateAssigned(String dateAssigned) {
        mDateAssigned = dateAssigned;
    }

    public String getDateDue() {
        return mDateDue;
    }

    public void setDateDue(String dateDue) {
        mDateDue = dateDue;
    }

    public String getDateSubmitted() {
        return mDateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        mDateSubmitted = dateSubmitted;
    }

    public String getDateGraded() {
        return mDateGraded;
    }

    public void setDateGraded(String dateGraded) {
        mDateGraded = dateGraded;
    }
}
