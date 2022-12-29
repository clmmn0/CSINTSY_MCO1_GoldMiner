import java.awt.List;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class Map {

    private char[][] grid = new char[64][64]; // grid
    private boolean[][] gridChecked = new boolean[64][64]; // used to determine if a node/square has been checked already, to avoid repetition of scan and checking
    private int cnt = 0; // delete, just for checking of display
    private int totalRotates = 0;
    private int totalMoves = 0;
    private int totalScans = 0;

    private int LENGTH; // nxn size of the grid
    private String direction = "South"; // direction faced by the player
    private char miner = 'v'; // miner character

    private int cntRotate = 0; // number of rotates a miner has taken ( ranges from 0-4
    private int positionX = 0; // x position of the player
    private int positionY = 0; // y position of the player
    private Stack <Integer> backStackX = new Stack(); // stack that contains all past moves of the player (x-coordinates). To be used for backtracking
    private Stack <Integer> backStackY = new Stack(); // stack that contains all past moves of the player (y-coordinates). To be used for backtracking

    private int start = 1; // used for display. Useless talaga toh, but important for display. Found in move()
    private char pastValue = '.'; // used for display. Stores the char of the past position

    private boolean isGoldFound = false;
    private boolean isPit = false;
    private int positionGoldX = 0;// position of gold (x-coordinate)
    private int positionGoldY = 0;// position of gold (y-coordinate)

    private int nBeacons, nPits;	// number of beacons and pits
    private int speed;
    private String actionString = "";

    // constructor
    public Map (int n, int p, int b, Point gold, Set<Point> pit, Set<Point> beacon, int level, int s){
        LENGTH = n;

        this.nBeacons = b;
        this.nPits = p;
        this.speed = s;

        // places positions from set into list
        ArrayList<Point> pits = new ArrayList<Point>();
        ArrayList<Point> beacons = new ArrayList<Point>();

        pits.addAll(pit);
        beacons.addAll(beacon);

        setGrid(); // sets values of the grid
        setGridChecked(); // initializes whether a square/grid has been checked or not
        setGold(gold); // sets positions of the gold
        setPit(pits); // sets position of the pits
        setBeacon(beacons); // sets positions of the beacons

        displayGrid();	// shows initial state

        switch (level) {
            case 1:  { random(); break; }
            case 2:  {smart(); break; }
        }
    }

    public void setGrid(){
        for(int i = 0; i < LENGTH; i++) {
            for (int j = 0; j < LENGTH; j++) {
                grid[i][j] = '.';
            }
        }
    }

    public void setGold(Point pos){
        grid[pos.x][pos.y] = 'G';
        positionGoldX = pos.x;
        positionGoldY = pos.y;
    }

    public void setPit(ArrayList<Point> pits){
        for(int i = 0; i < nPits; i++) {
            grid[pits.get(i).x][pits.get(i).y] = 'P';
        }
    }

    public void setBeacon(ArrayList<Point> pos){
        for(int i = 0; i < nBeacons; i++) {
            grid[pos.get(i).x][pos.get(i).y] = 'B';
        }
    }

    public void setGridChecked(){
        for(int r = 0; r < 64; r++){
            for(int c = 0; c < 64; c++){
                gridChecked[r][c] = true;
            }
        }

        for(int i = 0; i < LENGTH; i++) { // sets the whole playing grid into all false
            for (int j = 0; j < LENGTH; j++) {
                gridChecked[i][j] = false;
            }
        }
    }

    public void displayGrid(){
        pause();

        grid[positionX][positionY] = miner;

        for(int c = 0; c < LENGTH; c++)
            System.out.print("----");
        System.out.print("-\n");

        for(int i = 0; i < LENGTH; i++){
            for(int j = 0; j < LENGTH; j++){
                System.out.print("| ");
                if(grid[i][j] != '.')
                    System.out.print(grid[i][j] + " ");
                else
                    System.out.print("  ");
            }
            System.out.print("|\n");
            for(int c = 0; c < LENGTH; c++)
                System.out.print("----");
            System.out.print("-\n");
        }
        System.out.println(actionString);
        System.out.println("x = " + (positionX + 1) + " y = " + (positionY + 1));
        System.out.println("Direction: " + direction);
        System.out.println("Number of Rotates: " + totalRotates);
        System.out.println("Number of Scans: " + totalScans);
        System.out.println("Number of Moves: " + totalMoves);
        System.out.println("***************************** display" + cnt + " *****************************");
        System.out.println();
        cnt++;
        actionString = "";
    }

    public void pause(){
        try{
            Thread.sleep(speed);
        } catch(InterruptedException e){
            System.out.println(e.toString());
        }
    }

    public void rotate(){
        actionString += "Miner rotated. ";

        if(direction.equals("South")) {
            direction = "West";
            miner = '<';
        }

        else if(direction.equals("East")){
            direction = "South";
            miner = 'v';
        }

        else if(direction.equals("North")){
            direction = "East";
            miner = '>';
        }

        else if(direction.equals("West")){
            direction = "North";
            miner = '^';
        }

        totalRotates++;
        displayGrid();
    }

    public char scan(){
        actionString += "Miner scanned. ";
        char c = '.';

        if(direction.equals("North")){
            c = grid[positionX-1][positionY];
        }

        else if(direction.equals("South")){
            c = grid[positionX+1][positionY];
        }

        else if(direction.equals("East")){
            c = grid[positionX][positionY+1];
        }

        else if(direction.equals("West")){
            c = grid[positionX][positionY-1];
        }

        if(c == '.')
            actionString += "Scan action returned NULL. ";
        else
            actionString += "Scan action returned " + c + ". ";

        totalScans++;
        displayGrid();
        return c;
    }

    public boolean isChecked(){
        int x = positionX;
        int y = positionY;
        boolean iC = false;

        if(direction.equals("North")){
            if(x-1 < 0 || gridChecked[x-1][y] == true) // checks if the possible move is out of bounds or has been checked already, to avoid repetition of scans and moves
                iC = true;
        }
        else if(direction.equals("South")){
            if(x+1 >= LENGTH || gridChecked[x+1][y] == true)
                iC = true;
        }
        else if(direction.equals("East")){
            if(y+1 >= LENGTH || gridChecked[x][y+1] == true)
                iC = true;
        }
        else if(direction.equals("West")){
            if(y-1 < 0 || gridChecked[x][y-1] == true)
                iC = true;
        }

        return iC;
    }

    public void move(){
        actionString += "Miner moved. ";
        cntRotate = 0; // number of rotates returns to 0

        if(start == 1) { // used for display only
            start = 0;
            grid[positionX][positionY] = '.';
            pastValue = '.';
        }
        else{
            grid[positionX][positionY] = pastValue;
        }

        if(direction.equals("North"))
            positionX--;
        else if(direction.equals("South"))
            positionX++;
        else if(direction.equals("East"))
            positionY++;
        else if(direction.equals("West"))
            positionY--;

        if(gridChecked[positionX][positionY] == false) {
            gridChecked[positionX][positionY] = true; // sets the value of the new position to true
            backStackX.push(positionX); // stores all the past moves of X into a stack. to be used to backtracking
            backStackY.push(positionY); // stores all the past moves of Y into a stack. to be used to backtracking
        }

        pastValue = grid[positionX][positionY]; // stores the value of the current position, to be displayed later
        totalMoves++;

        if(grid[positionX][positionY] == 'G') // becomes true if the current position is a gold
            isGoldFound = true;

        else if(grid[positionX][positionY] == 'P') // becomes true if the current position is a gold
            isPit = true;

        displayGrid();
    }

    public void backtrack(){
        backStackX.pop(); // deletes the past move since we will backtrack here
        backStackY.pop();
        int xPop = backStackX.peek();
        int yPop = backStackY.peek();
        int xTemp;
        int yTemp;

        if(direction.equals("North") && (xPop == positionX-1) && (yPop == positionY))
            move();
        else if(direction.equals("South") && (xPop == positionX+1) && (yPop == positionY))
            move();
        else if(direction.equals("East") && (xPop == positionX) && (yPop == positionY+1))
            move();
        else if(direction.equals("West") && (xPop == positionX) && (yPop == positionY-1))
            move();
        else {
            do {
                rotate();
                xTemp = positionX;
                yTemp = positionY;
                if (direction.equals("North"))
                    xTemp = positionX - 1;
                else if (direction.equals("South"))
                    xTemp = positionX + 1;
                else if (direction.equals("East"))
                    yTemp = positionY + 1;
                else if (direction.equals("West"))
                    yTemp = positionY - 1;
            } while (xTemp != xPop || yTemp != yPop);

            move();
        }
    }

    public boolean isBeaconValid(){
        int i = 0;
        boolean valid = true;

        if(positionX != positionGoldX && positionY != positionGoldY) {
            valid = false;
        }
        else if(positionX == positionGoldX && positionY < positionGoldY){
            for(i = positionY; i < positionGoldY; i++){
                if(grid[positionX][i] == 'P')
                    valid = false;
            }
        }
        else if(positionX == positionGoldX && positionY > positionGoldY){
            for(i = positionGoldY; i < positionY; i++){
                if(grid[positionX][i] == 'P')
                    valid = false;
            }
        }
        else if(positionY == positionGoldY && positionX < positionGoldX){
            for(i = positionX; i < positionGoldX; i++){
                if(grid[i][positionY] == 'P')
                    valid = false;
            }
        }
        else if(positionY == positionGoldY && positionX > positionGoldX){
            for(i = positionGoldX; i < positionX; i++){
                if(grid[i][positionY] == 'P')
                    valid = false;
            }
        }

        return valid;
    }

    public int getBeacon(){
        int beaconValue = 0;

        if(positionX == positionGoldX && positionY != positionGoldY){
            if(positionY > positionGoldY)
                beaconValue = positionY - positionGoldY;
            else
                beaconValue = positionGoldY - positionY;
        }
        else {
            if (positionX > positionGoldX)
                beaconValue = positionX - positionGoldX;
            else
                beaconValue = positionGoldX - positionX;
        }

        return beaconValue;
    }

    public boolean isBeaconMoveValid(){
        int xTemp = positionX;
        int yTemp = positionY;

        if(direction.equals("North")){
            if(xTemp-1 < 0 || grid[xTemp-1][yTemp] == 'P')
                return false;
        }
        else if(direction.equals("South")){
            if(xTemp+1 >= LENGTH || grid[xTemp+1][yTemp] == 'P')
                return false;
        }
        else if(direction.equals("West")){
            if(yTemp-1 < 0 || grid[xTemp][yTemp-1] == 'P')
                return false;
        }
        else if(direction.equals("East")){
            if(yTemp+1 >= LENGTH || grid[xTemp][yTemp+1] == 'P')
                return false;
        }

        return true;
    }

    public void activateBeacon(){
        int steps = 0;
        int tempBeacon = getBeacon();
        boolean isEnd = false;
        actionString += "Beacon activated! Beacon returned " + tempBeacon + ". ";

        displayGrid();

        for(int i = 0; i < 4; i++){ // looks for the gold in all 4 directions
            isEnd = false;
            steps = 0;

            if(isBeaconMoveValid() == false){ //checks if the move is valid
                rotate();
                i++;
            }

            else{
                while(isEnd == false){
                    if(isBeaconMoveValid() == true){
                        move();
                        steps++;
                    }
                    if(isGoldFound == false) {
                        if (isBeaconMoveValid() == false || steps == tempBeacon) { // indicates that the miner must go back to the beacon bc move is invalid or it has reached the number stated in the beacon
                            isEnd = true;
                        }

                        if (isEnd == true) {// this means that the miner needs to go back to the beacon since the direction is wrong
                            rotate();
                            rotate();
                            for (int j = 0; j < steps; j++) { // miner moves until he reaches the beacon again
                                move();
                            }
                        }
                    }

                    if(isGoldFound == true)  // becomes true if gold is found
                        isEnd = true;
                }

                if(isGoldFound == false) { //this means that the miner is at the beacon, and he needs to rotate, to test if the Gold is on another direction
                    for(int c = 0; c < 3; c++) { // miner rotates three times, to check the next direction
                        rotate();
                    }
                }
            }

            if(isGoldFound == true) // ends the for loop, thus it ends game because gold has been found
                i = 4;
        }
    }

    public boolean isValid() {
        int r = positionX;
        int c = positionY;
        boolean bValid = true;

        if(direction.equals("North")){
            if(r == 0) // checks if the possible move is out of bounds or has been checked already, to avoid repetition of scans and moves
                bValid = false;
        }
        else if(direction.equals("South")){
            if(r == LENGTH - 1)
                bValid = false;
        }
        else if(direction.equals("East")){
            if(c == LENGTH - 1)
                bValid = false;
        }
        else if(direction.equals("West")){
            if(c == 0)
                bValid = false;
        }

        return bValid;
    }

    public void random() {
        Random rand = new Random();

        while(!isGoldFound && !isPit) {
            int n = rand.nextInt(3);

            switch(n) {
                case 0: {
                    if(isValid() == true) // checks if the square is within array bounds
                        scan();
                    else {
                        totalScans++;
                        actionString += "Miner scanned. ";
                        displayGrid();
                        break;
                    }

                    break;
                }

                case 1: {
                    if(isValid() == true) // checks if the square is within array bounds
                        move();
                    else {
                        totalMoves++;
                        actionString += "Miner moved.";
                        displayGrid();
                        break;
                    }
                    break;
                }

                case 2: {
                    rotate();
                    break;
                }
            }
        }

        if(isGoldFound){ // makes sure that the last if condition does not execute
            System.out.println("Gold found! Search successful.");
        }

        else if(isPit) {
            System.out.println("Miner fell onto pit!");
        }
    }

    public void smart(){
        char tempScan = ' ';

        while(isGoldFound == false) {
            tempScan = ' ';

            if(isChecked() == false) {// checks if the square is within array bounds & has not been scanned before
                tempScan = scan();
            }

            if(tempScan == '.' || tempScan == 'B' || tempScan == 'G') { // checks if the scanned square is valid, if yes, the miner moves
                move();
            }
            else{ // miner rotates, if square is not a valid move
                rotate();
                cntRotate++;
            }

            if(pastValue  == 'B') {
                if(isBeaconValid() == true){
                    activateBeacon();
                }
            }

            if(isGoldFound == true){ // makes sure that the last if condition does not execute
                System.out.println("Gold found! Search successful.");
                cntRotate = 0;
            }

            if(cntRotate == 4){ // miner backtracks, if all surrounding squares are invalid
                backtrack();
            }
        }
    }
}
