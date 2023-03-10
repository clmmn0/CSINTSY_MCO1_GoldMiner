# CSINTSY_MCO1_GoldMiner

A Java-based game whose main objective is for the player to find the hidden pot of gold. The game has two levels: random and smart. In random, the player randomly chooses a tile to mine and continues playing until the player falls into a pit or until the pot of gold is found - whichever happens first. In smart, the player is a smart/intelligent agent which creates rational movements. The smart agent makes use of Depth-First Search or DFS algorithm. This algorithm performs an exhaustive search of all nodes to move forward into the game. If the player has reached the end node, backtracking algorithm is performed wherein the player goes back to its previous node and explores other possible nodes to find the pot of gold. A stack was used to keep note of all movements made by the player for the backtracking. 

**Game Elements**
- Pot of Gold: if the player falls on the tile with the pot of gold, the player wins and the game ends
- Pit: if the player falls on the tile with a pit, the player loses and the game ends
- Beacon: shows how far the player is from the pot of gold in terms of the number of tiles, if the beacon and the pot of gold are in the same row or column, but does not show the direction where the pot of gold can be found

**Player Capabilities**
- Rotate: the player can rotate which changes its direction, but the player can only rotate clockwise
- Move Foward: the player can move one tile forward which depends on the direction that the player is facing
- Scan: the player can scan the tile in front of it to determine if it is a blank tile or if it contains a pot of gold, pit, or beacon

# How to Run the Program
1. Clone this repository.
2. Navigate to the cloned version of this repository.
3. Open the command prompt.
4. Type and run `javac driver.java` in the command prompt. An instance of the driver class must be created upon running this prompt.
5. Type and run `java driver` in the command prompt. 
6. Wait for the program to start.
7. Fill up all input fields.
8. Play the game.

You may also run the program using applications like Visual Studio Code.
