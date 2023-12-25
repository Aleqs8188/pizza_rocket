package org.denys.admin.super_users.chief_administrator;

import org.denys.admin.super_users.default_administrator.Admin;
import org.mindrot.jbcrypt.BCrypt;

public class SuperAdmin {
    private final String USER_NAME = "admin";
    private final String PASSWORD = "admin";

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }
}
