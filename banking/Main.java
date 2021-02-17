package banking;

import banking.controllers.AccountManager;
import banking.controllers.DatabaseManager;
import banking.ui.Menu;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        try {

            DatabaseManager database = new DatabaseManager("jdbc:sqlite:" + args[1]);
            database.setupDatabase();

            Scanner scanner = new Scanner(System.in);

            while (true) {

                Menu.showMainMenu();

                switch (Integer.parseInt(scanner.nextLine())) {
                    // Exit
                    case 0: {
                        System.exit(0);
                        break;
                    }
                    // Create Account
                    case 1: {
                        String cardNum = AccountManager.generateCardNumber();
                        String cardPIN = AccountManager.generatePIN();
                        database.insertAccount(cardNum, cardPIN);
                        break;
                    }
                    // Login Account
                    case 2: {
                        System.out.println("Enter your card number:");
                        String cardNum = scanner.nextLine();
                        System.out.println("Enter your PIN:");
                        String cardPIN = scanner.nextLine();

                        if (database.isAccountExist(cardNum, cardPIN)) {
                            System.out.println("You have successfully logged in!");

                            UserAccount user = new UserAccount(cardNum, cardPIN);
                            user.accessUserPortal(database);
                        } else {
                            System.out.println("Wrong card number or PIN!");
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}