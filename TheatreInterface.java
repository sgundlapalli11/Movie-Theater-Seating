import java.util.List;

public interface TheatreInterface {
	String requestSeats(Reservation r);

	List<Seat> findAvailableSeats(int seatsRequested);

	void readFile(String fileName);
}