package aut.ap.model;

import jakarta.persistence.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;
    private String subject;
    @Basic(optional = false)
    private String body;
    private LocalDateTime sentAt;
    @Basic(optional = false)
    private String emailCode;

    public Email() {}
    public Email(Integer id, User sender_id, String subject, String body, LocalDateTime sentAt) {
        this.id = id;
        this.sender = sender_id;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
        setEmailCode(getEmailCode());
    }

    public Integer getId() {
        return id;
    }
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
    public String getEmailCode() {
        return emailCode;
    }
    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    private static String generateEmailCode() {
        final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
        final int CODE_LENGTH = 6;
        final SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
