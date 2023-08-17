package PartOfRotSymmetric2DLattice;


public interface RotationallySymmetric2DLatticeInterface {

	
	public boolean isPartOfLattice(int i, int j);
	
	//TODO: make int ret[][] nullable for all cases.
	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j);
	
	public int getWeightOfPoint(int i, int j);
	
	
}
