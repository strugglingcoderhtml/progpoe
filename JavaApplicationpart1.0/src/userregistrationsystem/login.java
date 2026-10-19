package userregistrationsystem;

public class Login {

    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Variables used to check the login details
    private String storedUsername;
    private String storedPassword;

    public Login(String fname, String lname) {
        this.firstName = fname;
        this.lastName = lname;
    }

    // Checks that the username has an underscore
    // and is no more than five characters
    public boolean checkUserName(String username) {

        return username.contains("_") && username.length() <= 5;
    }

    // Checks the password complexity
    public boolean checkPasswordComplexity(String password) {

        boolean passwordOkay = false;
        boolean hasNumber = false;
        boolean hasCap = false;
        boolean hasSpecial = false;
        char current;

        if (password.length() >= 8) {

            for (int i = 0; i < password.length(); i++) {

                current = password.charAt(i);

                if (Character.isUpperCase(current)) {
                    hasCap = true;
                }

                if (Character.isDigit(current)) {
                    hasNumber = true;
                }

                if (!Character.isLetterOrDigit(current)) {
                    hasSpecial = true;
                }
            }

            if (hasNumber && hasCap && hasSpecial) {
                passwordOkay = true;
            }
        }

        return passwordOkay;
    }

    /*
     * Regular expression used to check a South African cellphone
     * number written using the international +27 country code.
     *
     * Regex research adapted from:
     * GeeksforGeeks - Regular Expressions in Java.
     */
    public boolean checkCellPhoneNumber(String number) {

        String regex = "^\\+27\\d{9}$";

        return number.matches(regex);
    }

    // Registers the user if all the information is correct
    public String registerUser(String username, String password,
                               String cellNumber, String fName,
                               String lName) {

        if (!checkUserName(username)) {

            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {

            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellNumber)) {

            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // Save the registration details for login
        this.firstName = fName;
        this.lastName = lName;
        this.cellPhoneNumber = cellNumber;
        this.storedUsername = username;
        this.storedPassword = password;

        return "Registration successful.";
    }

    // Checks if the login details match the registered details
    public boolean loginUser(String username, String password) {

        return username.equals(storedUsername)
                && password.equals(storedPassword);
    }

    // Returns the correct login message
    public String returnLoginStatus(String username, String password) {

        if (loginUser(username, password)) {

            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";

        } else {

            return "Username or password incorrect, please try again.";
        }
    }
}