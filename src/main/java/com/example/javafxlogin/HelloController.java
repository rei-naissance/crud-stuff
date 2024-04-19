package com.example.javafxlogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloController {
    public Text loginText, errorText;
    public TextField username;
    public PasswordField password;
    public Button btnLogin, btnLogout;
    public Label usernameLabel, passwordLabel;
    public AnchorPane anchorLogin, anchorLogout;
    public VBox vbLogin;
    public GridPane gridLogin;
    public ColorPicker cpPicker;
    static String userAdmin = "admin", pwAdmin = "adminpwpw", userNormal = "rei", pwNormal = "12345678", userRandom = "arce", pwRandom = "oeoeoeoe";
    @FXML
    protected void onLoginButtonClick() throws IOException {

        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM users";
            ResultSet set = statement.executeQuery(query);

            while(set.next()) {
                if(password.getText().equals(set.getString("password")) && username.getText().equals(set.getString("username"))) {
                    AnchorPane p = anchorLogin;
                    p.getScene().getStylesheets().clear();
                    p.getScene().getStylesheets().add(getClass().getResource("userOne.css").toExternalForm());
                    Parent scene = FXMLLoader.load(getClass().getResource("home-view.fxml"));
                    p.getChildren().clear();
                    p.getChildren().add(scene);
                }
            }

            errorText.setText("Invalid username or password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        if (password.getText().equals(pwAdmin) && username.getText().equals(userAdmin)) {
//            AnchorPane p = anchorLogin;
//            p.getScene().getStylesheets().clear();
//            p.getScene().getStylesheets().add(getClass().getResource("userAdmin.css").toExternalForm());
//            Parent scene = FXMLLoader.load(getClass().getResource("home-view.fxml"));
//            p.getChildren().clear();
//            p.getChildren().add(scene);
//        }
//        else if (password.getText().equals(pwNormal) && username.getText().equals(userNormal)) {
//            AnchorPane p = anchorLogin;
//            p.getScene().getStylesheets().clear();
//            p.getScene().getStylesheets().add(getClass().getResource("userOne.css").toExternalForm());
//            Parent scene = FXMLLoader.load(getClass().getResource("home-view.fxml"));
//            p.getChildren().clear();
//            p.getChildren().add(scene);
//        }
//        else if (password.getText().equals(pwRandom) && username.getText().equals(userRandom)) {
//            AnchorPane p = anchorLogin;
//            p.getScene().getStylesheets().clear();
//            p.getScene().getStylesheets().add(getClass().getResource("userRandom.css").toExternalForm());
//            Parent scene = FXMLLoader.load(getClass().getResource("home-view.fxml"));
//            p.getChildren().clear();
//            p.getChildren().add(scene);
//        }
//        errorText.setText("Invalid username or password");
    }

    public void onLogoutButtonClick() throws IOException {
        String color = cpPicker.getValue().toString();
        System.out.println(cpPicker.getValue());
        String temp = ".button{\n" + "-fx-background-color: #" + color + ";\n" + "\n}";
        String path = "";

//        if(username.getText().equals(userAdmin)){
//            path = "userAdmin.css";
//        }
//        if(username.getText().equals(userNormal)){
//            path = "userOne.css";
//        }
//        if(username.getText().equals(userRandom)){
//            path = "userRandom.css";
//        }
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(
//                    getClass().getResource(path).getPath(), true));
//            bw.append(temp);
//            bw.close();
//
//        } catch (IOException e) {
//        }
        AnchorPane p = anchorLogout;
        Parent scene = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        p.getChildren().clear();
        p.getStylesheets().clear();
        p.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        p.getScene().getStylesheets().clear();
        p.getChildren().add(scene);
    }
}