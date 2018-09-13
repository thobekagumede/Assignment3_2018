import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ParallelProgram {
	public double matrixArray[];
	public int row = 0;
	public int column = 0;
	public double totalSum = 0;
	
	public static void main(String[] args) {
		File file = new File("sample_in.txt");
		try {
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = null;
//			buff.readLine();
			int counter = 0;
			String dimensionStr = null;
			String var1 = null;
			int numOfTrees = 0;
			int[] tree_row = null;
			int[] tree_column = null;
			int[] tree_extent = null;
			
			// Read in dimensions
			line = buff.readLine();
			String[] matrixSize = line.trim().split("\\s+");
			int rowSize = Integer.parseInt(matrixSize[0]);
			int columnSize = Integer.parseInt(matrixSize[1]);		
			
			// Sunlight values
			line = buff.readLine();
			String[] sunlightStringMatrix = line.trim().split("\\s+");
			double sunlightMatrix[] = new double[rowSize*columnSize]; 
			for(int i=0; i<rowSize*columnSize; i++) {
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
			
			computeSequential(numOfTrees, treeArray, rowSize, sunlightMatrix);
			
			// Parallel implementation
			
			
//			System.out.println(sunlightSum(0,1,7,row,matrixArray));
			
					
		} 
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static void computeSequential(int numOfTrees, Tree [] treeArray, int rowSize, double [] sunlightMatrix) {
		for (int i=0; i<numOfTrees; i++) {
			double treeSum = sunlightSum(treeArray[i],rowSize,sunlightMatrix);
//			totalSum += treeSum;
			System.out.println(treeSum);
//			System.out.println(matrixArray[i]);
		}	
		
	}

	private static double sunlightSum(Tree tree, int dimension, double [] mat) {
		int x = tree.getxCoord();
		int y = tree.getyCoord();
		int extent = tree.getExtent();
		int x_extent = x + extent;
		int y_extent = y + extent;
		double sum = 0.0;
		int counter = 0;
		for (int i=x; i<x_extent && i < dimension; i++) {
			for(int j=y; j<y_extent && j < dimension; j++) {
				sum += mat[j+i*dimension];
				counter++;
			}
		}
		return sum;		
	}

}
