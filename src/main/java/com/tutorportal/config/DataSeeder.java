package com.tutorportal.config;

import com.tutorportal.problem.Problem;
import com.tutorportal.problem.ProblemRepository;
import com.tutorportal.problem.ProblemType;
import com.tutorportal.user.Role;
import com.tutorportal.user.User;
import com.tutorportal.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Creates a tutor account, a student account, and starter problems on first run.
 * Seeding here rather than in SQL means the passwords go through the real
 * BCrypt encoder instead of a hash pasted into a migration file.
 *
 * @Profile("dev") keeps this out of production. Delete the accounts before you
 * put this on the public internet.
 */
@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final ProblemRepository problems;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository users, ProblemRepository problems, PasswordEncoder encoder) {
        this.users = users;
        this.problems = problems;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!users.existsByEmail("tutor@example.com")) {
            users.save(new User("tutor@example.com", encoder.encode("changeme"), "Sebastian", Role.TUTOR));
        }
        if (!users.existsByEmail("student@example.com")) {
            users.save(new User("student@example.com", encoder.encode("changeme"), "Test Student", Role.STUDENT));
        }

        if (problems.count() == 0) {
            problems.saveAll(List.of(
                new Problem("I want to travel to Spain next year.",
                        "Quiero viajar a España el año que viene",
                        ProblemType.TRANSLATE_TO_ES, "Present tense", "querer + infinitive"),
                new Problem("She has been studying for three hours.",
                        "Ella lleva tres horas estudiando",
                        ProblemType.TRANSLATE_TO_ES, "Duration", "llevar + time + gerund"),
                new Problem("Ayer nosotros ___ (comer) paella en la playa.",
                        "comimos",
                        ProblemType.FILL_BLANK, "Preterite", "Regular -er verb, nosotros form"),
                new Problem("Conjugate 'tener' in the yo form, present tense.",
                        "tengo",
                        ProblemType.CONJUGATE, "Irregular verbs", "It is a go-verb"),
                new Problem("Conjugate 'ser' in the ellos form, imperfect tense.",
                        "eran",
                        ProblemType.CONJUGATE, "Imperfect", "One of only three irregular imperfects"),
                new Problem("If I had money, I would buy a house.",
                        "Si tuviera dinero, compraría una casa",
                        ProblemType.TRANSLATE_TO_ES, "Subjunctive", "Past subjunctive + conditional")
            ));
        }
    }
}
