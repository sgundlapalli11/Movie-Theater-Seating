import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;

public class Theatre implements TheatreInterface{
	public final int NUM_ROWS = 10;
	public final int NUM_SEATS = 20;
	public Seat[][] seats;
	public int seatsTaken;

	public int seatingCurrentRow;
	public int nextSeat;

	public boolean right = true;
	public List<Reservation> reservations;
	
	public Theatre() {
		this.seats = new Seat[NUM_ROWS][NUM_SEATS];
		for (int i = 0; i < seats.length; i++) {
			for (int j = 0; j < seats[0].length; j++) {
				seats[i][j] = new Seat(i, j);
			}
		}

		this.nextSeat = 0;
		this.seatingCurrentRow = this.seats.length - 1;
		this.reservations = new ArrayList<>();
	}

	public void readFile(String fileName) {
		try {
			Scanner sc = new Scanner(new File(fileName));

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] request = line.split(" ");
				System.out.println(Arrays.toString(request));
				this.reservations.add(new Reservation(request[0], Integer.parseInt(request[1])));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void handleReservations() {
		for (Reservation r : this.reservations) {
			this.requestSeats(r);
		}
	}

	public String requestSeats(Reservation r) {
		if (r.seatsRequested <= 0) {
			return "Reservation " + r.id + "requested 0 or fewer tickets. Why did you do this?";
		}

		if (r.seatsRequested > ((NUM_ROWS * NUM_SEATS) - seatsTaken)) {
			return "Reservation " + r.id + " was unable to be processed as" 
				+ " there weren't enough seats available";
		}

		List<Seat> seatsToReserve = findAvailableSeats(r.seatsRequested);
		System.out.println(seatsToReserve.toString());
		reserveSeats(seatsToReserve);
		return generateOutputString(r, seatsToReserve); 
	}

	public List<Seat> findAvailableSeats(int seatsRequested) {
		List<Seat> res = new ArrayList<>();
		
		while (res.size() < seatsRequested) {
			res.add(getNextSeat());
		}
		

		return res;
	}

	public void reserveSeats(List<Seat> seats) {
		for (Seat seat : seats) {
			seat.reserve();
		}
		this.seatsTaken++;
	}

	public Seat getNextSeat(){
		Seat res = this.seats[seatingCurrentRow][nextSeat];

		if (this.right) {
			nextSeat++;
			if (this.nextSeat == seats[0].length) {
				seatingCurrentRow--;
				nextSeat = seats[0].length - 1;
				this.right = !this.right;
			}
		} else {
			nextSeat--;
			if (this.nextSeat == -1) {
				seatingCurrentRow--;
				nextSeat = 0;
				this.right = !this.right;
			}
		}


		return res;
	}

	public String generateOutputString(Reservation r, List<Seat> seatsToReserve) {
		StringBuilder sb = new StringBuilder();
		sb.append(r.id + " ");
		for (Seat seat : seatsToReserve) {
			sb.append(seat);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < seats.length; i++) {
			for (int j = 0; j < seats[0].length; j++) {
				if (seats[i][j].occupied) {
					sb.append("1 ");
					//System.out.print("1 ");
				} else {
					sb.append("0 ");
					//System.out.print("0 ");
				}
			}
			sb.append("\n");
			//System.out.println("");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Theatre t = new Theatre();

		t.readFile(args[0]);
		t.handleReservations();

		// System.out.println(t.requestSeats(new Reservation("R000", 2)));
		// System.out.println(t.requestSeats(new Reservation("R001", 5)));
		// System.out.println(t.requestSeats(new Reservation("R002", 200)));
		// System.out.println(t.requestSeats(new Reservation("R003", 50)));
		// System.out.println(t.requestSeats(new Reservation("R004", 22)));
		// System.out.println(t.requestSeats(new Reservation("R005", 21)));
		//System.out.println(t.requestSeats(new Reservation("R004", 100)));
		System.out.println(t.toString());
	}
}