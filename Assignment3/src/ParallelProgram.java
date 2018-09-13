import java.io.*;

public class ParallelProgram {
	public double matrixArray[];
	public int row = 0;
	public int column = 0;
	public double totalSum = 0;
	
	public static void main(String[] args) {
		File file = new File("sample_input.txt");
		try {
			BufferedReader buff = new BufferedReader(new FileReader(file));
			String line = null;
//			buff.readLine();
			int counter = 0;
			String var = null;
			String var1 = null;
			int trees = 0;
			int[] tree_row = new int[1000000];
			int[] tree_column = new int[1000000];
			int[] tree_extent = new int[1000000];
			
			
			while((line=buff.readLine())!=null & line.length() != 0) {				
				counter++;
				if(counter == 1) {
					var = line;
				}
				
				else if(counter == 2) {
					var1 = line;
				}
				
				else if (counter == 3) {
					trees = Integer.parseInt(line);
				}
				
				else {
					tree_row[counter-4] = Integer.parseInt(line.trim().split("\\s+")[0]);
					tree_column[counter-4] = Integer.parseInt(line.trim().split("\\s+")[1]);
					tree_extent[counter-4] = Integer.parseInt(line.trim().split("\\s+")[2]);					
				}
			}
			


			String[] matrixSize = var.trim().split("\\s+");
			String[] matrix = var1.trim().split("\\s+");
			int row = Integer.parseInt(matrixSize[0]);
			int column = Integer.parseInt(matrixSize[1]);		
			double matrixArray[] = new double[row*column]; 
			for(int i=0; i<row*column; i++) {
				matrixArray[i] = Double.parseDouble(matrix[i]);
			}
			
//			System.out.println(sunlightSum(0,1,7,row,matrixArray));
			
			for (int i=0; i<tree_row.length; i++) {
				double treeSum = sunlightSum(tree_row[i],tree_column[i],tree_extent[i],row,matrixArray);
//				totalSum += treeSum;
				System.out.println(treeSum);
//				System.out.println(matrixArray[i]);
			}			
		} 
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static double sunlightSum(int x, int y, int extent, int dimension, double [] mat) {
		int x_extent = x + extent;
		int y_extent = y + extent;
		double sum = 0.0;
		int counter = 0;
		for (int i=x; i<x_extent; i++) {
			for(int j=y; j<y_extent; j++) {
				sum += mat[j+i*dimension];
				counter++;
			}
		}
		return sum;		
	}

}
