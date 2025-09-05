# 📱 VetClinic Mobile – Android App for Pet Owners

**VetClinic Mobile** is an Android application for pet owners to manage their pets, appointments, and profile within a veterinary clinic system. The app works entirely offline using a local SQLite database (Room), with no dependency on a web API - could be implemented in the fiuire. It supports multiple languages, theme customization, and provides a simple, intuitive interface built in XML.

---

## 🚀 Workflow Overview

![Welcome Screen](./screenshots/welcome.PNG)

The application begins with the **Welcome Activity**, where users can:

- 👤 Register (via `RegisterFragment`)
![Register Screen](./screenshots/register.PNG)

- 🔑 Log in (via `LoginFragment`)
![Login Screen](./screenshots/login.PNG)

After successful login, users are redirected to the **Main Activity**, which includes a **Bottom Navigation Menu** with the following sections:

---

## 🏠 Home Fragment

![Home Fragment](./Screenshots/home.PNG)

Displays:

- A personalized welcome message  
- General information about the veterinary clinic  

---

## 🐾 Pets Fragment

![Pets Fragment](./Screenshots/pets.PNG)

Users can:

- View a list of their pets  
- Edit or delete existing pet information by clicking on one of the pets
![Pet Details Screen](./screenshots/pet_details.PNG)

- Add a new pet by clicking on a floating action button
![Add Pet Screen](./screenshots/add_pet.PNG)

---

## 📅 Appointments Fragment

![Appointments Fragment](./Screenshots/appointments.PNG)

This section allows users to:

- View upcoming and past appointments  
- Cancel an existing appointment by clicking on Cancel button
- Mark an appointment as paid by clicking Pay button - could be implemepted via Stripe in the future 
- Book a new appointment
![Add Appointment Screen](./screenshots/add_appointment.PNG)

Here users choose which pet is appointment for, which veterinarian they want, what service they need and then they need to select a date and time of the appointment (in the future, when the veterinarian is not already booked).
![Select Date Screen](./screenshots/select_date.PNG)  

---

## 👤 Profile Fragment

![Profile Fragment](./Screenshots/profile.PNG)

Users can:

- View their profile
- Edit their profile (name, email, password) by clicking on the Edit profile button
![Edit Profile Screen](./screenshots/edit_profile.PNG)

- View a history of all their payments
![My Payments Screen](./screenshots/my_payments.PNG)
  
- Change the app language (English / Serbian)  
- Switch between light and dark themes
![Dark Serbian Screen](./screenshots/dark_serbian.PNG)
 

---

## ⚙️ Features Summary

- ✅ Offline-first Android app (no internet required)
- 🐶 Manage pets and their details
- 📅 Schedule and manage appointments
- 💳 Simulate payments and track history
- 🌐 Language switcher (English / Serbian)
- 🎨 Light/Dark theme toggle
- 🔐 Secure login and registration with local credential storage

---

## 🛠 Technologies Used

- **Java (Android SDK)**
- **Room (SQLite) for local database**
- **MVVM architecture**
- **LiveData & ViewModel**
- **Navigation Component**
- **Material Design Components**
- **XML layouts**
- **Android Emulator (for screenshots)**

---

## 📎 Notes

- All user and appointment data is stored locally using **Room database**.
- The app is modular and can be extended with features like push notifications or sync in future versions.

---

> 📸 **Make sure all screenshots are placed in the `screenshots/` folder of the repository.**

