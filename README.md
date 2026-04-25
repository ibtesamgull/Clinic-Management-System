# 🏥 Clinic Management System (CMS)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)

A robust, multi-tier desktop application built in Java designed to digitize and streamline the day-to-day operations of a medical clinic. From the moment a patient walks in, to their diagnosis, prescription, and final billing, this system handles the entire lifecycle securely and efficiently.

---

## 🌟 The Vision

Managing a clinic involves juggling patient records, doctor schedules, staff payroll, and complex billing. This system replaces paper trails and scattered spreadsheets with a centralized, intuitive graphical interface, ensuring healthcare professionals can focus on what matters most: **patient care**.

---

## 🏗️ Project Dimensions (Core Features)

This project is built around the distinct roles and workflows within a clinic.

### 1. 👑 Administrative Dimension (The Core)
* **Dashboard Analytics:** High-level overview via the `AdminDashboard`.
* **HR & Staff Management:** Add, update, and view details for all employees (`EmployeeListPanel`, `AddEmployeePanel`).
* **Payroll Automation:** Integrated salary generation logic (`generateSalaries`).
* **Access Control:** Secure user management (`ManageUsersPanel`) and password resets.

### 2. 🩺 Medical & Clinical Dimension (The Care)
* **Doctor Portals:** Dedicated views for doctors to check their daily queues (`MyAppointments`, `DoctorListPanel`).
* **Medical Records:** Digitized patient history tracking (`MedicalRecordsListPanel`, `AddMedicalRecordPanel`).
* **E-Prescriptions:** Seamless digital prescription creation and viewing (`PrescriptionPanel`, `ShowPrescriptionPanel`).

### 3. 🗓️ Front Desk Dimension (The Workflow)
* **Receptionist Module:** Dedicated interface for front-desk staff (`Receptionist`).
* **Patient Onboarding:** Register new patients and update their profiles (`UpdatePatientForm`, `ViewPatientForm`).
* **Smart Scheduling:** Book, update, and track appointments without double-booking (`AddAppointmentPanel`, `AppointmentListPanel`).

### 4. 💳 Financial Dimension (The Economy)
* **Automated Billing:** Generate itemized bills for consultations and treatments (`GenerateBillPanel`).
* **Invoice Tracking:** Keep a secure history of all financial transactions (`BillsListPanel`, `ViewBillFrame`).

---

## 💻 Tech Stack

* **Frontend UI:** Java Swing & AWT (Custom Panels, Frames, and Dialogs)
* **Backend Logic:** Core Java (Object-Oriented Architecture)
* **Database:** MySQL (Relational Database Management)
* **Connectivity:** JDBC (`Conn.java` for secure database interactions)

---

## 📸 System Showcase

*(Tip: Add screenshots of your application here to make the repository visually appealing!)*

| Login Portal | Admin Dashboard | Patient Billing |
| :---: | :---: | :---: |
| ![Login](link_to_login_image) | ![Admin](link_to_admin_image) | ![Billing](link_to_billing_image) |

---

## 🚀 Getting Started

Follow these steps to set up the clinic environment on your local machine.

### Prerequisites
* **Java Development Kit (JDK 8+)** installed.
* **MySQL Server** installed and running.
* An IDE like IntelliJ IDEA, Eclipse, or NetBeans.

### Installation

**1. Clone the repository**
```bash
git clone [https://github.com/ibtesamgull/clinic-management-system.git](https://github.com/ibtesamgull/clinic-management-system.git)
cd clinic-management-system
