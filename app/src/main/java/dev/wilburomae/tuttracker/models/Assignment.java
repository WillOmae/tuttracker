package dev.wilburomae.tuttracker.models;

public class Assignment {
    private String mId;
    private String mTutorId;
    private String mStudentId;
    private String mFile1Id;
    private String mFile2Id;
    private double mGrade;
    private long mDateAssigned;
    private long mDateDue;
    private long mDateSubmitted;
    private long mDateGraded;

    public Assignment() {
    }

    public Assignment(String id, String tutorId, String studentId, String file1Id, String file2Id, double grade, long dateAssigned, long dateDue, long dateSubmitted, long dateGraded) {
        mId = id;
        mTutorId = tutorId;
        mStudentId = studentId;
        mFile1Id = file1Id;
        mFile2Id = file2Id;
        mGrade = grade;
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

    public String getTutorId() {
        return mTutorId;
    }

    public void setTutorId(String tutorId) {
        mTutorId = tutorId;
    }

    public String getStudentId() {
        return mStudentId;
    }

    public void setStudentId(String studentId) {
        mStudentId = studentId;
    }

    public String getFile1Id() {
        return mFile1Id;
    }

    public void setFile1Id(String file1Id) {
        mFile1Id = file1Id;
    }

    public String getFile2Id() {
        return mFile2Id;
    }

    public void setFile2Id(String file2Id) {
        mFile2Id = file2Id;
    }

    public double getGrade() {
        return mGrade;
    }

    public void setGrade(double grade) {
        mGrade = grade;
    }

    public long getDateAssigned() {
        return mDateAssigned;
    }

    public void setDateAssigned(long dateAssigned) {
        mDateAssigned = dateAssigned;
    }

    public long getDateDue() {
        return mDateDue;
    }

    public void setDateDue(long dateDue) {
        mDateDue = dateDue;
    }

    public long getDateSubmitted() {
        return mDateSubmitted;
    }

    public void setDateSubmitted(long dateSubmitted) {
        mDateSubmitted = dateSubmitted;
    }

    public long getDateGraded() {
        return mDateGraded;
    }

    public void setDateGraded(long dateGraded) {
        mDateGraded = dateGraded;
    }
}
