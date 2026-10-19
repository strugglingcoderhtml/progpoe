
package userregistrationsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageIDSuccess() {

        Message message = new Message(
                "1234567890", 1, "+27718693002", "Hello");

        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageIDFailure() {

        Message message = new Message(
                "12345678901", 1, "+27718693002", "Hello");

        assertFalse(message.checkMessageID());
    }

    @Test
    public void testRecipientSuccess() {

        Message message = new Message(
                "1234567890", 1, "+27718693002", "Hello");

        assertTrue(message.checkRecipientCell());
    }

    @Test
    public void testRecipientFailure() {

        Message message = new Message(
                "1234567890", 1, "12345", "Hello");

        assertFalse(message.checkRecipientCell());
    }

    @Test
    public void testMessageLengthSuccess() {

        Message message = new Message(
                "1234567890", 1, "+27718693002", "Hello");

        assertEquals("Message ready to send.",
                message.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {

        String text = "A".repeat(251);

        Message message = new Message(
                "1234567890", 1, "+27718693002", text);

        assertEquals("Please send a maximum of 250 characters.",
                message.checkMessageLength());
    }

    @Test
    public void testMessageHash() {

        Message message = new Message(
                "1234567890", 1, "+27718693002",
                "Hi Mike how are you");

        assertEquals("12:1:HIYOU",
                message.createMessageHash());
    }

    @Test
    public void testTotalMessagesInitiallyZero() {

        Message message = new Message(
                "1234567890", 1, "+27718693002", "Hello");

        assertEquals(0, message.returnTotalMessages());
    }
}
