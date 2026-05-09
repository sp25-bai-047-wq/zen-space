package com.example.backup;

import java.io.*;
import java.util.*;

public class UserManager {

    private static final String FILE_NAME = "users.txt";

    // Save new user to file
    public static void saveUser(String username, String password) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(username + "," + password);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Check if username already exists
    public static boolean isUserExist(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // File may not exist yet, that's okay
        }
        return false;
    }

    // Get user object from file if username/password match
    public static NormalUser getUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return new NormalUser(username, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

