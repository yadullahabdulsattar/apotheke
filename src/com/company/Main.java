package com.company;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        QueriesMethods queriesMethods = new QueriesMethods();
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the inventory system!!!");
        int digit = -1;
        while (digit != 0) {
            System.out.println("Choose the digit from following menu:" +
                    "\n1- Test Queries." +
                    "\n2- Get a list of all the medicines or medicine types or companies." +
                    "\n3- Update the inventory." +
                    "\n4- Sales." +
                    "\n0- Exit the system.\n");
            if (scan.hasNextInt()){
                digit = scan.nextInt();
            } else {
                System.out.println("Invalid input!");
            }

            switch (digit) {
                case 1:
                    queriesMethods.testQueries();
                    break;
                case 2:
                    queriesMethods.getInventory();
                    break;
                case 3:
                    queriesMethods.updateInventory();
                    break;
                case 4:
                    queriesMethods.sale();
                    break;
                default:
                    System.out.println("You have exited the program!");
            }
        }
    }
}
