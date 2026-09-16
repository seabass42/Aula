package com.tutorportal.web;

import com.tutorportal.attempt.AttemptRepository;
import com.tutorportal.attempt.TopicStat;
import com.tutorportal.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final AttemptRepository attempts;
    private final CurrentUser currentUser;

    public DashboardController(AttemptRepository attempts, CurrentUser currentUser) {
        this.attempts = attempts;
        this.currentUser = currentUser;
    }

    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = currentUser.resolve(principal);

        long total = attempts.countByStudentId(user.getId());
        long correct = attempts.countByStudentIdAndCorrectTrue(user.getId());

        List<TopicStat> topics = attempts.topicBreakdownFor(user.getId()).stream()
                .map(row -> new TopicStat(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).longValue()))
                .toList();

        model.addAttribute("displayName", user.getDisplayName());
        model.addAttribute("totalAttempts", total);
        model.addAttribute("correctAttempts", correct);
        model.addAttribute("accuracy", total == 0 ? 0 : Math.round((100.0 * correct) / total));
        model.addAttribute("topics", topics);

        return "dashboard";
    }
}
