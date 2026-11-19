package userregistrationsystem;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Message {

    private final String messageID;
    private final int messageNumber;
    private String sender;
    private final String recipient;
    private final String message;
    private String messageHash;
    private String messageStatus;

    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<String> usedIDs = new ArrayList<>();

    private static int totalMessages = 0;
    private static int messageCount = 0;

    public Message(String messageID, int messageNumber,
            String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = userregistrationsystem.getFirstName()
                + " " + userregistrationsystem.getLastname();
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    public static void startQuickChat(Scanner input) {

        loadStoredMessages();

        System.out.println("\n==============================");
        System.out.println("      Welcome to QuickChat.");
        System.out.println("==============================");

        boolean running = true;

        while (running) {

            System.out.println("\n==============================");
            System.out.println("          QUICKCHAT");
            System.out.println("==============================");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Stored Messages");
            System.out.println("4. Quit");
            System.out.print("Choose an option: ");

            String option = input.nextLine();

            switch (option) {

                case "1":

                    int numberOfMessages = 0;
                    boolean validNumber = false;

                    while (!validNumber) {

                        System.out.print("How many messages would you like to send? ");

                        String number = input.nextLine();

                        try {

                            numberOfMessages = Integer.parseInt(number);

                            if (numberOfMessages > 0) {

                                validNumber = true;

                            } else {

                                System.out.println("Please enter a number greater than 0.");
                            }

                        } catch (NumberFormatException e) {

                            System.out.println("Please enter a numeric value.");
                        }
                    }

                    sendMessages(input, numberOfMessages);

                    break;

                case "2":

                    System.out.println(printSentMessages());

                    break;

                case "3":

                    storedMessagesMenu(input);

                    break;

                case "4":

                    running = false;

                    System.out.println("Total messages sent: " + totalMessages);
                    System.out.println("Thank you for using QuickChat.");

                    break;

                default:

                    System.out.println("Invalid option. Please select 1, 2, 3 or 4.");
            }
        }
    }

    public static void sendMessages(Scanner input, int numberOfMessages) {

        for (int i = 1; i <= numberOfMessages; i++) {

            messageCount++;

            System.out.println("\n==============================");
            System.out.println("MESSAGE " + messageCount);
            System.out.println("==============================");

            String messageID = generateMessageID();

            System.out.println("Message ID generated: " + messageID);

            String recipient;

            while (true) {

                System.out.print("Enter recipient cellphone number: ");

                recipient = input.nextLine();

                Message temporaryMessage = new Message(
                        messageID, messageCount, recipient, "");

                if (temporaryMessage.checkRecipientCell()) {

                    System.out.println("Cell phone number successfully captured.");
                    break;

                } else {

                    System.out.println("Cell phone number is incorrectly formatted.");
                }
            }

            String messageText;

            while (true) {

                System.out.print("Enter your message: ");

                messageText = input.nextLine();

                if (messageText.length() <= 250 && !messageText.isEmpty()) {

                    break;

                } else {

                    System.out.println("Please send a maximum of 250 characters.");
                }
            }

            Message newMessage = new Message(
                    messageID, messageCount, recipient, messageText);

            System.out.println("Message Hash: "
                    + newMessage.createMessageHash());

            String result = newMessage.SentMessage(input);

            System.out.println("\n" + result);

            if (newMessage.messageStatus.equals("Sent")) {

                System.out.println("\n==============================");
                System.out.println("Message ID: " + newMessage.messageID);
                System.out.println("Message Hash: " + newMessage.messageHash);
                System.out.println("Recipient: " + newMessage.recipient);
                System.out.println("Message: " + newMessage.message);
                System.out.println("==============================");
            }
        }

        System.out.println("\nTotal messages sent: " + totalMessages);
    }

    private static String generateMessageID() {

        Random random = new Random();
        String ID;

        do {

            long number = 1000000000L
                    + (long) (random.nextDouble() * 9000000000L);

            ID = String.valueOf(number);

        } while (usedIDs.contains(ID));

        usedIDs.add(ID);

        return ID;
    }

    public boolean checkMessageID() {

        return messageID.length() == 10;
    }

    public boolean checkRecipientCell() {

        return recipient.matches("^\\+\\d{11,13}$")
                || recipient.matches("^0\\d{9}$");
    }

    public String checkMessageLength() {

        if (message.length() <= 250 && !message.isEmpty()) {

            return "Message ready to send.";

        } else {

            return "Please send a maximum of 250 characters.";
        }
    }

    public String createMessageHash() {

        if (message.trim().isEmpty()) {

            return "";
        }

        String[] words = message.trim().split("\\s+");

        String first = words[0];
        String last = words[words.length - 1];

        messageHash = messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + first + last;

        return messageHash.toUpperCase();
    }

    public String SentMessage(Scanner input) {

        while (true) {

            System.out.println("\n1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Choose an option: ");

            String option = input.nextLine();

            if (option.equals("1")) {

                messageStatus = "Sent";
                sentMessages.add(this);
                totalMessages++;

                return "Message successfully sent";

            } else if (option.equals("2")) {

                messageStatus = "Disregarded";
                disregardedMessages.add(this);

                return "Message disregarded";

            } else if (option.equals("3")) {

                messageStatus = "Stored";
                storedMessages.add(this);
                storeMessage();

                return "Message successfully stored";

            } else {

                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static String printSentMessages() {

        StringBuilder result = new StringBuilder();

        for (Message item : sentMessages) {

            result.append("Message ID: ").append(item.messageID).append("\n");
            result.append("Message Hash: ").append(item.messageHash).append("\n");
            result.append("Recipient: ").append(item.recipient).append("\n");
            result.append("Message: ").append(item.message).append("\n\n");
        }

        if (result.length() == 0) {

            return "No messages sent.";

        } else {

            return result.toString();
        }
    }

    public int returnTotalMessages() {

        return totalMessages;
    }

    public static void storedMessagesMenu(Scanner input) {

        boolean running = true;

        while (running) {

            System.out.println("\n==============================");
            System.out.println("        STORED MESSAGES");
            System.out.println("==============================");
            System.out.println("1. Display all stored senders and recipients");
            System.out.println("2. Display longest stored message");
            System.out.println("3. Search message using Message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete message using Message Hash");
            System.out.println("6. Display complete stored messages report");
            System.out.println("7. Return to main menu");
            System.out.print("Choose an option: ");

            String option = input.nextLine();

            switch (option) {

                case "1":

                    displaySendersAndRecipients();

                    break;

                case "2":

                    displayLongestMessage();

                    break;

                case "3":

                    System.out.print("Enter Message ID: ");
                    String ID = input.nextLine();

                    searchMessageID(ID);

                    break;

                case "4":

                    System.out.print("Enter recipient cellphone number: ");
                    String recipient = input.nextLine();

                    searchRecipient(recipient);

                    break;

                case "5":

                    System.out.print("Enter Message Hash: ");
                    String hash = input.nextLine();

                    deleteMessage(hash);

                    break;

                case "6":

                    displayStoredReport();

                    break;

                case "7":

                    running = false;

                    break;

                default:

                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void displaySendersAndRecipients() {

        if (storedMessages.isEmpty()) {

            System.out.println("No stored messages available.");

            return;
        }

        for (Message item : storedMessages) {

            System.out.println("Sender: " + item.sender);
            System.out.println("Recipient: " + item.recipient);
            System.out.println("------------------------------");
        }
    }

    public static void displayLongestMessage() {

        if (storedMessages.isEmpty()) {

            System.out.println("No stored messages available.");

            return;
        }

        Message longest = storedMessages.get(0);

        for (Message item : storedMessages) {

            if (item.message.length() > longest.message.length()) {

                longest = item;
            }
        }

        System.out.println("Longest Stored Message:");
        System.out.println(longest.message);
        System.out.println("Characters: " + longest.message.length());
    }

    public static void searchMessageID(String ID) {

        boolean found = false;

        for (Message item : storedMessages) {

            if (item.messageID.equals(ID)) {

                System.out.println("Message ID: " + item.messageID);
                System.out.println("Recipient: " + item.recipient);
                System.out.println("Message: " + item.message);

                found = true;
            }
        }

        if (!found) {

            System.out.println("Message ID not found.");
        }
    }

    public static void searchRecipient(String recipient) {

        boolean found = false;

        for (Message item : storedMessages) {

            if (item.recipient.equals(recipient)) {

                System.out.println("Message ID: " + item.messageID);
                System.out.println("Recipient: " + item.recipient);
                System.out.println("Message: " + item.message);
                System.out.println("------------------------------");

                found = true;
            }
        }

        if (!found) {

            System.out.println("No messages found for this recipient.");
        }
    }

    public static void deleteMessage(String hash) {

        boolean found = false;

        for (int i = 0; i < storedMessages.size(); i++) {

            if (storedMessages.get(i).messageHash.equals(hash)) {

                storedMessages.remove(i);

                storeMessage();

                System.out.println("Message successfully deleted.");

                found = true;

                break;
            }
        }

        if (!found) {

            System.out.println("Message Hash not found.");
        }
    }

    public static void displayStoredReport() {

        if (storedMessages.isEmpty()) {

            System.out.println("No stored messages available.");

            return;
        }

        System.out.println("\n========== STORED MESSAGE REPORT ==========");

        for (Message item : storedMessages) {

            System.out.println("Sender: " + item.sender);
            System.out.println("Recipient: " + item.recipient);
            System.out.println("Message ID: " + item.messageID);
            System.out.println("Message Hash: " + item.messageHash);
            System.out.println("Message: " + item.message);
            System.out.println("Status: " + item.messageStatus);
            System.out.println("-------------------------------------------");
        }
    }

    public static void storeMessage() {

        try {

            FileWriter writer = new FileWriter("storedMessages.json");

            writer.write("[\n");

            for (int i = 0; i < storedMessages.size(); i++) {

                Message item = storedMessages.get(i);

                writer.write("  {\n");
                writer.write("    \"messageID\": \"" + item.messageID + "\",\n");
                writer.write("    \"messageNumber\": \"" + item.messageNumber + "\",\n");
                writer.write("    \"sender\": \"" + escape(item.sender) + "\",\n");
                writer.write("    \"messageHash\": \"" + escape(item.messageHash) + "\",\n");
                writer.write("    \"recipient\": \"" + escape(item.recipient) + "\",\n");
                writer.write("    \"message\": \"" + escape(item.message) + "\",\n");
                writer.write("    \"messageStatus\": \"" + item.messageStatus + "\"\n");
                writer.write("  }");

                if (i < storedMessages.size() - 1) {

                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("]\n");
            writer.close();

            System.out.println("Stored messages saved successfully.");

        } catch (IOException e) {

            System.out.println("Error storing message.");
        }
    }

    public static void loadStoredMessages() {

        storedMessages.clear();

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("storedMessages.json"));

            StringBuilder data = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                data.append(line);
            }

            reader.close();

            String json = data.toString();

            int position = 0;

            while ((position = json.indexOf("{", position)) != -1) {

                int end = findObjectEnd(json, position);

                if (end == -1) {

                    break;
                }

                String object = json.substring(position, end + 1);

                String ID = readJsonValue(object, "messageID");
                String number = readJsonValue(object, "messageNumber");
                String sender = readJsonValue(object, "sender");
                String hash = readJsonValue(object, "messageHash");
                String recipient = readJsonValue(object, "recipient");
                String message = readJsonValue(object, "message");
                String status = readJsonValue(object, "messageStatus");

                if (ID != null && recipient != null && message != null) {

                    int messageNo = messageCount + 1;

                    if (number != null && !number.isEmpty()) {

                        try {

                            messageNo = Integer.parseInt(number);

                        } catch (NumberFormatException e) {

                            messageNo = messageCount + 1;
                        }
                    }

                    Message item = new Message(
                            ID,
                            messageNo,
                            recipient,
                            message);

                    if (sender != null) {

                        item.sender = sender;
                    }

                    if (hash != null) {

                        item.messageHash = hash;
                    }

                    if (status != null) {

                        item.messageStatus = status;

                    } else {

                        item.messageStatus = "Stored";
                    }

                    storedMessages.add(item);

                    if (!usedIDs.contains(ID)) {

                        usedIDs.add(ID);
                    }

                    if (messageNo > messageCount) {

                        messageCount = messageNo;
                    }
                }

                position = end + 1;
            }

            if (!storedMessages.isEmpty()) {

                System.out.println("Stored messages loaded successfully.");
            }

        } catch (IOException e) {

            System.out.println("No previous stored messages found.");
        }
    }

    private static int findObjectEnd(String json, int start) {

        int brackets = 0;
        boolean insideString = false;
        boolean escaped = false;

        for (int i = start; i < json.length(); i++) {

            char current = json.charAt(i);

            if (escaped) {

                escaped = false;

            } else if (current == '\\') {

                escaped = true;

            } else if (current == '"') {

                insideString = !insideString;

            } else if (!insideString) {

                if (current == '{') {

                    brackets++;

                } else if (current == '}') {

                    brackets--;

                    if (brackets == 0) {

                        return i;
                    }
                }
            }
        }

        return -1;
    }

    private static String readJsonValue(String object, String key) {

        String search = "\"" + key + "\"";

        int position = object.indexOf(search);

        if (position == -1) {

            return null;
        }

        position = object.indexOf(":", position);

        if (position == -1) {

            return null;
        }

        position++;

        while (position < object.length()
                && Character.isWhitespace(object.charAt(position))) {

            position++;
        }

        if (position >= object.length()
                || object.charAt(position) != '"') {

            return null;
        }

        position++;

        StringBuilder value = new StringBuilder();

        boolean escaped = false;

        while (position < object.length()) {

            char character = object.charAt(position);

            if (escaped) {

                value.append(character);
                escaped = false;

            } else if (character == '\\') {

                value.append(character);
                escaped = true;

            } else if (character == '"') {

                break;

            } else {

                value.append(character);
            }

            position++;
        }

        return unescape(value.toString());
    }

    private static String unescape(String text) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            char current = text.charAt(i);

            if (current == '\\' && i + 1 < text.length()) {

                i++;

                char next = text.charAt(i);

                if (next == 'n') {

                    result.append('\n');

                } else if (next == 'r') {

                    result.append('\r');

                } else if (next == 't') {

                    result.append('\t');

                } else {

                    result.append(next);
                }

            } else {

                result.append(current);
            }
        }

        return result.toString();
    }

    private static String escape(String text) {

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}