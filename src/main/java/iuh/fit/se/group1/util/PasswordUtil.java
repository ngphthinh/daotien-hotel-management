package iuh.fit.se.group1.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    public static boolean verifyPassword(String oldPassword, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyPassword'");
    }
}
