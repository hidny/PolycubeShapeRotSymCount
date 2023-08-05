package NumSymmetricCubes2DSolve;
public class TopHalfIterator {

	// https://polyominoes.org/count
	
	// https://arxiv.org/pdf/cond-mat/0007239.pdf
	// has up to: 68557762666345165410168738
	
	/*
	 * Enumerations of lattice animals and trees
Iwan Jensen
Department of Mathematics and Statistics,
The University of Melbourne,
Victoria 3010, Australia
September 11, 2018

	 */
	
	
	public static void printIterator() {
		
		
		for(int dist = 0; dist < 10; dist++) {
			
			for(int a=-dist; a<=dist; a++) {
					
					int b = dist - Math.abs(a);
					System.out.println("next");
					System.out.println(a + " " + b);
					System.out.println();
					System.out.println();
					System.out.println("Prediction:");
					getNext(a, b);
			}
			System.out.println();
		}
		
	}
	
	public static int[] getNext(int a, int b) {
	    
	    int dist = Math.abs(a) + b;
	    
	    int anew = -1;
	    int bnew = -1;
	    
	    if( a< dist) {
	        
	        anew = a+1;
	        bnew = -1;
	        
	    	if(a < 0) {
	    		bnew = b + 1;
	    	} else {
	    		bnew = b - 1;
	    	}
	        
			//System.out.println(anew + " " + bnew + " (1)");
	    } else {
	        
	        anew = -dist - 1;
	        bnew = 0;
	        
			//System.out.println(anew + " " + bnew + " (2)");
	        
	    }
	    
	    return new int[] {anew, bnew};
	    
	}
	
	//TODO: put in utils:
	public static int getDist(int a[]) {
		int ret = 0;
		
		for(int i=0; i<a.length; i++) {
			ret += Math.abs(a[i]);
		}
		return ret;
	}
	
	public static int getDist(int a, int b) {
		return Math.abs(a) + Math.abs(b);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		printIterator();

	}

}