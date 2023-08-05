package PartOfRotSymmetric2DLattice;

public class HalfAxisAtLeft00 implements RotationallySymmetric2DLatticeInterface {

	//Case 3:

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	//TODO: have drawing to show it:
	// Basic quarter where rotation is at 0,0
	
	public static final int NUM_SYMMETRIES = 2;
	
	public boolean isPartOfLattice(int i, int j) {
		
		return j>= 0;
	}
	
	public int[][] getNeighbours(int ret[][], int i, int j) {
		
		for(int k=0; k<Constants.NUM_NEIGHBOURS_2D; k++) {
			
			ret[0][k] = i + Constants.nudgeBasedOnRotation[0][k];
			ret[1][k] = j + Constants.nudgeBasedOnRotation[1][k];
			

			if(ret[1][k] < 0) {
				ret[0][k] = - ret[0][k];
				ret[1][k] = 0;
			}
		}
		
		return ret;
	}
	

	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j) {


		ret[0][0] = i;
		ret[1][0] = j;
		

		ret[0][1] = - i;
		ret[1][1] = - j - 1;
		
		return ret;
	}
	

	public int getWeightOfPoint(int i, int j) {
		return NUM_SYMMETRIES;
	}

	public int getMaxNumSymmetries() {
		return NUM_SYMMETRIES;
	}
}
