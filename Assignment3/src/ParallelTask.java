import java.util.concurrent.RecursiveTask;

public class ParallelTask extends RecursiveTask<Double>{
	static final int SEQUENTIAL_CUTOFF=500;
	int numOfTrees;
	Tree[] treeArray;
	int rowSize; 
	double[] sunlightMatrix;
	int lo; // arguments
	int hi;
	StringBuilder treeSumTreeOutput;
	
	public ParallelTask(int numOfTrees, Tree[] treeArray, int rowSize, double[] sunlightMatrix, StringBuilder treeSumTreeOutput) {
		super();
		this.numOfTrees = numOfTrees;
		this.treeArray = treeArray;
		this.rowSize = rowSize;
		this.sunlightMatrix = sunlightMatrix;
		this.treeSumTreeOutput = treeSumTreeOutput;
		lo = 0;
		hi = treeArray.length;
	}
	
	public ParallelTask(int numOfTrees, Tree[] treeArray, int rowSize, double[] sunlightMatrix, StringBuilder treeSumTreeOutput, int lo, int hi) {
		this(numOfTrees, treeArray, rowSize, sunlightMatrix, treeSumTreeOutput);
		this.lo = lo;
		this.hi = hi;
	}



	@Override
	protected Double compute() {
		if((hi-lo) < SEQUENTIAL_CUTOFF) {
			double sum = 0;
			for (int i=lo; i<hi; i++)
			{
				double treeSum = sunlightSum(treeArray[i],rowSize,sunlightMatrix);
				sum += treeSum;
				treeSumTreeOutput.append(treeSum + "\n");
			}
			return sum;
		}
		else {
			  ParallelTask left = new ParallelTask(numOfTrees, treeArray, rowSize, sunlightMatrix,treeSumTreeOutput, lo,(hi+lo)/2);
			  ParallelTask right= new ParallelTask(numOfTrees, treeArray, rowSize, sunlightMatrix,treeSumTreeOutput, (hi+lo)/2,hi);
			  
			  // order of next 4 lines
			  // essential – why?
			  left.fork();
			  double rightAns = right.compute();
			  double leftAns  = left.join();
			  return leftAns + rightAns;     
		  }
	}
	
	public static double sunlightSum(Tree tree, int dimension, double [] mat)
	{
		int x = tree.getxCoord();
		int y = tree.getyCoord();
		int extent = tree.getExtent();
		int x_extent = x + extent;
		int y_extent = y + extent;
		double sum = 0.0;
		for (int i=x; i<x_extent && i < dimension; i++) 
		{
			for(int j=y; j<y_extent && j < dimension; j++)
			{
				sum += mat[j+i*dimension];
			}
		}
		return sum;		
	}

}
