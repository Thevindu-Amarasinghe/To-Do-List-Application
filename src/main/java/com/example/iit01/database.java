package com.example.iit01;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {

    public static Connection connectDB() {

        try{

            Class.forName("com.mysql.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/todolist", "root", "");
            return connect;
        }catch (Exception e) {e.printStackTrace();}
        return null;
    }

}
