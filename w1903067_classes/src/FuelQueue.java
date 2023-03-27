import java.util.ArrayList;

public class FuelQueue {

    //creating an array list to store details of the passengers in queues
    private ArrayList<Passenger> passengersInQueue = new ArrayList<>(6);
    private int FuelQueueNo;

    //constructors
    public FuelQueue(int QueueNo) {
        this.FuelQueueNo = QueueNo;
    }

    //getters and setter
    public int getFuelQueueNo() {
        return FuelQueueNo;
    }

    public void setFuelQueueNo(int fuelQueueNo) {
        FuelQueueNo = fuelQueueNo;
    }

    public ArrayList<Passenger> getPassengersInQueue() {
        return passengersInQueue;
    }

    //method to add passenger into an empty slot
    public void addPassenger(Passenger passenger) {
        for (int i = 0; i < passengersInQueue.size(); i++) {
            if (passengersInQueue.get(i).getFirstName().equals("Empty")) {
                passengersInQueue.set(i, passenger);
                break;
            }
        }
    }

    public void addEmptyPassenger(Passenger passenger) {
        passengersInQueue.add(passenger);
    }

}
