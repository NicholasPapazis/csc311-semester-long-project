package viewmodel;

import com.azure.storage.blob.BlobClient;
import dao.DbConnectivityClass;
import dao.StorageUploader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Person;
import service.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DB_GUI_Controller implements Initializable {

    StorageUploader store = new StorageUploader();

    @FXML
    Text alertTextFN;
    @FXML
    Text alertTextLN;
    @FXML
    Text alertTextDPT;
    @FXML
    ComboBox<Major> majorComboBox;
    @FXML
    ProgressBar progressBar;
    @FXML
    TextField first_name, last_name, department, email, imageURL;
    @FXML
    Button clearBtn, addBtn, deleteBtn, editBtn;
    @FXML
    ImageView img_view;
    @FXML
    MenuBar menuBar;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_department, tv_major, tv_email;
    private final DbConnectivityClass cnUtil = new DbConnectivityClass();
    private final ObservableList<Person> data = cnUtil.getData();

    //tracks whether the user has attempted to input into the fields
    private boolean firstNameClicked = false;
    private boolean lastNameClicked = false;
    private boolean emailClicked = false;
    private boolean departmentClicked = false;
    private boolean zipCodeClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tv.setItems(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //disable delete and edit button
        deleteBtn.setDisable(true);
        editBtn.setDisable(true);

        //listen for changes in first name input field
        first_name.textProperty().addListener((observable, oldValue, newValue) -> {
            validateFirstName(); //re-validate first name
        });
        //listen for changes in last name input field
        last_name.textProperty().addListener((observable, oldValue, newValue) -> {
            validateLastName(); //re-validate first name
        });
        //listen for changes in department input field
        department.textProperty().addListener((observable, oldValue, newValue) -> {
            validateDepartment(); //re-validate first name
        });
        //listen for changes in major input field
        majorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateMajor(); //re-validate first name
        });
        //listen for changes in email input field
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            validateEmail(); //re-validate first name
        });



        //disable delete and edit buttons if fields are empty.
        first_name.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());
        last_name.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());
        department.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());
        majorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());
        email.textProperty().addListener((observable, oldValue, newValue) -> checkValidationStatus());

        majorComboBox.setItems(FXCollections.observableArrayList(Major.values()));
        majorComboBox.getSelectionModel().selectFirst();



    } //initialize end


    @FXML
    protected void addNewRecord() {

            Person p = new Person(first_name.getText(), last_name.getText(), department.getText(),
                    majorComboBox.getValue().toString(), email.getText(), imageURL.getText());
            cnUtil.insertUser(p);
            cnUtil.retrieveId(p);
            p.setId(cnUtil.retrieveId(p));
            data.add(p);
            clearForm();

    }

    @FXML
    protected void clearForm() {
        first_name.setText("");
        last_name.setText("");
        department.setText("");
        majorComboBox.setValue(null);
        email.setText("");
        imageURL.setText("");
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").getFile());
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }

    @FXML
    protected void displayAbout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void editRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        Person p2 = new Person(index + 1, first_name.getText(), last_name.getText(), department.getText(),
                majorComboBox.getValue().toString(), email.getText(),  imageURL.getText());
        cnUtil.editUser(p.getId(), p2);
        data.remove(p);
        data.add(index, p2);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void deleteRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        cnUtil.deleteRecord(p);
        data.remove(index);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));
            //need to add the code here
            Task<Void> uploadTask = createUploadTask(file, progressBar);
            progressBar.progressProperty().bind(uploadTask.progressProperty());
            new Thread(uploadTask).start();
        }
    }

    @FXML
    protected void addRecord() {
        showSomeone();

    }

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p = tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDepartment());
        //majorComboBox.setValue(viewmodel.Major.valueOf(p.getMajor()));
        majorComboBox.setValue(Major.valueOf(p.getMajor()));
        email.setText(p.getEmail());
        imageURL.setText(p.getImageURL());
    }

    public void lightTheme(ActionEvent actionEvent) {
        try {
            Scene scene = menuBar.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.getScene().getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            System.out.println("light " + scene.getStylesheets());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void darkTheme(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSomeone() {
        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField1 = new TextField("Name");
        TextField textField2 = new TextField("Last Name");
        TextField textField3 = new TextField("Email ");
        ObservableList<Major> options =
                FXCollections.observableArrayList(Major.values());
        ComboBox<Major> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, textField1, textField2,textField3, comboBox));
        Platform.runLater(textField1::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(textField1.getText(),
                        textField2.getText(), comboBox.getValue());
            }
            return null;
        });
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            MyLogger.makeLog(
                    results.fname + " " + results.lname + " " + results.major);
        });
    }

    private static enum Major {Major, Business, CSC, CPIS, Accounting, Anthropology, Art, Biology, Chemistry, Communications, Economics, Education, Engineering, English, Finance, History, Mathematics, Nursing, Psychology, Physics, Sociology}

    private static class Results {

        String fname;
        String lname;
        Major major;

        public Results(String name, String date, Major venue) {
            this.fname = name;
            this.lname = date;
            this.major = venue;
        }
    }

    public void firstNameInputClicked(MouseEvent mouseEvent) {
        firstNameClicked = true; //marks that uer has clicked this field
        //focusLostAll(mouseEvent); //lose focus on all input fields
        first_name.requestFocus(); //focus on name input field
        //inputWrapperFN.setStyle("-fx-border-color: #3B8EDC"); //add border when clicked
    }
    public void lastNameInputClicked(MouseEvent mouseEvent) {
        lastNameClicked = true; //marks that uer has clicked this field
        //focusLostAll(mouseEvent); //lose focus on all input fields
        last_name.requestFocus(); //focus on name input field
        //inputWrapperFN.setStyle("-fx-border-color: #3B8EDC"); //add border when clicked
    }

    public void departmentInputClicked(MouseEvent mouseEvent) {
        departmentClicked = true; //marks that uer has clicked this field
        //focusLostAll(mouseEvent); //lose focus on all input fields
        department.requestFocus(); //focus on name input field
        //inputWrapperFN.setStyle("-fx-border-color: #3B8EDC"); //add border when clicked
    }

    public boolean validateFirstName() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = first_name.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){
            first_name.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextFN.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else {
            if(first_name.getLength() > 0 ){
                first_name.setStyle("-fx-border-color: red");
                alertTextFN.setStyle("-fx-text-fill: red");
                alertTextFN.setStyle("-fx-font-size: 10px");
                alertTextFN.setText("* First name must be 2-25 characters");

                return false; //returns false because the input is not valid
            }
            return false; //return false if user has not yet inputed anything into the text field
        }

    } //end validateFirstName

    public boolean validateLastName() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = last_name.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){
            last_name.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextLN.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else {
            if(last_name.getLength() > 0){
                last_name.setStyle("-fx-border-color: red");
                alertTextLN.setStyle("-fx-text-fill: red");
                alertTextLN.setStyle("-fx-font-size: 10px");
                alertTextLN.setText("* Last name must be 2-25 characters");

                return false; //returns false because the input is not valid
            }
            return false; //return false if user has not yet inputed anything into the text field
        }

    } //end validateLastName


    public boolean validateDepartment() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = department.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){
            department.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextDPT.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else {
            if(last_name.getLength() > 0){
                department.setStyle("-fx-border-color: red");
                alertTextDPT.setStyle("-fx-text-fill: red");
                alertTextDPT.setStyle("-fx-font-size: 10px");
                alertTextDPT.setText("* Last name must be 2-25 characters");

                return false; //returns false because the input is not valid
            }
            return false; //return false if user has not yet inputed anything into the text field
        }

    } //end validateFirstName



    //validate major
    public boolean validateMajor() {
        boolean fieldEmpty = majorComboBox.getValue().toString() == "Major";
        System.out.println("Please choose a major.");
        majorComboBox.setStyle("-fx-border-color: #ff5733; -fx-border-width: 2px;");
        return !fieldEmpty;

    }

    //validate email
    public boolean validateEmail() {
        boolean fieldEmpty = email.getText().isEmpty();
        return !fieldEmpty;

    }


    private void checkValidationStatus() {
        //check if all fields are valid
        boolean allFieldsValid = validateFirstName() && validateLastName() && validateDepartment() && validateMajor() && validateEmail();

        //enable register button if all fields are valid
        deleteBtn.setDisable(!allFieldsValid);
        editBtn.setDisable(!allFieldsValid);
    }



    //here is the task that I added to upload the image in its own thread
    private Task<Void> createUploadTask(File file, ProgressBar progressBar) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                BlobClient blobClient = store.getContainerClient().getBlobClient(file.getName());
                long fileSize = Files.size(file.toPath());
                long uploadedBytes = 0;

                try (FileInputStream fileInputStream = new FileInputStream(file);
                     OutputStream blobOutputStream = blobClient.getBlockBlobClient().getBlobOutputStream()) {

                    byte[] buffer = new byte[1024 * 1024]; // 1 MB buffer size
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        blobOutputStream.write(buffer, 0, bytesRead);
                        uploadedBytes += bytesRead;

                        // Calculate and update progress as a percentage
                        int progress = (int) ((double) uploadedBytes / fileSize * 100);
                        updateProgress(progress, 100);
                    }
                }

                return null;
            }
        };
    }

}