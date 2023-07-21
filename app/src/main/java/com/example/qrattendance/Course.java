package com.example.qrattendance;

public class Course {
    private String name;
    private String code;
    private int present;
    private int conducted;
    private String lastDate;
    private String lastTime;

    public Course(){}
    public Course(String name,String code, int present,int conducted,String date){
        this.name=name;
        this.code = code;
        this.present=present;
        this.conducted=conducted;
        this.lastDate = date;
        this.lastTime = "0";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getConducted() {
        return conducted;
    }

    public void setConducted(int conducted) {
        this.conducted = conducted;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
