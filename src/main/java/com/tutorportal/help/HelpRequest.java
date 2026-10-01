package com.tutorportal.help;

import com.tutorportal.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "help_requests")
public class HelpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private User student;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    @Column(columnDefinition = "text")
    private String reply;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HelpRequestStatus status = HelpRequestStatus.OPEN;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "replied_at")
    private Instant repliedAt;

    protected HelpRequest() { }

    public HelpRequest(User student, String message) {
        this.student = student;
        this.message = message;
    }

    /** Answers the request and flips it to ANSWERED in one step, so the two can't drift apart. */
    public void answer(String reply) {
        this.reply = reply;
        this.status = HelpRequestStatus.ANSWERED;
        this.repliedAt = Instant.now();
    }

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public String getMessage() { return message; }
    public String getReply() { return reply; }
    public HelpRequestStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getRepliedAt() { return repliedAt; }

    public boolean isAnswered() { return status == HelpRequestStatus.ANSWERED; }
}
