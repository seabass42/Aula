package com.tutorportal.problem;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Compares a submitted answer to the expected one.
 *
 * Spanish-specific detail: an answer that is right except for missing accents
 * is not the same kind of wrong as an answer that is just wrong, so it gets its
 * own verdict and its own feedback.
 */
@Component
public class AnswerGrader {

    public enum Verdict {
        CORRECT,
        MISSING_ACCENTS,
        INCORRECT
    }

    public Verdict grade(String expected, String submitted) {
        if (submitted == null || submitted.isBlank()) {
            return Verdict.INCORRECT;
        }

        String cleanExpected = collapse(expected);
        String cleanSubmitted = collapse(submitted);

        if (cleanExpected.equals(cleanSubmitted)) {
            return Verdict.CORRECT;
        }
        if (stripAccents(cleanExpected).equals(stripAccents(cleanSubmitted))) {
            return Verdict.MISSING_ACCENTS;
        }
        return Verdict.INCORRECT;
    }

    /** Lowercase, trim, collapse inner whitespace, drop trailing punctuation. */
    private String collapse(String raw) {
        return raw.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[.!?]+$", "");
    }

    /**
     * NFD splits "á" into "a" + combining acute, so the regex can drop the mark.
     * Note this deliberately leaves "ñ" alone -- in Spanish, n and ñ are
     * different letters, not an accent variation.
     */
    private String stripAccents(String input) {
        String protectedEnye = input.replace("ñ", "\u0001").replace("Ñ", "\u0002");
        String decomposed = Normalizer.normalize(protectedEnye, Normalizer.Form.NFD);
        String stripped = decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return stripped.replace("\u0001", "ñ").replace("\u0002", "Ñ");
    }
}
