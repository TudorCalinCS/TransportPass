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
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/5ef5b09b-afb8-407f-b382-7dd0c4a10baf?raw=true" alt="Screenshot 2" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/7d127556-dbb2-44af-a038-eb07e3cb09ac?raw=true" alt="Screenshot 3" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/5db0861b-1183-4c65-aec6-7ee26063d2f2?raw=true" alt="Screenshot 4" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/42eb6df5-1285-4e19-9a3d-1a9388c5cb42?raw=true" alt="Screenshot 5" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/4ee10215-d6d0-40c1-a954-2914fe16ab61?raw=true" alt="Screenshot 6" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/d5f5fb16-b2e1-48d3-9b5c-85902667d436?raw=true" alt="Screenshot 7" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/402a15ee-940d-443c-b6d5-f1f655dd2e3f?raw=true" alt="Screenshot 8" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/b2a482f3-0afc-4aab-ae79-5572d1a3a573?raw=true" alt="Screenshot 9" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/a78c66c7-6ece-464d-80c9-51b6e68d9f67?raw=true" alt="Screenshot 10" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/dac4f006-5ab2-4e2a-bf4e-b1bbb2107fc5?raw=true" alt="Screenshot 11" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/2129e162-01aa-4abb-9b00-f71b17b73f1c?raw=true" alt="Screenshot 12" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/e30c330d-8bac-4ea7-a997-f65346eaa52f?raw=true" alt="Screenshot 13" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/d7f5dde3-562e-42d8-b6cd-bdeda8d97b65?raw=true" alt="Screenshot 14" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/f4679d50-c1fe-405e-9c35-8af09fda3ad2?raw=true" alt="Screenshot 15" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/813a3690-375e-414f-9bd6-11d67a1cbf33?raw=true" alt="Screenshot 16" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/c8101c10-a1f3-4996-9a45-36241dccc91a?raw=true" alt="Screenshot 17" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/183cc115-3203-4227-83f5-9487a7f25ba3?raw=true" alt="Screenshot 18" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/163ba1ac-ac4b-4487-a565-0e24e10a6366?raw=true" alt="Screenshot 19" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/2c90810d-5abd-4ba8-b2ba-a67e86611ab5?raw=true" alt="Screenshot 20" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/ad8f3145-4a17-4765-bba5-bdce9ff2deea?raw=true" alt="Screenshot 21" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/5263bacb-558d-4103-aad3-faa42095544f?raw=true" alt="Screenshot 22" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/5a997a39-c293-4d2a-9b52-20c4489b603f?raw=true" alt="Screenshot 23" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/aba745cd-2683-48c0-aef4-e2139283ae58?raw=true" alt="Screenshot 24" width="200" />
  <img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/f9811017-a516-4446-4ad-15764e0b0337?raw=true" alt="Screenshot 25" width="200" />
<img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/4d9aebb2-f959-4ef1-8032-00ae3e46b5fd?raw=true" alt="Screenshot 26" width="200" />
<img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/c7ffb6dc-ab9f-4d46-b2f6-bb16891cd9c5?raw=true" alt="Screenshot 27" width="200" />
<img src="https://github.com/TudorCalinCS/TransportPass/assets/161611731/9c5d34b7-ca4c-46d1-90c8-2666e6e9323c?raw=true" alt="Screenshot 28" width="200" />































