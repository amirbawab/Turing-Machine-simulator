import java.io.FileNotFoundException;

import turingMachine.TuringMachine;

/**
* Turing Machine 
* Coded by Amir El Bawab
* Date: 3 January 2015
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class Addition {
	public static void main(String[] args) {
		try {
			
			// Import addition machine
			TuringMachine addition = TuringMachine.inParser("addition");
			
			// Print addition machine
			System.out.println("Machine content:");
			System.out.println(addition);
			
			// Process input
			addition.process("1011");
			
			// Print the tape content after the process
			System.out.println("Tape content after processing '1011':");
			System.out.println(addition.getTapeSnapshot());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
