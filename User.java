package com.Bank;

public class User {
    private String customerId;
    private String name;
    private String password;
    private String address;
    private String phone;

    public User(String customerId, String name, String password, String address, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }

    public String toFileString() {
        return String.format("%s,%s,%s,%s,%s",
            customerId, name, password, address, phone);
    }

    public static User fromFileString(String line) {
        if (line == null) throw new IllegalArgumentException("line is null");
        // Remove common BOM marker and trim whitespace
        String cleaned = line.replace("\uFEFF", "").trim();
        String[] parts = cleaned.split(",", -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid user record (expected 5 CSV parts): " + cleaned);
        }
        return new User(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            parts[4].trim()
        );
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
}