import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;

public class Theatre implements TheatreInterface{
	public final int NUM_ROWS = 10;
	public final int NUM_SEATS = 20;
	public Seat[][] seats;
	public int seatsTaken;

	public int seatingCurrentRow;
	public int nextSeat;

	public boolean right;
	public List<Reservation> reservations;
	public String fileName;
	
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
		this.right = true;
		this.seatsTaken = 0;
	}

	/**
	 * Reads in input file and creates all the reservations for the Theatre
	 * @input filename, absolute path to file containing reservation requests
	 */
	public void readFile(String filename) {
		try {
			File f = new File(filename);
			Scanner sc = new Scanner(f);
			String filenameWithExtension = f.getName();
			this.fileName = filenameWithExtension.substring(0, filenameWithExtension.lastIndexOf('.'));

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] request = line.split(" ");
				this.reservations.add(new Reservation(request[0], Integer.parseInt(request[1])));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Finds and assigns seats for each reservation and outputs result to output/ folder
	 * @return the absolute path of the output file
	 */
	public String handleReservations() {
		String outputFilePath = System.getProperty("user.dir") + "\\output\\" + this.fileName + "_output.txt";

		try {
			File output = new File(outputFilePath);
			output.createNewFile();
			FileWriter writer = new FileWriter(output);
			for (Reservation r : this.reservations) {
				writer.write(this.requestSeats(r));
			}

			writer.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return outputFilePath;
	}

	/**
	 * Finds the next available seats in the theatre and then reserves the seats
	 * Will not reserve seats if the whole reservation cannot be completed
	 * @input r, the reservation object containing the ID and # of seats requested
	 * @return String containing the reservation ID as well as the seats assigned
	 */
	public String requestSeats(Reservation r) {
		if (r.seatsRequested <= 0) {
			return "Reservation " + r.id + " requested 0 or fewer tickets. Why did you do this?\n";
		}

		if (r.seatsRequested > ((NUM_ROWS * NUM_SEATS) - seatsTaken)) {
			return "Reservation " + r.id + " was unable to be processed as" 
				+ " there weren't enough seats available\n";
		}

		List<Seat> seatsToReserve = findAvailableSeats(r.seatsRequested);
		reserveSeats(seatsToReserve);
		return generateOutputString(r, seatsToReserve); 
	}

	/**
	 * Finds the group of next available seats
	 * @input seatsRequested, the number of seats to find
	 * @return returns a list of the next amount of free seats
	 */
	public List<Seat> findAvailableSeats(int seatsRequested) {
		List<Seat> res = new ArrayList<>();
		
		while (res.size() < seatsRequested) {
			res.add(getNextSeat());
		}
		
		return res;
	}

	/**
	 * Marks the seats as occupied
	 * @input seats, the list of seats assigned to a reservation 
	 */
	public void reserveSeats(List<Seat> seats) {
		for (Seat seat : seats) {
			seat.reserve();
			this.seatsTaken++;
		}
	}

	/**
	 * Iterator for the seats in the theatre, goes from left to right, right to left, and so on
	 * starts from the backmost row and works its way up
	 * @return seat, the next available seat that is free
	 */
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

	public String generateOutputString(Reservation r, List<Seat> reservedSeats) {
		StringBuilder sb = new StringBuilder();
		sb.append(r.id + " ");
		for (Seat seat : reservedSeats) {
			sb.append(seat);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append('\n');
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < seats.length; i++) {
			for (int j = 0; j < seats[0].length; j++) {
				if (seats[i][j].occupied) {
					sb.append("1 ");
				} else {
					sb.append("0 ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Theatre t = new Theatre();

		t.readFile(args[0]);
		System.out.println(t.handleReservations());

		System.out.println(t.toString());
	}
}