import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class initializationMenu {
	private int n;
	private int b;
	private int p;
	private int x, y;
	
	private Point gold;
	private Set<Point> pit, beacon;
	
	int level = 0;
	int speed = 1000;

	
	public initializationMenu () {
		
		gold = new Point();
		pit = new HashSet<Point>();
		beacon = new HashSet<Point>();
		
		GridSize();
		NoOfObjects();
		Positions();
		Level();
		Speed();
	}
	
	// asks for size of grid
	public void GridSize () {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		do {
			System.out.println("Choose size between 8 and 64: ");
			try {
				n = Integer.parseInt(br.readLine().trim());
			} catch (Exception e) {
				System.out.println("Invalid input.");
			}
			
			if (n < 8 || n > 64)
				System.out.println("Input out of bounds.");
		} while (n < 8 || n > 64);
	}
	
	// asks for the number of pits and beacons
	public void NoOfObjects () {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		do {
			try {
				System.out.println("Number of pits: ");
				p = Integer.parseInt(br.readLine().trim());
				System.out.println("Number of beacons: ");
				b = Integer.parseInt(br.readLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
			} catch (IOException e) {
				System.out.println("Invalid input.");
			}
			
			if ((p + b + 1) >= (n * n))
				System.out.println("Too many objects.");
			else if(p <= 0)
				System.out.println("Invalid number of pits.");
			else if (b <= 0)
				System.out.println("Invalid number of beacons.");
		} while (((p + b + 1) >= (n * n)) || (p <= 0) || (b <= 0));
	}
	
	// asks for the position of pot of gold
	public void Positions () { 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		do {			
			String coordG[] = null;
			try {
				System.out.println("Position of Pot of Gold (row,col): ");
				coordG = br.readLine().split(",");
			} catch (Exception e) {
				System.out.println("Invalid input.");
			}
			
			x = Integer.parseInt(coordG[0]) - 1;
			y = Integer.parseInt(coordG[1]) - 1;
			gold.setLocation(x, y);
			
			if ((gold.x < 0) || (gold.x >= n) || (gold.y < 0) || (gold.y >= n) || (gold.x == 0 && gold.y == 0))
				System.out.println("Invalid input.");
		} while ((gold.x < 0) || (gold.x >= n) || (gold.y < 0) || (gold.y >= n) || (gold.x == 0 && gold.y == 0));
		
		boolean hasCommon1;
		boolean hasCommon2;
		boolean hasCommon3;
		
		boolean pInvalid = false;
		boolean bInvalid = false;
		// asks for the position/s of the pit/s
		do {
			pit.clear();
			beacon.clear();
			pInvalid = false;
			bInvalid = false;
			
			String s1[] = null;
			try {
				System.out.println("Position of " + p + " Pit/s (separate each position with a space): ");
				s1 = br.readLine().split("\\s+");
			} catch (IOException e) {
				System.out.println("Invalid input.");
			}
			
			String s2[] = null;
			try {
				System.out.println("Position of " + b + " Beacon/s (separate each position with a space): ");
				s2 = br.readLine().split("\\s+");
			} catch (IOException e) {
				System.out.println("Invalid input.");
			}
			
			for(String s : s1) {
    		    String coordP[] = s.split(","); 
    		    x = Integer.parseInt(coordP[0]) - 1;
    		    y = Integer.parseInt(coordP[1]) - 1;
    		    pit.add(new Point(x,y));
    		}
			
			for(String s : s2) {
	 		    String coordB[] = s.split(","); 
			    x = Integer.parseInt(coordB[0]) - 1;
			    y = Integer.parseInt(coordB[1]) - 1;
			    beacon.add(new Point(x,y));
			}
			
			hasCommon1 = Collections.disjoint(pit, beacon);		// true if no same elements
			hasCommon2 = pit.contains(gold);					// true if has same
			hasCommon3 = beacon.contains(gold);					// true if has same
			
			if ((pit.size() != p) || (beacon.size() != b))
				System.out.println("Number of positions are not as specified.");

            // if inputed coordinates for pit's are valid
            for (int i = 0; i < p; i++) {
            	if(pit.iterator().next().x < 0 || pit.iterator().next().x >= n || pit.iterator().next().y < 0 || pit.iterator().next().y >= n || (pit.iterator().next().x == 0 && pit.iterator().next().y == 0)) {
            		pInvalid = true;
            		System.out.println("Invalid pit coordinates.");
            		break;
            	}
            }
            
            // if inputed coordinates for beacon/s are valid
			for (int j = 0; j < b; j++) {
			    if(beacon.iterator().next().x < 0 || beacon.iterator().next().x >= n || beacon.iterator().next().y < 0 || beacon.iterator().next().y >= n || (beacon.iterator().next().x == 0 && beacon.iterator().next().y == 0)) {
			    	bInvalid = true;
			    	System.out.println("Invalid beacon coordinates.");
			    	break;
			    }
			}
			
			if(hasCommon2)
				System.out.println("Overlapping coordinates of pit/s and gold.");
			if(hasCommon3)
				System.out.println("Overlapping coordinates of beacon/s and gold.");
			if(!hasCommon1)
				System.out.println("Overlapping coordinates of pit/s and beacon/s.");
		} while ((pit.size() != p) || (beacon.size() != b) || pInvalid || bInvalid || !hasCommon1 || hasCommon2 || hasCommon3);
	}
	
	// asks for level of agent
	public void Level () { 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		do {
			try {
				System.out.println("Choose level of agent ([1] Random | [2] Smart): ");
				level = Integer.parseInt(br.readLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
			} catch (IOException e) {
				System.out.println("Invalid input.");
			}
			if (level < 1 || level > 2)
				System.out.println("Invalid input.");
		} while (level < 1 || level > 2);
	}
	
	public void Speed () {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		do {
			try {
			System.out.println("Specify update speed in milliseconds: ");
			speed = Integer.parseInt(br.readLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.");
			} catch (IOException e) {
				System.out.println("Invalid input.");
			}
		} while (speed < 1);
		
		// generates map
		Map map = new Map(n, p, b, gold, pit, beacon, level, speed);
		}
	}

