package banking;

import banking.controllers.AccountManager;
import banking.controllers.DatabaseManager;

import java.util.Scanner;

public class UserAccount {

    private final String cardNum;
    private final String cardPIN;

    UserAccount(String cardNum, String cardPIN) {
        this.cardNum = cardNum;
        this.cardPIN = cardPIN;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public String getCardPIN() {
        return this.cardPIN;
    }

    public int selectFunctionFromMenu() {
        System.out.println("1. Balance\n2. Add income\n3. Do transfer\n" +
                "4. Close account\n5. Log out\n0. Exit");

        Scanner scanner = new Scanner(System.in);
        return Integer.parseInt(scanner.nextLine());
    }

    public void accessUserPortal(DatabaseManager database) {

        while (true) {

            int userInput = selectFunctionFromMenu();

            switch (userInput) {
                // Exit
                case 0:
                    System.exit(0);
                    break;
                // Balance
                case 1:
                    System.out.println("Balance: "
                            + database.checkAccountBalance(this));
                    break;
                // Add Income
                case 2:
                    if (database.increaseAccountBalance(this, addIncome()) != 0) {
                        System.out.println("Income was added!");
                    }
                    break;
                // Do Transfer
                case 3:
                    if (isTransferSuccessful(database)) {
                        System.out.println("Success!");
                    }
                    break;
                // Close Account
                case 4:
                    if (database.closeAccount(this) != 0) {
                        System.out.println("The account has been closed!");
                    }
                    break;
                // Log out
                case 5:
                    System.out.println("You have successfully logged out!");
                default:
                    return;
            }
        }
    }

    private boolean isTransferSuccessful(DatabaseManager database) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Transfer\n" +
                "Enter card number:");
        String transferCardNum = scanner.nextLine();

        if (isCardNumberValid(database, transferCardNum)) {
            System.out.println("Enter how much money you want to transfer:");
            int amount = scanner.nextInt();
            if (isTransferAmountValid(database, amount)) {
                return database.transferAmount(this, transferCardNum, amount) != 0;
            }
        }
        return false;
    }

    private boolean isTransferAmountValid(DatabaseManager database, int amount) {
        if (amount > database.checkAccountBalance(this)) {
            System.out.println("Not enough money!");
            return false;
        }

        return true;
    }

    private boolean isCardNumberValid(DatabaseManager database, String transferCardNum) {
        if (this.cardNum.compareTo(transferCardNum) == 0) {
            System.out.println("You can't transfer money to " +
                    "the same account!");
        } else if (!AccountManager.verifyIfPassedLuhnAlgorithm(transferCardNum)) {
            System.out.println("Probably you made a mistake in the card number. " +
                    "Please try again!");
        } else if (!database.isAccountExist(transferCardNum)) {
            System.out.println("Such a card does not exist.");
        }  else {
           return true;
        }

        return false;
    }

    public int addIncome() {
        System.out.println("Enter income:");

        Scanner scanner = new Scanner(System.in);
        return Integer.parseInt(scanner.nextLine());
    }

}
