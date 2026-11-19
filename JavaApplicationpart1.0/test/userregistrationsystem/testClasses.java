package userregistrationsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class testClasses {

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

        String text = new String(new char[251]).replace('\0', 'A');

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
    public void testMessage1Sent() {

        Message message = new Message(
                "1234567891", 1, "+27834557896",
                "Did you get the cake?");

        String result = message.SentMessage(
                new java.util.Scanner("1\n"));

        assertEquals("Message successfully sent", result);
    }

    @Test
    public void testMessage2Stored() {

        Message message = new Message(
                "1234567892", 2, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");

        String result = message.SentMessage(
                new java.util.Scanner("3\n"));

        assertEquals("Message successfully stored", result);
    }

    @Test
    public void testMessage3Disregarded() {

        Message message = new Message(
                "1234567893", 3, "+27834484567",
                "Yohoooo, 1 am at your gate.");

        String result = message.SentMessage(
                new java.util.Scanner("2\n"));

        assertEquals("Message disregarded", result);
    }

    @Test
    public void testMessage4Sent() {

        Message message = new Message(
                "1234567894", 4, "0838884567",
                "It is dinner time!");

        assertTrue(message.checkRecipientCell());

        String result = message.SentMessage(
                new java.util.Scanner("1\n"));

        assertEquals("Message successfully sent", result);
    }

    @Test
    public void testMessage5Stored() {

        Message message = new Message(
                "1234567895", 5, "+27838884567",
                "Ok, 1 am leaving without you.");

        String result = message.SentMessage(
                new java.util.Scanner("3\n"));

        assertEquals("Message successfully stored", result);
    }
}