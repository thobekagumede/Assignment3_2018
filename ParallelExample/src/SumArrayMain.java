
public class SumArrayMain {
	public static void main(String [] args)
	{
		int arr[] = {1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		SumArray m = new SumArray(arr, 0, arr.length);
		System.out.println(m.compute());
	}
}
