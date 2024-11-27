![logo](https://github.com/user-attachments/assets/23b7bc91-9269-4226-b129-faf616423dd3)
# **Portal Pro** - Student Registration Application

**Portal Pro** simplifies the process of managing student records. Whether you're adding, updating, or viewing student data, this application provides a clean, intuitive interface and powerful features to keep everything organized.

## **Features**

### 1. **CRUD Functionality**
   - **Create**: Add new student records easily.
   - **Read**: View student details in a simple table.
   - **Update**: Edit records directly within the table.
   - **Delete**: Remove student records when necessary.

### 2. **Advanced Registration with Validation**
   - Ensures users enter a valid @farmingdale.edu email and passwords between 2 to 25 characters, using regular expression (regex) validation.

### 3. **User Preferences**
   - Saves user preferences like username and email via the Java Preferences API, so you don’t have to re-enter them every time.

### 4. **Azure SQL Database Integration**
   - All student data is securely stored in an Azure SQL Database, offering scalability and secure access.

### 5. **Profile Image Storage**
   - Upload and store student profile images in Azure Blob Storage for easy management.

### 6. **User Feedback & Status Bar**
   - Displays real-time feedback, such as successful registrations or data import status, in a user-friendly status bar.

### 7. **Import/Export CSV**
   - Import student data from CSV files or export current records to CSV for backup or further analysis.

### 8. **Generate PDF Reports**
   - Create PDF reports that include bar charts to visualize student distribution by major.

### 9. **Edit Records Directly in the Table**
   - Edit student records directly within the table, and changes are saved to the database immediately.

### 10. **Thread-Safe User Sessions**
    - Ensures smooth, concurrent usage by managing user sessions in a thread-safe manner.

## **Technologies Used**

- **JavaFX**: The user interface is built with JavaFX, providing a smooth and modern desktop experience.
- **Azure SQL Database**: All student data is securely stored in the cloud with Azure SQL Database.
- **Azure Blob Storage**: Profile images are stored securely using Azure Blob Storage.
- **Regular Expressions (Regex)**: Validates user inputs (email and password) during registration.
- **Java Preferences API**: Saves and loads user settings, improving convenience across sessions.

