package PartOfRotSymmetric2DLattice;

import Coord.Coord2D;

public class BasicHalf implements RotationallySymmetric2DLatticeInterface {

	//Case 4:
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	//TODO: have drawing to show it:
	//Basic half where rotation axis is at bottom left of 0,0
	
	public static final int NUM_SYMMETRIES = 2;

	public boolean isPartOfLattice(int i, int j) {
		
		return j>= 0;
	}
	
	public int[][] getNeighbours(int ret[][], int i, int j) {
		
		for(int k=0; k<Constants.NUM_NEIGHBOURS_2D; k++) {
			
			ret[0][k] = i + Constants.nudgeBasedOnRotation[0][k];
			ret[1][k] = j + Constants.nudgeBasedOnRotation[1][k];
			

			if(ret[1][k] < 0) {
				//The +1 makes the difference between this and HalfAxisAtLeft00:
				ret[0][k] = - ret[0][k] + 1;
				ret[1][k] = 0;
			}
		}
		
		return ret;
	}
	
	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j) {

		ret[0][0] = i;
		ret[1][0] = j;
		

		ret[0][1] = - i + 1;
		ret[1][1] = - j - 1;
		
		
		
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
}
