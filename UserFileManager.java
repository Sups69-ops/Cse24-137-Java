package com.Bank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserFileManager {
    private static final String FILE_PATH = "C:\\Users\\Mosupi\\OneDrive\\Desktop\\Bank\\users.txt";

    public static void saveUser(User user) throws IOException {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user.toFileString());
        }
    }

    public static List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        System.out.println("Looking for users file at: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("Users file does not exist!");
            return users;
        }
        System.out.println("Found users file, reading contents:");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Reading line: " + line);
                if (!line.trim().isEmpty()) {
                    try {
                        User user = User.fromFileString(line);
                        System.out.println("Created user: " + user.getCustomerId() + " with password: " + user.getPassword());
                        users.add(user);
                    } catch (Exception e) {
                        System.out.println("Error parsing line: " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("Total users loaded: " + users.size());
        return users;
    }

    public static User findUser(String customerId) throws IOException {
        for (User user : getAllUsers()) {
            if (user.getCustomerId().equals(customerId)) {
                return user;
            }
        }
        return null;
    }

    public static boolean validateUser(String customerId, String password) throws IOException {
        System.out.println("Attempting login with - Customer ID: " + customerId + ", Password: " + password);
        User user = findUser(customerId);
        if (user == null) {
            System.out.println("No user found with Customer ID: " + customerId);
            return false;
        }
        System.out.println("Found user: " + user.getCustomerId());
        System.out.println("Stored password: " + user.getPassword());
        return user.getPassword().equals(password);
    }
}