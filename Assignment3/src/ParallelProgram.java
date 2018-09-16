import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class ParallelProgram
{	
	public static String inputFileName = "sample_input.txt";
	public static String outputFileName = "sample_output.txt";
	public static void main(String[] args) 
	{
		System.out.println("Program Starting...");
		// Either code is run from command line with parameters or default is used
		boolean useArgs = args.length == 2;
		if(useArgs)
		{
			inputFileName = args[0];
			outputFileName = args[1];
		}
		// Create/load file
		File file = new File(inputFileName);
		try 
		{
			System.out.println("Reading File...");
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
			
			System.out.println("Data Reading Complete...");
			// Done reading from file
			buff.close();
			
			// Sequential implementation
			System.out.println("Starting Sequential Implementation...");
			long tic = System.currentTimeMillis();
			//computeSequential(numOfTrees, treeArray, rowSize, sunlightMatrix);
			long seqToc = System.currentTimeMillis() - tic;
			System.out.println("Sequential: " + seqToc);
			
			tic = System.currentTimeMillis();
			System.out.println("==========================================================");
			// Parallel implementation
			System.out.println("Starting Parallel Implementation...");
			computeParallel(numOfTrees, treeArray, rowSize, sunlightMatrix);
			long parToc = System.currentTimeMillis() - tic;
			System.out.println("Parallel: " + parToc);
			System.out.println("Program Completed...");
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	private static void computeParallel(int numOfTrees, Tree[] treeArray, int rowSize, double[] sunlightMatrix) {
		// Start parallel
		StringBuilder treeSumString = new StringBuilder();
		int numberOfProcessors = Runtime.getRuntime().availableProcessors();
		ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfProcessors);
		ParallelTask task = new ParallelTask(numOfTrees, treeArray, rowSize, sunlightMatrix, treeSumString);
		double totalSum = forkJoinPool.invoke(task);
		do
		{
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n", forkJoinPool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n", forkJoinPool.getActiveThreadCount());
			System.out.printf("******************************************\n");
			try
			{
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		} while (!task.isDone());
		forkJoinPool.shutdown();
		double average = totalSum/numOfTrees;
		String output = average + "\n" + numOfTrees + "\n" + treeSumString.toString().trim();
		System.out.println(output);
		FileUtil.writeStringToFile("parallel_" + outputFileName, output);
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
		String output = average + "\n" + numOfTrees + "\n" + treeSumString;
		System.out.println(output);
		FileUtil.writeStringToFile("sequential_" + outputFileName, output);
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
