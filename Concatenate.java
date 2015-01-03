import java.io.FileNotFoundException;

import turingMachine.State;
import turingMachine.Transition;
import turingMachine.TuringMachine;

/**
* Turing Machine 
* Coded by Amir El Bawab
* Date: 3 January 2015
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class Concatenate {
	public static void main(String[] args) {
		try {
			TuringMachine addition = TuringMachine.inParser("addition");
			TuringMachine multiplication = TuringMachine.inParser("multiplication");
			TuringMachine tm = new TuringMachine('q');
			
			// Add addition & multiplication
			tm.concatinate(addition);
			tm.concatinate(multiplication);
			
			// Add linking states
			tm.addState(State.NORMAL);
			tm.addState(State.NORMAL);
			tm.addState(State.NORMAL);
			
			// Get all states
			State states[] = tm.getStates();
			
			// Create the linking transitions
			tm.addTransition(states[4], states[4], '1', '1', Transition.RIGHT);
			tm.addTransition(states[4], states[18], Transition.BLANK, '0', Transition.RIGHT);
			tm.addTransition(states[18], states[19], Transition.BLANK, '1', Transition.RIGHT);
			tm.addTransition(states[19], states[20], Transition.BLANK, '1', Transition.LEFT);
			tm.addTransition(states[20], states[20], '1', '1', Transition.LEFT);
			tm.addTransition(states[20], states[20], '0', '0', Transition.LEFT);
			tm.addTransition(states[20], states[5], Transition.BLANK, Transition.BLANK, Transition.RIGHT);
			
			// Choose initial state
			tm.chooseInitialState(states[0]);
			
			// Process an input
			tm.process("101");
			
			// Print steps
			System.out.println(tm.getLastProcess());
			
			// Export new machine
			tm.export("addition_multiplication_2");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
