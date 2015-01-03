package turingMachine;
import graph.Edge;

/**
* Turing Machine 
* Coded by Amir El Bawab
* Date: 3 January 2015
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class Transition {
	
	// Attributes
	private char read;
	private char write;
	private int move;
	
	// Move constants
	public final static int RIGHT = 0;
	public final static int LEFT = 1;
	public final static int STAY = 2;
	
	// Edge that stores this Transition
	private Edge<State, Transition> edge;
	
	// Reserved constants
	public final static char BLANK = '□';
	
	/**
	 * Constructor
	 * @param s1
	 * @param s2
	 * @param read
	 * @param write
	 * @param move
	 */
	protected Transition(State s1, State s2, char read, char write, int move) {
		
		// Assign attributes
		setRead(read);
		setWrite(write);
		setMove(move);
	}
	
	/**
	 * Get the read
	 * @return read
	 */
	public char getRead() {
		return read;
	}
	
	/**
	 * Set the read
	 * @param read
	 */
	public void setRead(char read){
		this.read = read;
	}
	
	/**
	 * Get the write
	 * @return write
	 */
	public char getWrite() {
		return write;
	}

	/**
	 * Set the write
	 * @param write
	 */
	public void setWrite(char write){
		this.write = write;
	}
	
	/**
	 * Get the move
	 * @return move
	 */
	public int getMove() {
		return move;
	}
	
	/**
	 * Set the move
	 * @param move
	 */
	public void setMove(int move){
		
		// Move should be RIGHT | LEFT | STAY
		if(move < RIGHT || move > STAY)
			throw new TuringMachineException("Please enter a valid move");
		this.move = move;
	}

	/**
	 * Get the move as a string
	 * @return move string
	 */
	public String getMoveString(){
		return move == LEFT ? "LEFT" : "RIGHT";
	}

	/**
	 * Get the edge that stores this transition
	 * @return
	 */
	protected Edge<State, Transition> getEdge() {
		return edge;
	}

	/**
	 * Set the edge that stores this transition
	 * @param edge
	 */
	protected void setEdge(Edge<State, Transition> edge) {
		this.edge = edge;
	}
	
	/**
	 * To string of a transition
	 */
	public String toString(){
		return String.format("[%s, %s : (%c → %c, %s)]", edge.getV1().getData(), edge.getV2().getData(), read, write, getMoveString());
	}
}
