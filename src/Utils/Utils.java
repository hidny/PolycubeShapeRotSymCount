package Utils;

import Coord.Coord3D;
import PartOfRotSymmetric2DLattice.HalfAxisTopLeft00;
import PartOfRotSymmetric2DLattice.QuarterAxisTopLeft00;
import PartOfRotSymmetric2DLattice.HalfAxisAtLeft00;
import PartOfRotSymmetric2DLattice.HalfAxisAtMid00;
import PartOfRotSymmetric2DLattice.QuarterAxisAtMid00;
import PartOfRotSymmetric2DLattice.RotationallySymmetric2DLatticeInterface;

public class Utils {

	public static final int NUM_SIDES = 6;
	
	public static int[] getBorders(Coord3D cuboidsInOrderToDevelop[]) {
		
		if(cuboidsInOrderToDevelop.length ==0) {
			return null;
		}
		
		int ret[] = new int[6];
		
		Coord3D tmp = cuboidsInOrderToDevelop[0];
		
		ret[0] = tmp.a;
		ret[1] = tmp.b;
		ret[2] = tmp.c;
		ret[3] = tmp.a;
		ret[4] = tmp.b;
		ret[5] = tmp.c;
		
		for(int i=1; cuboidsInOrderToDevelop[i] != null; i++) {
			
			tmp = cuboidsInOrderToDevelop[i];
			
			if(tmp == null) {
				break;
			}
 			if(tmp.a < ret[0]) {
 				ret[0] = tmp.a;
 			}
 			if(tmp.b < ret[1]) {
 				ret[1] = tmp.b;
 			}
 			if(tmp.c < ret[2]) {
 				ret[2] = tmp.c;
 			}
 			if(tmp.a > ret[3]) {
 				ret[3] = tmp.a;
 			}
 			if(tmp.b > ret[4]) {
 				ret[4] = tmp.b;
 			}
 			if(tmp.c > ret[5]) {
 				ret[5] = tmp.c;
 			}
		}
		return ret;
	}
	
	public static void printCubes(boolean cubesUsed[][][], Coord3D cubesToDevelop[]) {
		//Just be lazy and do 2D for now:
		int startKIndex = cubesUsed.length / 2;
		
		StringBuilder sb = new StringBuilder();
		
		int borders[] = getBorders(cubesToDevelop);
		
		for(int i=borders[0]; i<borders[3] + 1; i++) {
			for(int j=borders[1]; j<borders[4] + 1; j++) {
				if(cubesUsed[i][j][startKIndex]) {
					sb.append('#');
				} else {
					sb.append('.');
				}
			}
			sb.append("|" + System.lineSeparator());
		}
		System.out.println(sb.toString());
		
	}
	
	public static void printCubesSingleDigitFirst10(boolean cubesUsed[][][], Coord3D cubesToDevelop[]) {
		//Just be lazy and do 2D for now:
		int startKIndex = cubesUsed.length / 2;
		
		StringBuilder sb = new StringBuilder();
		
		int borders[] = getBorders(cubesToDevelop);
		
		for(int i=borders[0]; i<borders[3] + 1; i++) {
			for(int j=borders[1]; j<borders[4] + 1; j++) {
				if(cubesUsed[i][j][startKIndex]) {
					
					boolean isFirst10 = false;
					int digit = -1;
					for(int m=0; m<10 && cubesToDevelop[m] != null; m++) {
						if(cubesToDevelop[m].a == i && cubesToDevelop[m].b == j && cubesToDevelop[m].c == startKIndex) {
							isFirst10 = true;
							digit = m;
							break;
						}
					}
					
					if(isFirst10) {
						sb.append(digit);
					} else {
						sb.append('#');
					}
				} else {
					sb.append('.');
				}
			}
			sb.append("|" + System.lineSeparator());
		}
		System.out.println(sb.toString());
		
	}

	
	public static void printSquares(boolean cubesToDevelop[][]) {
		
		int num = 0;
		
		for(int i=0; i<cubesToDevelop.length; i++) {
			for(int j=0; j<cubesToDevelop[0].length; j++) {
				if(cubesToDevelop[i][j]) {
					num++;
				}
			}
		}
		
		Coord3D ret[] = new Coord3D[num + 1];
		int curIndex = 0;
		
		for(int i=0; i<ret.length; i++) {
			ret[i] = null;
		}
		
		for(int i=0; i<cubesToDevelop.length; i++) {
			for(int j=0; j<cubesToDevelop[0].length; j++) {
				if(cubesToDevelop[i][j]) {
					ret[curIndex] = new Coord3D(i, j, 0);
					curIndex++;
				}
			}
		}
		
		printSquares(cubesToDevelop, ret);
		
	}
	
	public static void printSquares(boolean squaresUsed[][], Coord3D cubesToDevelop[]) {
		//Just be lazy and do 2D for now:
		
		StringBuilder sb = new StringBuilder();
		
		int borders[] = getBorders(cubesToDevelop);
		
		for(int i=borders[0]; i<borders[3] + 1; i++) {
			for(int j=borders[1]; j<borders[4] + 1; j++) {
				if(squaresUsed[i][j]) {
					sb.append('#');
				} else {
					sb.append('.');
				}
			}
			sb.append("|" + System.lineSeparator());
		}
		System.out.println(sb.toString());
		
	}
	
	
	public static int NUM_2D_NEIGHBOURDS = 4;
	
	public static final int nudgeBasedOnRotation2D[][] =
		{{-1, 0,  1,  0},
		 {0,  1,  0, -1}};
	
	
	
	//TODO: move to other file?
	
	public static boolean[][][] getDisallowed2DTransitionsBecauseItIsRedundant(RotationallySymmetric2DLatticeInterface lattice, int sizeArrayDims) {
		boolean ret[][][] = new boolean[sizeArrayDims][sizeArrayDims][NUM_2D_NEIGHBOURDS];
		int CENTER = sizeArrayDims /2;
		
		if(sizeArrayDims < 10) {
			System.out.println("ERROR: size of array for Utils.getDisallowed2DTransitions is too small");
			System.exit(1);
		}

		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				for(int k=0; k<ret[0][0].length; k++) {
					ret[i][j][k] = false;
				}
			}
		}
		
		int tmpArray[][] = new int[2][4]; 
		
		int PADDING = 3;
		
		for(int i=PADDING; i<ret.length - PADDING; i++) {
			for(int j=PADDING; j<ret[0].length - PADDING; j++) {
				
				boolean coordsUsedWithRotSymmetry[][] = new boolean[sizeArrayDims][sizeArrayDims];
				for(int i2=0; i2<ret.length; i2++) {
					for(int j2=0; j2<ret[0].length; j2++) {
						coordsUsedWithRotSymmetry[i][j] = false;
					}
				}
				
				NEXT_ROTATION:
				for(int r=0; r<NUM_2D_NEIGHBOURDS; r++) {
					
					int newi = i - CENTER + nudgeBasedOnRotation2D[0][r];
					int newj = j - CENTER + nudgeBasedOnRotation2D[1][r];
					
					int weight = lattice.getWeightOfPoint(newi, newj);
					tmpArray = lattice.getRotationallySymmetricPoints(tmpArray, newi, newj);
					
					//System.out.println("debug: " + i + ", " + j + ", " + r + "  (" + lattice + ")");
					
					for(int k=0; k<weight; k++) {
						
						int tmpi = tmpArray[0][k];
						int tmpj = tmpArray[1][k];
						
						
						if( ! coordsUsedWithRotSymmetry[tmpi + CENTER][tmpj + CENTER]) {
							coordsUsedWithRotSymmetry[tmpi + CENTER][tmpj + CENTER] = true;
						} else {
							//System.out.println("disallowed transition: " + (i - CENTER) + ", " + (j - CENTER) + ", " + r + "  (" + lattice + ")");
							ret[i][j][r] = true;
							continue NEXT_ROTATION;
						}
						
					}
					
				}
			}
		}
		
		
		return ret;
	}
	
	
	//TODO: move to constants:
	public static final int NUM_2D_LATTICE_CASES = 5;
	public static void main(String args[]) {
		
		RotationallySymmetric2DLatticeInterface lattices[] = new RotationallySymmetric2DLatticeInterface[NUM_2D_LATTICE_CASES];
		
		//TOOD: Put this in a factory class.
		
		//TODO: make these indexes constants.
		lattices[0] = new HalfAxisTopLeft00();
		lattices[1] = new HalfAxisAtLeft00();
		lattices[2] = new HalfAxisAtMid00();
		lattices[3] = new QuarterAxisTopLeft00();
		lattices[4] = new QuarterAxisAtMid00();
		
		for(int i=0; i<lattices.length; i++) {
			getDisallowed2DTransitionsBecauseItIsRedundant(lattices[i], 20);
			System.out.println();
			System.out.println();
			System.out.println();
		}
		
		
		
	}
	
	
	
	
	
}
