package com.tutorportal.web;

import com.tutorportal.problem.Problem;
import com.tutorportal.problem.ProblemRepository;
import com.tutorportal.problem.ProblemType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Lets a tutor manage the problem bank without editing SQL by hand.
 * Restricted to ROLE_TUTOR by SecurityConfig's /tutor/** rule.
 */
@Controller
@RequestMapping("/tutor/problems")
public class TutorProblemController {

    private final ProblemRepository problems;

    public TutorProblemController(ProblemRepository problems) {
        this.problems = problems;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long edit, Model model) {
        model.addAttribute("problems", problems.findAll(Sort.by("topic", "id")));
        model.addAttribute("types", ProblemType.values());

        if (edit != null) {
            model.addAttribute("editing", problems.findById(edit)
                    .orElseThrow(() -> new IllegalArgumentException("No problem with id " + edit)));
        }

        return "tutor-problems";
    }

    @PostMapping
    public String create(@RequestParam String prompt,
                         @RequestParam String correctAnswer,
                         @RequestParam ProblemType type,
                         @RequestParam String topic,
                         @RequestParam(required = false) String hint) {

        problems.save(new Problem(prompt, correctAnswer, type, topic, blankToNull(hint)));
        return "redirect:/tutor/problems";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String prompt,
                         @RequestParam String correctAnswer,
                         @RequestParam ProblemType type,
                         @RequestParam String topic,
                         @RequestParam(required = false) String hint) {

        Problem problem = problems.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No problem with id " + id));

        problem.setPrompt(prompt);
        problem.setCorrectAnswer(correctAnswer);
        problem.setType(type);
        problem.setTopic(topic);
        problem.setHint(blankToNull(hint));
        problems.save(problem);

        return "redirect:/tutor/problems";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            problems.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // Students have already attempted this problem; attempts.problem_id has no
            // cascade, so the FK blocks the delete rather than silently orphaning rows.
            redirectAttributes.addFlashAttribute("error",
                    "Can't delete that problem — students have already answered it.");
        }
        return "redirect:/tutor/problems";
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
