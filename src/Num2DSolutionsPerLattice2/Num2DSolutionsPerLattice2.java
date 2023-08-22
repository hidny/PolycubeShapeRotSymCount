package Num2DSolutionsPerLattice2;

import java.util.LinkedList;

import Coord.Coord2D;
import NumSymmetricCubes2DSolve.coordIterator;
import PartOfRotSymmetric2DLattice.Constants;
import PartOfRotSymmetric2DLattice.HalfAxisAtLeft00;
import PartOfRotSymmetric2DLattice.HalfAxisAtMid00;
import PartOfRotSymmetric2DLattice.HalfAxisTopLeft00;
import PartOfRotSymmetric2DLattice.QuarterAxisAtMid00;
import PartOfRotSymmetric2DLattice.QuarterAxisTopLeft00;
import PartOfRotSymmetric2DLattice.RotationallySymmetric2DLatticeInterface;
import Utils.DistanceUtils;
import Utils.Utils;

public class Num2DSolutionsPerLattice2 {
	
	//TODO: read https://arxiv.org/pdf/cond-mat/0007239.pdf to understand how n=56 is even achievable.

	//TODO: Count the number of rotationally sym answers as n increases!
	
	// Numbers I got from https://oeis.org/A001931
	// I added a 1 at the beginning, because I decided there's 1 way to do it with 0 cubes.
	public static final long numFixed2DPolycubes[] = 
		{ 1L, 1L, 2L, 6L, 19L, 63L, 216L, 760L, 2725L, 9910L, 36446L, 135268L, 505861L, 1903890L, 7204874L, 27394666L, 104592937L, 400795844L, 1540820542L, 5940738676L, 22964779660L, 88983512783L, 345532572678L, 1344372335524L, 5239988770268L, 20457802016011L, 79992676367108L, 313224032098244L, 1228088671826973L, 4820975409710116L, 18946775782611174L, 74541651404935148L, 293560133910477776L, 1157186142148293638L, 4565553929115769162L};
	/*
	 * A000988		Number of one-sided polyominoes with n cells.
(Formerly M1749 N0693)		+20
22
1, 1, 1, 2, 7, 18, 60, 196, 704, 2500, 9189, 33896, 126759, 476270, 1802312, 6849777, 26152418, 100203194, 385221143, 1485200848, 5741256764, 22245940545, 86383382827, 336093325058, 1309998125640, 5114451441106, 19998172734786, 78306011677182, 307022182222506, 1205243866707468, 4736694001644862

	 */
	public static final long expected[] = new long[]{1L, 1L, 1L, 2L, 7L, 18L, 60L, 196L, 704L, 2500L, 9189L, 33896L, 126759L, 476270L, 1802312L, 6849777L, 26152418L, 100203194L, 385221143L, 1485200848L, 5741256764L, 22245940545L, 86383382827L, 336093325058L, 1309998125640L, 5114451441106L, 19998172734786L, 78306011677182L, 307022182222506L, 1205243866707468L, 4736694001644862L, 18635412907198670L, 73390033697855860L, 289296535756895985L, 1141388483146794007L, 4506983054619138245L};
	
	
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
	
	public static void firstFewNValuesTest() {

		int MAX_N = 20;
		int MIN_N = Math.min(0, MAX_N);
		
		long series[] = new long[MAX_N + 1];
		
		long total = 0L;
		for(int i=MIN_N; i<=MAX_N; i++) {
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
	
	public static final int PADDING_FACTOR = 2;
	
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
			
			System.exit(1);
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
		
		
		boolean disallowedCoords[][] = new boolean[PADDING_FACTOR * (n + 2)][PADDING_FACTOR * (n + 2)];
		int CENTER = disallowedCoords.length /2;
		
		boolean disallowedTransitions[][][] = Utils.getDisallowed2DTransitionsBecauseItIsRedundant(lattice, disallowedCoords.length);
		
		
		//TODO: I don't know what the limit should be. (n/2 seems ok for now)
		while(startI < (n+1)/2) {

			if(! disallowedCoords[CENTER + startI][CENTER + startJ]) {


				int goalCoord[] = DistanceUtils.getGoalCoord(lattice, disallowedCoords, startI, startJ);
				
				int distances[][] = DistanceUtils.getDistancesFromGoal(disallowedCoords, goalCoord[0], goalCoord[1]);
				
				ret += countFor2DLattice(n, lattice, disallowedCoords, disallowedTransitions, startI, startJ, distances);
				
				int coordsToDisallow[][] = null;
				coordsToDisallow = lattice.getRotationallySymmetricPoints(tmpArray, startI, startJ);
	
				for(int k=0; k<lattice.getWeightOfPoint(startI, startJ); k++) {
					disallowedCoords[coordsToDisallow[0][k] + CENTER][coordsToDisallow[1][k] + CENTER] = true;
				}
				
				
			}
		
			int coord[] = coordIterator.getNext(startI, startJ);
			startI = coord[0];
			startJ = coord[1];
			
			
		}
		
		return ret;
	}
	
	
	public static int MAX_NUM_SYMMETRIES_2D = 4;
	
	public static long countFor2DLattice(int targetWeight, RotationallySymmetric2DLatticeInterface lattice, boolean disallowedCoords[][], boolean disallowedTransitions[][][], int startI, int startJ,
			int distancesToGoal[][]) {
		
		int CENTER = disallowedCoords.length /2;
		
		if(disallowedCoords[CENTER + startI][CENTER + startJ]) {
			System.out.println("ERROR: startI and startJ are on a disallowed coordinate!");
			System.exit(1);
		}
		
		boolean coordsUsedWithRotSymmetry[][] = new boolean[disallowedCoords.length][disallowedCoords.length];
		boolean coordsUsedInCurrentIsland[][] = new boolean[disallowedCoords.length][disallowedCoords.length];
		
		for(int i=0; i<coordsUsedWithRotSymmetry.length; i++) {
			for(int j=0; j<coordsUsedWithRotSymmetry[0].length; j++) {
				coordsUsedWithRotSymmetry[i][j] = false;
				coordsUsedInCurrentIsland[i][j] = false;
			}
		}
		
		int currentWeight = lattice.getWeightOfPoint(startI, startJ);
		int startNumSquaresToAddToConnect = Math.max(distancesToGoal[startI + CENTER][startJ + CENTER] - currentWeight/2, 0);
		
		int rotationallySymmetricPointsHolder[][] = new int[2][MAX_NUM_SYMMETRIES_2D];
		
		rotationallySymmetricPointsHolder = lattice.getRotationallySymmetricPoints(rotationallySymmetricPointsHolder, startI, startJ);
		
		for(int i=0; i<lattice.getWeightOfPoint(startI, startJ); i++) {
			coordsUsedWithRotSymmetry[rotationallySymmetricPointsHolder[0][i] + CENTER]
									 [rotationallySymmetricPointsHolder[1][i] + CENTER] = true;
			
		}
		
		if(targetWeight < currentWeight) {
			return 0L;
		} else if(targetWeight == currentWeight) {
			
			if(startNumSquaresToAddToConnect <= 0) {
				System.out.println("Rotationally symmetric solution:");
				Utils.printSquares(coordsUsedWithRotSymmetry);
				return 1L;
			} else {
				return 0L;
			}
			
		}

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
		
		coordsUsedInCurrentIsland[startI + CENTER]
 				[startJ + CENTER] = true;
		
		return doDepthFirstSearch(squaresToDevelop, numCellsUsedDepth,
				debugNope, debugIterations,
				squaresOrdering, minIndexToUse, minRotationToUse,
				lattice, currentWeight, coordsUsedWithRotSymmetry, disallowedCoords, disallowedTransitions,
				targetWeight, CENTER,
				startNumSquaresToAddToConnect, distancesToGoal,
				coordsUsedInCurrentIsland);
	}
	
	

	public static final int nudgeBasedOnRotation[][] = {{-1, 0,  1,  0},
														{0,  1,  0, -1}};
	
	public static long numIterations = 0L;
	public static long numSolutionsSoFarDebug = 0L;
	
	public static final int NOT_INSERTED = -1;
	
	public static int tmpArray[][] = new int[2][4];
	
	
	public static long doDepthFirstSearch(Coord2D squaresToDevelop[], int numCellsUsedDepth,
			boolean debugNope, long debugIterations[],
			int squaresOrdering[][], int minIndexToUse, int minRotationToUse,
			RotationallySymmetric2DLatticeInterface lattice, int currentWeight, boolean coordsUsedWithRotSymmetry[][], boolean disallowedCoords[][], boolean disallowedTransitions[][][],
			int targetWeight, int CENTER_ARRAY,
			int squaresNeededToConnect2, int distancesToGoal[][],
			boolean coordsUsedInCurrentIsland[][]) {

		// TODO: Add constant for the "/ 2" is a hack
		//Divide by 2 to go halfway around...
		//It should be div 4 if it's a 4 way symmetry
		
		
		// TODO: make it /4 for quarterAxis in future.
		if(squaresNeededToConnect2 > (targetWeight - currentWeight) / 2) {
			return 0L;
		}
		
		
		
		if(currentWeight > targetWeight) {
			return 0L;
		} else if(currentWeight == targetWeight) {
			
			numSolutionsSoFarDebug++;
			
			if(debugNope) {
				System.out.println("DEBUG Nope");
				Utils.printSquares(coordsUsedWithRotSymmetry);
				System.out.println("Lattice: " + lattice);
				System.out.println("DEBUG Nope");
				System.exit(1);
			}

			if(targetWeight < 7) {
				System.out.println("hello " + lattice);
				Utils.printSquares(coordsUsedWithRotSymmetry);
			}
			
			if(numSolutionsSoFarDebug % 1000000 == 0) {
				System.out.println("Solution " + numSolutionsSoFarDebug + ":");
				Utils.printSquares(coordsUsedWithRotSymmetry);
			}
			return 1L;
			
		}
		
		numIterations++;

		//Display debug/what's-going-on update:
		if(numIterations % 10000000L == 0) {
			
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
						squaresOrdering, curOrderedIndexToUse,
						new_i, new_j,
						CENTER_ARRAY
					);
				
				if( ! cantAddCellBecauseOfOtherNeighbours) {


					//Setup for adding new cube:
					
					tmpArray = lattice.getRotationallySymmetricPoints(tmpArray, new_i, new_j);
					
					coordsUsedInCurrentIsland[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j] = true;
					
					int weightOfPoint = lattice.getWeightOfPoint(new_i, new_j);
					for(int i=0; i<weightOfPoint; i++) {
						coordsUsedWithRotSymmetry[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = true;
						squaresOrdering[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = numCellsUsedDepth;
					}
					currentWeight += weightOfPoint;
					
					
					squaresToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);
					//End setup

					numCellsUsedDepth += 1;
					
					
					//distancesToGoal
					//TODO: I think this could be improved...
					boolean newCellCloserToGoal = 
							squaresNeededToConnect2 > 0
							&& DistanceUtils.couldGetToOtherIslandInWithNnewTiles(coordsUsedWithRotSymmetry, disallowedCoords, new_i, new_j, coordsUsedInCurrentIsland, squaresNeededToConnect2 - weightOfPoint/2);
					
					if(newCellCloserToGoal) {
						squaresNeededToConnect2 -= weightOfPoint/2;
					}
				
					retDuplicateSolutions += doDepthFirstSearch(squaresToDevelop, numCellsUsedDepth,
							debugNope, debugIterations,
							squaresOrdering, curOrderedIndexToUse, dirNewCellAdd,
							lattice, currentWeight, coordsUsedWithRotSymmetry, disallowedCoords, disallowedTransitions,
							targetWeight, CENTER_ARRAY,
							squaresNeededToConnect2, distancesToGoal,
							coordsUsedInCurrentIsland
						);
					
					if(newCellCloserToGoal) {
						squaresNeededToConnect2 += weightOfPoint/2;
					}
					
					numCellsUsedDepth -= 1;

					
					//Tear down (undo add of new cell)
					squaresToDevelop[numCellsUsedDepth] = null;
					
					coordsUsedInCurrentIsland[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j] = false;

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
	
	
	public static final int NUDGES[][] = new int[][]{{-1, 0, 1, 0},
												    {0, 1, 0, -1}};
	
	
	//I'm enforcing an artificial constraint where the polycube shape
		// has to develop in the same order as a breath-first-search.
		// This has a lot of advantages that I will need to explain in some docs.
		public static boolean cantAddCellBecauseOfOtherNeighbours(Coord2D squaresToDevelop[], boolean squaresUsed[][], int numCellsUsedDepth,
				boolean debugNope, long debugIterations[],
				
				int squaresOrdering[][], int curOrderedIndexToUse,
				int new_i, int new_j,
				int CENTER_ARRAY) {

			boolean cantAddCellBecauseOfOtherNeighbours = false;
			
			for(int rotReq=0; rotReq<NUDGES[0].length; rotReq++) {
				
				int i1 = new_i + NUDGES[0][rotReq];
				int j1 = new_j + NUDGES[1][rotReq];
			
				if(squaresToDevelop[curOrderedIndexToUse].a == i1 
					&& squaresToDevelop[curOrderedIndexToUse].b == j1) {
					
					continue;
				}
				
				
				if(squaresUsed[CENTER_ARRAY + i1][CENTER_ARRAY + j1]) {
					
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
