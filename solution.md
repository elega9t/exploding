This solution is intended to provide a simple design
that allows implementing a deterministic application,
free from external side effects. This design is loosely 
based on the Free Monad and Tagless Final patterns.

The Random interpreter provides a way to create a reproducible
side effect (randomness) dependent system. The same pattern can be used for other
common side effects eg, db interaction, msg publishing etc.

The design allows user to encapsulate the business logic as a pure
function, independent of external side effects. This would make testing
of current system and reproducing states from any given environment possible.

The solution finally uses StateT monad from cats. However, the Tagless final pattern allows
implementing the business logic independent of any specific effect type.

The main class of the solution is **com.elega9t.exploding.Game**

The app accepts user input via command line and acts based on specific keywords.
The keywords are as follows:

1. init: initializes the app with 1 explosive, 16 blank, and 2 diffuse cards (with 1 diffuse card in users hand)
2. draw: draws a card from the deck and prints the outcome 
3. exit | quit: terminates the execution  

The current state of the game is printed as a debug statement which can be suppressed
via the logback config @ game/src/main/resources/logback.xml
