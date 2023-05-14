package com.example.todoapp;

import java.time.LocalDate;

public class Entity {
    private String action;
    private String description;
    private Boolean checkStatus;
    private int status;
    private LocalDate deadline;
    private String changeReason;
    private int taskType;

    Entity() {

    }
    Entity(String action, String description, boolean checkStatus, int status, LocalDate deadline,
           String changeReason, int taskType){
        this.action = action;
        this.description = description;
        this.checkStatus = checkStatus;
        this.status = status;
        this.deadline = deadline;
        this.changeReason = changeReason;
        this.taskType = taskType;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) { this.action = action; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean getCheck(){ return checkStatus; }
    public void setCheck(boolean checkStatus) { this.checkStatus = checkStatus; }

    public int getStatus(){ return status; }
    public void setStatus(int status) { this.status = status; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getChangeReason(){ return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public int getTaskType(){ return taskType; }
    public void setTaskType(int taskType) { this.taskType = taskType; }
}
