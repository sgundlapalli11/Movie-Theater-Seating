import java.util.List;

public interface TheatreInterface {
	String handleReservations();

	// returns output string of seats assigned to given reservation
	String requestSeats(Reservation r);

	List<Seat> findAvailableSeats(int seatsRequested);

	void readFile(String fileName);
}