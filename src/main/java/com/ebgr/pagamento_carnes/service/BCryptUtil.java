package com.ebgr.pagamento_carnes.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtil {
    public static boolean checkpw(String s1, String s2) {
        return BCrypt.checkpw(s1, s2);
    }
    public static String hashpw(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}