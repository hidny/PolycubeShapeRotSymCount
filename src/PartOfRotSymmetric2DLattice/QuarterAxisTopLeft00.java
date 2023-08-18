package PartOfRotSymmetric2DLattice;

import Coord.Coord2D;

public class QuarterAxisTopLeft00 implements RotationallySymmetric2DLatticeInterface {

	//Case 1:
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	//TODO: have drawing to show it:
	// Basic quarter where rotation is at 0,0
	
	
	public static final int NUM_SYMMETRIES = 4;
	
	
	
	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j) {

		ret = new int[2][4];
		ret[0][0] = i;
		ret[1][0] = j;
		

		ret[0][1] = 0 - j - 1;
		ret[1][1] = i;

		ret[0][2] = j;
		ret[1][2] = 0 - i - 1;

		ret[0][3] = 0 - i - 1;
		ret[1][3] = 0 - j - 1;
		
		
		return ret;
	}


	public int getWeightOfPoint(int i, int j) {
		return NUM_SYMMETRIES;
	}

	public String toString() {
		return "Quarter with axis at top-left of 00";
	}
}
