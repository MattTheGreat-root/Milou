   Email Service CLI
A command-line Java application for sending, receiving, and managing emails between registered users. This project demonstrates the use of Hibernate (JPA) with a MySQL database for object-relational mapping.

   Features

User registration & login
Compose and send emails to one or multiple users
View unread emails
View all received emails
View sent emails
Mark emails as read when opened
Secure, randomized email codes for referencing emails
Lazy loading handling using JOIN FETCH
Transaction management via Hibernate

   Technologies Used
   
Java 17+
Hibernate ORM
MySQL
Jakarta Persistence (JPA)
SecureRandom for email codes
Console-based UI (CLI)

   Project Structure
   
aut.ap
├── model       # JPA Entity classes (User, Email, Recipient)
├── service     # Business logic (userService, EmailService)
├── Main.java   # Entry point of the application
└── resources   # Hibernate configuration files (hibernate.cfg.xml)

  To Use:
  
1.Install all the dependencies specified in pom.xml
2.Create a MySQL database and Run the schema SQL to create required tables(as provided).
3.Specify YOUR_DB_NAME, YOUR_USERNAME and YOUR_PASSWORD in the hibernate.cfg.xml.template file, and then remove ".template" from the end of the file. Change the whole database connection url if necessary.
4.Build and run this program
