package turingMachine;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import graph.doublyLinkedList.DLLNode;
import graph.doublyLinkedList.DoublyLinkedList;
import graph.doublyLinkedList.NodeIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Turing Machine 
* Coded by Amir El Bawab
* Date: 3 January 2015
* License: MIT License ~ Please read License.txt for more information about the usage of this software
* */
public class TuringMachine {
	
	// Attributes
	private Graph<State,Transition> turing;
	private DoublyLinkedList<Character> tape;
	private char vertexPrefix;
	private DLLNode<Character> tapeHead;
	private String lastProcess;
	private State initialState;
	
	/**
	 * Constructor
	 * @param vertexPrefix
	 */
	public TuringMachine(char vertexPrefix) {
		
		// DiGraph
		turing = new Graph<>(true);
		this.vertexPrefix = vertexPrefix;
	}
	
	/**
	 * Add a state
	 * @param status
	 * @return added state
	 */
	public State addState(int status){
		
		// Only one initial state
		if(initialState != null && status == State.INITIAL)
			throw new TuringMachineException("You cannot have more than one initial state");
		
		State state = new State(status);
		Vertex<State, Transition> vertex = turing.addVertex(state);
		state.setName(String.format("%c%d", vertexPrefix, vertex.getID()));
		state.setVertex(vertex);
		
		// Assign the initial and/or final state
		if(status == State.INITIAL || status == State.INITIAL_FINAL)
			initialState = state;
		
		// Return the added states
		return state;
	}
	
	/**
	 * Add a transition
	 * @param s1
	 * @param s2
	 * @param read
	 * @param write
	 * @param move
	 * @return Added transition
	 */
	public Transition addTransition(State s1, State s2, char read, char write, int move){
		
		// A final state cannot have transitions
		if(s1.isFinal())
			throw new TuringMachineException("A final state cannot have an outgoing transition.");
		
		// Only one transition for each read is allowed
		NodeIterator<Edge<State,Transition>> iterE = s1.getVertex().getOutEdges();
		while(iterE.hasNext())
			if(iterE.next().getLabel().getRead() == read)
				throw new TuringMachineException(String.format("Only one transition is allowed when readin '%c'", read));
		
		// Create the transition
		Transition transition = new Transition(s1,s2,read,write,move);
		Edge<State, Transition> edge[] = turing.addEdge(s1.getVertex(), s2.getVertex(),transition,0.0);
		transition.setEdge(edge[0]);
		
		// Return added transition
		return transition;
	}
	
	/**
	 * Remove a state
	 * @param state
	 */
	public void removeState(State state){
		if(state.isInitial())
			initialState = null;
		turing.removeVertex(state.getVertex());
	}
	
	/**
	 * Remove a transition
	 * @param transition
	 */
	public void removeTransition(Transition transition){
		turing.removeEdge(transition.getEdge());
	}
	
	/**
	 * Get the array of states
	 * @return array of states
	 */
	public State[] getStates(){
		
		// Create the states array
		State[] states = new State[turing.vertices().size()];
		NodeIterator<Vertex<State, Transition>> iter = turing.vertices();
		int index=0;
		while(iter.hasNext())
			states[index++] = iter.next().getData();
		return states;
	}
	
	/**
	 * Get the array of transitions
	 * @return array of transitions
	 */
	public Transition[] getTransitions(){
		
		// Create the transitions array
		Transition[] transition = new Transition[turing.edges().size()];
		NodeIterator<Edge<State, Transition>> iter = turing.edges();
		int index=0;
		while(iter.hasNext())
			transition[index++] = iter.next().getLabel();
		return transition;
	}
	
	/**
	 * Get the initial state
	 * @return initial state
	 */
	public State getInitialState(){
		return initialState;
	}
	
	/**
	 * Process a string
	 * @param input
	 */
	public boolean process(String input){
		
		// An initial state is required to start
		if(initialState == null)
			throw new TuringMachineException("An initial state is required");
		
		// Reset the last process steps
		this.lastProcess = "";
		int stepCounter = 0;
		
		// Create the infinite tape
		tape = new DoublyLinkedList<>();
		
		// Copy input to the tape
		for(int i=0; i<input.length(); i++)
			tape.add(input.charAt(i));
		
		// Initialize the configuration
		State currentState = initialState;
		
		// If no node exist in the tape, add a blank node
		if(tape.first() == null) 
			tape.add(Transition.BLANK);
		tapeHead = tape.first();
		
		// While the current state is not final
		while(! currentState.isFinal()){
			
			// Record steps
			lastProcess += String.format("%s #%d\n", getTapeSnapshot(), ++stepCounter);
			
			// Check all outgoing transitions from a state
			NodeIterator<Edge<State, Transition>> outEdges = currentState.getVertex().getOutEdges();
			boolean transitionFound = false;
			while(outEdges.hasNext() && !transitionFound){
				Edge<State, Transition> currentTransition = outEdges.next();
				
				// If read match
				if(currentTransition.getLabel().getRead() == tapeHead.getData()){
					
					// Replace tape head character
					tapeHead.setData(currentTransition.getLabel().getWrite());
					
					// Move tape head
					if(currentTransition.getLabel().getMove() == Transition.LEFT){
						
						// If move left but has no left, put BLANK
						if(!tapeHead.hasPrevious())
							tape.addFirst(Transition.BLANK);
						
						tapeHead = tapeHead.previous();
					} else if(currentTransition.getLabel().getMove() == Transition.RIGHT){
						
						// If move right but has no right, put BLANK
						if(!tapeHead.hasNext())
							tape.add(Transition.BLANK);

						tapeHead = tapeHead.next();
					}
					
					// Move state
					currentState = currentTransition.getV2().getData();
					transitionFound = true;
				}
			}
			
			// If no transition found, halt and return false
			if(!transitionFound)
				return false;
		}
		
		// Record the last step
		lastProcess += String.format("%s #%d\n", getTapeSnapshot(), ++stepCounter);
		
		// If halts on a final state, return true
		return true;
	}
	
	/**
	 * Get tape content with the tape head
	 * @return tape content | NULL
	 */
	public String getTapeSnapshot(){
		
		// If tape is not set
		if(tape == null)
			return null;
		
		String pointerLine = "";
		DLLNode<Character> currentNode = tape.first();
		while(currentNode != null){
			if(currentNode == tapeHead)
				pointerLine += " ↓ ";
			else
				pointerLine += "   ";
			currentNode = currentNode.next();
		}
		return String.format("%s\n%s", pointerLine,tape.toString());
	}
	
	/**
	 * Get tape content as an array
	 * @return content array
	 */
	public char[] getTapeContentArray(){
		char[] tape_array = new char[tape.size()];
		int index = 0;
		NodeIterator<Character> iterC = tape.iterator();
		while(iterC.hasNext())
			tape_array[index++] = iterC.next();
		return tape_array;
	}
	
	/**
	 * Remove old initial state (if any). Choose an initial state
	 * @param stateID
	 */
	public void chooseInitialState(State state){
		
		// If the new initial state is the same as the old one, return
		if(initialState == state)
			return;
		
		// If there was an initial state
		if(initialState != null){
			
			// Adjust the old 
			if(initialState.getStatus() == State.INITIAL_FINAL)
				initialState.setStatus(State.FINAL);
			else
				initialState.setStatus(State.NORMAL);
		}
		
		// Adjust the initial state
		initialState = state;
		
		// Adjust the new initial state status
		if(state.getStatus() == State.FINAL)
			state.setStatus(State.INITIAL_FINAL);
		else
			state.setStatus(State.INITIAL);
	}
	
	/**
	 * Add a final state
	 * @param state
	 */
	public void addFinalState(State state){
		
		// If state has at least one transition, throw error
		if(state.getVertex().getOutEdges().size() > 0)
			throw new TuringMachineException(String.format("%s cannot be a final state because it has outgoing transitions", state.getName()));
		
		if(state.getStatus() == State.INITIAL){
			state.setStatus(State.INITIAL_FINAL);
		}else if(state.getStatus() == State.NORMAL){
			state.setStatus(State.FINAL);
		}
	}
	
	/**
	 * Remove a final state
	 * @param state
	 */
	public void removeFinalState(State state){
		if(state.getStatus() == State.FINAL)
			state.setStatus(State.NORMAL);
		else if(state.getStatus() == State.INITIAL_FINAL)
			state.setStatus(State.INITIAL);
	}
	
	/**
	 * Get the last process steps
	 * @return last process steps
	 */
	public String getLastProcess(){
		return this.lastProcess;
	}
	
	/**
	 * Get a list of states and transitions
	 * @return List of states and transitions
	 */
	public String toString(){
		String output = "States:\n";
		NodeIterator<Vertex<State, Transition>> iterV = turing.vertices();
		while(iterV.hasNext())
			output += String.format("%s ", iterV.next().getData());
		
		output += "\n\nTransitions:\n";
		NodeIterator<Edge<State, Transition>> iterE = turing.edges();
		while(iterE.hasNext())
			output += String.format("%s\n", iterE.next().getLabel());

		return output;
	}
	
	/**
	 * Import a machine to an existing machine
	 * @param machine
	 * @param keepState (if false, all cloned states will be set to NORMAL)
	 */
	public void concatinate(TuringMachine machine){
		
		// Prepare the arrays of states
		State imported_states[] = machine.getStates();
		Vertex<State, Transition> imported_statesV[] = new Vertex[imported_states.length];
		State cloned_states[] = new State[imported_states.length];
		
		// Initialize the states for the new machine
		for(int i=0; i<imported_states.length; i++){
			cloned_states[i] = this.addState(State.NORMAL);
			imported_statesV[i] = imported_states[i].getVertex();
		}
		
		// Copy the transitions
		for(int i=0; i<imported_states.length; i++){
			State currentState = imported_states[i];
			NodeIterator<Edge<State,Transition>> iterE = currentState.getVertex().getOutEdges();
			while(iterE.hasNext()){
				Edge<State,Transition> currentEdge = iterE.next();
				State s1 = cloned_states[i];
				State s2 = cloned_states[turing.getIndexOfVertexByID(imported_statesV, currentEdge.getV2().getID())];
				this.addTransition(s1, s2, currentEdge.getLabel().getRead(), currentEdge.getLabel().getWrite(), currentEdge.getLabel().getMove());
			}
		}
	}
	
	/////////////////////////// I/O HELPER ///////////////////////////////
	
	/**
	 * Parse input to populate the turing machine
	 * @param file
	 * @return Turing machine
	 * @throws FileNotFoundException
	 */
	public static TuringMachine inParser(String file) throws FileNotFoundException{
		TuringMachine machine;
		State states[];
		Scanner scanFile = new Scanner(new File(file));
		String readLine;
		Pattern pattern;
		Matcher matcher;
		int[] statesStatus;
		char blankChar;
		
		// Read the machine prefix
		readLine = scanFile.nextLine();
		pattern = Pattern.compile("prefix\\s*=\\s*(.)");
		matcher = pattern.matcher(readLine);
		matcher.find();
		machine = new TuringMachine(matcher.group(1).charAt(0));
		
		// Read number of states
		readLine = scanFile.nextLine();
		pattern = Pattern.compile("states\\s*=\\s*(\\d+)");
		matcher = pattern.matcher(readLine);
		matcher.find();
		states = new State[Integer.parseInt(matcher.group(1))];
		statesStatus = new int[states.length];
		
		// Read the special blank character
		readLine = scanFile.nextLine();
		pattern = Pattern.compile("blank\\s*=\\s*(.)");
		matcher = pattern.matcher(readLine);
		matcher.find();
		blankChar = matcher.group(1).charAt(0);
		
		// Read the initial state
		readLine = scanFile.nextLine();
		pattern = Pattern.compile("initial\\s*=\\s*(\\d+)");
		matcher = pattern.matcher(readLine);
		matcher.find();
		statesStatus[Integer.parseInt(matcher.group(1))] = State.INITIAL;
		
		// While there more final states
		while(!(readLine = scanFile.nextLine()).equals(";") ){
			
			// Read the final states
			pattern = Pattern.compile("final\\s*=\\s*(\\d+)");
			matcher = pattern.matcher(readLine);
			matcher.find();
			statesStatus[Integer.parseInt(matcher.group(1))] = State.FINAL;
		}
				
		// Initialize all states
		for(int i=0; i<states.length; i++){
			if(statesStatus[i] == 0)
				states[i] = machine.addState(State.NORMAL);
			else
				states[i] = machine.addState(statesStatus[i]);
		}
		
		// Read all transitions
		while(!(readLine = scanFile.nextLine()).equals(";") ){
			
			// Read the final states
			pattern = Pattern.compile("(\\d+)\\s*,\\s*(\\d+)\\s*:\\s*([^\\s,]+)\\s*,\\s*([^\\s,]+)\\s*,\\s*(R|L|N)");
			matcher = pattern.matcher(readLine);
			matcher.find();
			
			int move = -1;
			switch(matcher.group(5).charAt(0)){
			case 'R':
				move = Transition.RIGHT;
				break;
			case 'L':
				move = Transition.LEFT;
				break;
			case 'N':
				move = Transition.STAY;
				break;
			}
			
			char read = matcher.group(3).charAt(0) == blankChar ? Transition.BLANK : matcher.group(3).charAt(0);
			char write = matcher.group(4).charAt(0) == blankChar ? Transition.BLANK : matcher.group(4).charAt(0);
			machine.addTransition(states[Integer.parseInt(matcher.group(1))], states[Integer.parseInt(matcher.group(2))], read, write, move);
		}
		
		scanFile.close();
		return machine;
	}
	
	/**
	 * Export Turing Machine to input file
	 * @param filename
	 * @throws FileNotFoundException 
	 */
	public void export(String filename) throws FileNotFoundException{
		
		// Print writer
		PrintWriter write = new PrintWriter(filename);
		
		// Store all vertices of the machine
		Vertex<State, Transition> statesV[] = turing.vertices_array();
		
		write.println(String.format("prefix = %c", vertexPrefix));
		write.println(String.format("states = %d", turing.vertices().size()));
		write.println(String.format("blank = %c", Transition.BLANK));
		if(initialState != null)
			write.println(String.format("initial = %d", turing.getIndexOfVertexByID(statesV, initialState.getVertex().getID())));
		else
			write.println("initial = <Enter a state id>");
		
		// Write all final states
		for(int i=0; i<statesV.length; i++){
			if(statesV[i].getData().isFinal())
				write.println(String.format("final = %d", i));
		}
		
		write.println(";");
		
		// Write all transitions
		for(int i=0; i<statesV.length; i++){
			
			NodeIterator<Edge<State,Transition>> iterE = statesV[i].getOutEdges();
			while(iterE.hasNext()){
				Transition transition = iterE.next().getLabel();
				char move;
				if(transition.getMove() == Transition.LEFT)
					move = 'L';
				else if(transition.getMove() == Transition.RIGHT)
					move = 'R';
				else
					move = 'N';
				write.println(String.format("%d,%d : %c,%c,%c", i, turing.getIndexOfVertexByID(statesV, transition.getEdge().getV2().getID()), transition.getRead(), transition.getWrite(), move));
			}
		}
		
		write.println(";");
		write.close();
	}
}
