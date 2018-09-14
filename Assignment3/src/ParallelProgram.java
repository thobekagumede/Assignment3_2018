import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ParallelProgram
{	
	public static void main(String[] args) 
	{
		File file = new File("sample_in.txt");
		try 
		{
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = null;
			int numOfTrees = 0;

			// Read in dimensions
			line = buff.readLine();
			String[] matrixSize = line.trim().split("\\s+");
			int rowSize = Integer.parseInt(matrixSize[0]);
			int columnSize = Integer.parseInt(matrixSize[1]);		
			
			// Sunlight values
			line = buff.readLine();
			String[] sunlightStringMatrix = line.trim().split("\\s+");
			double sunlightMatrix[] = new double[rowSize*columnSize]; 
			for(int i=0; i<rowSize*columnSize; i++)
			{
				sunlightMatrix[i] = Double.parseDouble(sunlightStringMatrix[i]);
			}
			
			// Number of trees
			line = buff.readLine();
			numOfTrees = Integer.parseInt(line);
			
			// Tree Array
			Tree [] treeArray = new Tree [numOfTrees];
			
			for(int j=0; j<numOfTrees; j++)
			{
				line = buff.readLine();
				String [] treeInfo = line.trim().split("\\s+");
				int xCoord = Integer.parseInt(treeInfo[0]);
				int yCoord = Integer.parseInt(treeInfo[1]);
				int extent = Integer.parseInt(treeInfo[2]);
				
				treeArray[j] = new Tree(xCoord, yCoord, extent);
			}
			
			// Done reading from file
			buff.close();
			
			// Sequential implementation
			long tic = System.currentTimeMillis();
			computeSequential(numOfTrees, treeArray, rowSize, sunlightMatrix);
			long seqToc = System.currentTimeMillis() - tic;
			System.out.println("Sequential: " + seqToc);
			
			tic = System.currentTimeMillis();
			System.out.println("==========================================================");
			// Parallel implementation
			computeParallel(numOfTrees, treeArray, rowSize, sunlightMatrix);
			long parToc = System.currentTimeMillis() - tic;
			System.out.println("Parallel: " + parToc);
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	private static void computeParallel(int numOfTrees, Tree[] treeArray, int rowSize, double[] sunlightMatrix) {
		// Start parallel
		StringBuilder treeSumString = new StringBuilder();
		ParallelTask task = new ParallelTask(numOfTrees, treeArray, rowSize, sunlightMatrix, treeSumString);
		double totalSum = task.compute();
		double average = totalSum/numOfTrees;
		System.out.println(average + "\n" + numOfTrees + "\n" + treeSumString.toString().trim());
		
	}

	private static void computeSequential(int numOfTrees, Tree [] treeArray, int rowSize, double [] sunlightMatrix) 
	{
		double totalSum = 0.0;
		String treeSumString = "";
		for (int i=0; i<numOfTrees; i++)
		{
			double treeSum = sunlightSum(treeArray[i],rowSize,sunlightMatrix);
			totalSum += treeSum;
			treeSumString += treeSum + "\n";
		}
		double average = totalSum/numOfTrees;
		System.out.println(average + "\n" + numOfTrees + "\n" + treeSumString);
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
