package com.example.iit01;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class todolistController implements Initializable {

    @FXML
    private Button completedTasks_btn;

    @FXML
    private TableColumn<taskData, String> completedTasks_col_endDate;

    @FXML
    private TableColumn<taskData, String> completedTasks_col_startDate;

    @FXML
    private TableColumn<taskData, String> completedTasks_col_status;

    @FXML
    private TableColumn<taskData, String> completedTasks_col_task;

    @FXML
    private TableColumn<taskData, String> completedTasks_col_taskID;

    @FXML
    private AnchorPane completedTasks_form;

    @FXML
    private ComboBox<?> completedTasks_status;

    @FXML
    private TableView<taskData> completedTasks_tableView;

    @FXML
    private TextField completedTasks_taskID;

    @FXML
    private Button completedTasks_updateBtn;

    @FXML
    private Label home_activeTasks;

    @FXML
    private Button home_btn;

    @FXML
    private Label home_completedTasks;

    @FXML
    private Label home_dateRegistered;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_username;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button myTasks_addBtn;

    @FXML
    private Button myTasks_btn;

    @FXML
    private Button myTasks_clearBtn;

    @FXML
    private TableColumn<taskData, String> myTasks_col_dateCreated;

    @FXML
    private TableColumn<taskData, String> myTasks_col_endDate;

    @FXML
    private TableColumn<taskData, String> myTasks_col_startDate;

    @FXML
    private TableColumn<taskData, String> myTasks_col_status;

    @FXML
    private TableColumn<taskData, String> myTasks_col_tasks;

    @FXML
    private Button myTasks_deleteBtn;

    @FXML
    private DatePicker myTasks_endDate;

    @FXML
    private AnchorPane myTasks_form;

    @FXML
    private DatePicker myTasks_startDate;

    @FXML
    private TableView<taskData> myTasks_tableView;

    @FXML
    private TextArea myTasks_task;

    @FXML
    private Button myTasks_updateBtn;

    @FXML
    private Label page_label;

    @FXML
    private Label username;


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private Alert alert;

    public void homeDisplayUsername() {
        home_username.setText(username.getText());
    }
    public void homeDisplayDateRegistered() {

        String selectDate = "SELECT date FROM user WHERE username = '"
                    + data.username + "'";

        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(selectDate);
            result = prepare.executeQuery();

            if(result.next()) {
                home_dateRegistered.setText(result.getString("date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void homeDisplayNAT() {
        String sql = "SELECT COUNT(id) FROM tasks WHERE scheduler = '"
                + username.getText() + "'";

        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if(result.next()) {
                home_activeTasks.setText(result.getString("COUNT(id)"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeDisplayCT() {

        String sql = "SELECT COUNT(id) FROM tasks WHERE scheduler = '"
                + username.getText() + "' AND status = 'Completed'";

        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if(result.next()) {
                home_completedTasks.setText(result.getString("COUNT(id)"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void myTasksAddBtn() {

        connect = database.connectDB();

        try{

            if (myTasks_task.getText().isEmpty() || myTasks_startDate.getValue() == null
                    || myTasks_endDate.getValue() == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the fields");
                alert.showAndWait();
            }else{
                if (myTasks_endDate.getValue().isBefore(myTasks_startDate.getValue())){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Date");
                    alert.showAndWait();
                }else {

                    String checkTask = "SELECT task FROM tasks WHERE task = '"
                         + myTasks_task.getText() + "'";

                    prepare = connect.prepareStatement(checkTask);
                    result = prepare.executeQuery();

                    if (result.next()){
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Task " + myTasks_task.getText() + " is already recorded");
                        alert.showAndWait();
                    }else {
                        String insertData = "INSERT INTO tasks (task, startDate, endDate, dateCreated, status, scheduler)"
                                + "VALUES(?,?,?,?,?,?)";

                        prepare = connect.prepareStatement(insertData);
                        prepare.setString(1, myTasks_task.getText());
                        prepare.setString(2, String.valueOf(myTasks_startDate.getValue()));
                        prepare.setString(3, String.valueOf(myTasks_endDate.getValue()));

                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        prepare.setString(4, String.valueOf(sqlDate));
                        prepare.setString(5, "Not Completed");
                        prepare.setString(6, username.getText());
                        prepare.executeUpdate();

                        myTasksShowData();
                        myTasksClearBtn();
                    }

                }
            }

        }catch (Exception e) {e.printStackTrace();}

    }

    public void myTasksUpdateBtn(){

        connect = database.connectDB();

        try{

            if(taskID == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Task: " + myTasks_task.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    String checkData = "SELECT * FROM tasks WHERE id = " + taskID;

                    statement = connect.createStatement();
                    result = statement.executeQuery(checkData);
                    String currentD;

                    if (result.next()) {
                        currentD = result.getString("dateCreated");

                        String updateData = "UPDATE tasks SET task = '"
                                + myTasks_task.getText() + "', startDate = '"
                                + myTasks_startDate.getValue() + "', endDate = '"
                                + myTasks_endDate.getValue() + "', dateCreated = '"
                                + currentD + "' WHERE id = " + taskID;

                        prepare = connect.prepareStatement(updateData);
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated");
                        alert.showAndWait();

                        myTasksShowData();
                        myTasksClearBtn();

                    } else {

                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void myTasksDeleteBtn(){
        connect = database.connectDB();

        try {

            if(taskID == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Task: " + myTasks_task.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    String checkData = "SELECT * FROM tasks WHERE id = " + taskID;

                    statement = connect.createStatement();
                    result = statement.executeQuery(checkData);
                    String currentD;

                    if (result.next()) {
                        currentD = result.getString("dateCreated");

                        String deleteData = "DELETE FROM tasks WHERE id = " + taskID;

                        prepare = connect.prepareStatement(deleteData);
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Deleted");
                        alert.showAndWait();

                        myTasksShowData();
                        myTasksClearBtn();

                    } else {

                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {e. printStackTrace();}

    }

    public void myTasksClearBtn(){
        myTasks_task.setText("");
        myTasks_startDate.setValue(null);
        myTasks_endDate.setValue(null);
        taskID = 0;
    }


    public ObservableList<taskData> myTasksDataList() {

        ObservableList<taskData> listData = FXCollections.observableArrayList();

        String selectData = "SELECT * FROM tasks WHERE scheduler = '"
                + username.getText() + "'";
        connect = database.connectDB();

        try{

            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            taskData tData;
            while (result.next()) {
                tData = new taskData(result.getInt("id"), result.getString("task"),
                        result.getDate("startDate"), result.getDate("endDate"),
                        result.getDate("dateCreated"), result.getString("status"),
                        result.getString("scheduler"));

                listData.add(tData);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<taskData> myTasksListData;
    public void myTasksShowData() {
        myTasksListData = myTasksDataList();

        myTasks_col_tasks.setCellValueFactory(new PropertyValueFactory<>("task"));
        myTasks_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        myTasks_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        myTasks_col_dateCreated.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        myTasks_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        myTasks_tableView.setItems(myTasksListData);
    }

    private int taskID;
    public void myTasksSelectData() {

        taskData tData = myTasks_tableView.getSelectionModel().getSelectedItem();
        int num = myTasks_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1) return;

        taskID = tData.getTaskID();

        myTasks_task.setText(tData.getTask());
        myTasks_startDate.setValue(LocalDate.parse(String.valueOf(tData.getStartDate())));
        myTasks_endDate.setValue(LocalDate.parse(String.valueOf(tData.getEndDate())));

    }

    public void completedTasksUpdateBtn() {

        connect = database.connectDB();

        try {

            if (completedTasks_taskID.getText().isEmpty()
                    || completedTasks_status.getSelectionModel().getSelectedItem() == null ) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Task ID: " + completedTasks_taskID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){

                    String task = null;
                    String startDate = null;
                    String endDate = null;
                    String dateCreated = null;
                    String scheduler = null;

                    String selectData = "SELECT * FROM tasks WHERE id = "
                            + completedTasks_taskID.getText();

                    statement = connect.createStatement();
                    result = statement.executeQuery(selectData);

                    if (result.next()) {
                        task = result.getString("task");
                        startDate = result.getString("startDate");
                        endDate = result.getString("endDate");
                        dateCreated = result.getString("dateCreated");
                        scheduler = result.getString("scheduler");

                        String updateData = "UPDATE tasks SET task = '"
                                + task + "', startDate = '"
                                + startDate + "', endDate = '"
                                + endDate + "', dateCreated = '"
                                + dateCreated + "', status = '"
                                + completedTasks_status.getSelectionModel().getSelectedItem()
                                + "', scheduler = '" + scheduler + "' WHERE id = "
                                + completedTasks_taskID.getText();

                        prepare = connect.prepareStatement(updateData);
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated");
                        alert.showAndWait();

                        completedTasksShowData();

                    }

                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    private final String[] listStatus = {"Completed", "Not Completed"};
    public void completedTasksListStatus() {
        List<String> listS = new ArrayList<>();

        for (String data: listStatus) {
            listS.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listS);
        completedTasks_status.setItems(listData);
    }

    public ObservableList<taskData> completedTasksDataList(){

        ObservableList<taskData> listData = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM tasks WHERE scheduler = '"
                + username.getText() + "'";
        connect = database.connectDB();

        try {

            prepare = connect.prepareStatement(selectData);
            result = prepare.executeQuery();

            taskData tData;
            while (result.next()) {

                tData = new taskData(result.getInt("id"), result.getString("task"),
                        result.getDate("startDate"), result.getDate("endDate"),
                        result.getDate("dateCreated"), result.getString("status"),
                        result.getString("scheduler"));
                listData.add(tData);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<taskData> completedTasksListData;
    public void completedTasksShowData() {

        completedTasksListData = completedTasksDataList();

        completedTasks_col_taskID.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        completedTasks_col_task.setCellValueFactory(new PropertyValueFactory<>("task"));
        completedTasks_col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        completedTasks_col_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        completedTasks_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        completedTasks_tableView.setItems(completedTasksListData);

    }

    public void completedTasksSelectData() {
        taskData tData = completedTasks_tableView.getSelectionModel().getSelectedItem();
        int num = completedTasks_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < - 1) {
            return;
        }

        completedTasks_taskID.setText(String.valueOf(tData.getTaskID()));
    }

    public void displayUsername() {

        String user = data.username;
        username.setText(user.substring(0, 1).toUpperCase() + user.substring(1));

    }

    public void switchForm(ActionEvent event) {

        if(event.getSource() == home_btn) {
            home_form.setVisible(true);
            myTasks_form.setVisible(false);
            completedTasks_form.setVisible(false);

            page_label.setText("HOME");

            homeDisplayUsername();
            homeDisplayDateRegistered();
            homeDisplayNAT();
            homeDisplayCT();

        } else if (event.getSource() == myTasks_btn) {
            home_form.setVisible(false);
            myTasks_form.setVisible(true);
            completedTasks_form.setVisible(false);

            page_label.setText("MY TASKS");

            myTasksShowData();

        } else if (event.getSource() == completedTasks_btn) {
            home_form.setVisible(false);
            myTasks_form.setVisible(false);
            completedTasks_form.setVisible(true);

            page_label.setText("COMPLETED TASKS");

            completedTasksShowData();
        }

    }

    public void logout() {

        try {

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        homeDisplayUsername();
        homeDisplayDateRegistered();
        homeDisplayNAT();
        homeDisplayCT();
        myTasksShowData();
        completedTasksListStatus();
        completedTasksShowData();
    }

}
