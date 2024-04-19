package com.example.javafxlogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.javafxlogin.HelloController.activeUser;
import static javafx.scene.paint.Color.WHITE;

public class SettingsViewController {

    public CheckBox cbxDarkMode;
    public AnchorPane anchorSettings;
    public Button btnGoBack;

    public void onCheckDarkMode()   {
        AnchorPane p = anchorSettings;
        p.setStyle(cbxDarkMode.isSelected() ? "-fx-background-color: black" : "-fx-background-color: white");
        cbxDarkMode.setStyle(cbxDarkMode.isSelected() ? "-fx-text-fill: white" : "-fx-text-fill: black");
    }


    public void onButtonGoBackClick() throws IOException {
        AnchorPane p = anchorSettings;
        Parent scene = FXMLLoader.load(getClass().getResource("home-view.fxml"));
        setTextColorWhite(scene);
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    private void setTextColorWhite(Parent parent) {
        parent.lookupAll(".text").forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setTextFill(WHITE);
            }
        });
    }

    public void onButtonClickDeleteAccount() throws IOException {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement stmt = c.prepareStatement(
                     "DELETE FROM users WHERE id=?"
             )) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete your account?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                stmt.setString(1, String.valueOf(activeUser));
                int i = stmt.executeUpdate();
                AnchorPane p = anchorSettings;
                Parent scene = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                p.getChildren().clear();
                p.getChildren().add(scene);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
