package com.tutorportal.problem;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String prompt;

    @Column(name = "correct_answer", nullable = false, length = 300)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProblemType type;

    @Column(nullable = false)
    private String topic;

    @Column(length = 500)
    private String hint;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected Problem() { }

    public Problem(String prompt, String correctAnswer, ProblemType type, String topic, String hint) {
        this.prompt = prompt;
        this.correctAnswer = correctAnswer;
        this.type = type;
        this.topic = topic;
        this.hint = hint;
    }

    public Long getId() { return id; }
    public String getPrompt() { return prompt; }
    public String getCorrectAnswer() { return correctAnswer; }
    public ProblemType getType() { return type; }
    public String getTopic() { return topic; }
    public String getHint() { return hint; }
}
