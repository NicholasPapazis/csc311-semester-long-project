package viewmodel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    Text alertTextEmail;
    @FXML
    Text alertTextUsername;
    @FXML
    Text alertTextPassword;

    @FXML
    TextField EmailField;
    @FXML
    TextField UsernameField;
    @FXML
    TextField PasswordField;

    @FXML
    Button registerButton;

    public void initialize() {
        registerButton.setDisable(true);//disable register button until user clicks enters valid credentials

        PasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateEmail(); //re-validate first name
        });
        UsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateUsername(); //validate
        });
        PasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePassword(); //re-validate first name
        });

        EmailField.textProperty().addListener((observable, oldValue, newValue) ->  checkValidationStatus());
        UsernameField.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());
        PasswordField.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());

    }


    public void createNewAccount(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Info for the user. Message goes here");
        alert.showAndWait();
    }

    public void signUp(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean validateEmail() {
        final String regex = "(([a-zA-Z])(\\w)+)@(farmingdale).(edu)";//regular expression
        String emailInput = EmailField.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(emailInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){ //if input is valid
            EmailField.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextEmail.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(EmailField.isFocused() && !emailInput.isEmpty()  ) { //if field is focused and not empty
            EmailField.setStyle("-fx-border-color: red");
            alertTextEmail.setStyle("-fx-text-fill: red");
            alertTextEmail.setStyle("-fx-font-size: 10px");
            alertTextEmail.setText("* Email must end with @farmingdale.edu");

        } else { //if the field is empty or not focused, then remove styling and alert
            EmailField.setStyle("");
            alertTextEmail.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field


    } //end validateEmail

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

    //validate password
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


    //validation methods:
    private void checkValidationStatus() {
        //check if all fields are valid
        boolean allFieldsValid = validateEmail() && validateUsername() && validatePassword();

        //enable register button if all fields are valid
        registerButton.setDisable(!allFieldsValid);
    }


}
