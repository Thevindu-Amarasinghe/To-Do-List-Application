package com.example.iit01;

import java.util.Date;

public class taskData {

    private Integer taskID;
    private String task;
    private Date startDate;
    private Date endDate;
    private Date createdDate;
    private String status;
    private String scheduler;

    public taskData(Integer taskID, String task, Date startDate, Date endDate,
                    Date createdDate, String status, String scheduler) {
        this.taskID = taskID;
        this.task = task;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.status = status;
        this.scheduler = scheduler;
    }

    public Integer getTaskID() {
        return taskID;
    }
    public String getTask() {
        return task;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public String getStatus() {
        return status;
    }
    public String getScheduler() {
        return scheduler;
    }
}