package ProjectPhase1;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Simple memory-based login (used for Admin)
    public boolean login(String u, String p) {
        return username.equals(u) && password.equals(p);
    }

    public String getUsername() {
        return username;
    }
}
class AdminUser extends User {
    public AdminUser(String username, String password) {
        super(username, password);
    }

    public String adminMessage() {
        return "Welcome Admin! You have full access.";
    }
}
class NormalUser extends User {

    public NormalUser(String username, String password) {
        super(username, password);
    }

    public String userMessage() {
        return "Welcome User! Limited access granted.";
    }

    // Factory method for login via file
    public static NormalUser loginFromFile(String username, String password) {
        return UserManager.getUser(username, password);
    }

    // Register new user to file
    public static boolean registerToFile(String username, String password) {
        if (!UserManager.isUserExist(username)) {
            UserManager.saveUser(username, password);
            return true; // successfully registered
        }
        return false; // username exists
    }
}



