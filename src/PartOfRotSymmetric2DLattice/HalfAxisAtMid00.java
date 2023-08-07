package PartOfRotSymmetric2DLattice;

import Coord.Coord2D;

public class HalfAxisAtMid00 implements RotationallySymmetric2DLatticeInterface {

	//Case 5:

	
	//TODO: have drawing to show it:
	// Basic quarter where rotation is at 0,0
	
	public static final int NUM_SYMMETRIES = 2;

	public boolean isPartOfLattice(int i, int j) {
		
		return j >= 1 || ( i <= 0 && j >= 0);
	}
	
	public int[][] getNeighbours(int ret[][], int i, int j) {
		
		if(i==0 && j== 0) {
			for(int k=0; k<Constants.NUM_NEIGHBOURS_2D / 2; k++) {
				
				ret[0][k] = i + Constants.nudgeBasedOnRotation[0][k];
				ret[1][k] = j + Constants.nudgeBasedOnRotation[1][k];
			}
			for(int k=0; k<Constants.NUM_NEIGHBOURS_2D / 2; k++) {
				
				ret[0][2 + k] = i + Constants.nudgeBasedOnRotation[0][k];
				ret[1][2 + k] = j + Constants.nudgeBasedOnRotation[1][k];
			}
			
			return ret;
		}
		
		for(int k=0; k<Constants.NUM_NEIGHBOURS_2D; k++) {
			
			ret[0][k] = i + Constants.nudgeBasedOnRotation[0][k];
			ret[1][k] = j + Constants.nudgeBasedOnRotation[1][k];
			

			if(ret[1][k] < 0) {

				ret[0][k] = - ret[0][k];
				ret[1][k] = 1;
			
			} else if(ret[1][k] == 0 && ret[0][k] > 0) {

				ret[0][k] = - ret[0][k];
				ret[1][k] = 0;
				
			}
		}
		
		return ret;
	}
	

	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j) {


		ret[0][0] = i;
		ret[1][0] = j;
		

		if(i == 0 && j == 0) {

			ret[0][1] = 0;
			ret[1][1] = 0;
		} else {
			
			ret[0][1] = - i;
			
			ret[1][1] = - j;
		}
		
		return ret;
	}
	
	
	public int getWeightOfPoint(int i, int j) {
		if(i == 0 && j == 0) {
			return 1;
		}
		return NUM_SYMMETRIES;
	}

	public int getMaxNumSymmetries() {
		return NUM_SYMMETRIES;
	}

	// Edge case where the solutions for this lattice could be double-counted is handled here:
	@Override
	public boolean isSolutionAcceptableAndNotDoubleCounting(Coord2D squaresUsed[]) {
		
		return true;
		/*for(int i=0; i<squaresUsed.length && squaresUsed[i] != null; i++) {
			
			if(squaresUsed[i].a < 0) {
				return true;
			}
		}
		return false;*/
	}

}
