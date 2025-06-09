package aut.ap.service;

import aut.ap.framework.SingletonSessionFactory;
import aut.ap.model.User;

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
            unreadEmails();
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

    public static void unreadEmails() {}
}
