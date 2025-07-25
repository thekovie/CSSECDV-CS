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
    
    public static String sanitizeString(String input) {
    if (input == null) return "";
    // Remove control characters and trim whitespace
    return input.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "").trim();
}

    public static boolean matchesPattern(String input, String regex) {
        if (input == null) return false;
        return input.matches(regex);
    }
    
    
    public static int parsePositiveInt(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        String trimmed = input.trim();
        if (!matchesPattern(trimmed, "^\\d+$")) {
            throw new IllegalArgumentException(fieldName + " must be a whole number.");
        }

        try {
            int value = Integer.parseInt(trimmed);
            if (value < 1) {
                throw new IllegalArgumentException(fieldName + " must be at least 1.");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " is too large.");
        }
    }

    public static int validateQuantity(String input, int availableStock) {
        int qty = parsePositiveInt(input, "Quantity");
        if (qty > availableStock) {
            throw new IllegalArgumentException("Cannot purchase more than " + availableStock + " in stock.");
        }
        return qty;
    }
}
