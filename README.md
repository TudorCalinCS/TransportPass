# Transport Pass 

## Overview

Transport Pass is an Android application designed to streamline the process of managing and validating public transport passes in Cluj Napoca. This app provides features for users to create accounts, purchase and manage digital tickets and subscriptions, and display QR codes for access. Controllers can use the app to scan these QR codes and verify users' tickets and subscriptions in real time.

## Features

### For Users

1. **Account Management**
   - Create and manage user accounts.
   - Upload student card photos for identity verification if the user is a student.

2. **Subscription Selection**
   - Choose from various subscription types: student, pupil, pensioner, external, single line, or all lines.

3. **Digital Ticket Management**
   - Purchase and manage digital tickets and subscriptions.
   - Generate and display QR codes for public transport access.

4. **Payment Options**
   - Payment method for purchasing and renewing tickets and subscriptions.
   - If the user is a student, pupil or pensioner, he benefits of free tickets and subscriptions.

5. **Notifications**
   - Get alerts for events such as "Green Friday" where tickets are available for free.
   - Get reminders for ticket renewal before expiration.

6. **Bus Schedule**
   - View bus schedules for all 105 routes from Cluj Napoca.

### For Controllers

1. **QR Code Scanning**
   - Scan QR codes to verify user tickets and subscriptions in public transport.

2. **Real-time Subscription Information**
   - Access real-time information about user subscriptions.

## Architecture

### Main Components

1. **User Interface (UI)**
   - Developed using Android Studio and Kotlin for the Android platform.
   - Intuitive and user-friendly design for both users and controllers.

2. **Backend and Application Logic**
   - Backend managed using Java, utilizing ServerSocket for handling application logic and database communication.
   - Python script integrated with an HTTP client to perform Optical Character Recognition (OCR) for verifying student IDs. The client sends photos to the Python server, which functions as a web service, via POST requests.

3. **Database**
   - Relational database SQLite used for storing user data, subscriptions, tickets, and images.
  
   ## Screenshots

<p float="left">
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/4cbc22ba-1d77-4c3d-b569-00967ab8aae8?raw=true" alt="Screenshot 1" width="200" />
  <img src="https://github.com/username/repository-name/blob/main/path/to/screenshot2.png?raw=true" alt="Screenshot 2" width="200" />
  <img src="https://github.com/username/repository-name/blob/main/path/to/screenshot3.png?raw=true" alt="Screenshot 3" width="200" />
  <img src="https://github.com/username/repository-name/blob/main/path/to/screenshot4.png?raw=true" alt="Screenshot 4" width="200" />
</p>

