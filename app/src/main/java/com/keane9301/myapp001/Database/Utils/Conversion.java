package com.keane9301.myapp001.Database.Utils;

public class Conversion {
    // Prevent data from being used by unauthorised user by switching number with alphabet
    public static String fromFloat (String num) {
        StringBuilder encoded = new StringBuilder();
        if (num != null) {
            char[] split = num.toCharArray();
            for (int i = 0; i < split.length; i++) {
                switch (split[i]) {
                    case '1':
                        split[i] = 'D';
                        break;
                    case '2':
                        split[i] = 'R';
                        break;
                    case '3':
                        split[i] = 'I';
                        break;
                    case '4':
                        split[i] = 'V';
                        break;
                    case '5':
                        split[i] = 'E';
                        break;
                    case '6':
                        split[i] = 'S';
                        break;
                    case '7':
                        split[i] = 'H';
                        break;
                    case '8':
                        split[i] = 'A';
                        break;
                    case '9':
                        split[i] = 'F';
                        break;
                    case '0':
                        split[i] = 'T';
                        break;
                    case '.':
                        split[i] = '.';
                        break;
                }
                encoded.append(split[i]);
            }
        }
        return encoded.toString();
    }



    // Swapping from alphabet back to number where it will be useful for calculations of
    // Profit or Selling price to be done
    public static String fromString (String num) {
        StringBuilder decoded = new StringBuilder();
        if (num != null) {
            char[] split = num.toCharArray();
            for (int i = 0; i < split.length; i++) {
                switch (split[i]) {
                    case 'D':
                        split[i] = '1';
                        break;
                    case 'R':
                        split[i] = '2';
                        break;
                    case 'I':
                        split[i] = '3';
                        break;
                    case 'V':
                        split[i] = '4';
                        break;
                    case 'E':
                        split[i] = '5';
                        break;
                    case 'S':
                        split[i] = '6';
                        break;
                    case 'H':
                        split[i] = '7';
                        break;
                    case 'A':
                        split[i] = '8';
                        break;
                    case 'F':
                        split[i] = '9';
                        break;
                    case 'T':
                        split[i] = '0';
                        break;
                    case '.':
                        split[i] = '.';
                        break;
                }
                decoded.append(split[i]);
            }
        }
        return decoded.toString();
    }
}
