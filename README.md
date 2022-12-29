# CSINTSY_MCO1_GoldMiner

A Java-based game whose main objective is for the player to find the hidden pot of gold. The game has two levels: random and smart. In random, the player randomly chooses a tile to mine and continues playing until the player falls into a pit or until the pot of gold is found - whichever happens first. In smart, the player is a smart/intelligent agent which creates rational movements. The smart agent makes use of Depth-First Search or DFS algorithm. This algorithm performs an exhaustive search of all nodes to move forward into the game. If the player has reached the end node, backtracking algorithm is performed wherein the player goes back to its previous node and explores other possible nodes to find the pot of gold. A stack was used to keep note of all movements made by the player for the backtracking. 

**Game Elements**
- Pot of Gold: if the player falls on the tile with the pot of gold, the player wins and the game ends
- Pit: if the player falls on the tile with a pit, the player loses and the game ends
- Beacon: shows how far the player is from the pot of gold in terms of the number of tiles, if the beacon and the pot of gold are in the same row or column, but does not show the direction where the pot of gold can be found

**Running the Game**
1. Type `javac driver.java` on your command prompt and run. The different classes should have been initialized after running this prompt.
2. Type `java driver` on your command prompt and run. 
3. Fill up all necessary inputs.
4. The board will generate and the game will start.
