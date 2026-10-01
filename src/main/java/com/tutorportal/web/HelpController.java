package com.tutorportal.web;

import com.tutorportal.help.HelpRequest;
import com.tutorportal.help.HelpRequestRepository;
import com.tutorportal.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Lets a student ask the tutor a question that auto-grading can't answer, and
 * see the tutor's reply once it comes in.
 */
@Controller
@RequestMapping("/help")
public class HelpController {

    private final HelpRequestRepository helpRequests;
    private final CurrentUser currentUser;

    public HelpController(HelpRequestRepository helpRequests, CurrentUser currentUser) {
        this.helpRequests = helpRequests;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = currentUser.resolve(principal);
        model.addAttribute("requests", helpRequests.findByStudentIdOrderByCreatedAtDesc(user.getId()));
        return "help";
    }

    @PostMapping
    public String submit(@RequestParam String message, @AuthenticationPrincipal UserDetails principal) {
        User user = currentUser.resolve(principal);
        helpRequests.save(new HelpRequest(user, message));
        return "redirect:/help";
    }
}
