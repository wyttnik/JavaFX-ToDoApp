package com.example.todoapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private ListView<Entity> listView;

    @FXML
    private Button loadList;

    @FXML
    private Button saveList;

    @FXML
    private Text desc_taskName;

    @FXML
    private Text desc_taskType;

    @FXML
    private Text desc_deadline;

    @FXML
    private Button btn_editDate;

    @FXML
    private Text desc_reason;

    @FXML
    private Text desc_descText;

    private Entity currentSelectedEntity;

    private ObservableList<Entity> toDoList;

    public AppController() {
        toDoList = Data.getData();
    }

    @FXML
    protected void saveTaskList() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("save_warning.fxml"));
        Scene saveWarning = new Scene(fxmlLoader.load(), 330, 140); //330 210
        Stage saveWarningStage = new Stage();
        saveWarningStage.setTitle("Save tasks list?");
        saveWarningStage.getIcons().add(new Image(new File("images/tasks-app.png").toURI().toString()));
        saveWarningStage.initModality(Modality.APPLICATION_MODAL);
        saveWarningStage.setResizable(false);
        saveWarningStage.setScene(saveWarning);

        SaveWarningController controller = fxmlLoader.getController();
        controller.setDialogStage(saveWarningStage);

        saveWarningStage.showAndWait();

        if (controller.isOkClicked()){
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            try {
                mapper.writeValue(Paths.get("todolist.json").toFile(),toDoList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected void loadTasks(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(loadList.getScene().getWindow());
        if (selectedFile != null){
            String fileName = selectedFile.getName();
            if (!fileName.substring(fileName.lastIndexOf(".") + 1).equals("json")) return;
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

            try {
                ArrayList<Entity> toAddList= mapper.readValue(selectedFile, new TypeReference<>() {});
                toDoList.setAll(toAddList);
                listView.scrollTo(listView.getItems().size() - 1);
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    @FXML
    protected void editTask() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_date.fxml"));
        Scene newTask = new Scene(fxmlLoader.load(), 330, 230); //330 210
        Stage newTaskDialogStage = new Stage();
        newTaskDialogStage.setTitle("Edit date");
        newTaskDialogStage.getIcons().add(new Image(new File("images/tasks-app.png").toURI().toString()));
        newTaskDialogStage.initModality(Modality.APPLICATION_MODAL);
        newTaskDialogStage.setResizable(false);
        newTaskDialogStage.setScene(newTask);

        EditController controller = fxmlLoader.getController();
        controller.setEditTask(currentSelectedEntity);
        controller.setDialogStage(newTaskDialogStage);

        newTaskDialogStage.showAndWait();

        if (controller.isOkClicked()){
            toDoList.set(toDoList.indexOf(currentSelectedEntity),currentSelectedEntity);
            listView.getSelectionModel().clearSelection();
            listView.getSelectionModel().select(currentSelectedEntity); // clearAndSelect(toDoList.indexOf(currentSelectedEntity));
        }
    }

    @FXML
    protected void newTask() throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new_item_dialog.fxml"));
        Scene newTask = new Scene(fxmlLoader.load(), 330, 260); //330 210
        Stage newTaskDialogStage = new Stage();
        newTaskDialogStage.setTitle("New task");
        newTaskDialogStage.getIcons().add(new Image(new File("images/tasks-app.png").toURI().toString()));
        newTaskDialogStage.initModality(Modality.APPLICATION_MODAL);
        newTaskDialogStage.setResizable(false);
        newTaskDialogStage.setScene(newTask);

        NewItemDialogController controller = fxmlLoader.getController();
        controller.setDialogStage(newTaskDialogStage);

        newTaskDialogStage.showAndWait();

        if (controller.isOkClicked()){
            toDoList.add(controller.getNewTask());
            listView.scrollTo(listView.getItems().size() - 1);
        }
    }

    @FXML
    protected void deleteAll() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("delete_warning.fxml"));
        Scene deleteWarning = new Scene(fxmlLoader.load(), 330, 140); //330 210
        Stage deleteTasksDialogStage = new Stage();
        deleteTasksDialogStage.setTitle("Delete all?");
        deleteTasksDialogStage.getIcons().add(new Image(new File("images/tasks-app.png").toURI().toString()));
        deleteTasksDialogStage.initModality(Modality.APPLICATION_MODAL);
        deleteTasksDialogStage.setResizable(false);
        deleteTasksDialogStage.setScene(deleteWarning);

        DeleteWarningController controller = fxmlLoader.getController();
        controller.setDialogStage(deleteTasksDialogStage);

        deleteTasksDialogStage.showAndWait();

        if (controller.isOkClicked()) toDoList.clear(); // toDoList.removeAll(listView.getItems());}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listView.setItems(toDoList);
        listView.setCellFactory(todoListView -> new todoListViewCell());

        showTaskDetails(null);
        listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));
    }

    private void showTaskDetails(Entity entity){
        if (entity != null) {
            currentSelectedEntity = entity;
            desc_taskName.setText("Task: " + entity.getAction());
            desc_taskType.setText(
                    switch(entity.getTaskType()){
                        case(0) -> "One-time task";
                        case(1) -> "Daily task";
                        case(2) -> "Weekly task";
                        default -> "Monthly task";
                    }
            );
            desc_deadline.setText("Must be done " +
                switch(entity.getTaskType()){
                    case(0) -> "on " + entity.getDeadline().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    case(1) -> "every day";
                    case(2) -> "every week";
                    default -> "every month";
                }
            );

            btn_editDate.setVisible(true);
            if (entity.getChangeReason().length() > 0) desc_reason.setText("Reason for last date change: " + entity.getChangeReason());
            else desc_reason.setText("");

            desc_descText.setText(entity.getDescription());
        }
        else {
            desc_taskName.setText("");
            desc_taskType.setText("");
            desc_deadline.setText("");
            btn_editDate.setVisible(false);
            desc_reason.setText("");
            desc_descText.setText("");
        }
    }
}