package com.example.javafxlogin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    public static int activeUser;
    public Text loginText, errorText;
    public TextField username;
    public PasswordField password;
    public Button btnLogin, btnLogout, btnSignUp;
    public Label usernameLabel, passwordLabel;
    public AnchorPane anchorLogin, anchorMenu;
    public VBox vbLogin;
    public GridPane gridLogin;
    public Button btnSettings;
    public Button btnAddTask;
    public TextField taskInput;

    ScrollPane scrollPane = new ScrollPane();

    VBox vbox = new VBox();

    Map<Integer, CheckBox> checkBoxMap = new HashMap<Integer, CheckBox>();


    @FXML
    protected void onLoginButtonClick() throws IOException {

        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            c.setAutoCommit(false);
            String query = "SELECT * FROM users";
            ResultSet set = statement.executeQuery(query);

            while(set.next()) {
                if(password.getText().equals(set.getString("password")) && username.getText().equals(set.getString("username"))) {
                    activeUser = set.getInt(1);
                    AnchorPane p = anchorLogin;
                    p.getScene().getStylesheets().clear();
                    Parent scene = FXMLLoader.load(getClass().getResource("home-view.fxml"));
                    p.getChildren().clear();
                    p.getChildren().add(scene);


                }
            }

            errorText.setText("Invalid username or password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onSignUpButtonClick() throws IOException {
        AnchorPane p = anchorLogin;
        p.getScene().getStylesheets().clear();
        Parent scene = FXMLLoader.load(getClass().getResource("signup-view.fxml"));
        p.getChildren().clear();
        p.getChildren().add(scene);
    }

    public void onLogoutButtonClick() throws IOException {
        activeUser = 0;
        AnchorPane p = anchorMenu;
        Parent scene = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        p.getChildren().clear();
        p.getStylesheets().clear();
        p.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        p.getScene().getStylesheets().clear();
        p.getChildren().add(scene);
    }

    public void onSettingsButtonClick() throws IOException {
        AnchorPane p = anchorMenu;
        Parent scene = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
        p.getChildren().clear();
        p.getStylesheets().clear();
        p.getChildren().add(scene);
    }

    public void onAddTaskButtonClick() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO tasks (taskdescription, taskstatus, user_id) VALUES(?, ?, ?)"
             )) {

            statement.setString(1, taskInput.getText());
            statement.setString(2, "0");
            statement.setString(3, String.valueOf(activeUser));

            int i = statement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Task inserted.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refresh(){

        for (Integer key : checkBoxMap.keySet()) {
            CheckBox temp = checkBoxMap.get(key);

            if(temp.isSelected()) {
                try (Connection c = MySQLConnection.getConnection();
                    PreparedStatement updateStatus = c.prepareStatement(
                            "UPDATE tasks SET taskstatus=? WHERE taskid=?"
                    )) {
                    updateStatus.setString(1, "1");
                    updateStatus.setString(2, key.toString());
                    int i = updateStatus.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        checkBoxMap.clear();
        vbox.getChildren().clear();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement display = c.prepareStatement(
                     "SELECT * FROM tasks WHERE user_id=? AND taskstatus=?"
             )) {
            AnchorPane p = anchorMenu;
            display.setString(1, String.valueOf(activeUser));
            display.setString(2, "0");
            ResultSet tasks = display.executeQuery();

            if (!p.getChildren().contains(scrollPane)) {
                p.getChildren().add(scrollPane);
            }

            scrollPane.setPrefSize(300, 500);

            while(tasks.next()) {
                CheckBox checkBox = new CheckBox(tasks.getString("taskdescription"));
                checkBoxMap.put(tasks.getInt(1), checkBox);
                vbox.getChildren().add(checkBox);
            }
            scrollPane.setContent(vbox);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}