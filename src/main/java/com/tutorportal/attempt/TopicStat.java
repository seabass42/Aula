package com.tutorportal.attempt;

/**
 * A plain record for the dashboard. Kept separate from the entity so the view
 * never touches a lazily-loaded JPA object outside a transaction -- that is the
 * classic LazyInitializationException you will otherwise hit in Thymeleaf.
 */
public record TopicStat(String topic, long attempts, long correct) {

    public int accuracyPercent() {
        return attempts == 0 ? 0 : (int) Math.round((100.0 * correct) / attempts);
    }
}
