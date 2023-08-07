package Num2DSolutionsPerLattice;

import java.awt.peer.SystemTrayPeer;
import java.util.LinkedList;

import Coord.Coord2D;
import NumSymmetricCubes2DSolve.TopHalfIterator;
import PartOfRotSymmetric2DLattice.BasicHalf;
import PartOfRotSymmetric2DLattice.BasicQuarter;
import PartOfRotSymmetric2DLattice.Constants;
import PartOfRotSymmetric2DLattice.HalfAxisAtLeft00;
import PartOfRotSymmetric2DLattice.HalfAxisAtMid00;
import PartOfRotSymmetric2DLattice.QuarterAxisAtMid00;
import PartOfRotSymmetric2DLattice.RotationallySymmetric2DLatticeInterface;

public class Num2DSolutionsPerLattice {
	
	//TODO: read https://arxiv.org/pdf/cond-mat/0007239.pdf to understand how n=56 is even achievable.

	//TODO: Count the number of rotationally sym answers as n increases!
	
	// Numbers I got from https://oeis.org/A001931
	// I added a 1 at the beginning, because I decided there's 1 way to do it with 0 cubes.
	public static final long numFixed3DPolycubes[] = 
		{1, 1, 2, 6, 19, 63, 216, 760, 2725, 9910, 36446, 135268, 505861, 1903890, 7204874, 27394666, 104592937, 400795844, 1540820542, 5940738676L, 22964779660L, 88983512783L, 345532572678L, 1344372335524L, 5239988770268L, 20457802016011L, 79992676367108L, 313224032098244L, 1228088671826973L};
	
	public static void main(String args[]) {
		
		int MAX_N = 7;
		
		long series[] = new long[MAX_N + 1];
		
		long total = 0L;
		for(int i=7; i<=MAX_N; i++) {
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
			System.out.println(i + ": " + series[i]);
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
		lattices[0] = new BasicHalf();
		lattices[1] = new HalfAxisAtLeft00();
		lattices[2] = new HalfAxisAtMid00();
		lattices[3] = new BasicQuarter();
		lattices[4] = new QuarterAxisAtMid00();
		

		long numSolutionsWith90Rot = 0L;
		long numSolutionsWith180Rot = 0L;

		//--------
		
		long halfAxisAtTopLeft00 = getNumSymmetriesForLattice(n, lattices[0]); // N =2 gives 1 solution
		long BasicQuarter = getNumSymmetriesForLattice(n, lattices[3]);//N=4 gives 1 solution
		
		
		long tmp = halfAxisAtTopLeft00 + BasicQuarter;
		
		if(tmp % 2 != 0) {
			System.out.println("OOPS!");
			System.exit(1);
		}
		
		numSolutionsWith180Rot = (tmp) / 2;
		
		numSolutionsWith90Rot += BasicQuarter;


		//--------
		
		long halfAxisAtMid00 = getNumSymmetriesForLattice(n, lattices[2]); // N =1 gives 1 solution
		
		long quarterAxisAtMid00 = getNumSymmetriesForLattice(n, lattices[4]);
		
		tmp = halfAxisAtMid00 + quarterAxisAtMid00;
		
		System.out.println(tmp);
		System.out.println(quarterAxisAtMid00);
		if(tmp % 2 != 0) {
			System.out.println("OOPS 2!");
			System.exit(1);
		}

		numSolutionsWith180Rot = (tmp) / 2;
		
		numSolutionsWith90Rot += quarterAxisAtMid00;
		
		//--------

		numSolutionsWith180Rot += getNumSymmetriesForLattice(n, lattices[1]); // N =2 gives 1 solution


		
		long answerBeforeDiv4 = numFixed3DPolycubes[n] + 2 * numSolutionsWith180Rot + 1 * numSolutionsWith90Rot; 
	
		if(answerBeforeDiv4 % 4 != 0) {
			System.out.println("ERROR: the number of solutions doesn't divide cleanly into 4.");
		}
		
		System.out.println("numSolutionsWith180Rot: " + numSolutionsWith180Rot);
		System.out.println("numSolutionsWith90Rot: " + numSolutionsWith90Rot);
		
		long ret = answerBeforeDiv4 / 4;
		
		System.out.println("Ret: " + ret);
		
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
		
		//TODO: I don't know what the limit should be. (n/2 seems ok for now)
		while(startI < (n+1)/2) {
			
			ret += countFor2DLattice(n, lattice, disallowedCoords, startI, startJ);
				
			disallowedCoords[startI + CENTER][startJ + CENTER] = true;

			int coord[] = TopHalfIterator.getNext(startI, startJ);
			startI = coord[0];
			startJ = coord[1];
			
			//System.out.println("Using this startI and startJ coord: " + startI + ", " + startJ);
		}
		
		return ret;
	}
	
	public static long countFor2DLattice(int targetWeight, RotationallySymmetric2DLatticeInterface lattice, boolean disallowedCoords[][], int startI, int startJ) {
		
		int CENTER = disallowedCoords.length /2;
		
		if( ! lattice.isPartOfLattice(startI, startJ)) {
			return 0L;
		}
		
		boolean coordsUsed[][] = new boolean[disallowedCoords.length][disallowedCoords.length];
		boolean coordsUsedWithRotSymmetry[][] = new boolean[disallowedCoords.length][disallowedCoords.length];
		
		coordsUsed[startI + CENTER][startJ + CENTER] = true;
		
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
				Utils.Utils.printSquares(coordsUsedWithRotSymmetry);
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
		
		squaresOrdering[startI + CENTER][startJ + CENTER] = 0;
		
		
		int numCellsUsedDepth = 1;
		squaresToDevelop[0] = new Coord2D(startI, startJ);
		
		
		return doDepthFirstSearch(squaresToDevelop, coordsUsed, numCellsUsedDepth,
				debugNope, debugIterations,
				squaresOrdering, minIndexToUse, minRotationToUse,
				lattice, currentWeight, coordsUsedWithRotSymmetry, disallowedCoords,
				targetWeight, CENTER);
	}
	
	//This is wildly inefficient.
	public static boolean isConnected(boolean coordsUsedWithRotSymmetry[][], int startI, int startJ, int weight, int CENTER) {
		
		
		int numFound = 1;
		
		LinkedList<Coord2D> queue = new LinkedList<Coord2D>();
		
		//TODO: Avoid making new here?
		queue.add(new Coord2D(startI, startJ));
		
		boolean coordsFound[][] = new boolean[coordsUsedWithRotSymmetry.length][coordsUsedWithRotSymmetry[0].length];
		int squaresOrdering[][] = new int[coordsUsedWithRotSymmetry.length][coordsUsedWithRotSymmetry[0].length];
		
		coordsFound[CENTER + startI][CENTER + startJ] = true;
		squaresOrdering[CENTER + startI][CENTER + startJ] = 0;

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
			Utils.Utils.printSquares(coordsUsedWithRotSymmetry);
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
	public static long doDepthFirstSearch(Coord2D squaresToDevelop[], boolean coordsUsed[][], int numCellsUsedDepth,
			boolean debugNope, long debugIterations[],
			int squaresOrdering[][], int minIndexToUse, int minRotationToUse,
			RotationallySymmetric2DLatticeInterface lattice, int currentWeight, boolean coordsUsedWithRotSymmetry[][], boolean disallowedCoords[][],
			int targetWeight, int CENTER_ARRAY) {

		//TODO: coordsused for all and coordsused for legal section.
		//TODO: keep track of the weight.
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
				&& lattice.isSolutionAcceptableAndNotDoubleCounting(squaresToDevelop)) {

				numSolutionsSoFarDebug++;

				System.out.println("hello");
				Utils.Utils.printSquares(coordsUsedWithRotSymmetry);
				
				if(numSolutionsSoFarDebug % 100000 == 0) {
					System.out.println("Solution " + numSolutionsSoFarDebug + ":");
					Utils.Utils.printSquares(coordsUsedWithRotSymmetry);
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
			Utils.Utils.printSquares(coordsUsed);
			
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
				
				if(coordsUsed[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j]) {
					//Cell we are considering to add is already there...
					continue;

				} else if(! lattice.isPartOfLattice(new_i, new_j)) {
					continue;

				} else if(disallowedCoords[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j]) {
					continue;
				}
				
				boolean cantAddCellBecauseOfOtherNeighbours = cantAddCellBecauseOfOtherNeighbours(
						squaresToDevelop, coordsUsed, numCellsUsedDepth,
						debugNope, debugIterations,
						squaresOrdering, curOrderedIndexToUse, dirNewCellAdd,
						curOrderedIndexToUse,
						new_i, new_j,
						CENTER_ARRAY
					);
				
				
				if( ! cantAddCellBecauseOfOtherNeighbours) {


					//Setup for adding new cube:
					coordsUsed[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j] = true;
					
					tmpArray = lattice.getRotationallySymmetricPoints(tmpArray, new_i, new_j);
					
					int weightOfPoint = lattice.getWeightOfPoint(new_i, new_j);
					for(int i=0; i<weightOfPoint; i++) {
						coordsUsedWithRotSymmetry[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = true;
					}
					currentWeight += weightOfPoint;
					
					//TODO: check for repeat coords.
					squaresToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);
					squaresOrdering[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j] = numCellsUsedDepth;
					//End setup

					numCellsUsedDepth += 1;
				
					retDuplicateSolutions += doDepthFirstSearch(squaresToDevelop, coordsUsed, numCellsUsedDepth,
							debugNope, debugIterations,
							squaresOrdering, curOrderedIndexToUse, dirNewCellAdd,
							//TODO: update currentWeight and coordsUsedWithRotSymmetry
							lattice, currentWeight, coordsUsedWithRotSymmetry, disallowedCoords,
							targetWeight, CENTER_ARRAY
						);
					
					numCellsUsedDepth -= 1;

					//Tear down (undo add of new cell)
					coordsUsed[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j] = false;
					squaresToDevelop[numCellsUsedDepth] = null;
					squaresOrdering[CENTER_ARRAY + new_i][CENTER_ARRAY + new_j] = NOT_INSERTED;
					
					tmpArray = lattice.getRotationallySymmetricPoints(tmpArray, new_i, new_j);
					
					for(int i=0; i<weightOfPoint; i++) {
						coordsUsedWithRotSymmetry[CENTER_ARRAY + tmpArray[0][i]][CENTER_ARRAY + tmpArray[1][i]] = false;
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
				int squaresOrdering[][], int minIndexToUse, int minRotationToUse,
				int curOrderedIndexToUse,
				int new_i, int new_j,
				int CENTER_ARRAY) {

			boolean cantAddCellBecauseOfOtherNeighbours = false;
			
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
