package SanityCheckRotationCount;

import java.math.BigInteger;
import java.util.HashSet;

import Utils.Utils;
import Coord.Coord3D;

public class Debug2DRotations {


	public static int NUM_REFLECTIONS = 2;
	public static int NUM_ROTATIONS = 4;
	public static int CHECK_SYMMETRIES_ONE_DIM_FACTOR = 2;

	public static int MAX_WIDTH_PLUS_ONE = 256;

	public static HashSet<BigInteger> debugUniqList = new HashSet<BigInteger>();
	
	public static HashSet<BigInteger> uniqList = new HashSet<BigInteger>();
	public static BigInteger debugLastScore = null;
	
	public static void resetUniqList() {
		uniqList = new HashSet<BigInteger>();
	}
	
	public static int getRotation180DegType(Coord3D cubesToDevelop[], boolean array[][][]) {
		
		boolean array2[][] = new boolean[array.length][array[0].length];

		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				array2[i][j] = array[i][j][array.length / 2];
				
				/*for(int k=0; k<array[0][0].length; k++) {
					if(array[i][j][k]) {
						System.out.println("Debug: " + i+","+j+","+k);
					}
				}*/
			}
		}
		
		return getRotation180DegType(cubesToDevelop, array2);
	}
	public static int getRotation180DegType(Coord3D paperToDevelop[], boolean array[][]) {

		int borders[] = Utils.getBorders(paperToDevelop);

		int firsti = borders[0];
		int lasti = borders[3];
		
		int firstj = borders[1];
		int lastj = borders[4];

		long heightShape = lasti - firsti + 1;
		long widthShape = lastj - firstj + 1;
		
		int debugCount = 0;
		
		for(int i=0; i<heightShape; i++) {
			for(int j=0; j<widthShape; j++) {
				
				if(array[firsti + i][firstj + j] != array[lasti - i][lastj - j]) {
					return -1;
				}
				
				if(array[firsti + i][firstj + j]) {
					debugCount++;
				}
			}
		}
		
		//System.out.println("Count: " + debugCount);
		//System.out.println(heightShape + ", " + widthShape);
		
		if(heightShape % 2 == 0 && widthShape % 2 == 0) {
			return 0;
		} else if(heightShape % 2 != 0 && widthShape % 2 != 0) {
			return 2;
		} else {
			return 1;
		}
	}
	
	public static int getRotation90DegType(Coord3D cubesToDevelop[], boolean array[][][]) {
		
		boolean array2[][] = new boolean[array.length][array[0].length];
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				array2[i][j] = array[i][j][array.length / 2];
			}
		}
		
		return getRotation90DegType(cubesToDevelop, array2);
	}

	public static int getRotation90DegType(Coord3D paperToDevelop[], boolean array[][]) {

		int borders[] = Utils.getBorders(paperToDevelop);

		int firsti = borders[0];
		int lasti = borders[3];
		
		int firstj = borders[1];
		int lastj = borders[4];

		long heightShape = lasti - firsti + 1;
		long widthShape = lastj - firstj + 1;
		
		if(heightShape != widthShape) {
			return -1;
		}
		
		for(int i=0; i<heightShape; i++) {
			for(int j=0; j<widthShape; j++) {
				
				if(array[firsti + i][firstj + j] != array[firsti + j][lastj - i]) {
					return -1;
				}
			}
		}
		
		if(heightShape != widthShape) {
			System.out.println("DOH!");
			System.exit(1);
			return -1;
		} else {
			
			if(heightShape % 2 == 0) {
				return 3;
			} else {
				return 4;
			}
		}
		
	}
}
