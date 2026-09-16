package com.tutorportal.problem;

public enum ProblemType {
    TRANSLATE_TO_ES("Translate to Spanish"),
    FILL_BLANK("Fill in the blank"),
    CONJUGATE("Conjugate the verb");

    private final String label;

    ProblemType(String label) { this.label = label; }

    public String getLabel() { return label; }
}
