package giq;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class KnightsDialer {
	
	int dialBoardwidth = 3;
	int dialBoardHeight = 4;
	List<List<Cell>> board;

	public static void main(String[] args) {
		KnightsDialer kd = new KnightsDialer();
		kd.printBoard();
		
		boolean printNumbers = false;
		
		for (int len=1; len<=9; len++) {
			// start finding numbers
			long time = System.currentTimeMillis();
			List<String> numbers = kd.findNumbers(1, len);
			time = System.currentTimeMillis() - time;
			out("\nLenght: " + len + ", found numbers: " + numbers.size() + " (" + time + " ms)");
			
			// print found numbers
			if (printNumbers) {
				for (int i=0; i<numbers.size(); i++) {
					out((i+1) + ":\t" + numbers.get(i));
				}
			}
			
			// check all numbers are unique
			out("All numbers are distinct: " + kd.checkDistiction(numbers));
		}
	}
	
	public KnightsDialer() {
		this.board = generateDialBoard();
	}
	
	/**
	 * Entry method for Knight's dialer.
	 * @param startKeyNumber - start key number of the Knight - number of key on dial board
	 * @param phoneNumberLenght - length of phone number to generate
	 * @return - list of strings with phone numbers for given length
	 */
	public List<String> findNumbers(int startKeyNumber, int phoneNumberLenght) {
		Pos startPos = getPositionByPadNumber(startKeyNumber);
		if (startPos != null) {
			return makeAHop(startPos, 0, phoneNumberLenght, new LinkedList<Integer>(), new LinkedList<String>());
		} else {
			return new LinkedList<String>();
		}
	}
	
	/**
	 * Main method which collects passed hops as array of strings via recursion.
	 */
	List<String> makeAHop(Pos pos, int currDepth, int maxDepth, List<Integer> path, List<String> numbers) {
		currDepth++;														// current depth
		push(path, cellValue(pos));											// remember current position in path
		if (currDepth < maxDepth) {											// check we not exceeded depth for this path
			List<Pos> nextHops = getPossibleHops(pos);						// obtain possible knight hops
			for (Pos hop : nextHops) {										// for each found hop
				if (!hop.equals(pos)) {										// check we not going back in path
					List<Integer> branch = new LinkedList<>(path);			// create new branch for new hop
					makeAHop(hop, currDepth, maxDepth, branch, numbers);	// dive into this hop recursively
				}
			}
		} else {															// else we made enough recursion
			if (numbers.size() < Integer.MAX_VALUE) {
				numbers.add(pathToString(path));							// convert passed path into string of numbers
			} else {
				out("max int value exceeded for solution array size");
			}
		}
		return numbers;
	}
	
	/**
	 * Generate all possible hops from given position
	 */
	List<Pos> getPossibleHops(Pos pos) {
		List<Pos> hops = new ArrayList<>();
		addHop(hops, getHop(pos, 2, 1));
		addHop(hops, getHop(pos, 2, -1));
		addHop(hops, getHop(pos, -2, 1));
		addHop(hops, getHop(pos, -2, -1));
		addHop(hops, getHop(pos, 1, 2));
		addHop(hops, getHop(pos, 1, -2));
		addHop(hops, getHop(pos, -1, 2));
		addHop(hops, getHop(pos, -1, -2));
		return hops;
	}
	
	/**
	 * Generates new position from given position to delta x and delta y.
	 */
	Pos getHop(Pos fromPos, int dx, int dy) {
		return new Pos(fromPos.x + dx, fromPos.y + dy);
	}
	
	/**
	 * Adds to list of hops if given position is valid.
	 */
	void addHop(List<Pos> hops, Pos pos) {
		if (isValidPosition(pos)) {
			hops.add(pos);
		}
	}
	
	/**
	 * Checks is it correct position on dial board.
	 */
	boolean isValidPosition(Pos pos) {
		return pos.x >= 0                  // should be positive x
			&& pos.x < dialBoardwidth      // x not exceeded by width
			&& pos.y >= 0                  // y is positive value
			&& pos.y < dialBoardHeight     // y not exceeded by height
			&& cellValue(pos) >= 0;        // and located not under empty pad
	}
	
	/**
	 * Returns key pad value under given position.
	 */
	int cellValue(Pos pos) {
		return board.get(pos.y).get(pos.x).value;
	}
	
	Pos getPositionByPadNumber(int keyPadNumber) {
		for (List<Cell> row : board) {
			for (Cell cell : row) {
				if (cell.value == keyPadNumber) {
					return cell.pos;
				}
			}
		}
		return null;
	}
	
	void push(List<Integer> list, int item) {
		list.add(item);
	}
	
	int pop(List<Integer> list) {
		int res = -1;
		int index = list.size() - 1;
		if (index >= 0) {
			res = list.get(index);
			list.remove(index);
		}
		return res;
	}
	
	public static void out(Object o) {
		System.out.println(o);
	}
	
	/**
	 * Generator of the dial board.
	 * Should be like this:
	 * 1 2 3
	 * 4 5 6
	 * 7 8 9
	 *   0
	 * Empty pad contains -1 value.
	 */
	List<List<Cell>> generateDialBoard() {
		List<List<Cell>> board = new ArrayList<>(dialBoardHeight);
		int cnt = 0;
		for (int y=0; y<dialBoardHeight; y++) {
			List<Cell> row = new ArrayList<Cell>(dialBoardwidth);
			for (int x=0; x<dialBoardwidth; x++) {
				int cellValue = ++cnt;
				if (cellValue == 10 || cellValue == 12) cellValue = -1;
				else if (cellValue == 11) cellValue = 0;
				row.add(x, new Cell(new Pos(x, y), cellValue));
			}
			board.add(y, row);
		}
		return board;
	}

	/**
	 * Displays generated dial board.
	 */
	void printBoard() {
		System.out.println("Dial board:");
		for (List<Cell> row : board) {
			for (Cell cell : row) {
				System.out.print(cell.value > -1 ? cell.value + " " : "  ");
			}
			System.out.println();
		}
	}
	
	String pathToString(List<Integer> path) {
		return path.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(""));
	}
	
	void printNumber(List<Integer> list) {
		for (int i : list) {
			System.out.print(i);
		}
		System.out.println();
	}
	
	boolean checkDistiction(List<String> list) {
		Set<String> s = new HashSet<>(list);
		return list.size() == s.size();
	}
}

/**
 * Position of key pad within dial board.
 */
class Pos {
	int x;
	int y;
	
	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		Pos p = (Pos) obj;
		return x == p.x && y == p.y;
	}
	
	@Override
	public String toString() {
		return "Pos:{x=" + x + ", y=" + y + "}";
	}
}

/**
 * Cell attributes value object.
 */
class Cell {
	Pos pos;   // position of the key pad
	int value; // key pad number value
	
	public Cell(Pos pos, int value) {
		this.pos = pos;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
