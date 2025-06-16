package aut.ap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recipients")
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id")
    private Email email;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_user_id")
    private User recipienUser;
    @Basic(optional = false)
    @Column(name = "is_read")
    private boolean isRead = false;

    public Recipient() {}
    public Recipient(Email email, User recipienUser) {
        this.email = email;
        this.recipienUser = recipienUser;
    }

    public Integer getId() {
        return id;
    }
    public User getRecipienUser() {
        return recipienUser;
    }
    public void setRecipienUser(User recipienUser) {
        this.recipienUser = recipienUser;
    }
    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }
    public boolean isRead() {
        return isRead;
    }
    public void setRead(boolean read) {
        isRead = read;
    }


}
