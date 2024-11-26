package com.ebgr.pagamento_carnes.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtil {
    //public static final String salt = BCrypt.gensalt();
    public static final String salt = "$2a$10$SEch7t8CN1.5ySr/v2r3D.";

    public static boolean checkpw(String s1, String s2) {
        return BCrypt.checkpw(s1, s2);
    }
    public static String hashpw(String password) {
        return BCrypt.hashpw(password, salt);
    }
}