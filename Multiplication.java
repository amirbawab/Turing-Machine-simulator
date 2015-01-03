import java.io.FileNotFoundException;

import turingMachine.TuringMachine;

/**
* Turing Machine 
* Coded by Amir El Bawab
* Date: 3 January 2015
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class Multiplication {
	public static void main(String[] args) {
		try {
			
			// Import multiplication machine
			TuringMachine multiplication = TuringMachine.inParser("multiplication");
			
			// Process input
			multiplication.process("11011");
			
			// Print process steps
			System.out.println("Process steps for '11011':");
			System.out.println(multiplication.getLastProcess());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
