package com.example.javafxlogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;

import static com.example.javafxlogin.HelloController.activeUser;

public class AccountController{

    public PasswordField oldPW;
    public PasswordField newPW;
    public AnchorPane anchorPaneChangePW;
    public Button btnChangePW;
    public Button btnCancelPW;
    public AnchorPane anchorPaneChangeUser;
    public Button btnChangeUser;
    public Button btnCancelUser;
    public TextField newUser;
    public PasswordField userChangePass;

    public void onButtonPWChangeClick() throws IOException {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement stmt = c.prepareStatement(
                     "UPDATE users SET password=? WHERE id=?"
             )) {
            Alert alert;
            if(oldPW.getText().equals(newPW.getText())) {
                stmt.setString(1, newPW.getText());
                stmt.setString(2, String.valueOf(activeUser));
                stmt.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Password changed successfully");
                alert.showAndWait();
                AnchorPane p = anchorPaneChangePW;
                p.getScene().getStylesheets().clear();
                Parent scene = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
                p.getChildren().clear();
                p.getChildren().add(scene);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Passwords do not match");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CancelPW() throws IOException {
        AnchorPane p = anchorPaneChangePW;
        p.getScene().getStylesheets().clear();
        Parent scene = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    public void onButtonChangeUser(ActionEvent actionEvent) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement stmt = c.prepareStatement(
                     "UPDATE users SET username=? WHERE id=?"
             )) {

            Statement getPass = c.createStatement();
            String pass = "SELECT password FROM users WHERE id = " + activeUser;
            ResultSet res = getPass.executeQuery(pass);

            Alert alert;
            if(res.getString("password").equals(newPW.getText())) {
                stmt.setString(1, newUser.getText());
                stmt.setString(2, String.valueOf(activeUser));
                stmt.executeUpdate();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Username changed successfully");
                alert.showAndWait();
                AnchorPane p = anchorPaneChangeUser;
                p.getScene().getStylesheets().clear();
                Parent scene = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
                p.getChildren().clear();
                p.getChildren().add(scene);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Password does not match");
                alert.showAndWait();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void onButtonCancelUser(ActionEvent actionEvent) throws IOException {
        AnchorPane p = anchorPaneChangeUser;
        p.getScene().getStylesheets().clear();
        Parent scene = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }
}
