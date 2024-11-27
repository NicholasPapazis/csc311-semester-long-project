package viewmodel;

import com.azure.storage.blob.BlobClient;
import dao.DbConnectivityClass;
import dao.StorageUploader;
import javafx.animation.FadeTransition;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import model.Person;
import service.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DB_GUI_Controller implements Initializable {

    StorageUploader store = new StorageUploader();


    private BarChart<String, Number> barChart;
    @FXML
    Text statusText;
    @FXML
    Text alertTextFN;
    @FXML
    Text alertTextLN;
    @FXML
    Text alertTextDPT;
    @FXML
    Text alertTextMajor;
    @FXML
    Text alertTextEmail;
    @FXML
    Text alertTextURL;

    @FXML
    MenuItem newItem, ChangePic, logOut, editItem, deleteItem, ClearItem, CopyItem, lightTheme, darkTheme, displayAbout;

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


        tv.setEditable(true);//allow the TableView to be editable

        try {
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));

            //create text field inside each cell when clicked
            tv_fn.setCellFactory(TextFieldTableCell.forTableColumn());
            tv_ln.setCellFactory(TextFieldTableCell.forTableColumn());
            tv_department.setCellFactory(TextFieldTableCell.forTableColumn());
            tv_email.setCellFactory(TextFieldTableCell.forTableColumn());

            //change data in the person instance if text in changed in the cell
            tv_fn.setOnEditCommit(event -> {
                Person person = event.getRowValue(); //get the person in the row
                person.setFirstName(event.getNewValue()); //change their value
                first_name.setText(person.getFirstName()); //update TextField value
                editRecord(); //edit record so that it saves in the database
            });
            tv_ln.setOnEditCommit(event -> {
                Person person = event.getRowValue(); //get the person in the row
                person.setLastName(event.getNewValue()); //change their value
                last_name.setText(person.getLastName()); //update TextField value
                editRecord(); //edit record so that it saves in the database
            });
            tv_department.setOnEditCommit(event -> {
                Person person = event.getRowValue(); //get the person in the row
                person.setDepartment(event.getNewValue()); //change their value
                department.setText(person.getDepartment()); //update TextField value
                editRecord(); //edit record so that it saves in the database
            });
            tv_email.setOnEditCommit(event -> {
                Person person = event.getRowValue();
                person.setEmail(event.getNewValue());
                email.setText(person.getEmail());
                editRecord();
            });


            tv.setItems(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //notifies user that they can edit value in the cell
        tv.setOnMouseMoved((MouseEvent event) -> {
            statusText.setText("Double click to edit value");
        });


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


        progressBar.setOpacity(0); //makes progress bar invisible when it is not being used




    } //initialize end

    protected void isFirstnameFocused()
    {
        System.out.println(first_name.isFocused());
    }

    @FXML
    protected void addNewRecord() {

        Person p = new Person(first_name.getText(), last_name.getText(), department.getText(),
                majorComboBox.getValue().toString(), email.getText(), alertTextURL.getText());
        cnUtil.insertUser(p);
        cnUtil.retrieveId(p);
        p.setId(cnUtil.retrieveId(p));
        data.add(p);
        clearForm();

        statusText.setText("A new user was inserted successfully.");
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), statusText);
        fadeOut.setDelay(Duration.seconds(3));//delays fade
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            statusText.setText("A new user was inserted successfully.");
        });
        fadeOut.play();

    }

    @FXML
    protected void clearForm() {
        first_name.setText("");
        last_name.setText("");
        department.setText("");
        majorComboBox.setValue(Major.values()[0]);
        email.setText("");
        //imageURL.setText("");
        img_view.setImage(new Image(getClass().getResourceAsStream("/images/profile.png")));
        alertTextURL.setText("default.png");
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
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
                majorComboBox.getValue().toString(), email.getText(),  alertTextURL.getText());
        cnUtil.editUser(p.getId(), p2);
        data.remove(p);
        data.add(index, p2);
        tv.getSelectionModel().select(index);
        statusText.setText("User was updated successfully.");
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), statusText);
        fadeOut.setDelay(Duration.seconds(3));//delays fade
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            statusText.setText("User was updated successfully.");
        });
        fadeOut.play();
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
            alertTextURL.setText(file.getName()); //gets just the file name
            //need to add the code here
            progressBar.setOpacity(1); //make progress bar visible when in use.

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
        Person p = tv.getSelectionModel().getSelectedItem();//selects the person object that corresponds to the table row that was selected
        first_name.setText(p.getFirstName()); //
        last_name.setText(p.getLastName());
        department.setText(p.getDepartment());
        //majorComboBox.setValue(viewmodel.Major.valueOf(p.getMajor()));
        majorComboBox.setValue(Major.valueOf(p.getMajor()));
        email.setText(p.getEmail());
        //imageURL.setText("https://papaziscsc311storage.blob.core.windows.net/media-files/" + p.getImageURL());
        alertTextURL.setText(p.getImageURL());
        store.listImages();
        //img_view.setImage(new Image("https://developer.apple.com/wwdc24/images/og/phase-3-xjf/wwdc24-p3-og.png"));*/
        img_view.setImage(store.loadImageFromBlob(p.getImageURL()));
        System.out.println("image here");
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


    public boolean validateFirstName() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = first_name.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){ //if input is valid
            first_name.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextFN.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(first_name.isFocused() && !userInput.isEmpty()  ) { //if field is focused and not empty
            first_name.setStyle("-fx-border-color: red");
            alertTextFN.setStyle("-fx-text-fill: red");
            alertTextFN.setStyle("-fx-font-size: 10px");
            alertTextFN.setText("* First name must be 2-25 characters");

        } else { //if the field is empty or not focused, then remove styling and alert
            first_name.setStyle("");
            alertTextFN.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field


    } //end validateFirstName

    public boolean validateLastName() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = last_name.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){ //if input is valid
            last_name.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextLN.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(last_name.isFocused() && !userInput.isEmpty()  ) { //if field is focused and not empty
            last_name.setStyle("-fx-border-color: red");
            alertTextLN.setStyle("-fx-text-fill: red");
            alertTextLN.setStyle("-fx-font-size: 10px");
            alertTextLN.setText("* Last name must be 2-25 characters");

        } else { //if the field is empty or not focused, then remove styling and alert
            last_name.setStyle("");
            alertTextLN.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field, or if the user input is incorrect.

    } //end validateLastName


    public boolean validateDepartment() {
        final String regex = "^.{2,25}$"; //regular expression
        String userInput = department.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){ //if input is valid
            department.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextDPT.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(department.isFocused() && !userInput.isEmpty()  ) { //if field is focused and not empty
            department.setStyle("-fx-border-color: red");
            alertTextDPT.setStyle("-fx-text-fill: red");
            alertTextDPT.setStyle("-fx-font-size: 10px");
            alertTextDPT.setText("* Department name must be 2-25 characters");

        } else { //if the field is empty or not focused, then remove styling and alert
            department.setStyle("");
            alertTextDPT.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field, or if the user input is incorrect.

    } //end validateFirstName



    //validate major
    public boolean validateMajor() {
        boolean fieldSelected = !(majorComboBox.getValue().toString() .equals("Major"));
        //checks if above statement is valid
        if(fieldSelected){ //if input is valid
            majorComboBox.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextMajor.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(majorComboBox.isFocused() && majorComboBox.getValue().toString().equals("Major")  ) { //if field is focused and
            majorComboBox.setStyle("-fx-border-color: red");
            alertTextMajor.setStyle("-fx-text-fill: red");
            alertTextMajor.setStyle("-fx-font-size: 10px");
            alertTextMajor.setText("* Please select a major");

        } else { //if the field is empty or not focused, then remove styling and alert
            majorComboBox.setStyle("");
            alertTextMajor.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field, or if the user input is incorrect.

    }


    public boolean validateEmail() {
        final String regex = "(([a-zA-Z])(\\w)+)@(farmingdale).(edu)"; //regular expression
        String userInput = email.getText(); //gets text from input

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(userInput);

        boolean inputValid = matcher.matches(); //check if input matches regex

        //checks if above statement is valid
        if(inputValid){ //if input is valid
            email.setStyle("-fx-border-color: #15bc98"); //sets border color of input field to green
            alertTextEmail.setText(""); //gets rid of any alert text in alert box
            return true; //returns true because the input is valid
        }
        else if(email.isFocused() && !userInput.isEmpty()  ) { //if field is focused and not empty
            email.setStyle("-fx-border-color: red");
            alertTextEmail.setStyle("-fx-text-fill: red");
            alertTextEmail.setStyle("-fx-font-size: 10px");
            alertTextEmail.setText("* Email must end with @farmingdale.edu");

        } else { //if the field is empty or not focused, then remove styling and alert
            email.setStyle("");
            alertTextEmail.setText("");
        }
        return false; //return false if user has not yet inputed anything into the text field


    } //end validateEmail


    private void checkValidationStatus() {
        //check if all fields are valid
        boolean allFieldsValid = validateFirstName() && validateLastName() && validateDepartment() && validateMajor() && validateEmail();

        //enable register button if all fields are valid
        deleteBtn.setDisable(!allFieldsValid);
        editBtn.setDisable(!allFieldsValid);
        addBtn.setDisable(!allFieldsValid);
        clearBtn.setDisable(!allFieldsValid);
    }



    //here is the task that I added to upload the image in its own thread
    private Task<Void> createUploadTask(File file, ProgressBar progressBar) {
        progressBar.setOpacity(1);
        progressBar.progressProperty().unbind();

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


                progressBar.setOpacity(0); //hide progress bar when finished with task.
                statusText.setText("Profile image updated successfully.");
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), statusText);
                fadeOut.setDelay(Duration.seconds(3));//delays fade
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(e -> {
                    statusText.setText("Profile image updated successfully.");
                });
                fadeOut.play();


                return null;
            }
        };
    }


    @FXML
    private void createBarChartOfMajors(){
        Map<String, Integer> majorCount = new HashMap<>(); //hashmap will store the number of times each major occurs
        //iterates through data
        for(Person p: data){
            String major = p.getMajor(); //gets major of current item in data
            majorCount.put(major, majorCount.getOrDefault(major, 0) + 1);
        }

        //iterates through HashMap and prints number for each major
        for(Map.Entry<String, Integer> major: majorCount.entrySet()){
            System.out.println(major.getKey() + ": " + major.getValue());
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Major");
        yAxis.setLabel("Count");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.legendVisibleProperty().setValue(false); //hides graph legend

        XYChart.Series<String, Number> set1 = new XYChart.Series<>();

        for(Map.Entry<String, Integer> major: majorCount.entrySet()){

            set1.getData().add(new XYChart.Data(major.getKey(), major.getValue()));

            System.out.println(major.getKey() + ": " + major.getValue());

        }
        barChart.getData().addAll(set1);//adds data to barChart

        // Create a new Stage (pop-up window) for the chart
        Stage popupStage = new Stage();
        Scene popupScene = new Scene(barChart, 600, 400);
        popupStage.setTitle("Student Major Chart");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(popupScene);

        // Show the pop-up window
        popupStage.show();


    } //createBarChartOfMajors() end








}