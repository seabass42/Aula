package com.tutorportal.help;

/**
 * A plain record for the tutor's queue. Kept separate from the entity so the
 * view never touches the lazily-loaded `student` association outside a
 * transaction -- see TopicStat for the same reasoning on the dashboard.
 */
public record HelpRequestView(Long id, String studentName, String message, String reply, HelpRequestStatus status) {

    public boolean isAnswered() {
        return status == HelpRequestStatus.ANSWERED;
    }
}
