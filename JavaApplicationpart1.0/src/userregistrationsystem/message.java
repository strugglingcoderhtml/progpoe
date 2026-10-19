
package userregistrationsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String messageStatus;

    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<String> usedIDs = new ArrayList<>();

    private static int totalMessages = 0;
    private static int messageCount = 0;

    public Message(String messageID, int messageNumber,
            String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    public static void startQuickChat(Scanner input) {

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
            System.out.println("3. Quit");
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

                    System.out.println("\nComing Soon.");

                    break;

                case "3":

                    running = false;

                    System.out.println("Total messages sent: " + totalMessages);
                    System.out.println("Thank you for using QuickChat.");

                    break;

                default:

                    System.out.println("Invalid option. Please select 1, 2 or 3.");
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
                    + (long)(random.nextDouble() * 9000000000L);

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

    public String printMessages() {

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

    public void storeMessage() {

        try {

            FileWriter writer = new FileWriter("storedMessages.json");

            writer.write("[\n");

            for (int i = 0; i < storedMessages.size(); i++) {

                Message item = storedMessages.get(i);

                writer.write("  {\n");
                writer.write("    \"messageID\": \"" + item.messageID + "\",\n");
                writer.write("    \"messageHash\": \"" + item.messageHash + "\",\n");
                writer.write("    \"recipient\": \"" + item.recipient + "\",\n");
                writer.write("    \"message\": \"" + escape(item.message) + "\"\n");
                writer.write("  }");

                if (i < storedMessages.size() - 1) {

                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("]\n");
            writer.close();

        } catch (IOException e) {

            System.out.println("Error storing message.");
        }
    }

    private String escape(String text) {

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
