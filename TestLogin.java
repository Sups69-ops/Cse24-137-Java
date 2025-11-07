package com.Bank;

public class TestLogin {
    public static void main(String[] args) {
        try {
            System.out.println("Testing valid user CS001/password:");
            boolean ok = UserDAO.validateUser("CS001", "password");
            System.out.println("Result: " + ok);

            System.out.println("Testing invalid user foo/secret:");
            boolean ok2 = UserDAO.validateUser("foo", "secret");
            System.out.println("Result: " + ok2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
