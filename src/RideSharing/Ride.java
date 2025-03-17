package RideSharing;
 
class Ride {
    String rideId;
    Rider rider;
    Driver driver;
    String destination;
    int rating;
 
    public Ride(String rideId, Rider rider, Driver driver, String destination) {
        this.rideId = rideId;
        this.rider = rider;
        this.driver = driver;
        this.destination = destination;
        this.rating = 0;
    }
 
    public void rateRide(int rating) {
        this.rating = rating;
    }
}