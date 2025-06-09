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
        });
    }
}
