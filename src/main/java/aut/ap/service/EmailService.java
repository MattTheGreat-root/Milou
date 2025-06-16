package aut.ap.service;

import aut.ap.framework.SingletonSessionFactory;
import aut.ap.model.Email;
import aut.ap.model.Recipient;
import aut.ap.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class EmailService {
    public static void sendEmail(User sender, List<User> recipients, String subject, String body) {
        SingletonSessionFactory.get().inTransaction(session -> {

            Email email = new Email(sender, subject, body, LocalDateTime.now());
            session.persist(email);

            for (User recipientUser : recipients) {
                Recipient recipient = new Recipient(email, recipientUser);
                session.persist(recipient);
            }
            System.out.println("Successfully sent email");
            System.out.println("Code: " + email.getEmailCode());
        });
    }

    public static void showUnreadEmails(User user) {
        List<Email> unreadEmails = SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createQuery("""
            SELECT e FROM Email e
            JOIN Recipient r ON r.email = e
            JOIN FETCH e.sender
            WHERE r.recipienUser.id = :userId AND r.isRead = false
            """, Email.class)
                                .setParameter("userId", user.getId())
                                .getResultList()
                );
        System.out.println("Unread emails: ");
        for (Email email : unreadEmails) {
            System.out.println(email);
        }
    }

    public static void showAllEmails(User user) {
        List<Recipient> recipients = SingletonSessionFactory.get()
                .fromTransaction(session -> session.createQuery(
                                """
                                SELECT r FROM Recipient r
                                JOIN FETCH r.email e
                                JOIN FETCH e.sender
                                WHERE r.recipienUser.id = :userId
                                """, Recipient.class)
                        .setParameter("userId", user.getId())
                        .getResultList());

        System.out.println("All received emails:");
        for (Recipient recipient : recipients) {
            Email email = recipient.getEmail();
            System.out.println(email);
        }
    }


    public static void showSentEmails(User user) {
        List<Email> sentEmails = SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createQuery("""
                        SELECT e FROM Email e
                        JOIN FETCH e.sender
                        WHERE e.sender.id = :userId
                        """, Email.class)
                                .setParameter("userId", user.getId())
                                .getResultList()
                );

        System.out.println("Sent emails:");
        for (Email email : sentEmails) {
            System.out.println(email);
        }
    }

    public static void showEmailByCode(String emailCode, User user) {
        Email email = SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createQuery("""
                        FROM Email e
                        WHERE e.emailCode = :code
                          AND (e.sender.id = :userId OR EXISTS (
                              SELECT 1 FROM Recipient r
                              WHERE r.email.id = e.id AND r.recipienUser.id = :userId
                          ))
                        """, Email.class)
                                .setParameter("code", emailCode)
                                .setParameter("userId", user.getId())
                                .uniqueResult()
                );

        if (email == null) {
            throw new RuntimeException("Email with code '" + emailCode + "' not found.");
        }

        List<String> recipients = SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createQuery("""
                        SELECT r.recipienUser.email
                        FROM Recipient r
                        WHERE r.email.id = :emailId
                        """, String.class)
                                .setParameter("emailId", email.getId())
                                .getResultList()
                );

        System.out.println("Code: " + email.getEmailCode());
        System.out.println("Recipient(s): " + String.join(", ", recipients));
        System.out.println("Subject: " + email.getSubject());
        System.out.println("Date: " + email.getSentAt().toLocalDate());
        System.out.println();
        System.out.println(email.getBody());

        SingletonSessionFactory.get().inTransaction(session -> {
            Recipient recipient = session.createQuery("""
            FROM Recipient r
            WHERE r.email.id = :emailId AND r.recipienUser.id = :userId
            """, Recipient.class)
                    .setParameter("emailId", email.getId())
                    .setParameter("userId", user.getId())
                    .uniqueResult();

            if (recipient != null && !recipient.isRead()) {
                recipient.setRead(true);
                session.persist(recipient);
            }
        });
    }

    public static Email findEmailByCode(String emailCode, User user) {
        Email email = SingletonSessionFactory.get()
                .fromTransaction(session ->
                        session.createQuery("""
                        FROM Email e
                        WHERE e.emailCode = :code
                          AND (e.sender.id = :userId OR EXISTS (
                              SELECT 1 FROM Recipient r
                              WHERE r.email.id = e.id AND r.recipienUser.id = :userId
                          ))
                        """, Email.class)
                                .setParameter("code", emailCode)
                                .setParameter("userId", user.getId())
                                .uniqueResult()
                );
        if (email == null){
            System.err.println("Email not found.");
        }
        return email;
    }

    public static void replyToEmail(User sender, String originalEmailCode, String replyBody) {
        Email originalEmail = findEmailByCode(originalEmailCode, sender);
        if (originalEmail == null) {
            throw new RuntimeException("Email not found or you don’t have access to it.");
        }

        String replySubject = "[Re] " + originalEmail.getSubject();
        Email replyEmail = new Email(sender, replySubject, replyBody, LocalDateTime.now());

        SingletonSessionFactory.get().inTransaction(session -> {
            session.persist(replyEmail);

            Recipient recipient = new Recipient(replyEmail, originalEmail.getSender());
            session.persist(recipient);
        });

        System.out.println("Successfully sent your reply to email: " + originalEmailCode);
        System.out.println("Reply Code: " + replyEmail.getEmailCode());
    }

    public static void forwardEmail(User sender, String originalEmailCode, List<User> recipients) {
        Email originalEmail = findEmailByCode(originalEmailCode, sender);
        if (originalEmail == null) {
            throw new RuntimeException("Email not found or you don't have access to it.");
        }

        String forwardSubject = "[Fwd] " + originalEmail.getSubject();
        String forwardBody = originalEmail.getBody();

        Email forwardEmail = new Email(sender, forwardSubject, forwardBody, LocalDateTime.now());

        SingletonSessionFactory.get().inTransaction(session -> {
            session.persist(forwardEmail);

            for (User recipient : recipients) {
                Recipient recipientObj = new Recipient(forwardEmail, recipient);
                session.persist(recipientObj);
            }
        });

        System.out.println("Email forwarded successfully.");
        System.out.println("Email Code: " + forwardEmail.getEmailCode());
    }

}
