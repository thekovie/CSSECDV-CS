package Model;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class Validator {
    
    private static int reviewPasswordStrength(String password) {
        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);

        return strength.getScore();
    }
    public static String validateRegistration(String username, String password, String confirmPassword, boolean userNameExists) {
        if (userNameExists) return "Username already Exists!";
        if (password.toLowerCase().contains(username.toLowerCase())) return "Password should not contain your username!";
        if (!password.equals(confirmPassword)) return "Passwords do not match!";
        if (password.length() < 8) return "Password must be at least 8 characters!";
        if (password.length() > 128) return "Password must be less than 128 characters!";
        if (reviewPasswordStrength(password) == 0) return "Your password is too weak!";
        return null; //valid
    }
}
