package com.tutorportal.attempt;

import com.tutorportal.problem.Problem;
import com.tutorportal.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "attempts")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "submitted_answer", nullable = false, length = 300)
    private String submittedAnswer;

    @Column(nullable = false)
    private boolean correct;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected Attempt() { }

    public Attempt(User student, Problem problem, String submittedAnswer, boolean correct) {
        this.student = student;
        this.problem = problem;
        this.submittedAnswer = submittedAnswer;
        this.correct = correct;
    }

    public Long getId() { return id; }
    public User getStudent() { return student; }
    public Problem getProblem() { return problem; }
    public String getSubmittedAnswer() { return submittedAnswer; }
    public boolean isCorrect() { return correct; }
    public Instant getCreatedAt() { return createdAt; }
}
