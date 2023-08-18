package PartOfRotSymmetric2DLattice;

import Coord.Coord2D;

public class HalfAxisAtMid00 implements RotationallySymmetric2DLatticeInterface {

	//Case 5:

	
	//TODO: have drawing to show it:
	// Basic quarter where rotation is at 0,0
	
	public static final int NUM_SYMMETRIES = 2;

	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j) {

		ret = new int[2][2];
		ret[0][0] = i;
		ret[1][0] = j;
		

		ret[0][1] = - i;
		ret[1][1] = - j;
		
		
		return ret;
	}
	
	
	public int getWeightOfPoint(int i, int j) {
		if(i == 0 && j == 0) {
			return 1;
		}
		return NUM_SYMMETRIES;
	}


	public String toString() {
		return "Half with axis at mid of 00";
	}

}
