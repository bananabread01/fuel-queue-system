public class Passenger {

    //variables to assign passenger information
    private String firstName;
    private String secondName;
    private String vehicleNo;
    private int litresRequired;

    //constructor of the class with parameters
    public Passenger(String firstName, String secondName, String vehicleNo, int litresRequired) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.vehicleNo = vehicleNo;
        this.litresRequired = litresRequired;
    }

    public Passenger (int a, int b){
        int d = b/a;
        System.out.println(d);
    }



    //getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public int getLitresRequired() {
        return litresRequired;
    }

    public void setLitresRequired(int litresRequired) {
        this.litresRequired = litresRequired;
    }
}
