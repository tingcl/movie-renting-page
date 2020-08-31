package edu.uci.ics.tingcl2.service.idm.core;

import edu.uci.ics.tingcl2.service.idm.models.RegisterRequestModel;
import edu.uci.ics.tingcl2.service.idm.security.Crypto;

import java.util.Arrays;

/*
 * IDM Login
 *
 * Includes functionality for surface level checking and deeper logical correctness checking.
 */


public class ValidateLogin {
    public static int handler(RegisterRequestModel requestModel){
        //[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-z]+.[a-z]+
        if (!requestModel.getEmail().matches("^(.+)@(.+)$")) {
            return  -11;
        }
        if (!UserRecords.emailExists(requestModel.getEmail())) {
            return 14;
        }
        if (!comparePword(requestModel)){
            return 11;
        }
        return 120;
    }

    // Validate login helper functions

    public static boolean comparePword(RegisterRequestModel requestModel){
        char[] original = requestModel.getPassword();
        byte[] salt = UserRecords.getSalt(requestModel.getEmail());
        String password = UserRecords.getPassword(requestModel.getEmail());
        byte[] hashedPassword = Crypto.hashPassword(original, salt, Crypto.ITERATIONS, Crypto.KEY_LENGTH);
        String hashed = getHashedPass(hashedPassword);

        if(hashed.equals(password)){
            Arrays.fill(original, '0');
            return true;
        }
        Arrays.fill(original, '0');
        return false;
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
