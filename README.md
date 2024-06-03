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

5. **Notifications**
   - Receive notifications for events like "Green Friday" when you can't buy tickets.
   - Get reminders for ticket renewal before expiration.

6. **Bus Schedule**
   - View bus schedules for all 105 routes from Cluj Napoca.

### For Controllers

1. **QR Code Scanning**
   - Scan QR codes to verify user tickets and subscriptions in public transport.

2. **Real-time Subscription Information**
   - Access real-time information about user subscriptions.

## Proposed Architecture

### Main Components

1. **User Interface (UI)**
   - Developed using Android Studio and Kotlin for the Android platform.
   - Intuitive and user-friendly design for both users and controllers.

2. **Backend and Application Logic**
   - Backend developed using Java for managing application logic and database communication.
   - Python script for Optical Character Recognition (OCR) to verify student IDs.

3. **Database**
   - Relational database SQLite used for storing user data, subscriptions, tickets, and images.
