package aut.ap.service;

import aut.ap.framework.SingletonSessionFactory;
import aut.ap.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class userService {
    public static void persist(String name, String email, String password) {
        User user = new User(name, email, password);
        SingletonSessionFactory.get()
                .inTransaction(session -> {
                    session.persist(user);
                });
    }

    public static List<User> getAll() {
        return SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createNativeQuery("select * from users", User.class)
                                .getResultList());
    }


    public static void signUp() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Name: ");
        String name = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        User existingUser = findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("Email already exists");
        }

        persist(name, email, password);
        System.out.println("Your new account is created. ");
        System.out.println("Go ahead and login!");
    }


    public static void signIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        User user = findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Welcome back, " + user.getName());
            System.out.println();
            EmailService.showUnreadEmails(user);
            showLoginPage(user);
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }



    public static User findByEmail(String email) {
        return SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createQuery("FROM User WHERE email = :email", User.class)
                                .setParameter("email", email)
                                .uniqueResult()
                );
    }

    public static void showLoginPage(User user) {
        System.out.println("[S]end, [V]iew, [R]eply, [F]orward: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        switch (choice.toUpperCase()){
            case "S" -> {
                System.out.println("Recipients (separated by ','): ");
                String recipients = scanner.nextLine();
                String[] allRecipients = recipients.split(",");

                List<User> recipientUsers = new ArrayList<>();
                for (String recipientEmail : allRecipients) {
                    User recipientUser = findByEmail(recipientEmail.trim());
                    if (recipientUser != null) {
                        recipientUsers.add(recipientUser);
                    } else {
                        System.out.println("User with email '" + recipientEmail + "' not found. Skipping.");
                    }
                }

                System.out.println("Subject: ");
                String subject = scanner.nextLine();
                System.out.println("Body: ");
                String body = scanner.nextLine();

                EmailService.sendEmail(user, recipientUsers, subject, body);
            }
            case "V" -> {
                System.out.println("[A]ll emails, [U]nread emails, [S]ent emails, Read by [C]ode: ");
                String view  = scanner.nextLine();
                switch (view.toUpperCase()){
                    case "A" -> {
                        EmailService.showAllEmails(user);
                        break;
                    }
                    case "U" -> {
                        EmailService.showUnreadEmails(user);
                        break;
                    }
                    case "S" -> {
                        EmailService.showSentEmails(user);
                        break;
                    }
                    case "C" -> {
                        System.out.println("Enter the code: ");
                        String code = scanner.nextLine();
                        EmailService.showEmailByCode(code, user);
                    }

                }
                break;
            }
            case "R" -> {
                break;
            }
            case "F" -> {
                break;
            }
            default -> {
                System.out.println("Invalid choice");
                break;
            }
        }


    }


}
