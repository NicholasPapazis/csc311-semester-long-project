package viewmodel;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginController {

    @FXML
    Text alertTextUsername;
    @FXML
    Text alertTextPassword;

    @FXML
    Button loginButton;

    @FXML
    TextField UsernameField;
    @FXML
    TextField PasswordField;

    @FXML
    private Pane rootpane;
    @FXML
    private Pane leftSection;
    public void initialize() {

        loginButton.setDisable(true); //disable loginButton until input fields are valid

        UsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateUsername(); //validate
        });
        PasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePassword(); //re-validate first name
        });

        //disable delete and edit buttons if fields are empty.
        UsernameField.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());
        PasswordField.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());

        //leftSection.setStyle("-fx-background-color: #15bc98");

        rootpane.setBackground(new Background(
                        createImage("https://edencoding.com/wp-content/uploads/2021/03/layer_06_1920x1080.png"),
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );


        rootpane.setOpacity(0);
        FadeTransition fadeOut2 = new FadeTransition(Duration.seconds(1), rootpane);
        fadeOut2.setFromValue(0);
        fadeOut2.setToValue(1);
        fadeOut2.play();
    }
    private static BackgroundImage createImage(String url) {
        return new BackgroundImage(
                new Image(url),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true));
    }
    @FXML
    public void login(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/db_interface_gui.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signUp(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/signUp.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean validateUsername() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = UsernameField.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){ //if input is valid
            UsernameField.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextUsername.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(UsernameField.isFocused() && !userInput.isEmpty()  ) { //if field is focused and not empty
            UsernameField.setStyle("-fx-border-color: red");
            alertTextUsername.setStyle("-fx-text-fill: red");
            alertTextUsername.setStyle("-fx-font-size: 10px");
            alertTextUsername.setText("* Username must be 2-25 characters");

        } else { //if the field is empty or not focused, then remove styling and alert
            UsernameField.setStyle("");
            alertTextUsername.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field


    } //end validateUsername

    public boolean validatePassword() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = PasswordField.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){ //if input is valid
            PasswordField.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextPassword.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(PasswordField.isFocused() && !userInput.isEmpty()  ) { //if field is focused and not empty
            PasswordField.setStyle("-fx-border-color: red");
            alertTextPassword.setStyle("-fx-text-fill: red");
            alertTextPassword.setStyle("-fx-font-size: 10px");
            alertTextPassword.setText("* First name must be 2-25 characters");

        } else { //if the field is empty or not focused, then remove styling and alert
            PasswordField.setStyle("");
            alertTextPassword.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field


    } //end validatePassword


    private void checkValidationStatus() {
        //check if all fields are valid
        boolean allFieldsValid = validateUsername() && validatePassword() ;

        //enable register button if all fields are valid
        loginButton.setDisable(!allFieldsValid);
    }



} //end class
