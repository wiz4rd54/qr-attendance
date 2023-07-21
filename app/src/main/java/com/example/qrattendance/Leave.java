package com.example.qrattendance;

public class Leave {
    String fromDate;
    String toDate;
    String leaveReason;

    public Leave(){}

    public Leave(String dateFrom, String dateTo, String reason){
        fromDate = dateFrom;
        toDate = dateTo;
        leaveReason = reason;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }
}
