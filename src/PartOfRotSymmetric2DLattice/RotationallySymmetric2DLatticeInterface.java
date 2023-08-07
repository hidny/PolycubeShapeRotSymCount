package PartOfRotSymmetric2DLattice;

import Coord.Coord2D;

public interface RotationallySymmetric2DLatticeInterface {

	public boolean isPartOfLattice(int i, int j);
	
	public boolean isSolutionAcceptableAndNotDoubleCounting(Coord2D squaresUsed[]);
	
	public int[][] getNeighbours(int ret[][], int i, int j);
	

	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j);
	
	public int getWeightOfPoint(int i, int j);
	
	public int getMaxNumSymmetries();
	
}
