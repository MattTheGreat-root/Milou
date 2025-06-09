package aut.ap;

import aut.ap.service.userService;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String choice;
        do {
            System.out.println("[L]og in, [S]ign up, [Q]uit: ");
            choice = sc.nextLine();
            switch (choice.toUpperCase()) {
                case "L":{
                    break;
                }
                case "S":{
                    userService.signUp();
                    choice = "L";
                    break;
                }
                case "Q":{
                    System.out.println("Exiting...");
                    break;
                }
                default:{
                    System.out.println("Invalid choice");
                }
            }
        } while (!Objects.equals(choice, "Q"));
    }
}