//need to calculate the total sales at the end using Parallel Stream
//

public class BookingRequest {
    private String userId;
    private int pax;

    public BookingRequest(String userId, int pax) {
        this.userId = userId;
        this.pax = pax;
    }

    public int getPax() { return pax; }
    public String getUserId() { return userId; }
}
