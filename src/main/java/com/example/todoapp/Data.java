package com.example.todoapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Data {
    private static ObservableList<Entity> actions;

    public static void fillData() {
        File f = new File("todolist.json");
        if(f.exists()) {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

            try {
                actions = FXCollections.observableList(mapper.readValue(
                        f,
                        new TypeReference<ArrayList<Entity>>() {}));
            } catch (IOException e) { throw new RuntimeException(e); }
        }
        else actions = FXCollections.observableArrayList();

        if (actions.size() == 0){
            actions.addAll(
                    new Entity( "task0", "desc0", false, 0, LocalDate.of(2023, 5, 1),"",0),
                    new Entity( "task1", "desc1", false, 0, LocalDate.of(2023, 5, 2),"",0),
                    new Entity( "task2", "desc2", false, 0, LocalDate.of(2023, 5, 3),"",0),
                    new Entity( "task3", "desc3", false, 0, LocalDate.of(2023, 5, 4),"",0),
                    new Entity( "task4", "desc4", false, 0, LocalDate.of(2023, 5, 5),"",0),
                    new Entity( "task5", "desc5", false, 0, LocalDate.of(2023, 5, 6),"",0),
                    new Entity( "task6", "desc6", false, 0, LocalDate.of(2023, 5, 7),"",0),
                    new Entity( "task7", "desc7", false, 0, LocalDate.of(2023, 5, 8),"",0),
                    new Entity( "task8", "desc8", false, 0, LocalDate.of(2023, 5, 9),"",0),
                    new Entity( "task9", "desc9", false, 0, LocalDate.of(2023, 5, 10),"",0)
            );
        }
    }

    public static ObservableList<Entity> getData(){
        if (actions == null) fillData();
        return actions;
    }
}
