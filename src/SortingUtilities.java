
public class SortingUtilities {

	
	//Merge Sort from Wikipedia, modified to fit the cicumstances
	public static void TopDownMergeSort(long distances[], int[][]toSort, int zeropart, int length)
	{
		long [] B = new long[distances.length];
		int[][] B2 = new int[toSort.length][];
		System.arraycopy(distances, 0, B, 0, length);
		System.arraycopy(toSort, 0, B2, 0, length);
		
		TopDownSplitMerge(B, B2, zeropart, length, distances, toSort);
	}
	
	public static void TopDownSplitMerge(long[] B, int[][] B2, int iBegin, int iEnd, long[] A, int[][]A2)
	{
		if(iEnd - iBegin < 2)                       // if run size == 1
	        return;                                 //   consider it sorted
	    // split the run longer than 1 item into halves
	    int iMiddle = (iEnd + iBegin) / 2;              // iMiddle = mid point
	    // recursively sort both runs from array A[] into B[]
	    TopDownSplitMerge(A, A2, iBegin,  iMiddle, B, B2);  // sort the left  run
	    TopDownSplitMerge(A, A2, iMiddle,    iEnd, B, B2);  // sort the right run
	    // merge the resulting runs from array B[] into A[]
	    TopDownMerge(B, B2, iBegin, iMiddle, iEnd, A, A2);
	}
	
	public static void TopDownMerge(long A[], int[][]A2, int iBegin, int iMiddle, int iEnd, long B[], int[][]B2)
	{
	    int i = iBegin, j = iMiddle;
	    
	    
	    // While there are elements in the left or right runs...
	    for (int k = iBegin; k < iEnd; k++) {
	        // If left run head exists and is <= existing right run head.
	        if (i < iMiddle && (j >= iEnd || A[i] <= A[j])) {
	            B[k] = A[i];
	            B2[k] = A2[i];
	            i = i + 1;
	        } else {
	            B[k] = A[j];
	            B2[k] = A2[j];
	            
	            j = j + 1;    
	        }
	    } 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	static int QUICKSORT_THEN_INSERTION_K = 524;
	public static void Quicksort(long distances[], int[][]toSort, int p, int pivot)
	{
		if ( p < pivot)
		{
			int q = Partition(distances, toSort,p,pivot);
			Quicksort(distances, toSort, p, q-1);
			Quicksort(distances, toSort, q+1, pivot);
		}
	}
	
	
	public static int Partition(long distances[], int[][]toSort, int p, int pivot)
	{
		long x = distances[pivot];
		long temp;
		int temp2[];
		
		int i = p-1;
		for(int j = p; j < pivot; j++)
		{
			
			if(distances[j] <= x)
			{
				i++;
				temp = distances[i];
				distances[i] = distances[j];
				distances[j] = temp;

				temp2 = toSort[i];
				toSort[i] = toSort[j];
				toSort[j] = temp2;
			}
		}

		temp = distances[i+1];
		distances[i+1] = distances[pivot];
		distances[pivot] = temp;
		
		temp2 = toSort[i+1];
		toSort[i+1] = toSort[pivot];
		toSort[pivot] = temp2;
		
		return  i + 1;
	}
	
	
	public static void Quicksort_then_Insertionsort(long distances[], int[][]toSort, int p, int pivot)
	{

		//Only do insertion sort for low enough subarray sizes [p,pivot]
		if((pivot - p) > QUICKSORT_THEN_INSERTION_K)
		{
			if ( p < pivot)
			{
				System.out.println("DOING QUICK");
				int q = Partition(distances, toSort,p,pivot);
				Quicksort_then_Insertionsort(distances, toSort, p, q-1);
				Quicksort_then_Insertionsort(distances, toSort, q+1, pivot);
			}
		}
		else
		{
			System.out.println("DOING INSERTION");
			//Sort the subarray using insertion sort
			InsertionSort(distances, toSort, p, pivot);
		}
	}
	
	
	
	
	public static void InsertionSort(long distances[], int [][]toSort, int start, int end)
	{
		int i;
		long key;
		int []key2;
		for(int j = start+1; j <= end; j++)
		{
			key = distances[j];
			key2 = toSort[j];
			
			i = j-1;
			
			while(i >= start && distances[i] > key)
			{
				distances[i + 1] = distances[i];
				toSort[i+1] = toSort[i];
				i = i-1;
			}
			
			distances[i+1] = key;
			toSort[i+1] = key2;
		}
		
	}
	
	private static void swap(long[] distances, int [][]toSort, int i, int j) {
	    long temp = distances[i];
	    distances[i] = distances[j];
	    distances[j] = temp;
	    
	    int temp2[] = toSort[i];
	    toSort[i] = toSort[j];
	    toSort[j] = temp2;
	}
}
