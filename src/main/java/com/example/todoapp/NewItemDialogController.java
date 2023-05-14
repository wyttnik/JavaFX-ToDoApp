package com.example.todoapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class NewItemDialogController {
    @FXML
    private TextField new_taskName;

    @FXML
    private TextArea new_desc;

    @FXML
    private DatePicker new_deadline;

    @FXML
    private ComboBox<String> new_taskType;

    @FXML
    private Button new_btnAdd;

    @FXML
    private Button new_btnCancel;

    @FXML
    private Label label_deadline;

    private Stage dialogStage;
    private boolean okClicked = false;
    private Entity newTask;


    @FXML
    private void initialize() {
        new_taskType.getItems().addAll("One-time task", "Daily task", "Weekly task", "Monthly task");
        label_deadline.setVisible(false);
        new_deadline.setVisible(false);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public Entity getNewTask(){
        return newTask;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleBox() {
        String taskType = new_taskType.getValue();
        if (taskType.equals("One-time task")){
            label_deadline.setVisible(true);
            new_deadline.setVisible(true);
        }
        else{
            label_deadline.setVisible(false);
            new_deadline.setVisible(false);
        }
    }

    @FXML
    private void handleOk() {
        String taskName = new_taskName.getText();
        String desc = new_desc.getText();
        int taskType = new_taskType.getSelectionModel().getSelectedIndex();
        LocalDate deadline;
        if (taskType == 0) deadline = new_deadline.getValue();
        else deadline = LocalDate.now();

        if (isInputValid(taskName, deadline, taskType)){
            newTask = new Entity(taskName, desc, false, 0, deadline, "",
                    new_taskType.getSelectionModel().getSelectedIndex());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid(String taskName, LocalDate deadline, int taskType) {
        String errorMessage = "";

        if (taskName == null || taskName.length() == 0) {
            errorMessage += "Not valid task name!\n";
        }
        if (deadline == null) {
            errorMessage += "Enter date!\n";
        }

        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            errorMessage += "Enter current date or future one!\n";
        }

        if (taskType == -1) {
            errorMessage += "Pick a task type!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
