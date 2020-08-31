package edu.uci.ics.tingcl2.service.idm.core;

import edu.uci.ics.tingcl2.service.idm.models.RegisterRequestModel;
import edu.uci.ics.tingcl2.service.idm.security.Crypto;

import java.util.Arrays;

/*
 * IDM Register
 *
 * Includes functionality for surface level checking and deeper logical correctness checking.
 */


public class ValidateRegister {

    //  General case checks

    public static int surface(RegisterRequestModel requestModel){
        if(requestModel.getPassword() == null || requestModel.getPassword().length == 0){
            return -12;
        }
        else if(requestModel.getEmail().length() > 50){
            return -10;
        }
        else if(requestModel.getPassword().length < 7 || requestModel.getPassword().length > 16){
            return 12;
        }
        // General tests passed
        return 0;
    }

    // Logical correctness checks

    public static int handler(RegisterRequestModel requestModel) {

        if(!requestModel.getEmail().matches(
                "^(.+)@(.+)$")){
            return  -11;
        }
        if (UserRecords.emailExists(requestModel.getEmail())) {
            return 16;
        }
        char[] pword = requestModel.getPassword();

        if(!isCorrectFormat(pword)){
            Arrays.fill(pword, '0');
            return 13;
        }

        // All tests passed

        // Salt & hash password
        byte[] salt = Crypto.genSalt();
        byte[] hashedPassword = Crypto.hashPassword(pword, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);
        String password = getHashedPass(hashedPassword);

        // Insert user into database

        UserRecords.addUser(requestModel, salt, password);
        Arrays.fill(pword, '0');

        return 110;
    }

    // Register validation helper functions

    public static boolean isCorrectFormat(char[] pword){
        // i: Counter
        int i = 0;
        // s: Special character counter
        int s = 0;
        // u: Uppercase character counter
        int u = 0;
        // l: Lowercase character counter
        int l = 0;
        // n: Integer counter
        int n = 0;
        // run: If all cases above have not been met, keep running
        boolean run = true;

        while (i < pword.length && run) {
            if(Character.isDigit(pword[i])){
                n++;
            }
            else if(Character.isLetter(pword[i])){
                if (Character.isLowerCase(pword[i])) {
                    l++;
                }
                else{
                    u++;
                }
            }
            else if("!@#$%^&*()_+|}{\":?><\\][';/.,~".indexOf(pword[i]) != -1){
                s++;
            }
            i++;
            run = !(s>0 && u>0 && l>0 && n>0);
        }
        return (s>0 && u>0 && l>0 && n>0);
    }

    private static String getHashedPass(byte[] hashedPassword) {
        StringBuffer buf = new StringBuffer();
        for (byte b : hashedPassword) {
            buf.append(format(Integer.toHexString(Byte.toUnsignedInt(b))));
        }
        return buf.toString();
    }

    private static String format(String binS) {
        int length = 2 - binS.length();
        char[] padArray = new char[length];
        Arrays.fill(padArray, '0');
        String padString = new String(padArray);
        return padString + binS;
    }
}


