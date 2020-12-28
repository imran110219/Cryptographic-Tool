package com.sadman.controller;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainTest extends Application {

    private ObservableList<Person> getPeople() {
        ObservableList<Person> people = FXCollections
                .<Person> observableArrayList();

        Person docX = new Person("Programmer", "A", "B");
        docX.employeesProperty().add(new Person("XML", "J", "H"));
        docX.employeesProperty().add(new Person("CSS", "S", "S"));
        docX.employeesProperty().add(new Person("HTML", "O", "M"));

        Person magneto = new Person("Tester", "M", "E");

        Person biker = new Person("Manager", "J", "G");

        people.addAll(docX, magneto, biker);
        return people;
    }

    private ListView<Person> createLeaderListView(ObservableList<Person> leaders) {
        final ListView<Person> leaderListView = new ListView<>(leaders);
        leaderListView.setPrefWidth(150);
        leaderListView.setMaxWidth(Double.MAX_VALUE);
        leaderListView.setPrefHeight(150);

        leaderListView
                .setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
                    @Override
                    public ListCell<Person> call(ListView<Person> param) {
                        Tooltip tooltip = new Tooltip();

                        ListCell<Person> cell = new ListCell<Person>() {
                            @Override
                            public void updateItem(Person item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    // leadLbl.setText(item.getAliasName());
                                    setText(item.getFirstName() + " " + item.getLastName());
                                    tooltip.setText(item.getAliasName());
                                    setTooltip(tooltip);
                                }
                            }
                        };
                        return cell;
                    }
                });

        return leaderListView;
    }

    private TableView<Person> createEmployeeTableView(
            ObservableList<Person> teamMembers) {
        final TableView<Person> employeeTableView = new TableView<>();
        employeeTableView.setPrefWidth(300);

        employeeTableView.setItems(teamMembers);

        TableColumn<Person, String> aliasNameCol = new TableColumn<>("Alias");
        TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
        TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");

        aliasNameCol.setEditable(true);

        aliasNameCol.setPrefWidth(employeeTableView.getPrefWidth() / 3);
        firstNameCol.setPrefWidth(employeeTableView.getPrefWidth() / 3);
        lastNameCol.setPrefWidth(employeeTableView.getPrefWidth() / 3);

        aliasNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("aliasName"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("lastName"));

        employeeTableView.getColumns().setAll(aliasNameCol, firstNameCol,
                lastNameCol);

        return employeeTableView;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 500, 250, Color.WHITE);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        root.setCenter(gridpane);

        ObservableList<Person> leaders = getPeople();
        final ObservableList<Person> teamMembers = FXCollections
                .observableArrayList();

        ListView<Person> leaderListView = createLeaderListView(leaders);
        TableView<Person> employeeTableView = createEmployeeTableView(teamMembers);

        Label bossesLbl = new Label("Boss");
        GridPane.setHalignment(bossesLbl, HPos.CENTER);
        gridpane.add(bossesLbl, 0, 0);
        gridpane.add(leaderListView, 0, 1);

        Label emplLbl = new Label("Employees");
        GridPane.setHalignment(emplLbl, HPos.CENTER);
        gridpane.add(emplLbl, 2, 0);
        gridpane.add(employeeTableView, 2, 1);

        leaderListView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (ObservableValue<? extends Person> observable, Person oldValue,
                         Person newValue) -> {
                            if (observable != null && observable.getValue() != null) {
                                teamMembers.clear();
                                teamMembers.addAll(observable.getValue().employeesProperty());
                            }
                        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public static class Person {

        private StringProperty aliasName;
        private StringProperty firstName;
        private StringProperty lastName;

        private ObservableList<Person> employees = FXCollections
                .observableArrayList();

        public Person(String alias, String firstName, String lastName) {
            this.setAliasName(alias);
            this.setFirstName(firstName);
            this.setLastName(lastName);
        }

        public final void setAliasName(String value) {
            aliasNameProperty().set(value);
        }

        public final String getAliasName() {
            return aliasNameProperty().get();
        }

        public StringProperty aliasNameProperty() {
            if (aliasName == null) {
                aliasName = new SimpleStringProperty();
            }
            return aliasName;
        }

        public final void setFirstName(String value) {
            firstNameProperty().set(value);
        }

        public final String getFirstName() {
            return firstNameProperty().get();
        }

        public StringProperty firstNameProperty() {
            if (firstName == null) {
                firstName = new SimpleStringProperty();
            }
            return firstName;
        }

        public final void setLastName(String value) {
            lastNameProperty().set(value);
        }

        public final String getLastName() {
            return lastNameProperty().get();
        }

        public StringProperty lastNameProperty() {
            if (lastName == null) {
                lastName = new SimpleStringProperty();
            }
            return lastName;
        }

        public ObservableList<Person> employeesProperty() {
            return employees;
        }
    }
}