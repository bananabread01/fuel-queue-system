import java.io.*;
import java.util.Scanner;

/*
SD2 CW Fuel Queue Management System
author: Krishna Smriti Premkumar
date: 08-08-2022
 */

public class FuelQueueManagementSystem {
    public static void main(String[] args) throws IOException {
        int totalFuelStock = 6600;

        //declaration and instantiation of 2d array
        String[][] fuelPumpLines = new String[3][6];

        //passing array to method to initialize array
        initializeEmpty(fuelPumpLines);

        while (true) {
            menuConsole();

            Scanner scan = new Scanner(System.in);
            System.out.print("Enter an option: ");
            String menuInput = scan.nextLine().toUpperCase();

            switch (menuInput) {
                case "100":
                case "VFQ":
                    viewAllFuelQueues(fuelPumpLines);
                    break;
                case "101":
                case "VEQ":
                    viewAllEmptyQueues(fuelPumpLines);
                    break;
                case "102":
                case "ACQ":
                    totalFuelStock = addCustomerToQueue(fuelPumpLines, totalFuelStock);
                    break;
                case "103":
                case "RCQ":
                    totalFuelStock = removeCustomerFromQueue(fuelPumpLines, totalFuelStock);
                    break;
                case "104":
                case "PCQ":
                    removeServedCustomer(fuelPumpLines);
                    break;
                case "105":
                case "VCS":
                    viewSortedCustomers(fuelPumpLines);
                    break;
                case "106":
                case "SPD":
                    storeProgramData(fuelPumpLines);
                    break;
                case "107":
                case "LPD":
                    loadProgramData();
                    break;
                case "108":
                case "STK":
                    viewRemainingFuelStock(totalFuelStock);
                    break;
                case "109":
                case "AFS":
                    totalFuelStock = addFuelStock(totalFuelStock);
                    break;
                case "999":
                case "EXT":
                    exitProgram();
                    break;
                default:
                    System.out.println("---Invalid input----");
            }
        }
    }

    //method holds menu options
    private static void menuConsole() {

        String menu = ("""
                --------------------------------------------------------
                --------------------Fuel Center Menu--------------------
                100 VFQ: View all Fuel Queues
                101 VEQ: View all Empty Queues
                102 ACQ: Add customer to a Queue
                103 RCQ: Remove a customer from a Queue
                104 PCQ: Removed a served customer
                105 VCS: View Customers Sorted in alphabetical order
                106 SPD: Store Program data into a file
                107 LPD: Load Program Data into file
                108 STK: View Remaining Fuel Stock
                109 AFS: Add Fuel Stock
                999 EXT: Exit the Program
                ----------------------------------------------------------""");

        System.out.println(menu);
    }

    //method to initialize all queue slots to empty
    public static void initializeEmpty(String[][] fuelQueues){
        //traverses 2d array and sets column elements to "empty"
        //outer loop
        for (int r = 0; r < fuelQueues.length; r++){
            //inner loop
            for (int c = 0; c < fuelQueues[r].length; c++){
                fuelQueues[r][c] = "empty";
            }
        }
    }

    /*
    method to view all the fuel queues
    @param fuelLines array to view elements
     */
    public static void viewAllFuelQueues(String[][] fuelLines) {
        //traverses the 2d array using nested for loop
        //outer loop
        for (int r = 0; r < fuelLines.length; r++){
            System.out.println("\n-------------" + "Queue No. " + (r+1) + "-------------\n");
            //inner loop
            for (int c = 0; c < fuelLines[r].length; c++){
                if (!(fuelLines[r][c] .equals("empty"))){
                    System.out.println("Fuel slot " + (c+1) + " is occupied by " + fuelLines[r][c]);
                } else{
                    System.out.println("Fuel slot " + (c+1) + " is " + fuelLines[r][c]);
                }
            }
        }
    }

    //method to view empty queue
    public static void viewAllEmptyQueues(String[][] fuelLines) {
        //outer loop prints queue numbers
        for (int r = 0; r < fuelLines.length; r++){
            System.out.println("\n-------------" + "Queue No. " + (r+1) + "-------------\n");
            //inner loop prints all elements in column that are "empty"
            for (int c = 0; c < fuelLines[r].length; c++){
                if ((fuelLines[r][c] .equals("empty"))){
                    System.out.println("Fuel slot " + (c+1) + " is " + fuelLines[r][c]);
                }
            }
        }
    }

    /*
    method to add a customer to a specific queue
    @param fuelLines array to add customer into
    @param fuelAmount to reduce fuel quantity once customer is added
    @return the new fuel quantity
     */
    public static int addCustomerToQueue(String[][] fuelLines, int fuelAmount) {
        Scanner userInput = new Scanner(System.in);
        //declaring variables
        int queueNo;
        String customerName;
        while (true) {
            //validation
            while (true) {
                try {
                    System.out.print("Enter Queue No. (1-3): ");
                    //store user input to integer variable queue number
                    queueNo = Integer.parseInt(userInput.next());
                } catch (NumberFormatException e) {
                    System.out.println("Valid integer is required." + "\nPlease try again.");
                    continue;
                }
                if (!(queueNo >= 1 && queueNo <= 3)) {
                    System.out.println("Invalid queue number.");
                } else {
                    break;
                }
            }
            System.out.print("Enter customer name: ");
            customerName = userInput.next();    //store user input in variable
            System.out.println(customerName + " has been added to queue " +queueNo +" successfully.");
            for (int c = 0; c < fuelLines[queueNo - 1].length; c++) {
                if (fuelLines[queueNo - 1][c].equals("empty")) {
                    fuelLines[queueNo - 1][c] = customerName;
                    fuelAmount -= 10;   //reduce fuel amount

                    //print out warning message when if condition is met
                    if (fuelAmount <= 500) {
                        System.out.println("!WARNING! Fuel capacity is at 500 litres.");
                    }
                    return fuelAmount;
                }
            }
            System.out.println("Queue number " + queueNo + " is full.");
        }
    }

    /*
    method to remove a customer from a specific queue and slot
    @param fuelLines array to remove customer from
    @param fuelAmount to add fuel quantity when removing customer
    @return the new fuel quantity
     */
    public static int removeCustomerFromQueue(String[][] fuelLines, int fuelAmount) {
        Scanner input = new Scanner(System.in);
        //declare variables
        int queueNo;
        int slotNo;
        while (true){
            //validation
            while (true) {
                try {
                    System.out.print("Enter Queue No. (1-3): ");
                    queueNo = Integer.parseInt(input.next());
                } catch (NumberFormatException e) {
                    System.out.println("Valid integer is required." + "\nPlease try again.");
                    continue;
                }
                if (!(queueNo >= 1 && queueNo <= 3)) {
                    System.out.println("Invalid queue number.");
                } else {
                    break;
                }
            }
            while (true) {
                try {
                    System.out.print("Enter Slot No. (1-6): ");
                    //store user input in variable
                    slotNo = Integer.parseInt(input.next());
                } catch (NumberFormatException e) {
                    System.out.println("Valid integer is required." + "\nPlease try again.");
                    continue;
                }
                if (!(slotNo >= 1 && slotNo <= 6)) {
                    System.out.println("Invalid slot number.");
                } else {
                    break;
                }
            }
            if(!(fuelLines[queueNo-1][slotNo-1].equals("empty"))){
                for (int c = slotNo; c < fuelLines[queueNo-1].length; c++){
                    //move customers one up the slots in queue
                    fuelLines[queueNo-1][slotNo-1] = fuelLines[queueNo-1][slotNo];
                }
                fuelAmount += 10;   //add fuel amount
                fuelLines[queueNo-1][5] = "empty";  //set last slot to "empty"
                System.out.println("The customer in queue number " + queueNo + ", slot number " + slotNo + " has been removed.");
                return fuelAmount;
            } else{
                System.out.println("The chosen slot number " + slotNo + " in queue " + queueNo + " is empty.");
                System.out.println("Please try again.");
            }
        }
    }

    /*
    method to remove a served customer from specific queue
    @param fuelLines array to remove customer from
     */
    public static void removeServedCustomer(String[][] fuelLines) {
        Scanner input = new Scanner(System.in);
        int queueNo;
        int slotNo = 1;
        while (true) {
            //validation
            while (true) {
                try {
                    System.out.print("Enter Queue No. (1-3): ");
                    queueNo = Integer.parseInt(input.next());
                } catch (NumberFormatException e) {
                    System.out.println("Valid integer is required." + "\nPlease try again.");
                    continue;
                }
                if (!(queueNo >= 1 && queueNo <= 3)) {
                    System.out.println("Invalid queue number.");
                } else {
                    break;
                }
            }
            if (!(fuelLines[queueNo - 1][slotNo].equals("empty"))) {
                for (int c = slotNo; c < fuelLines[queueNo - 1].length; c++) {
                    //move column indexes one up in queue
                    fuelLines[queueNo - 1][c - 1] = fuelLines[queueNo - 1][c];
                }
                //set last column index as "empty"
                fuelLines[queueNo - 1][5] = "empty";
                break;
            } else {
                System.out.println("The chosen queue " + queueNo + " is empty.");
                System.out.println("---Please try again---");
            }
        }
        System.out.println("---The served customer has been removed from the fuel queue---");
    }

    /*
    method to sort customers in alphabetical order
    @param fuelLines array to access elements that will be sorted
     */
    public static void viewSortedCustomers(String[][] fuelLines){
        //declare and instantiate 2d array
        String [][] customersSorted = new String[3][6];

        //outer loop
        for (int r = 0; r < fuelLines.length; r++) {
            //inner loop
            for (int c = 0; c < fuelLines[r].length; c++) {
                //store array fuelLines elements in new array
                customersSorted[r][c] = fuelLines[r][c];
            }
        }
        //traverse array to sort column elements
        for(int r = 0; r < customersSorted.length; r++){
            for(int c = 0; c < customersSorted[r].length; c++){
                for(int i = 1; i < customersSorted[r].length-c-1; i++){
                    //compares each element of the array to the remaining elements
                    if(customersSorted[r][i-1].compareToIgnoreCase(customersSorted[r][i])>0){
                        //swapping array elements
                        String temp = customersSorted[r][i-1];
                        customersSorted[r][i-1] = customersSorted[r][i];
                        customersSorted[r][i] = temp;
                    }
                }
            }
        }

        //loop to print the sorted array in ascending order
        for(int r = 0; r < customersSorted.length; r++){
            System.out.println("\n-------------" + "Queue No. " + (r+1) + "-------------\n");
            for(int c = 0; c < customersSorted[r].length; c++){
                if(!(customersSorted[r][c].equals("empty"))){    //exclude "empty" slots
                    System.out.println((customersSorted[r][c]));
                }
            }
        }
    }

    /*
    method to create and write to a file that stores fuel queue data
    @param fuelLines array to save data about
     */
    public static void storeProgramData(String[][] fuelLines) throws IOException {
            //create file
            File file = new File("fuelQueueData.txt");
            //display message when text file is created
            if(file.createNewFile()){
                System.out.println("File has been created: " + file.getName());
            }

            //creating a writer and attaching file to file writer
            FileWriter fw = new FileWriter(file);

            //write multiple lines of string using into file writer using for loop
            for (int r = 0; r < fuelLines.length; r++){
                fw.write("\n-------------" + "Queue No. " + (r+1) + "-------------\n");
                for (int c = 0; c < fuelLines[r].length; c++){
                    fw.write("Fuel slot " + (c+1) + " is " + fuelLines[r][c] + "\n");
                }
            }

            //close the writer
            fw.close();
            System.out.println("Your data has been saved.");

    }

    //method to read to file that stores fuel queue data and prints it
    public static void loadProgramData() throws FileNotFoundException {
            File fileRead = new File("fuelQueueData.txt");

            //read the content of file by line and print it
            Scanner scan = new Scanner(fileRead);
            String fileLine;
            while (scan.hasNext()){
                fileLine = scan.nextLine();
                System.out.println(fileLine);
            }
            scan.close();
    }

    /*
    method to display quantity of fuel
    @param fuelStock display stock of fuel remaining
     */
    public static void viewRemainingFuelStock(int fuelStock) {
        System.out.println("Remaining stock of fuel: " + fuelStock + " litres");
    }

    /*
    method to add to the existing stock of fuel
    @param fuelStock to add onto the fuel quantity
    @return the updated fuelStock
     */
    public static int addFuelStock(int fuelStock) {
        Scanner input = new Scanner(System.in);
        int newStock;
        while (true) {
            try {
                System.out.print("Enter fuel quantity (litres) to add: ");
                newStock = Integer.parseInt(input.next());
            } catch (NumberFormatException e) {
                //validation
                System.out.println("Valid integer is required." + "\nPlease try again");
                continue;
            }
            if (fuelStock + newStock <= 6600) {
                //add new fuel amount onto existing fuel stock
                fuelStock += newStock;
                System.out.println("Added!");
                System.out.println("Current total fuel stock: " + fuelStock);
                return fuelStock;   //return new value
            } else {
                System.out.println("------Fuel Stock has reached full capacity (6600L)------");
                break;
            }
        }
        return fuelStock;
    }

    //method to exit program
    public static void exitProgram() {
        System.out.println("Thank you!");
        System.exit(0);
    }
}
