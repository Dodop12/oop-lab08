package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

public class TestDeathNote {

    private static final int DETAILS_OVERTIME_WAIT = 6500;
    private static final int CAUSE_OVERTIME_WAIT = 100;
    private final String testName = "Light";
    private final String testName2 = "Pippo";

    private DeathNote dNote;

    @BeforeEach
    public void setUp() {
        this.dNote = new DeathNoteImpl();
    }

    @Test
    public void testRuleNumberExistance() {
        try {
            this.dNote.getRule(0);
            Assertions.fail("Exception was expected, but not thrown"); // The test fails if no exceptions are thrown
        } catch (final IllegalArgumentException e) {
            checkExceptionMessage(e);
        }

        try {
            this.dNote.getRule(-1);
            Assertions.fail("Exception was expected, but not thrown");
        } catch (final IllegalArgumentException e) {
            checkExceptionMessage(e);
        }

        try {
            this.dNote.getRule(DeathNote.RULES.size() + 1);
            Assertions.fail("Exception was expected, but not thrown");
        } catch (final IllegalArgumentException e) {
            checkExceptionMessage(e);
        }
    }

    @Test
    public void ruleValidity() {
        for (int i = 1; i <= DeathNote.RULES.size(); i++) {
            final var rule = dNote.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    @Test
    public void testDeath() {
        assertFalse(this.dNote.isNameWritten(testName));
        this.dNote.writeName(testName);
        assertTrue(this.dNote.isNameWritten(testName));
        assertFalse(this.dNote.isNameWritten(testName2));
        assertFalse(this.dNote.isNameWritten(""));
    }

    @Test
    public void testDeathCause() throws InterruptedException {
        try {
            this.dNote.writeDeathCause("burns");
            Assertions.fail("Exception was expected, but not thrown");
        } catch (final IllegalStateException e) {
            checkExceptionMessage(e);

            this.dNote.writeName(testName);
            assertEquals("heart attack", dNote.getDeathCause(testName));
            this.dNote.writeName(testName2);

            assertTrue(this.dNote.writeDeathCause("karting accident"));
            assertEquals("karting accident", dNote.getDeathCause(testName));

            Thread.sleep(CAUSE_OVERTIME_WAIT); // Pauses the program: the method must declare "throws
                                               // InterruptException"

            assertTrue(this.dNote.writeDeathCause("suicide"));
            assertEquals("karting accident", dNote.getDeathCause(testName));
        }
    }

    @Test
    public void testDeathDetails() throws InterruptedException {
        try {
            this.dNote.writeDetails("fire caused by a metheor impact");
            Assertions.fail("Exception was expected, but not thrown");
        } catch (final IllegalStateException e) {
            checkExceptionMessage(e);

            this.dNote.writeName(testName);
            assertEquals("", this.dNote.getDeathDetails(testName));

            assertTrue(this.dNote.writeDetails("ran for too long"));
            assertEquals("ran for too long", this.dNote.getDeathDetails(testName));

            this.dNote.writeName(testName2);

            Thread.sleep(DETAILS_OVERTIME_WAIT);
            assertFalse(this.dNote.writeDeathCause("rocket explosion"));
            assertEquals("ran for too long", this.dNote.getDeathDetails(testName));
        }
    }

    private static void checkExceptionMessage(final Exception e) {
        final var exceptionMessage = e.getMessage();
        assertNotNull(exceptionMessage);
        assertFalse(exceptionMessage.isBlank() || exceptionMessage.isEmpty());
    }
}