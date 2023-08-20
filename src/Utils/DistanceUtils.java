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


	public static final int nudgeBasedOnRotation2D[][] =
		{{-1, 0,  1,  0},
		 {0,  1,  0, -1}};

	
	public static int[][] getDistancesFromGoal(boolean disallowedCoords[][], int goalI, int goalJ) {
		
		int ret[][] = new int[disallowedCoords.length][disallowedCoords[0].length];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				ret[i][j] = -1;
			}
		}

		boolean found[][] = new boolean[disallowedCoords.length][disallowedCoords[0].length];
		
		for(int i=0; i<found.length; i++) {
			for(int j=0; j<found[0].length; j++) {
				found[i][j] = false;
			}
		}
		
		int CENTER_INDEX = disallowedCoords.length / 2;
		
		Queue<Coord3D> queue = new LinkedList<Coord3D>();

		queue.add(new Coord3D(goalI, goalJ, 0));
		ret[CENTER_INDEX + goalI][CENTER_INDEX + goalJ] = 0;
		found[CENTER_INDEX + goalI][CENTER_INDEX + goalJ] = true;
		
		while( ! queue.isEmpty()) {
			
			Coord3D cur = queue.remove();
			
			//System.out.println(cur.a + ", " + cur.b + ": " + cur.c);
			//System.out.println("GOAL: " + goalI + ", " + goalJ);
			
			for(int k=0; k<nudgeBasedOnRotation2D[0].length; k++) {
				
				int newI = cur.a +  nudgeBasedOnRotation2D[0][k];
				int newJ = cur.b +  nudgeBasedOnRotation2D[1][k];
				
				if(CENTER_INDEX + newI < 0 || CENTER_INDEX + newI >= disallowedCoords.length 
						|| CENTER_INDEX + newJ < 0 || CENTER_INDEX + newJ >= disallowedCoords.length) {
					continue;

				} else if( ! found[CENTER_INDEX + newI][CENTER_INDEX + newJ] && !disallowedCoords[CENTER_INDEX + newI][CENTER_INDEX + newJ]) {
					
					found[CENTER_INDEX + newI][CENTER_INDEX + newJ] = true;

					queue.add(new Coord3D(newI, newJ, cur.c + 1));
					ret[CENTER_INDEX + newI][CENTER_INDEX + newJ] = cur.c + 1;
				}
				
			}
			
		}
		
		
		//debugPrintDist(ret);
		
		return ret;
		
	}

	public static int[] getGoalCoord(RotationallySymmetric2DLatticeInterface lattice, boolean disallowedCoords[][], int startI, int startJ) {
			
			int startPoints[][] = lattice.getRotationallySymmetricPoints(new int[2][4], startI, startJ);
			int weight = lattice.getWeightOfPoint(startI, startJ);
			
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
			
			return new int[] {goalI, goalJ};
	}
	
	//TODO: delete next two functions:
	public static int getMinNumSquaresToConnectAtStart2(RotationallySymmetric2DLatticeInterface lattice, int distances[][], int startI, int startJ) {
		
		int CENTER_INDEX = distances.length / 2;
		
		//For now, just DFS
		int dist = distances[CENTER_INDEX + startI][CENTER_INDEX + startJ];
		
		//TODO: Do I go wild with A*?
		//Why not?
		
		int weight = lattice.getWeightOfPoint(startI, startJ);
		
		int ret = dist / weight;
		if(dist % weight > 0) {
			ret++;
		}

		return ret;
	}

	public static void debugPrintDist(int distances[][]) {
		
		for(int i=0; i<distances.length; i++) {
			for(int j=0; j<distances[0].length; j++) {
				
				int tmp = distances[i][j];
				
				if(tmp < 0) {
					System.out.print(" " + tmp);
				} else if(tmp < 10) {
					System.out.print("  " + tmp);
				} else {
					System.out.print(" " + tmp);
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
