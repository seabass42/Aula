package com.tutorportal.web;

import com.tutorportal.attempt.Attempt;
import com.tutorportal.attempt.AttemptRepository;
import com.tutorportal.problem.AnswerGrader;
import com.tutorportal.problem.Problem;
import com.tutorportal.problem.ProblemRepository;
import com.tutorportal.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/practice")
public class PracticeController {

    private final ProblemRepository problems;
    private final AttemptRepository attempts;
    private final AnswerGrader grader;
    private final CurrentUser currentUser;

    public PracticeController(ProblemRepository problems,
                              AttemptRepository attempts,
                              AnswerGrader grader,
                              CurrentUser currentUser) {
        this.problems = problems;
        this.attempts = attempts;
        this.grader = grader;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String next(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = currentUser.resolve(principal);

        return problems.findNextUnmasteredFor(user.getId())
                .map(problem -> {
                    model.addAttribute("problem", problem);
                    return "practice";
                })
                .orElse("all-done");
    }

    @PostMapping("/{id}")
    public String submit(@PathVariable Long id,
                         @RequestParam String answer,
                         @AuthenticationPrincipal UserDetails principal,
                         Model model) {

        User user = currentUser.resolve(principal);
        Problem problem = problems.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No problem with id " + id));

        AnswerGrader.Verdict verdict = grader.grade(problem.getCorrectAnswer(), answer);
        boolean correct = verdict == AnswerGrader.Verdict.CORRECT;

        attempts.save(new Attempt(user, problem, answer, correct));

        model.addAttribute("problem", problem);
        model.addAttribute("submitted", answer);
        model.addAttribute("verdict", verdict.name());
        model.addAttribute("expected", problem.getCorrectAnswer());

        return "practice";
    }
}
