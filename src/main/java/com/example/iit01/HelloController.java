package com.example.iit01;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class HelloController {
    @FXML
    private Hyperlink login_createaccount;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Button login_loginbtn;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Hyperlink reg_loginredirect;

    @FXML
    private PasswordField reg_password;

    @FXML
    private Button reg_registerbtn;

    @FXML
    private TextField reg_username;

    @FXML
    private AnchorPane register_form;


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void loginAccount(){

        String selectData = "SELECT username, password FROM user WHERE username = '"
                + login_username.getText() + "' and password = '" + login_password.getText() + "'";

        connect = database.connectDB();

        try{

            if(login_username.getText().isEmpty() || login_password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the fields");
                alert.showAndWait();
            }else{
                prepare = connect.prepareStatement(selectData);
                result = prepare.executeQuery();

                if(result.next()){

                    data.username = login_username.getText();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful");
                    alert.showAndWait();

                    //TO HIDE THE LOGIN FORM
                    login_loginbtn.getScene().getWindow().hide();

                    //TO DISPLAY THE MAIN FORM
                    Parent root = FXMLLoader.load(getClass().getResource("todolist.fxml"));

                    Stage stage = new Stage();
                    stage.setTitle("To-Do List");

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }
            }


        }catch (Exception e) {e.printStackTrace();}
    }

    public void registerAccount(){

        String insertData = "INSERT INTO user (username, password, date) Values(?,?,?)";

        connect = database.connectDB();

        try{

            if(reg_username.getText().isEmpty() || reg_password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the fields");
                alert.showAndWait();
            }else{
                String checkUsername = "SELECT username FROM user WHERE username = '"
                        + reg_username.getText() + "'";

                prepare = connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();

                if (result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(reg_username.getText() + " was already taken");
                    alert.showAndWait();
                }else{

                    if(reg_password.getText().length() < 8) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Your password must have at least 8 characters");
                        alert.showAndWait();
                    }else {
                        prepare = connect.prepareStatement(insertData);
                        prepare.setString(1, reg_username.getText());
                        prepare.setString(2, reg_password.getText());

                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                        prepare.setString(3, String.valueOf(sqlDate));

                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Your account has been created successfully");
                        alert.showAndWait();

                        reg_username.setText("");
                        reg_password.setText("");

                        register_form.setVisible(false);
                        login_form.setVisible(true);
                    }
                }
            }


        }catch(Exception e) {e.printStackTrace();}

    }


    public void switchForm(ActionEvent event){
        if(event.getSource() == login_createaccount) {
            register_form.setVisible(true);
            login_form.setVisible(false);
        } else if (event.getSource() == reg_loginredirect) {
            register_form.setVisible(false);
            login_form.setVisible(true);
        }
    }
}