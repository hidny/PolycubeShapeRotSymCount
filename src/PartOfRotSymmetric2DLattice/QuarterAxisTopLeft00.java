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
	
	public boolean isPartOfLattice(int i, int j) {
		
		return i >= 0 && j>= 0;
	}
	
	public int[][] getNeighbours(int ret[][], int i, int j) {
		
		for(int k=0; k<Constants.NUM_NEIGHBOURS_2D; k++) {
			
			ret[0][k] = i + Constants.nudgeBasedOnRotation[0][k];
			ret[1][k] = j + Constants.nudgeBasedOnRotation[1][k];
			
			if(ret[0][k] < 0) {
				ret[0][k] = ret[1][k];
				ret[1][k] = 0;
			
			} else if(ret[1][k] < 0) {

				ret[0][k] = 0;
				ret[1][k] = ret[0][k];
			}
		}
		
		return ret;
	}
	
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

	public int getMaxNumSymmetries() {
		return NUM_SYMMETRIES;
	}

	@Override
	public boolean isSolutionAcceptableAndNotDoubleCounting(Coord2D squaresUsed[]) {
		return true;
	}
	

	public String toString() {
		return "Quarter with axis at top-left of 00";
	}
}