package Model;

public class Validator {
    
    
    public static String validateRegistration(String username, String password, String confirmPassword, boolean userNameExists) {
        if (userNameExists) return "Username already Exists!";
        if (!password.equals(confirmPassword)) return "Passwords do not match!";
        if (password.length() < 8) return "Password must be at least 8 characters!";
        if (password.length() > 128) return "Password must be less than 128 characters!";
        
        // TODO: if necessary, implement password strength check using zxcvbn
        return null; //valid
    }
}
