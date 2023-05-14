package com.example.todoapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class todoListViewCell extends ListCell<Entity> {
    @FXML
    private CheckBox checkBox;

    @FXML
    private Text actionName;

    @FXML
    private Text status;

    @FXML
    private Text taskType;

    @FXML
    private Text deadline;

    @FXML
    private ImageView deleteIcon;

    @FXML
    private AnchorPane itemPane;

    private FXMLLoader loader;

    protected void onCheck(Entity entity){
        entity.setCheck(!entity.getCheck());

        if (entity.getCheck()){
            switch(entity.getTaskType()){
                case(0) -> {
                    LocalDate today = LocalDate.now();
                    LocalDate deadline = entity.getDeadline();
                    if (today.isBefore(deadline)) entity.setStatus(1); // done before time
                    else if (today.isEqual(deadline)) entity.setStatus(2); // done in time
                    else entity.setStatus(3); // done after time
                }
                case(1) -> entity.setStatus(4); // done for this day
                case(2) -> entity.setStatus(5); // done for this week
                case(3) -> entity.setStatus(6); // done for this month
            }
        }
        else entity.setStatus(0); // "" - in process
        getListView().getItems().set(getListView().getItems().indexOf(entity),entity);
        //getListView().refresh();
    }

    protected void onDelete(Entity entity){
        getListView().getItems().remove(entity);
    }

    @Override
    protected void updateItem(Entity entity, boolean empty) {
        super.updateItem(entity, empty);

        if (empty || entity == null) {
            setGraphic(null);
        }
        else{
            if (loader == null){
                loader = new FXMLLoader(getClass().getResource("item.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            checkTimes(entity);
            checkBox.setSelected(entity.getCheck());

            checkBox.setOnAction(actionEvent -> onCheck(entity));
            actionName.setText(entity.getAction());

            switch (entity.getStatus()) {
                case (0) -> status.setText("");
                case (1) -> {
                    status.setText("Done before time");
                    status.setFill(Color.GREEN);
                }
                case (2) -> {
                    status.setText("Done in time");
                    status.setFill(Color.GREEN);
                }
                case (3) -> {
                    status.setText("Done after time");
                    status.setFill(Color.RED);
                }
                case (4) -> {
                    status.setText("Done for this day");
                    status.setFill(Color.GREEN);
                }
                case (5) -> {
                    status.setText("Done for this week");
                    status.setFill(Color.GREEN);
                }
                case (6) -> {
                    status.setText("Done for this month");
                    status.setFill(Color.GREEN);
                }
                case (7) -> {
                    status.setText("Wasn't done yesterday");
                    status.setFill(Color.RED);
                }
                case (8) -> {
                    status.setText("Wasn't done last week");
                    status.setFill(Color.RED);
                }
                case (9) -> {
                    status.setText("Wasn't done last month");
                    status.setFill(Color.RED);
                }
            }

            switch (entity.getTaskType()) {
                case (0) -> {
                    taskType.setText("One-time task");
                    deadline.setText(entity.getDeadline().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
                case (1) -> {
                    taskType.setText("");
                    deadline.setText("Every day");
                }
                case (2) -> {
                    taskType.setText("");
                    deadline.setText("Every week");
                }
                case (3) -> {
                    taskType.setText("");
                    deadline.setText("Every month");
                }
            }

            deleteIcon.setImage(new Image(new File("images/rubbish-bin-svgrepo-com.png").toURI().toString()));
            deleteIcon.setOnMouseClicked(e->onDelete(entity));
            setGraphic(itemPane);
        }
    }

    private void checkTimes(Entity entity) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = entity.getDeadline();

        int taskType = entity.getTaskType();
        switch(taskType) {
            case(1) -> {
                if (today.isEqual(deadline.plusDays(1))) {
                    entity.setDeadline(today);
                    if (entity.getCheck()) entity.setCheck(!entity.getCheck());
                    else {
                        entity.setStatus(7);
                    }
                }
            }
            case(2) -> {
                if (today.isEqual(deadline.plusWeeks(1))) {
                    entity.setDeadline(today);
                    if (entity.getCheck()) entity.setCheck(!entity.getCheck());
                    else {
                        entity.setStatus(8);
                    }
                }
            }
            case(3) -> {
                if (today.isEqual(deadline.plusMonths(1))) {
                    entity.setDeadline(today);
                    if (entity.getCheck()) entity.setCheck(!entity.getCheck());
                    else {
                        entity.setStatus(9);
                    }
                }
            }
        }
    }
}
