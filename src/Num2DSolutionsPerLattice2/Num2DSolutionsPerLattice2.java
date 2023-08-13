package Num2DSolutionsPerLattice2;

import java.util.LinkedList;

import Coord.Coord2D;
import NumSymmetricCubes2DSolve.TopHalfIterator;
import PartOfRotSymmetric2DLattice.HalfAxisTopLeft00;
import PartOfRotSymmetric2DLattice.QuarterAxisTopLeft00;
import PartOfRotSymmetric2DLattice.Constants;
import PartOfRotSymmetric2DLattice.HalfAxisAtLeft00;
import PartOfRotSymmetric2DLattice.HalfAxisAtMid00;
import PartOfRotSymmetric2DLattice.QuarterAxisAtMid00;
import PartOfRotSymmetric2DLattice.RotationallySymmetric2DLatticeInterface;
import Utils.Utils;

public class Num2DSolutionsPerLattice2 {
	
	//TODO: read https://arxiv.org/pdf/cond-mat/0007239.pdf to understand how n=56 is even achievable.

	//TODO: Count the number of rotationally sym answers as n increases!
	
	// Numbers I got from https://oeis.org/A001931
	// I added a 1 at the beginning, because I decided there's 1 way to do it with 0 cubes.
	public static final long numFixed2DPolycubes[] = 
		{1, 1, 2, 6, 19, 63, 216, 760, 2725, 9910, 36446, 135268, 505861, 1903890, 7204874, 27394666, 104592937, 400795844, 1540820542, 5940738676L, 22964779660L, 88983512783L, 345532572678L, 1344372335524L, 5239988770268L, 20457802016011L, 79992676367108L, 313224032098244L, 1228088671826973L};
	
	/*
	 * A000988		Number of one-sided polyominoes with n cells.
(Formerly M1749 N0693)		+20
22
1, 1, 1, 2, 7, 18, 60, 196, 704, 2500, 9189, 33896, 126759, 476270, 1802312, 6849777, 26152418, 100203194, 385221143, 1485200848, 5741256764, 22245940545, 86383382827, 336093325058, 1309998125640, 5114451441106, 19998172734786, 78306011677182, 307022182222506, 1205243866707468, 4736694001644862

	 */
	public static final long expected[] = new long[]{1, 1, 1, 2, 7, 18, 60, 196, 704, 2500, 9189, 33896, 126759, 476270, 1802312, 6849777, 26152418, 100203194, 385221143, 1485200848, 5741256764L, 22245940545L, 86383382827L, 336093325058L, 1309998125640L, 5114451441106L, 19998172734786L, 78306011677182L, 307022182222506L, 1205243866707468L, 4736694001644862L};
	
	
	
	public static void main(String args[]) {
		
		firstFewNValuesTest();
		//testSolve();
	}
	
	public static int DEBUG_TEST_N = 20;

	public static void testSolve() {
		RotationallySymmetric2DLatticeInterface lattices[] = new RotationallySymmetric2DLatticeInterface[NUM_2D_LATTICE_CASES];
		
		//TOOD: Put this in a factory class.
		
		//TODO: make these indexes constants.
		lattices[0] = new HalfAxisTopLeft00();
		lattices[1] = new HalfAxisAtLeft00();
		lattices[2] = new HalfAxisAtMid00();
		lattices[3] = new QuarterAxisTopLeft00();
		lattices[4] = new QuarterAxisAtMid00();
		
		int LATTICE_INDEX = 2;
		
		long ret = getNumSymmetriesForLattice(DEBUG_TEST_N, lattices[LATTICE_INDEX]); // N =2 gives 1 solution
		
		System.out.println("Num found for N = " + DEBUG_TEST_N + " and lattice index " + LATTICE_INDEX + ": " + ret);
		
	}


	/*
	 * A000105		Number of free polyominoes (or square animals) with n cells.
(Formerly M1425 N0561)		+20
156
1, 1, 1, 2, 5, 12, 35, 108, 369, 1285, 4655, 17073, 63600, 238591, 901971, 3426576, 13079255, 50107909, 192622052, 742624232, 2870671950, 11123060678, 43191857688, 168047007728, 654999700403, 2557227044764, 9999088822075, 39153010938487, 153511100594603

	 */
	
	/* Debug N=16
	 * Sanity check results:
	 * Debugging 2D rotations:
Number of rotations by type:
Type 0: 2685
Type 1: 13520
Type 2: 472
Type 3: 19
Type 4: 10

Lattice2 results:
Solutions for (Half with axis at top left of 00): 2685
Solutions for (Half with axis at left of 00): 6760  (x2 because this doesn't also count top of 00)
Solutions for (Half with axis at mid of 00): 472
Solutions for (Quarter with axis at top-left of 00): 19
Solutions for (Quarter with axis at mid of 00): 9

Looks like I missed 1 for Quarter with axis at mid of 00...
	 */

	public static void firstFewNValuesTest() {

		int MAX_N = 20;
		
		long series[] = new long[MAX_N + 1];
		
		long total = 0L;
		for(int i=MAX_N; i<=MAX_N; i++) {
			System.out.println("Search for weight " + i);
			long ret = solveForN(i);
			
			System.out.println("Answer for weight " + i + ": " + ret);
			System.out.println();
			
			series[i] = ret;
			
			total += ret;
		}
		
		//total += solveForN(56);
		
		System.out.println("Total: " + total);
		
		System.out.println("Printing series obtained:");

		for(int i=0; i<=MAX_N; i++) {
			System.out.println(i + ": " + series[i] + " (expected: " + expected[i] + ")");
			
			if(series[i] != expected[i]) {
				System.out.println("********* TEST FAIL **********");
			}
		}
		//solveForN(4);
	}
	
	public static final int PADDING_FACTOR = 10;
	
	public static final int NUM_2D_LATTICE_CASES = 5;
	
	public static final int NUM_2D_NEIGHBOURS = 4;
	
	public static final int NOT_APPLICABLE = -2;
	
	
	public static long solveForN(int n) {

		if(n == 0) {
			return 1L;
		}
		
		RotationallySymmetric2DLatticeInterface lattices[] = new RotationallySymmetric2DLatticeInterface[NUM_2D_LATTICE_CASES];
		
		//TOOD: Put this in a factory class.
		lattices[0] = new HalfAxisTopLeft00();
		lattices[1] = new HalfAxisAtLeft00();
		lattices[2] = new HalfAxisAtMid00();
		lattices[3] = new QuarterAxisTopLeft00();
		lattices[4] = new QuarterAxisAtMid00();
		
		long symmetries[] = new long[lattices.length];

		for(int i=0; i<symmetries.length; i++) {
			symmetries[i] = getNumSymmetriesForLattice(n, lattices[i]);
			System.out.println("------");
			System.out.println("------");
		}

		System.out.println("Numbers for N = " + n);
		System.out.println("Num fixed polycubes: " + numFixed2DPolycubes[n]);
		for(int i=0; i<symmetries.length; i++) {
			System.out.println("Solutions for (" + lattices[i] + "): " + symmetries[i]);
		}

		//long retMult4 = numFixed2DPolycubes[n] + 2 * symmetries[0] + 2 * symmetries[1] + symmetries[2]   +  symmetries[3] + 2 * symmetries[4];
		long retMult4 = numFixed2DPolycubes[n] + 1 * symmetries[0] + 2 * symmetries[1] + 1 * symmetries[2] +  2 * symmetries[3] + 2 * symmetries[4];
		
		
		if(retMult4 % 4 != 0) {
			System.out.println("ERROR: the number of solutions don't divide cleanly into 4. Got: " + retMult4);
			
			if(n != 16) {
				System.exit(1);
			}
		}
		
		long ret = retMult4 / 4;
		
		return ret;
		
	}
	
	public static long getNumSymmetriesForLattice(int n, RotationallySymmetric2DLatticeInterface lattice) {
		
		if(n == 0) {
			return 1L;
		}
		
		long ret = 0L;
		
		int startI = 0;
		int startJ = 0;
		
		
		boolean disallowedCoords[][] = new boolean[PADDING_FACTOR * n][PADDING_FACTOR * n];
		int CENTER = disallowedCoords.length /2;
		
		boolean disallowedTransitions[][][] = Utils.getDisallowed2DTransitionsBecauseItIsRedundant(lattice, disallowedCoords.length);
		
		
		//TODO: I don't know what the limit should be. (n/2 seems ok for now)
		while(startI < (n+1)/2) {

			//System.out.println("Using this startI and startJ coord: " + startI + ", " + startJ);
			ret += countFor2DLattice(n, lattice, disallowedCoords, disallowedTransitions, startI, startJ);

			int coordsToDisallow[][] = null;
			coordsToDisallow = lattice.getRotationallySymmetricPoints(tmpArray, startI, startJ);

			for(int k=0; k<lattice.getWeightOfPoint(startI, startJ); k++) {
				disallowedCoords[coordsToDisallow[0][k] + CENTER][coordsToDisallow[1][k] + CENTER] = true;
			}
			
			do {
				int coord[] = TopHalfIterator.getNext(startI, startJ);
				startI = coord[0];
				startJ = coord[1];
			
			} while( ! lattice.isPartOfLattice(startI, startJ));
			

			
		}
		
		return ret;
	}
	
	public static long countFor2DLattice(int targetWeight, RotationallySymmetric2DLatticeInterface lattice, boolean disallowedCoords[][], boolean disallowedTransitions[][][], int startI, int startJ) {
		
		int CENTER = disallowedCoords.length /2;
		
		if( ! lattice.isPartOfLattice(startI, startJ)) {
			return 0L;
		}
		
		/*if(lattice.toString().contains("with axis at mid of 00")
				&& lattice.toString().contains("Quarter")
				&& disallowedCoords[CENTER][CENTER]
				&& disallowedCoords[CENTER + 1][CENTER]
						) {
			System.out.println("DEBUG");
		}*/
		
		if(disallowedCoords[CENTER + startI][CENTER + startJ]) {
			System.out.println("ERROR: startI and startJ are on a disallowed coordinate!");
			System.exit(1);
		}
		
		boolean coordsUsedWithRotSymmetry[][] = new boolean[disallowedCoords.length][disallowedCoords.length];
		
		
		int currentWeight = lattice.getWeightOfPoint(startI, startJ);

		int rotationallySymmetricPointsHolder[][] = new int[2][lattice.getMaxNumSymmetries()];
		
		//for(int i=0; i<)
		rotationallySymmetricPointsHolder = lattice.getRotationallySymmetricPoints(rotationallySymmetricPointsHolder, startI, startJ);
		
		for(int i=0; i<rotationallySymmetricPointsHolder[0].length; i++) {
			coordsUsedWithRotSymmetry[rotationallySymmetricPointsHolder[0][i] + CENTER]
									 [rotationallySymmetricPointsHolder[1][i] + CENTER] = true;
		}
		
		if(targetWeight < currentWeight) {
			return 0L;
		} else if(targetWeight == currentWeight) {
			
			if(isConnected(coordsUsedWithRotSymmetry, startI, startJ, currentWeight, CENTER)) {
				System.out.println("Rotationally symmetric solution:");
				Utils.printSquares(coordsUsedWithRotSymmetry);
				return 1L;
			} else {
				return 0L;
			}
			
		}
		
		//TODO: Start recursion HERE
		Coord2D squaresToDevelop[] = new Coord2D[targetWeight + 1];
		boolean debugNope = false;
		long debugIterations[] = new long[targetWeight + 1];
		int minIndexToUse = 0;
		int minRotationToUse = 0;
		
		int squaresOrdering[][] = new int[disallowedCoords.length][disallowedCoords.length];
		
		for(int i=0; i<squaresOrdering.length; i++) {
			for(int j=0; j<squaresOrdering[0].length; j++) {
				squaresOrdering[i][j] = NOT_APPLICABLE;
			}
		}
		
		for(int i=0; i<rotationallySymmetricPointsHolder[0].length; i++) {
			squaresOrdering[rotationallySymmetricPointsHolder[0][i] + CENTER]
						   [rotationallySymmetricPointsHolder[1][i] + CENTER] = 0;
		}
		
		
		
		int numCellsUsedDepth = 1;
		squaresToDevelop[0] = new Coord2D(startI, startJ);
		
		
		return doDepthFirstSearch(squaresToDevelop, numCellsUsedDepth,
				debugNope, debugIterations,
				squaresOrdering, minIndexToUse, minRotationToUse,
				lattice, currentWeight, coordsUsedWithRotSymmetry, disallowedCoords, disallowedTransitions,
				targetWeight, CENTER);
	}
	
	//This is wildly inefficient.
	public static boolean isConnected(boolean coordsUsedWithRotSymmetry[][], int startI, int startJ, int weight, int CENTER) {
		
		
		int numFound = 1;
		
		LinkedList<Coord2D> queue = new LinkedList<Coord2D>();
		
		//TODO: Avoid making new here?
		queue.add(new Coord2D(startI, startJ));
		
		boolean coordsFound[][] = new boolean[coordsUsedWithRotSymmetry.length][coordsUsedWithRotSymmetry[0].length];
		
		coordsFound[CENTER + startI][CENTER + startJ] = true;

		while(queue.isEmpty() == false) {
			
			Coord2D curCoord = queue.getFirst();
			queue.remove();
			
			
			for(int k=0; k<Constants.NUM_NEIGHBOURS_2D; k++) {
				
				int newI = curCoord.a + Constants.nudgeBasedOnRotation[0][k];
				int newJ = curCoord.b + Constants.nudgeBasedOnRotation[1][k];
				
				if(coordsUsedWithRotSymmetry[CENTER + newI][CENTER + newJ] && ! coordsFound[CENTER + newI][CENTER + newJ]) {
					numFound++;
					queue.add(new Coord2D(newI, newJ));
					coordsFound[CENTER + newI][CENTER + newJ] = true;
				}
			}
			
		}
		
		/*
		System.out.println("StartI " + startI);
		System.out.println("StartJ " + startJ);
		
		System.out.println("Num found: " + numFound);
		*/
		if(numFound > weight) {
			System.out.println("AHH! Found too many squares");
			Utils.printSquares(coordsUsedWithRotSymmetry);
			System.exit(1);
			return false;
			
		} else if(numFound == weight) {
			numSolutionsSoFarDebug++;
			return true;

		} else {
			return false;

		}
	}
	

	public static final int nudgeBasedOnRotation[][] = {{-1, 0,  1,  0},
														{0,  1,  0, -1}};
	
	public static long numIterations = 0L;
	public static long numSolutionsSoFarDebug = 0L;
	
	public static final int NOT_INSERTED = -1;
	
	public static int tmpArray[][] = new int[2][4];
	
	
	//TODO: recursive function
	public static long doDepthFirstSearch(Coord2D squaresToDevelop[], int numCellsUsedDepth,
			boolean debugNope, long debugIterations[],
			int squaresOrdering[][], int minIndexToUse, int minRotationToUse,
			RotationallySymmetric2DLatticeInterface lattice, int currentWeight, boolean coordsUsedWithRotSymmetry[][], boolean disallowedCoords[][], boolean disallowedTransitions[][][],
			int targetWeight, int CENTER_ARRAY) {

		
		/*System.out.println("boom " + currentWeight);

		System.out.println("temp");
		Utils.Utils.printSquares(coordsUsedWithRotSymmetry);
		System.out.println("tmp2");
		Utils.Utils.printSquares(coordsUsed);
		System.out.println("End tmp");
		*/
		if(currentWeight > targetWeight) {
			return 0L;
		} else if(currentWeight == targetWeight) {
			
			if(isConnected(coordsUsedWithRotSymmetry, squaresToDevelop[0].a, squaresToDevelop[0].b, targetWeight, CENTER_ARRAY)
				/*&& lattice.isSolutionAcceptableAndNotDoubleCounting(squaresToDevelop)*/) {

				numSolutionsSoFarDebug++;

				if(targetWeight < 7
						|| (targetWeight == 16 && lattice.toString().contains("Quarter"))) {
					System.out.println("hello " + lattice);
					Utils.printSquares(coordsUsedWithRotSymmetry);
				}
				
				if(numSolutionsSoFarDebug % 100000 == 0) {
					System.out.println("Solution " + numSolutionsSoFarDebug + ":");
					Utils.printSquares(coordsUsedWithRotSymmetry);
				}
				return 1L;
			} else {
				return 0L;
			}
		}
		
		numIterations++;

		//Display debug/what's-going-on update:
		if(numIterations % 1000000L == 0) {
			
			System.out.println("Num iterations: " + numIterations);
			Utils.printSquares(coordsUsedWithRotSymmetry);
			
			System.out.println("Solutions: " + numSolutionsSoFarDebug);
			System.out.println();
			
			
		}
		//End display debug/what's-going-on update
		debugIterations[numCellsUsedDepth] = numIterations;
		
		long retDuplicateSolutions = 0L;
		
		//DEPTH-FIRST START:
		for(int curOrderedIndexToUse=minIndexToUse; curOrderedIndexToUse<numCellsUsedDepth && squaresToDevelop[curOrderedIndexToUse] != null; curOrderedIndexToUse++) {
			

			//Try to attach a cell onto indexToUse using all rotations:
			for(int dirNewCellAdd=0; dirNewCellAdd<NUM_2D_NEIGHBOURS; dirNewCellAdd++) {
				
				if(curOrderedIndexToUse == minIndexToUse
						&& dirNewCellAdd <  minRotationToUse) {
					continue;
				}

				int new_i = squaresToDevelop[curOrderedIndexToUse].a + nudgeBasedOnRotation[0][dirNewCellAdd];
				int new_j = squaresToDevelop[curOrderedIndexToUse].b + nudgeBasedOnRotation[1][dirNewCellAdd];
				
				if(coordsUsedWithRotSymmetry[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j]) {
					//Cell we are considering to add is already there...
					continue;

				} else if(disallowedCoords[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j]) {
					continue;

				} else if(disallowedTransitions[CENTER_ARRAY + squaresToDevelop[curOrderedIndexToUse].a][CENTER_ARRAY + squaresToDevelop[curOrderedIndexToUse].b][dirNewCellAdd]) {
					//System.out.println("Debug: Disallowed transition!");
					continue;
				}
				
				
				
				boolean cantAddCellBecauseOfOtherNeighbours = cantAddCellBecauseOfOtherNeighbours(
						squaresToDevelop, coordsUsedWithRotSymmetry, numCellsUsedDepth,
						debugNope, debugIterations,
						squaresOrdering, curOrderedIndexToUse, dirNewCellAdd,
						new_i, new_j,
						CENTER_ARRAY
					);
				
				if( ! cantAddCellBecauseOfOtherNeighbours) {


					//Setup for adding new cube:
					
					tmpArray = lattice.getRotationallySymmetricPoints(tmpArray, new_i, new_j);
					
					int weightOfPoint = lattice.getWeightOfPoint(new_i, new_j);
					for(int i=0; i<weightOfPoint; i++) {
						coordsUsedWithRotSymmetry[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = true;
						squaresOrdering[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = numCellsUsedDepth;
					}
					currentWeight += weightOfPoint;
					
					
					squaresToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);
					//End setup

					numCellsUsedDepth += 1;
				
					retDuplicateSolutions += doDepthFirstSearch(squaresToDevelop, numCellsUsedDepth,
							debugNope, debugIterations,
							squaresOrdering, curOrderedIndexToUse, dirNewCellAdd,
							lattice, currentWeight, coordsUsedWithRotSymmetry, disallowedCoords, disallowedTransitions,
							targetWeight, CENTER_ARRAY
						);
					
					numCellsUsedDepth -= 1;

					
					//Tear down (undo add of new cell)
					squaresToDevelop[numCellsUsedDepth] = null;
					
					tmpArray = lattice.getRotationallySymmetricPoints(tmpArray, new_i, new_j);
					
					for(int i=0; i<weightOfPoint; i++) {
						coordsUsedWithRotSymmetry[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = false;
						squaresOrdering[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = NOT_INSERTED;
					}
					currentWeight -= weightOfPoint;
					
					//End tear down


				} // End recursive if cond
			} // End loop rotation
		} //End loop index

		return retDuplicateSolutions;
	}
	
	
	//I'm enforcing an artificial constraint where the polycube shape
		// has to develop in the same order as a breath-first-search.
		// This has a lot of advantages that I will need to explain in some docs.
		public static boolean cantAddCellBecauseOfOtherNeighbours(Coord2D squaresToDevelop[], boolean squaresUsed[][], int numCellsUsedDepth,
				boolean debugNope, long debugIterations[],
				
				//TODO: will minRotaitonToUse even be a thing?
				int squaresOrdering[][], int curOrderedIndexToUse, int minRotationToUse,
				int new_i, int new_j,
				int CENTER_ARRAY) {

			boolean cantAddCellBecauseOfOtherNeighbours = false;
			
			//TODO: just record the nudges next time:
			int neighboursBasedOnRotation[][] = {{new_i-1,   new_j},
												 {new_i,   new_j+1},
												 {new_i+1,   new_j},
												 {new_i, new_j - 1}
												 };
			
			
			for(int rotReq=0; rotReq<neighboursBasedOnRotation.length; rotReq++) {
				
				int i1 = neighboursBasedOnRotation[rotReq][0];
				int j1 = neighboursBasedOnRotation[rotReq][1];
			
				if(squaresToDevelop[curOrderedIndexToUse].a == i1 
					&& squaresToDevelop[curOrderedIndexToUse].b == j1) {
					
					continue;
				}
				
				//System.out.println("Cube neighbour:" + i1 + ", " + j1 + ", " + k1);
				
				if(squaresUsed[CENTER_ARRAY + i1][CENTER_ARRAY + j1]) {
					//System.out.println("Connected to another paper");
					
					int orderOtherCell = squaresOrdering[CENTER_ARRAY + i1][CENTER_ARRAY + j1];
			
					if(orderOtherCell < curOrderedIndexToUse ) {
						cantAddCellBecauseOfOtherNeighbours = true;
						break;
					}
					
				}
			}

			return cantAddCellBecauseOfOtherNeighbours;
		}
		
}
