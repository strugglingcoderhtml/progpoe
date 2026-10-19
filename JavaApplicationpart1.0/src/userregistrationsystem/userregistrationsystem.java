
package userregistrationsystem;

import java.util.Scanner;
import userregistrationsystem.Login;

public class userregistrationsystem {

    private static String userName;
    private static String password;
    private static String cellPhoneNumber;
    private static String firstName;
    private static String lastName;

    public static void setFirstName(String fname) {
        firstName = fname;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setUserName(String username) {
        userName = username;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setPassword(String passw0rd) {
        password = passw0rd;
    }

    public static String getPassword() {
        return password;
    }

    public static void setLastname(String lname) {
        lastName = lname;
    }

    public static String getLastname() {
        return lastName;
    }

    public static void setCellNumber(String cellphonenumber) {
        cellPhoneNumber = cellphonenumber;
    }

    public static String getCellNumber() {
        return cellPhoneNumber;
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("***** User Registration *****");

        System.out.print("Please enter your first name: ");
        firstName = input.nextLine();

        if (firstName.isEmpty()) {
            System.out.println("First name cannot be empty.");
        } else {

            System.out.print("Please enter your last name: ");
            lastName = input.nextLine();

            if (lastName.isEmpty()) {
                System.out.println("Last name cannot be empty.");
            } else {

                Login login = new Login(firstName, lastName);

                System.out.print("Enter your username (no more than 5 characters and must contain an underscore): ");
                userName = input.nextLine();

                if (!login.checkUserName(userName)) {

                    System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");

                } else {

                    System.out.println("Username successfully captured.");

                    System.out.print("Create a password (at least 8 characters, 1 capital letter, 1 number, 1 special character): ");
                    password = input.nextLine();

                    if (!login.checkPasswordComplexity(password)) {

                        System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");

                    } else {

                        System.out.println("Password successfully captured.");

                        System.out.print("Please enter cellphone number with international country code: ");
                        cellPhoneNumber = input.nextLine();

                        if (!login.checkCellPhoneNumber(cellPhoneNumber)) {

                            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");

                        } else {

                            System.out.println("Cell phone number successfully added.");

                            String regStatus = login.registerUser(
                                    userName,
                                    password,
                                    cellPhoneNumber,
                                    firstName,
                                    lastName
                            );

                            System.out.println(regStatus);

                            if (regStatus.equals("Registration successful.")) {

                                System.out.println();
                                System.out.println("***** User Login *****");

                                System.out.print("Enter the username you created: ");
                                String loginUser = input.nextLine();

                                System.out.print("Enter the password you created: ");
                                String loginPass = input.nextLine();

                                String loginStatus = login.returnLoginStatus(
                                        loginUser,
                                        loginPass
                                );

                                System.out.println(loginStatus);

                                if (login.loginUser(loginUser, loginPass)) {

                                    Message.startQuickChat(input);

                                }
                            }
                        }
                    }
                }
            }
        }

        input.close();
    }
}
