package com.example.javafxlogin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;

public class SignupViewController {

    @FXML
    AnchorPane signUpAnchor;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField usernameField;
    @FXML
    Text signError;

    public void onAltSignUpButtonClick() throws IOException {

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO users (username, password) VALUES(?, ?)"
             )) {

            String username = usernameField.getText();
            String password = passwordField.getText();

            PreparedStatement checker = c.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?"
            );

            checker.setString(1, username);
            checker.setString(2, password);


            ResultSet list = checker.executeQuery();

            if (list.next()) {
                signError.setText("This account already exists");
            } else {
                statement.setString(1, username);
                statement.setString(2, password);
                int i = statement.executeUpdate();
                System.out.println("Sign up successful.");

                AnchorPane p = signUpAnchor;
                p.getScene().getStylesheets().clear();
                Parent scene = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                p.getChildren().clear();
                p.getChildren().add(scene);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onAltLoginButtonClick() throws IOException {
        AnchorPane p = signUpAnchor;
        p.getScene().getStylesheets().clear();
        Parent scene = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }
}
