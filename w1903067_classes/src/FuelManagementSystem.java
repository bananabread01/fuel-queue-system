import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/*
SD2 CW Fuel Queue Management System
author: Krishna Smriti Premkumar
date: 08-08-2022
 */

public class FuelManagementSystem {
    public static void main(String[] args) {
        int totalFuelStock = 6600;

        //creating an object using FuelQueue class
        FuelQueue[] FQ = new FuelQueue[5];
        FQ[0] = new FuelQueue(1);
        FQ[1] = new FuelQueue(2);
        FQ[2] = new FuelQueue(3);
        FQ[3] = new FuelQueue(4);
        FQ[4] = new FuelQueue(5);

        //calling method to set all slots to empty
        initializeEmpty(FQ);

        //creating an array of objects to be used for waiting queue
        WaitingQueue<Passenger> fuelWaitingQueue = new WaitingQueue(50);

        while (true) {
            menuConsole(totalFuelStock);

            Scanner scan = new Scanner(System.in);
            System.out.print("Enter an option: ");
            String menuInput = scan.nextLine().toUpperCase();

            switch (menuInput) {
                case "100":
                case "VFQ":
                    viewAllFuelQueues(FQ);
                    break;
                case "101":
                case "VEQ":
                    viewAllEmptyQueues(FQ);
                    break;
                case "102":
                case "ACQ":
                    totalFuelStock = addCustomerToQueue(FQ, totalFuelStock, fuelWaitingQueue);
                    break;
                case "103":
                case "RCQ":
                    totalFuelStock = removeCustomerFromQueue(FQ, totalFuelStock, fuelWaitingQueue);
                    break;
                case "104":
                case "PCQ":
                    removeServedCustomer(FQ, fuelWaitingQueue);
                    break;
                case "105":
                case "VCS":
                    viewSortedCustomers(FQ);
                    break;
                case "106":
                case "SPD":
                    storeProgramData(FQ);
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
                case "110":
                case "IFQ":
                    calculateFuelQueueIncome(FQ);
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
    private static void menuConsole(int totalFuelStock) {

        String menu = ("""
                --------------------------------------------------------
                --------------------Fuel Center Menu (%d Litres Remaining)--------------------
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
                110 IFQ: Print Fuel Queue Income
                999 EXT: Exit the Program
                ----------------------------------------------------------\n""");

        //formats so total fuel stock is shown on the menu
        System.out.printf(menu, totalFuelStock);
    }

    //method to initialize all queue name slots to empty
    public static void initializeEmpty(FuelQueue[] fuelQueue) {
        for (int i = 0; i < fuelQueue.length; i++) {
            for (int j = 0; j < 6; j++) {
                Passenger passenger = new Passenger("Empty", "", "", 0);
                //call fuel class method to see each slot to empty
                fuelQueue[i].addEmptyPassenger(passenger);
            }
        }
    }

    /*
    method to view all the fuel queues
    @param fuelQueue object to view elements
     */
    public static void viewAllFuelQueues(FuelQueue[] fuelQueues) {
        // This for loop views all the fuel slots in all fuel queues
        for (int i = 0; i < fuelQueues.length; i++) {
            System.out.println("\n-------------" + "Queue No. " + (i + 1) + "-------------\n");
            for (int j = 0; j < fuelQueues[i].getPassengersInQueue().size(); j++) {
                //accessing the first name and second name using the getter method and assigning it to string variable
                String firstName = fuelQueues[i].getPassengersInQueue().get(j).getFirstName();
                String secondName = fuelQueues[i].getPassengersInQueue().get(j).getSecondName();
                //print out the spots in fuel queues. if condition to display name of customer if slot is taken
                if (!firstName.equals("Empty")) {
                    System.out.println("Fuel slot " + (j + 1) + " is occupied by " + firstName + " " + secondName);
                } else {
                    System.out.println("Fuel slot " + (j + 1) + " is " + firstName);
                }
            }
        }
    }

    //method to view all empty queues
    public static void viewAllEmptyQueues(FuelQueue[] fuelQueues) {
        //outer loop prints queue numbers
        for (int i = 0; i < fuelQueues.length; i++) {
            System.out.println("\n-------------" + "Queue No. " + (i + 1) + "-------------\n");
            //inner loop to print slots that are empty
            for (int j = 0; j < fuelQueues[i].getPassengersInQueue().size(); j++) {
                String firstName = fuelQueues[i].getPassengersInQueue().get(j).getFirstName();
                if (firstName.equals("Empty")) {
                    System.out.println("Fuel slot " + (j + 1) + " is " + firstName);
                }
            }
        }
    }

    //method to count the number of empty slots
    public static int getFuelQueueEmptySlots(FuelQueue fuelQueue) {
        int emptySlots = 0;
        for (int i = 0; i < fuelQueue.getPassengersInQueue().size(); i++) {
            if (fuelQueue.getPassengersInQueue().get(i).getFirstName().equals("Empty")) {
                emptySlots++;
            }
        }
        return emptySlots;
    }

    //method to find queue with minimum length
    public static FuelQueue getShortestFuelQueue(FuelQueue[] fuelQueues) {
        int tempFQ = 0;
        for (int i = 0; i < fuelQueues.length; i++) {
            //calling method to find queue index that has the highest number of empty slots
            if (getFuelQueueEmptySlots(fuelQueues[i]) > getFuelQueueEmptySlots(fuelQueues[tempFQ])) {
                //storing index number of queue with shortest queue
                tempFQ = fuelQueues[i].getFuelQueueNo() - 1;
            }
        }
        return fuelQueues[tempFQ];
    }

    public static boolean isAllSlotsFull(FuelQueue[] fuelQueues) {
        for (int i = 0; i < fuelQueues.length; i++) {
            //true when there are no empty slots in queue
            if (getFuelQueueEmptySlots(fuelQueues[fuelQueues.length - (i + 1)]) == 0) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    //method to get user input
    public static String getUserInput(Scanner userInput, String caption) {
        System.out.print(caption + " :");
        return userInput.next();
    }

    /*
    method to add a customer to a specific queue
    @param fuelQueue object to add customer into
    @param fuelAmount to reduce fuel quantity once customer is added
    @param waiting queue object to access customers in waiting list
    @return the new fuel quantity
     */
    public static int addCustomerToQueue(FuelQueue[] fuelQueues, int fuelAmount, WaitingQueue<Passenger> waitingQueue) {
        Scanner userInput = new Scanner(System.in);

        //variables
        String CustomerFirstName = "";
        String CustomerSecondName = "";
        String CustomerVehicleNumber = "";
        int CustomerFuelReqLitres = 0;


        while (true) {

            //if fuel stock is empty, notify user with message
            if (fuelAmount == 0) {
                System.out.println("🟥 ATTENTION 🟥 Fuel is over.");
                break;
            }

            //validation
            try {
                //store user inputs into variables
                CustomerFirstName = getUserInput(userInput, "Enter customer First name");
                CustomerSecondName = getUserInput(userInput, "Enter customer Second name");
                CustomerVehicleNumber = getUserInput(userInput, "Enter customer Vehicle number");
                CustomerFuelReqLitres = Integer.parseInt(getUserInput(userInput, "Enter customer Fuel required"));
            } catch (NumberFormatException e) {
                System.out.println("Valid integer is required." + "\nPlease try again.");
                continue;
            }

            //if all slots are full, notify user and add customer to waiting list with the passenger details
            if (isAllSlotsFull(fuelQueues)) {
                System.out.println("🟥 ATTENTION 🟥 Slots are full; you will be added to a waiting queue.");
                Passenger passenger = new Passenger(CustomerFirstName, CustomerSecondName, CustomerVehicleNumber, CustomerFuelReqLitres);
                waitingQueue.addQueue(passenger);
                break;
            }

            //object with parameters
            Passenger passenger = new Passenger(CustomerFirstName, CustomerSecondName, CustomerVehicleNumber, CustomerFuelReqLitres);
            //call method to add customer to the queue with minimum length
            getShortestFuelQueue(fuelQueues).addPassenger(passenger);
            //reduce fuel quantity customer requires from the total fuel stock
            fuelAmount -= CustomerFuelReqLitres;

            if (fuelAmount <= 500) {
                System.out.println("🟨 WARNING 🟨 Fuel capacity is at 500 litres.");
            }
            break;
        }
        System.out.println("Customer " + CustomerFirstName + " " + CustomerSecondName + " has been added successfully!");
        return fuelAmount;
    }

    public static void addFromWaitingQ(WaitingQueue<Passenger> waitingQueue, FuelQueue fuelQueue) {
        if (waitingQueue.isWQueueEmpty()) {
            Passenger passenger = new Passenger("Empty", "", "", 0);
            fuelQueue.addPassenger(passenger);
        } else {
            //add customers from the waiting list to the fuel queue
            Passenger nextPassenger = waitingQueue.takeQueue();
            fuelQueue.addPassenger(nextPassenger);
        }

    }

    /*
    method to remove a customer from a specific queue and slot
    @param fuelQueues object to remove customer from
    @param fuelAmount to add fuel quantity when removing customer
    @param waitingQueue object to access customers in waiting list
    @return the new fuel quantity
     */
    public static int removeCustomerFromQueue(FuelQueue[] fuelQueues, int fuelAmount, WaitingQueue<Passenger> waitingQueue) {
        Scanner input = new Scanner(System.in);
        //declare variables
        int queueNo;
        int slotNo;
        while (true) {
            //validation
            while (true) {
                try {
                    System.out.print("Enter Queue No. (1-5): ");
                    queueNo = Integer.parseInt(input.next());
                } catch (NumberFormatException e) {
                    System.out.println("Valid integer is required." + "\nPlease try again.");
                    continue;
                }
                if (!(queueNo >= 1 && queueNo <= 5)) {
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
            // if fuel slot is taken, remove passenger and set last slot to empty
            if (!(fuelQueues[queueNo - 1].getPassengersInQueue().get(slotNo - 1).equals("empty"))) {

                Passenger deletePassenger = fuelQueues[queueNo - 1].getPassengersInQueue().get(slotNo - 1);
                fuelQueues[queueNo - 1].getPassengersInQueue().remove(slotNo - 1);
                Passenger passenger = new Passenger("Empty", "", "", 0);
                fuelQueues[queueNo - 1].getPassengersInQueue().add(passenger);

                System.out.println(deletePassenger.getFirstName() + " " + deletePassenger.getSecondName()
                        + " in queue number " + queueNo + ", slot number " + slotNo + " has been removed.");
                addFromWaitingQ(waitingQueue, fuelQueues[queueNo - 1]);

                //add on the amount the passenger had required to the total fuel stock
                fuelAmount += deletePassenger.getLitresRequired();
                return fuelAmount;
            } else {
                System.out.println("The chosen slot number " + slotNo + " in queue " + queueNo + " is empty.");
                System.out.println("Please try again.");
            }
        }
    }

    /*
    method to remove a served customer from specific queue
    @param fuelQueue object to remove customer from
    @param waitingQueue object to add customers from waiting list to fuel queue
     */
    public static void removeServedCustomer(FuelQueue[] fuelQueues, WaitingQueue<Passenger> waitingQueue) {
        Scanner input = new Scanner(System.in);
        int queueNo;
        int slotNo = 1;
        while (true) {
            //validation
            while (true) {
                try {
                    System.out.print("Enter Queue No. (1-5): ");
                    queueNo = Integer.parseInt(input.next());
                } catch (NumberFormatException e) {
                    System.out.println("Valid integer is required." + "\nPlease try again.");
                    continue;
                }
                if (!(queueNo >= 1 && queueNo <= 5)) {
                    System.out.println("Invalid queue number.");
                } else {
                    break;
                }
            }
            //if fuel slot is taken, remove the passenger and set last slot to empty
            if (!(fuelQueues[queueNo - 1].getPassengersInQueue().get(slotNo - 1).equals("empty"))) {

                Passenger removePassenger = fuelQueues[queueNo - 1].getPassengersInQueue().get(slotNo - 1);
                fuelQueues[queueNo - 1].getPassengersInQueue().remove(slotNo - 1);
                Passenger passenger = new Passenger("Empty", "", "", 0);
                fuelQueues[queueNo - 1].getPassengersInQueue().add(passenger);

                //add customer from waiting queue to end of the fuel queue
                try {
                    addFromWaitingQ(waitingQueue, fuelQueues[queueNo - 1]);
                } catch (QueueEmptyException e) {
                    break;
                }

                System.out.println("--" + removePassenger.getFirstName() + " " + removePassenger.getFirstName()
                        + " in queue number " + queueNo + ", slot number " + slotNo + " has been removed.--");
                break;
            } else {
                System.out.println("The chosen queue " + queueNo + " is empty.");
                System.out.println("---Please try again---");
            }
        }
    }

    /*
    method to sort customers in alphabetical order
    @param fuelQueue object to access elements that will be sorted
     */
    public static void viewSortedCustomers(FuelQueue[] fuelQueues) {
        FuelQueue[] fuelQSorted = fuelQueues.clone();

        //sort passenger first names in ascending order
        for (int i = 0; i < fuelQueues.length; i++) {
            Collections.sort(fuelQSorted[i].getPassengersInQueue(), (Comparator<Passenger>) (FqCurrent, FqPrev) -> {
                String s1 = FqCurrent.getFirstName();
                String s2 = FqPrev.getFirstName();
                if (!s1.equals("Empty")) {
                    return s1.compareToIgnoreCase(s2);
                }
                return s1.compareToIgnoreCase(s1);
            });
        }
        //call method to view all queues for copy of fuel queue
        viewAllFuelQueues(fuelQSorted);
    }

    /*
    method to create and write to a file that stores fuel queue data
    @param fuelQueue object to save data about
     */
    public static void storeProgramData(FuelQueue[] fuelQueues) {
        try {
            //create file
            File file = new File("fuelQueueData.txt");
            //display message when new text file is created
            if (file.createNewFile()) {
                System.out.println("File has been created: " + file.getName());
            }

            //creating a writer and attaching file to file writer
            FileWriter fw = new FileWriter(file);

            //write multiple lines of string using into file writer using for loop
            for (int r = 0; r < fuelQueues.length; r++) {
                fw.write("\n-------------" + "Queue No. " + (r + 1) + "-------------\n");
                for (int c = 0; c < fuelQueues[r].getPassengersInQueue().size(); c++) {
                    if (!(fuelQueues[r].getPassengersInQueue().get(c).getFirstName().equals("Empty"))) {

                        fw.write("⛽ Fuel Queue - " + (r + 1) + " Slot - " + (c + 1) + ":" + "\n"
                                + "Name: " + fuelQueues[r].getPassengersInQueue().get(c).getFirstName() + " "
                                + fuelQueues[r].getPassengersInQueue().get(c).getSecondName() + "\n"
                                + "Vehicle Number: " + fuelQueues[r].getPassengersInQueue().get(c).getVehicleNo() + "\n"
                                + "Litres required: " + fuelQueues[r].getPassengersInQueue().get(c).getLitresRequired() + "\n\n");
                    } else {
                        fw.write("⛽ Fuel Queue - " + (r + 1) + " Slot - " + (c + 1) + ":" + "\nEmpty \n");
                    }
                }
            }


            //close the writer
            fw.close();
            System.out.println("Your data has been saved.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    //method to read to file that stores fuel queue data and prints it
    public static void loadProgramData() {
        try {
            File fileRead = new File("fuelQueueData.txt");

            //read the content of file by line and print it
            Scanner scan = new Scanner(fileRead);
            String fileLine;
            while (scan.hasNext()) {
                fileLine = scan.nextLine();
                System.out.println(fileLine);
            }
            scan.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
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
        Scanner inputFuel = new Scanner(System.in);
        int newStock;
        while (true) {
            try {
                System.out.print("Enter fuel quantity (litres) to add: ");
                newStock = Integer.parseInt(inputFuel.next());
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

    //method to calculate income of chosen fuel queue
    public static void calculateFuelQueueIncome(FuelQueue[] fuelQueues) {
        int queueNo;
        double income = 0.00;

        Scanner input = new Scanner(System.in);
        try {
            System.out.print("Enter Queue No. (1-5): ");
            queueNo = Integer.parseInt(input.next());

            for (int i = 0; i < fuelQueues[queueNo - 1].getPassengersInQueue().size(); i++) {
                income += fuelQueues[queueNo - 1].getPassengersInQueue().get(i).getLitresRequired() * 430;
            }

            System.out.println("Income for Queue No. " + queueNo + " is " + income + " LKR.");
        } catch (NumberFormatException e) {
            System.out.println("Valid integer is required." + "\nPlease try again.");
        }
    }

    //method to exit program
    public static void exitProgram() {
        System.out.println("Thank you!");
        System.exit(0);
    }

}