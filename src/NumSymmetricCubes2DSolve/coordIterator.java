package NumSymmetricCubes2DSolve;

public class coordIterator {

	public static int[] initIterator(int size) {
		
		int ret[] = new int[size];
		
		for(int i=0; i<ret.length; i++) {
			ret[i] = 0;
		}
		
		return ret;
	}
	
	public static int[] getNext(int i, int j) {
		return getNext(new int[]{i, j});
	}

	
	//TODO: why not make squares instead of diamonds/
	
	//TODO: figure out how to do thsi in 3D...

	//Let current[0] be i and current[1] be j.
	public static int[] getNext(int current[]) {
		
		if(current[0] <= 0 && current[1] == -1)  {
			current[0] = current[0] - 2;
			current[1] = 0;
			
		} else if(current[0] == 0 && current[1] == 0) {
			current[0] = -1;
			current[1] = 0;
			
		} else if(current[0] < 0 && current[1] >=0 ) {
			current[0]++;
			current[1]++;

		} else if(current[0] >= 0 && current[1] > 0) {
			current[0]++;
			current[1]--;

		} else if(current[0] > 0 && current[1] <= 0) {
			current[0]--;
			current[1]--;

		} else if(current[0] <= 0 && current[1] < 0) {
			current[0]--;
			current[1]++;

		} else {
			System.out.println("DOH!");
			System.exit(1);
		}
		
		//System.out.println(current[0] + ", " + current[1]);
		return current;
	}


	public static void main(String[] args) {
		testIterator();
		
	}

	public static void testIterator() {
		//Found this formula in the results:
		//A001844		Centered square numbers: a(n) = 2*n*(n+1)+1. Sums of two consecutive squares.
		int GRID_SIZE = 30;
		int tmp[] = initIterator(2);
		
		int space[][] = new int[GRID_SIZE][GRID_SIZE];
		
		int MID = space.length / 2;
		
		for(int k=0; k <100; k++) {
			
			space[MID + tmp[0]][MID + tmp[1]] = k;
			
			
			tmp = getNext(tmp);
		}

		for(int i=0; i<space.length; i++) {
			for(int j=0; j<space[0].length; j++) {
				System.out.print(" ");
				if(space[i][j] < 10) {
					System.out.print(" " + space[i][j]);
					
				} else {
					System.out.print(space[i][j]);
				}
				
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	
}
