package banking.controllers;

import java.util.Random;

public class AccountManager {

    public static String generateCardNumber() {
        Random random = new Random();
        String cardNumber = "400000"
                + (random.nextInt(900_000_000) + 100_000_000);
        cardNumber = cardNumber + generateChecksum(cardNumber);

        System.out.println("Your card has been created.\n"
                + "Your card number:\n" + cardNumber);
        return cardNumber;
    }

    public static String generatePIN() {
        Random random = new Random();
        String cardPIN = String.valueOf(random.nextInt(9000) + 1000);

        System.out.println("Your card PIN:\n" + cardPIN);
        return cardPIN;
    }

    private static String generateChecksum(String cardNum) {
        int sum = 0;
        int[] intArray = new int[15];
        for (int i = 0; i < 15; i++) {
            intArray[i] = Integer.parseInt(String.valueOf(cardNum.charAt(i)));
            if ((i + 1) % 2 != 0) intArray[i] *= 2;
            if (intArray[i] > 9) intArray[i] -= 9;
            sum += intArray[i];
        }

        return String.valueOf(10 - (sum % 10) == 10 ?
                0 : 10 - (sum % 10));
    }

    public static boolean verifyIfPassedLuhnAlgorithm(String cardNum) {
        String checksum = cardNum.substring(cardNum.length() - 1);
        String cardNumWithoutChecksum = cardNum.substring(0, cardNum.length() - 1);

        return (checksum.compareTo(generateChecksum(cardNumWithoutChecksum)) == 0);
    }

}
