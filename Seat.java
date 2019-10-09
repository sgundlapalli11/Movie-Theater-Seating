public class Seat{
	public boolean occupied;
	public int reservation;
	public int row;
	public int seat;
	public final char[] ROW_MAPPING = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

	public Seat(int row, int seat) {
		this.row = row;
		this.seat = seat;
	}

	public void reserve() {
		this.occupied = true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ROW_MAPPING[row] + "" + (seat + 1));
		return sb.toString();
	}
}