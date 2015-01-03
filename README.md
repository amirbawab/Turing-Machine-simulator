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
