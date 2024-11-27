Student Registration Application

The Student Registration Application is designed to simplify the process of managing student records. Whether you're adding new students, updating their details, or generating insightful reports, this application provides an intuitive interface and robust features for managing student data efficiently.
Features
1. CRUD Functionality

The app supports CRUD (Create, Read, Update, Delete) functionality for student records:

    Create: You can add new student records to the system with ease.
    Read: View a table of all students with their details fetched directly from the database.
    Update: Edit any student's information directly within the table view.
    Delete: Remove a student record when it's no longer needed.

2. Advanced Registration with RegEx Validation

    During registration, the app ensures that users enter a valid @farmingdale.edu email address and passwords between 2 to 25 characters.
    The app uses advanced regular expressions (regex) to validate these inputs and give you instant feedback if something’s wrong.

3. User Preferences

    The app uses the Java Preferences API to store user preferences such as previously entered registration details (username, email, etc.), so you don't have to keep typing them in again.

4. Azure SQL Database Integration

    All the student data is securely stored in an Azure SQL Database. This cloud-based database ensures the app is scalable and can handle a large amount of student data with ease.

5. Blob Storage for Profile Images

    Users can upload their profile images, which are then stored in Azure Blob Storage. This helps with managing profile pictures in a way that’s both secure and flexible.

6. User Feedback and Status Bar

    The app has a handy status bar that gives you real-time feedback on what’s happening—whether it's confirming your successful registration, indicating a successful data import, or notifying you of errors.

7. Import/Export CSV

    Users can import and export CSV files to manage student records.
    You can load student data from a CSV file into the table, or save the current data from the table to a CSV file for backup or further analysis.

8. Generate PDF Reports with Bar Charts

    Want to see how students are distributed by major? You can generate a PDF report that includes a bar chart showing this distribution.
    This makes it easy to share visual data about the student population with others.

9. Edit Records Directly in the Table

    Need to change something? No problem! You can edit student records directly within the table view. Any changes you make are saved instantly to the database.

10. Thread-Safe User Session Management

    The app uses a thread-safe UserSession class to ensure everything runs smoothly even if multiple users are interacting with the app at the same time.

Technologies Used
JavaFX

    The app’s graphical interface is built using JavaFX. It’s a great framework for building modern desktop applications and provides the smooth, user-friendly experience you see in this app.

Azure SQL Database

    Azure SQL Database is used for securely storing student records. This ensures that data is safely stored in the cloud and can be easily queried when needed.

Azure Blob Storage

    Azure Blob Storage is used to store student profile images. This allows users to upload and retrieve their photos without worrying about managing the files locally.

Regular Expressions (Regex)

    The app uses regex to validate email addresses and passwords during the registration process, ensuring data integrity from the start.

Java Preferences API

    The Java Preferences API allows the app to save user settings and preferences locally, making the user experience more seamless across sessions.
