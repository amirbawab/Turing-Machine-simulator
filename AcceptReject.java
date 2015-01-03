import turingMachine.State;
import turingMachine.Transition;
import turingMachine.TuringMachine;

/**
* Turing Machine 
* Coded by Amir El Bawab
* Date: 3 January 2015
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class AcceptReject {
	public static void main(String[] args) {
		
		// Create the machine that will only accept 1's
		TuringMachine tm = new TuringMachine('q');
		
		// Create the states
		State[] q = new State[3];
		q[0] = tm.addState(State.INITIAL);
		q[1] = tm.addState(State.FINAL);
		q[2] = tm.addState(State.NORMAL);
		
		// Add transition
		tm.addTransition(q[0], q[0], '1', '1', Transition.RIGHT);
		tm.addTransition(q[0], q[1], Transition.BLANK, Transition.BLANK, Transition.LEFT);
		tm.addTransition(q[0], q[2], '0', '0', Transition.RIGHT);
		
		// Process input 111
		if(tm.process("111")) // Accept because only 1's
			System.out.println("Accept 111");
		else
			System.out.println("Reject 111");
		
		// Process input 1011
		if(tm.process("1011")) // Reject because there's a 0
			System.out.println("Accept 1011");
		else
			System.out.println("Reject 1011");
	}
}
