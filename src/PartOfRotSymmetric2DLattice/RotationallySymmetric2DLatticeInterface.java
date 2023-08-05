package PartOfRotSymmetric2DLattice;

public interface RotationallySymmetric2DLatticeInterface {

	public boolean isPartOfLattice(int i, int j);
	
	public int[][] getNeighbours(int ret[][], int i, int j);
	

	public int[][] getRotationallySymmetricPoints(int ret[][], int i, int j);
	
	public int getWeightOfPoint(int i, int j);
	
	public int getMaxNumSymmetries();
}
