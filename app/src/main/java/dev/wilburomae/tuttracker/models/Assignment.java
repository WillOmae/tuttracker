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
    private double mGradeScored;
    private double mGradeMax;
    private String mDateAssigned;
    private String mDateDue;
    private String mDateSubmitted;
    private String mDateGraded;
    private String mMimeAssigned;
    private String mMimeSubmitted;
    private String mMimeGraded;

    public Assignment() {
    }

    public Assignment(String id, String title, String description, String tutorId, String tutorName, String tutorEmail, String studentId, String studentName, String studentEmail, double gradeScored, double gradeMax, String dateAssigned, String dateDue, String dateSubmitted, String dateGraded, String mimeAssigned, String mimeSubmitted, String mimeGraded) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mTutorId = tutorId;
        mTutorName = tutorName;
        mTutorEmail = tutorEmail;
        mStudentId = studentId;
        mStudentName = studentName;
        mStudentEmail = studentEmail;
        mGradeScored = gradeScored;
        mGradeMax = gradeMax;
        mDateAssigned = dateAssigned;
        mDateDue = dateDue;
        mDateSubmitted = dateSubmitted;
        mDateGraded = dateGraded;
        mMimeAssigned = mimeAssigned;
        mMimeSubmitted = mimeSubmitted;
        mMimeGraded = mimeGraded;
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

    public String getMimeAssigned() {
        return mMimeAssigned;
    }

    public void setMimeAssigned(String mimeAssigned) {
        mMimeAssigned = mimeAssigned;
    }

    public String getMimeSubmitted() {
        return mMimeSubmitted;
    }

    public void setMimeSubmitted(String mimeSubmitted) {
        mMimeSubmitted = mimeSubmitted;
    }

    public String getMimeGraded() {
        return mMimeGraded;
    }

    public void setMimeGraded(String mimeGraded) {
        mMimeGraded = mimeGraded;
    }
}
