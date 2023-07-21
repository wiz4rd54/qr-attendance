package com.example.qrattendance;

public class Student {
    private String enrollment;
    private String name;
    private String email;
    private String course;
    private String year;
    private String semester;

    public Student(String stu_enrollment,String stu_name, String stu_email, String stu_course, String stu_year, String stu_semester){
        this.enrollment = stu_enrollment;
        this.name = stu_name;
        this.email = stu_email;
        this.course = stu_course;
        this.year = stu_year;
        this.semester = stu_semester;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
