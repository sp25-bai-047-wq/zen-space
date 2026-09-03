package com.example.backup;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String u, String p) {
        return username.equals(u) && password.equals(p);
    }

    public String getUsername() {
        return username;
    }
}

class NormalUser extends User {

    public NormalUser(String username, String password) {
        super(username, password);
    }

    public String userMessage() {
        return "Welcome User! Limited access granted.";
    }

    public static NormalUser loginFromFile(String username, String password) {
        return UserManager.getUser(username, password);
    }
    public static String registerToFile(String username, String password) {

        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        if (!password.matches("[a-zA-Z0-9]+")) {
            return "Password must be alphanumeric only.";
        }

        if (UserManager.isUserExist(username)) {
            return "Username already exists.";
        }

        UserManager.saveUser(username, password);
        return "SUCCESS";
    }


    private static boolean isValidPassword(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;

        // Only letters and digits allowed
        return password.matches("[a-zA-Z0-9]+");
    }

}
