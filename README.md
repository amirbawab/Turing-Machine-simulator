Turing-Machine
==============

####Methods to build your Turing machine<br>

#####Turing machine methods and complexities:

| Method        | Return | Explanation | Complexity  |
| ------------- | ------ |------------ | :----------:|
| addState(status) | Added State | Add a state to the turing machine | O(1) |
| addTransition(...) | Added Transition | Add a transition between two states | O(s1 deg) |
| removeState(s) | void | Remove state and all related transitions | O(s deg) |
| removeTransition(t) | void | Remove a transition | O(1) |
| getStates() | Array of states | Get all states in the turing machine | O(\|S\|) |
| getTransitions() | Array of transitions | Get all transitions in the turing machine | O(\|T\|) |
| getInitialState() | State | Get the initial state in the turing machine | O(1) |
| process(string) | Boolean: True if accepted, False if rejected | Process a string with the turing machine | O(Γ.Moves) <br>Γ*: Tape alphabet* |
| getTapeSnapshot() | String | Get a snapshot of the tape content | O(\|Tape\|) |
| getTapeContentArray() | Array of characters | Get the tape content as an array of characters | O(\|Tape\|) |
| chooseInitialState(state) | void | Choose the initial state for the turing machine | O(1) |
| addFinalState(state) | void | Make a state as final | O(1) |
| removeFinalState(state) | void | Remove the final status for a final state | O(1) |
| getLastProcess() | String | Get the detailed steps for the last process | O(1) |
| concatinate(machine) | void | Concatenate one or more Turing machines in one machine | O(\|S\|+\|T\|) |
| export(filename) | void | Export turing machine to a text file readable by the Turing machine parser | O(\|S\|+\|T\|log \|S\|) |

####Populate your Turing Machine

#####A) Use an input file:

######Input 1 : addition

    prefix = q
    states = 5
    blank = $
    initial = 0
    final = 4
    ;
    0,0:1,1,R
    0,1:0,1,R
    1,1:1,1,R
    1,2:$,$,L
    2,3:1,$,L
    3,3:1,1,L
    3,4:$,$,R
    ;

**Understand input file**

First line is the vertices prefix character:<br>
`prefix = C` where `C` is any character 

Second line is the number of states:<br>
`states = X` where `X` is the number of states

Third line is the blank character:<br>
`blank = B` where `B` is any character.

Fourth line is the ID of the initial state:<br>
`initial = I` where `I` is a number between `0` and `X-1`

The next K lines, where K ≥ 0, are the final states:<br>
`final = F` where `F` is a number between `0` and `X-1`

`;` is used to mark the end of state declaration and initialization <br>

The next lines are the transitions between states:<br>
`FROM, TO : READ, WRITE, MOVE` where `FROM` & `TO` are between `0` and `X-1`. `READ` & `WRITE` are any characters. `Move` is `R` for right, `L` for left or `N` for stay.<br>

`;` is used to mark the end of transitions declaration

*Note:* 
- White spaces in the above syntax are ignored.


**Function of addition machine:**

f(1<sup>m</sup>01<sup>n</sup>) = 1<sup>m+n</sup>

**Output of Addition.java :**

    Machine content:
    States:
    →(q0) (q1) (q2) (q3) ((q4)) 
    
    Transitions:
    [→(q0), →(q0) : (1 → 1, RIGHT)]
    [→(q0), (q1) : (0 → 1, RIGHT)]
    [(q1), (q1) : (1 → 1, RIGHT)]
    [(q1), (q2) : (□ → □, LEFT)]
    [(q2), (q3) : (1 → □, LEFT)]
    [(q3), (q3) : (1 → 1, LEFT)]
    [(q3), ((q4)) : (□ → □, RIGHT)]
    
    Tape content after processing '1011':
        ↓             
    [□, 1, 1, 1, □, □]

More examples: Multiplication.java, Concatenate.java

**Function of multiplication machine:**

f(1<sup>m</sup>01<sup>n</sup>) = 1<sup>m.n</sup>


**Function of addition_multiplication_2 machine (To obtain this file, execute Concatenate.java):**

f(1<sup>m</sup>01<sup>n</sup>) = 1<sup>2(m+n)</sup>

#####B) Use methods:

**Example: AcceptReject.java**

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
	
**Output of AcceptReject.java:**

    Accept 111
    Reject 1011

#####Library used:
https://github.com/amirbawab/GraphADT
