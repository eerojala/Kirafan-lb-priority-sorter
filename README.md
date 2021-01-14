# Kirafan LB priority sorter
### What is Kirafan and 'LB'
_Kirafan_ (short for _Kirara Fantasia_) is an online RPG for smartphones created by Drecom and released in december 2017. LB is short for 'limit break' (_限界突破_) which in the context of the game means increasing a character's level cap (as opposed to a game like _Final Fantasy VII_ where a limit break means a powerful special attack instead). Limit breaking a character requires using items which are in a limited supply (in a month a player can get enough of these items to fully limit break ~2-3 characters per month, assuming they do not spend excessive amounts of money on the game). Therefore I created an arbitrary flowchart which I use to determine the order in which I should limit break my characters . I used to do this process manually in notepad++ but I figured I could create a small desktop application that does the sorting automatically instead.

### About the application
This application has a JavaFX GUI which I created with Gluon Scene Builder. With the GUI I can input all the relevant data I need, and with the date the application will automatically sort the characters based on my flowchart and show me a sorted list on the GUI. The inputted data is stored to and read from .json files so I do not need to reinput the data every time I restart the program. The program uses the JsonDB library for h andling the .json files.

### What did I learn during the making of this program
* Java (and many other languages) handle arithmetic operations on doubles poorly. Store the double in two separate integers or use classes like BigDecimal for the operations instead.
* Objects with bidirectional relations (e.g. in this program's case ```gameCharacter.preferredWeapon``` and ```weapon.ExclusiveCharacter```) cause infinite recursions when they are serialized into .json files with Jackson (JsonDB is essentially provides a MongoDB-like API for Jackson), and need to be taken care of with annotations like @JsonIgnore, @JsonIdentityInfo, etc. (https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion)