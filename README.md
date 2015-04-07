Turing-Machine 1.0
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

<img src="https://raw.githubusercontent.com/amirbawab/Turing-Machine/master/documentation/addition.jpg">

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
```java
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
```	
**Output of AcceptReject.java:**

    Accept 111
    Reject 1011

####Example of step by step process for the multiplication machine:

**Function of multiplication machine:**

f(1<sup>m</sup>01<sup>n</sup>) = 1<sup>m.n</sup>

**Idea**

1. Place a zero to the right of the string to process.
2. For each 1, to the left of the first 0, convert it to X, then copy each 1, to the right of the first 0, to the right of the second 0. To copy the 1s, convert each to Y, then replace the leftmost BLANK, to the right of the second zero, by a 1. <br>
Repeat the process untill all the 1s to left of the first 0 are X, and all the 1s between the first and second 0 are Y.
3. Replace everything before and including the second zero by a BLANK.
4. Place the tape head on the first 1 in the tape.

**Code: Multiplication.java (Available in the repo)**

**Step by step process of '11011':**

	Process steps for '11011':
	 ↓             
	[1, 1, 0, 1, 1] #1
	    ↓          
	[1, 1, 0, 1, 1] #2
	       ↓       
	[1, 1, 0, 1, 1] #3
	          ↓    
	[1, 1, 0, 1, 1] #4
	             ↓ 
	[1, 1, 0, 1, 1] #5
	                ↓ 
	[1, 1, 0, 1, 1, □] #6
	             ↓    
	[1, 1, 0, 1, 1, 0] #7
	          ↓       
	[1, 1, 0, 1, 1, 0] #8
	       ↓          
	[1, 1, 0, 1, 1, 0] #9
	    ↓             
	[1, 1, 0, 1, 1, 0] #10
	 ↓                
	[1, 1, 0, 1, 1, 0] #11
	 ↓                   
	[□, 1, 1, 0, 1, 1, 0] #12
	    ↓                
	[□, 1, 1, 0, 1, 1, 0] #13
	       ↓             
	[□, X, 1, 0, 1, 1, 0] #14
	          ↓          
	[□, X, 1, 0, 1, 1, 0] #15
	             ↓       
	[□, X, 1, 0, 1, 1, 0] #16
	                ↓    
	[□, X, 1, 0, Y, 1, 0] #17
	                   ↓ 
	[□, X, 1, 0, Y, 1, 0] #18
	                      ↓ 
	[□, X, 1, 0, Y, 1, 0, □] #19
	                   ↓    
	[□, X, 1, 0, Y, 1, 0, 1] #20
	                ↓       
	[□, X, 1, 0, Y, 1, 0, 1] #21
	             ↓          
	[□, X, 1, 0, Y, 1, 0, 1] #22
	                ↓       
	[□, X, 1, 0, Y, 1, 0, 1] #23
	                   ↓    
	[□, X, 1, 0, Y, Y, 0, 1] #24
	                      ↓ 
	[□, X, 1, 0, Y, Y, 0, 1] #25
	                         ↓ 
	[□, X, 1, 0, Y, Y, 0, 1, □] #26
	                      ↓    
	[□, X, 1, 0, Y, Y, 0, 1, 1] #27
	                   ↓       
	[□, X, 1, 0, Y, Y, 0, 1, 1] #28
	                ↓          
	[□, X, 1, 0, Y, Y, 0, 1, 1] #29
	                   ↓       
	[□, X, 1, 0, Y, Y, 0, 1, 1] #30
	                ↓          
	[□, X, 1, 0, Y, Y, 0, 1, 1] #31
	             ↓             
	[□, X, 1, 0, Y, 1, 0, 1, 1] #32
	          ↓                
	[□, X, 1, 0, 1, 1, 0, 1, 1] #33
	       ↓                   
	[□, X, 1, 0, 1, 1, 0, 1, 1] #34
	    ↓                      
	[□, X, 1, 0, 1, 1, 0, 1, 1] #35
	       ↓                   
	[□, X, 1, 0, 1, 1, 0, 1, 1] #36
	          ↓                
	[□, X, X, 0, 1, 1, 0, 1, 1] #37
	             ↓             
	[□, X, X, 0, 1, 1, 0, 1, 1] #38
	                ↓          
	[□, X, X, 0, Y, 1, 0, 1, 1] #39
	                   ↓       
	[□, X, X, 0, Y, 1, 0, 1, 1] #40
	                      ↓    
	[□, X, X, 0, Y, 1, 0, 1, 1] #41
	                         ↓ 
	[□, X, X, 0, Y, 1, 0, 1, 1] #42
	                            ↓ 
	[□, X, X, 0, Y, 1, 0, 1, 1, □] #43
	                         ↓    
	[□, X, X, 0, Y, 1, 0, 1, 1, 1] #44
	                      ↓       
	[□, X, X, 0, Y, 1, 0, 1, 1, 1] #45
	                   ↓          
	[□, X, X, 0, Y, 1, 0, 1, 1, 1] #46
	                ↓             
	[□, X, X, 0, Y, 1, 0, 1, 1, 1] #47
	             ↓                
	[□, X, X, 0, Y, 1, 0, 1, 1, 1] #48
	                ↓             
	[□, X, X, 0, Y, 1, 0, 1, 1, 1] #49
	                   ↓          
	[□, X, X, 0, Y, Y, 0, 1, 1, 1] #50
	                      ↓       
	[□, X, X, 0, Y, Y, 0, 1, 1, 1] #51
	                         ↓    
	[□, X, X, 0, Y, Y, 0, 1, 1, 1] #52
	                            ↓ 
	[□, X, X, 0, Y, Y, 0, 1, 1, 1] #53
	                               ↓ 
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, □] #54
	                            ↓    
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, 1] #55
	                         ↓       
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, 1] #56
	                      ↓          
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, 1] #57
	                   ↓             
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, 1] #58
	                ↓                
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, 1] #59
	                   ↓             
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, 1] #60
	                ↓                
	[□, X, X, 0, Y, Y, 0, 1, 1, 1, 1] #61
	             ↓                   
	[□, X, X, 0, Y, 1, 0, 1, 1, 1, 1] #62
	          ↓                      
	[□, X, X, 0, 1, 1, 0, 1, 1, 1, 1] #63
	       ↓                         
	[□, X, X, 0, 1, 1, 0, 1, 1, 1, 1] #64
	          ↓                      
	[□, X, X, 0, 1, 1, 0, 1, 1, 1, 1] #65
	       ↓                         
	[□, X, X, 0, 1, 1, 0, 1, 1, 1, 1] #66
	    ↓                            
	[□, X, X, 0, 1, 1, 0, 1, 1, 1, 1] #67
	 ↓                               
	[□, X, X, 0, 1, 1, 0, 1, 1, 1, 1] #68
	    ↓                            
	[□, X, X, 0, 1, 1, 0, 1, 1, 1, 1] #69
	       ↓                         
	[□, □, X, 0, 1, 1, 0, 1, 1, 1, 1] #70
	          ↓                      
	[□, □, □, 0, 1, 1, 0, 1, 1, 1, 1] #71
	             ↓                   
	[□, □, □, □, 1, 1, 0, 1, 1, 1, 1] #72
	                ↓                
	[□, □, □, □, □, 1, 0, 1, 1, 1, 1] #73
	                   ↓             
	[□, □, □, □, □, □, 0, 1, 1, 1, 1] #74
	                      ↓          
	[□, □, □, □, □, □, □, 1, 1, 1, 1] #75
	


#####Library used:
https://github.com/amirbawab/GraphADT
