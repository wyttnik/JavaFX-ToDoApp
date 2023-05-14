package com.example.todoapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditController {
    @FXML
    private DatePicker edit_date;

    @FXML
    private TextArea edit_reason;


    @FXML
    private Button edit_ok;

    @FXML
    private Button edit_btnCancel;

    @FXML
    private Label labelText;

    @FXML
    private ComboBox<String> edit_taskType;

    private Stage dialogStage;
    private boolean okClicked = false;
    private Entity editTask;

    public void setEditTask(Entity editTask) { this.editTask = editTask; }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        LocalDate deadline;
        String reason = edit_reason.getText();
        int taskType = edit_taskType.getSelectionModel().getSelectedIndex();
        if (taskType == 0) deadline = edit_date.getValue();
        else deadline = LocalDate.now();

        if (isInputValid(deadline, reason, taskType)){
            editTask.setDeadline(deadline);
            editTask.setCheck(false);
            editTask.setStatus(0);
            editTask.setChangeReason(reason);
            editTask.setTaskType(taskType);
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void initialize() {
        edit_taskType.getItems().addAll("One-time task", "Daily task", "Weekly task", "Monthly task");
        labelText.setVisible(false);
        edit_date.setVisible(false);
    }

    @FXML
    private void handleBox() {
        String taskType = edit_taskType.getValue();
        if (taskType.equals("One-time task")){
            labelText.setVisible(true);
            edit_date.setVisible(true);
        }
        else{
            labelText.setVisible(false);
            edit_date.setVisible(false);
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid(LocalDate deadline, String reason, int taskType) {
        String errorMessage = "";
        if (reason == null || reason.length() == 0) {
            errorMessage += "Enter reason for changing date!\n";
        }

        if (taskType == editTask.getTaskType() && taskType != 0) {
            errorMessage += "Pick different task type!\n";
        }

        if (deadline == null) {
            errorMessage += "Enter date!\n";
        }

        if (deadline != null && taskType == 0 && deadline.isEqual(LocalDate.now())) {
            errorMessage += "Enter different date!\n";
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
