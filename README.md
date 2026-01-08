
Hospital Management System – Project Report

1. Project Overview

The Hospital Management System is a Java-based application designed to manage hospital operations efficiently.
The system focuses on patient handling, emergency cases, OPD flow, medical history, and department management using appropriate Data Structures and Algorithms (DSA) to ensure fast, organized, and reliable processing.

The project follows a modular and centralized architecture for better control, scalability, and maintainability.

2. System Architecture

The system is controlled by a central logic class: HospitalSystemLogic (Singleton Pattern)

* Acts as the main controller of the system
* Ensures only one instance manages hospital operations
* Coordinates all modules safely and efficiently

This design avoids data inconsistency and improves system reliability.

3. Functional Modules & Their Purpose

3.1 Patient Management (Linked List)
Purpose: Manage general patient records.

Functionalities:

* Add new patient records
* View all registered patients
* Search and remove patients
* Maintain dynamic records without fixed size

Why Linked List?

* Patients can be added or removed easily
* Efficient memory usage
* Suitable for frequently changing data

3.2 Emergency Management (Priority Queue)
Purpose: Handle emergency patients based on severity.

Functionalities:

* Add emergency patients with priority levels
* Automatically serve critical patients first
* View emergency queue status

Why Priority Queue?

* Ensures critical patients are treated first
* Ideal for real-world emergency handling

3.3 OPD Management (Queue)
Purpose: Manage Out-Patient Department flow.

Functionalities:

* Register OPD patients
* Serve patients on First Come First Serve (FCFS) basis
* Display OPD waiting list

Why Queue?

* Maintains fairness
* Simple and efficient patient flow

3.4 Medical History (Stack)
Purpose: Track recent medical records.

Functionalities:

* Push new medical records
* View recent medical history
* Remove last added record if needed

Why Stack?

* Follows Last In First Out (LIFO)
* Useful for undo operations and recent record tracking

3.5 Department Management (BST + HashMap)
Purpose: Manage hospital departments efficiently.

Functionalities:

* Add and remove departments
* Search departments quickly
* View department statistics

Why BST + HashMap?

* BST: Maintains sorted structure and fast searching
* HashMap: Provides instant access using department names
* Combination improves performance and organization

4. User Interface

The project includes a modern Java GUI:

* Clean dashboard design
* Separate panels for each module
* Easy navigation for hospital staff
* Suitable for real-world demonstration

5. Data Structures & Algorithms Used

Module | Data Structure
Patient Records | Linked List
Emergency Handling | Priority Queue
OPD Flow | Queue
Medical History | Stack
Departments | BST + HashMap
System Control | Singleton Pattern

6. Project Significance

* Demonstrates practical use of DSA concepts
* Mimics real hospital workflows
* Efficient, scalable, and well-structured
* Suitable for academic evaluation and real-world simulation

7. Conclusion

This Hospital Management System effectively integrates Data Structures, Algorithms, and Object-Oriented Programming to solve real-life hospital management problems.
The system is simple to understand, logically structured, and suitable for presentation to academic authorities as a DSA-based practical project.

