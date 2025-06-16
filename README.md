# Email Management System

A simple console-based email application built with Java, Hibernate (JPA), and MySQL.  
Users can sign up, log in, send emails to other users, and view received or sent emails.

## Features

- User registration and login
- Compose and send emails to multiple recipients
- View **unread**, **all received**, and **sent** emails
- Mark emails as read when opened

## Technologies Used

- Java 17+
- Hibernate (JPA)
- MySQL
- JDBC
- Maven (optional)

## Database Schema Overview

- `users`: Stores user accounts.
- `emails`: Stores email metadata and body.
- `recipients`: Maps emails to their recipients, with read/unread status.

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/MattTheGreat-root/Milou.git
2. **DataBase**
Create a MySQL database and Run the schema SQL to create required tables(as provided).
Specify YOUR_DB_NAME, YOUR_USERNAME and YOUR_PASSWORD in the hibernate.cfg.xml.template file, and then remove ".template" from the end of the file. Change the whole database connection url if necessary.
4. Build and run the program
