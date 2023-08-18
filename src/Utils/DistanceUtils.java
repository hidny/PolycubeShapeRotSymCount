package Utils;

import java.util.LinkedList;
import java.util.Queue;

import Coord.Coord3D;
import PartOfRotSymmetric2DLattice.RotationallySymmetric2DLatticeInterface;

//TODO: this is not complete and not tested!
public class DistanceUtils {

	public static int getManhantanDist(int i1, int i2, int j1, int j2) {
		return Math.abs(i1 - i2) + Math.abs(j1 - j2);
	}

	public static int getMinNumSquaresToConnectAtStart(RotationallySymmetric2DLatticeInterface lattice, boolean disallowedCoords[][], int startI, int startJ) {
		
		int CENTER_INDEX = disallowedCoords.length / 2;
		
		int startPoints[][] = lattice.getRotationallySymmetricPoints(new int[2][4], startI, startJ);
		int weight = lattice.getWeightOfPoint(startI, startJ);
		
		if(weight <= 0) {
			return 0;
		}
		
		int currentFurthestDist = 0;
		int currentFurthestIndex = 0;
		
		for(int k=0; k<weight; k++) {
			int dist = getManhantanDist(startI, startPoints[0][k], startJ, startPoints[1][k]);
			
			
			if(dist > 0) {
				if(currentFurthestDist < dist) {
					currentFurthestDist = dist;
					currentFurthestIndex = k;
					
				}
			}
		}
		
		//System.out.println(startI + ", " + startJ);
		//System.out.println("Current Furthest dist: " + currentFurthestDist);
		
		int goalI = startPoints[0][currentFurthestIndex];
		int goalJ = startPoints[1][currentFurthestIndex];
		
		//For now, just DFS
		int dist = getDistBreadthFirst(startI, startJ, disallowedCoords, goalI, goalJ);
		
		//TODO: Do I go wild with A*?
		//Why not?
		
		int ret = dist / weight;
		if(dist % weight > 0) {
			ret++;
		}

		return ret;
	}
	

	public static final int nudgeBasedOnRotation2D[][] =
		{{-1, 0,  1,  0},
		 {0,  1,  0, -1}};

	public static int getDistBreadthFirst(int startI, int startJ, boolean disallowedCoords[][], int goalI, int goalJ) {
		
		if(startI == goalI && startJ == goalJ) {
			return 0;
		}
		
		int CENTER_INDEX = disallowedCoords.length / 2;
		
		Queue<Coord3D> queue = new LinkedList<Coord3D>();
		queue.add(new Coord3D(startI, startJ, 0));
		
		boolean found[][] = new boolean[disallowedCoords.length][disallowedCoords[0].length];
		
		for(int i=0; i<found.length; i++) {
			for(int j=0; j<found[0].length; j++) {
				found[i][j] = false;
			}
		}
		
		while( ! queue.isEmpty()) {
			
			Coord3D cur = queue.remove();
			
			//System.out.println(cur.a + ", " + cur.b + ": " + cur.c);
			//System.out.println("GOAL: " + goalI + ", " + goalJ);
			
			for(int k=0; k<nudgeBasedOnRotation2D[0].length; k++) {
				
				int newI = cur.a +  nudgeBasedOnRotation2D[0][k];
				int newJ = cur.b +  nudgeBasedOnRotation2D[1][k];
				
				if(newI == goalI && newJ == goalJ) {
					return cur.c + 1;

				} else if(CENTER_INDEX + newI < 0 || CENTER_INDEX + newI >= disallowedCoords.length 
						|| CENTER_INDEX + newJ < 0 || CENTER_INDEX + newJ >= disallowedCoords.length) {
					continue;

				} else if( ! found[CENTER_INDEX + newI][CENTER_INDEX + newJ] && !disallowedCoords[CENTER_INDEX + newI][CENTER_INDEX + newJ]) {
					
					found[CENTER_INDEX + newI][CENTER_INDEX + newJ] = true;
					queue.add(new Coord3D(newI, newJ, cur.c + 1));
				}
				
			}
			
		}
		
		return -1;
	}
	

}
