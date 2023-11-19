package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {

    private static final int CAUSE_TIME = 40;
    private static final int DETAILS_TIME = 6000 + CAUSE_TIME;

    private final Map<String, Death> deathnote;
    private String lastName;
    private long lastDeathTime;
    private long lastCauseTime;

    public DeathNoteImpl() {
        this.deathnote = new HashMap<>();
    }

    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Rule number " + ruleNumber + " is not valid");
        }
        return RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name);
        this.deathnote.put(name, new Death());
        this.lastDeathTime = System.currentTimeMillis();
        this.lastCauseTime = this.lastDeathTime;
        this.lastName = name;
    }

    @Override
    public boolean writeDeathCause(final String cause) {
        Objects.requireNonNull(cause);
        if (this.lastName == null) {
            throw new IllegalStateException("No name written yet");
        }

        if (System.currentTimeMillis() - this.lastDeathTime > CAUSE_TIME) {
            return false;
        }
        this.deathnote.get(lastName).setDeathCause(cause);
        this.lastCauseTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public boolean writeDetails(final String details) {
        Objects.requireNonNull(details);
        if (this.lastName == null) {
            throw new IllegalStateException("No name written yet");
        }

        if (System.currentTimeMillis() - this.lastCauseTime > DETAILS_TIME) {
            return false;
        }
        this.deathnote.get(lastName).setDeathDetails(details);
        return true;
    }

    @Override
    public String getDeathCause(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Given name is not written yet");
        }
        return deathnote.get(name).getDeathCause();
    }

    @Override
    public String getDeathDetails(final String name) {
        if (!isNameWritten(name)) {
            throw new IllegalArgumentException("Given name is not written yet");
        }
        return deathnote.get(name).getDeathDetails();
    }

    @Override
    public boolean isNameWritten(final String name) {
        return this.deathnote.containsKey(name);
    }

    private static final class Death {

        private static final String DEFAULT_CAUSE = "heart attack";
        private String deathCause;
        private String deathDetails;

        private Death(final String cause, final String details) {
            this.deathCause = cause;
            this.deathDetails = details;
        }

        private Death() {
            this(DEFAULT_CAUSE, "");
        }

        public String getDeathCause() {
            return deathCause;
        }

        public String getDeathDetails() {
            return deathDetails;
        }

        public void setDeathCause(String deathCause) {
            this.deathCause = deathCause;
        }

        public void setDeathDetails(String deathDetails) {
            this.deathDetails = deathDetails;
        }
    }
}
