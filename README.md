# Student Registration App

This is a Student Registration Application built using JavaFX, designed to manage student records. The application allows users to add, edit, and delete student records. It also provides features to import/export student data from/to CSV files, enhance user feedback with a status message, and includes form field validations using advanced regex patterns.

## Features

### UI State Management
- **Edit Button**: The "Edit" button is disabled unless a record is selected from the table view.
- **Delete Button**: The "Delete" button is disabled unless a record is selected from the table view.
- **Add Button**: The "Add" button is enabled only if the form fields contain valid data.
- **Menu Items**: Menu items corresponding to actions (e.g., Edit, Delete) are grayed out when the required selection is not made in the table or form.

### Form Enhancement
- **Field Validation**: Ensures that all fields (First Name, Last Name, Email, Department, etc.) are filled with valid data using advanced regular expressions (regex).
- **Major Field**: The "Major" field has been converted into a dropdown menu with predefined values, such as CS (Computer Science), CPIS (Computer Programming and Information Systems), and English. The "Major" options are defined using an `enum`.

### User Feedback
- **Status Bar**: A status message is displayed at the bottom of the window to inform the user of successful data addition or updates.
  
### Menu Items
- **Import CSV**: A menu item to import student records from a CSV file.
- **Export CSV**: A menu item to export the current student records to a CSV file.

### Thread Safety
- **Thread-Safe UserSession**: The `UserSession` class has been modified to ensure thread safety, allowing for concurrent access to user session data without causing data inconsistencies.

### User Session and Preferences
- **Signup Page**: Complete functionality for user sign-up, allowing users to create an account.
- **User Preferences**: Records the username and password upon sign-in in a preferences file for future logins.
