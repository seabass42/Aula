package com.tutorportal.web;

import com.tutorportal.help.HelpRequest;
import com.tutorportal.help.HelpRequestRepository;
import com.tutorportal.help.HelpRequestStatus;
import com.tutorportal.help.HelpRequestView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The tutor's queue of student questions. Restricted to ROLE_TUTOR by
 * SecurityConfig's /tutor/** rule.
 */
@Controller
@RequestMapping("/tutor/help")
public class TutorHelpController {

    private final HelpRequestRepository helpRequests;

    public TutorHelpController(HelpRequestRepository helpRequests) {
        this.helpRequests = helpRequests;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("open", toViews(
                helpRequests.findRowsByStatusOrderByCreatedAtAsc(HelpRequestStatus.OPEN)));
        model.addAttribute("answered", toViews(
                helpRequests.findRowsByStatusOrderByCreatedAtDesc(HelpRequestStatus.ANSWERED)));
        return "tutor-help";
    }

    @PostMapping("/{id}/reply")
    public String reply(@PathVariable Long id, @RequestParam String reply) {
        HelpRequest helpRequest = helpRequests.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No help request with id " + id));

        helpRequest.answer(reply);
        helpRequests.save(helpRequest);

        return "redirect:/tutor/help";
    }

    private static List<HelpRequestView> toViews(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new HelpRequestView(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (HelpRequestStatus) row[4]))
                .toList();
    }
}
