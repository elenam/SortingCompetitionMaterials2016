import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Group10 {
	private static int x1;
	private static int y1;
	private static int x2;
	private static int y2;
	private static double threshold = 0.0000000001;

	public static void main(String[] args) throws InterruptedException {
		if (args.length < 2) {
			System.out
					.println("Please run with two command line arguments: input and output file names");
			System.exit(0);
		}

		String inputFileName = args[0];
		String outFileName = args[1];

		int[][] points = readInData(inputFileName);

		//printArray(points, 100);
		
		int[][] toSort = points.clone();
				
		int[][] sorted = sort(toSort);
		
		//printArray(sorted, 100);
		
		toSort = points.clone();
		
		Thread.sleep(10); //to let other things finish before timing; adds stability of runs

		long start = System.currentTimeMillis();
		
		sorted = sort(toSort);
		
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);

		writeOutResult(sorted, outFileName);
		
		/**
		 * Testing (assuming data2.txt points)
		int[] test1 = {23790, 94342, 8922566};
		int[] test2 = {77579, 45587, 8546858};
		System.out.println(distance(test1));
		System.out.println(distance(test2));
		**/
		
	}
	
	// YOUR SORTING METHOD GOES HERE. 
	// You may call other methods and use other classes. 
	// Note: you may change the return type of the method. 
	// You would need to provide your own function that prints your sorted array to 
	// a while in the exact same format that my program outputs
	private static int[][] sort(int[][] toSort) {
		
		//Get the distances and move 0's into their
		//sorted positions
		long distances[] = new long[toSort.length];
		int zeropart = partiallySort(toSort, distances);
		
		//Sort based on the distances
		TopDownMergeSort(distances, toSort, zeropart, toSort.length);
		return toSort;
	}

	/**
	 * Compute the Squared Euclidean Distance between the 3-tuple point and (px, py)
	 * @param point
	 * @param px
	 * @param py
	 * @return
	 */
	private static long distance(int[] point, final int px, final int py) {
		final long deltaX = px - point[0];
		final long deltaY = py - point[1];
		return deltaX * deltaX + deltaY * deltaY;
	}
	
	
	/**
	 * Computes the distances of each point in toSort to their closest points in {(x1,y1),(x2,y2)}
	 * Stably rearranges all points whose computed distances are 0 to the beginning of toSort.
	 * 
	 * @param toSort The array of 3-tuples (x, y, i) to be sorted
	 * @param distances an array of size toSort.length of distances to determine
	 * @return The (non-inclusive) index of the last element in the zero partition. 
	 * 		
	 */
	static int partiallySort(int[][] toSort, long distances[])
	{
		//Zero partition starts at [0,0) (no elements)
		//and ends with [0, zeropart) all elements whose distance is 0
		//and sorted by precedence implicitly by the iteration through toSort
		int zeropart = 0;
		
		//Find line between p1 and p2
		final double m_between = (y2 - y1) / (double)(x2 - x1);

		//Find the perpendicular line of slope m that
		//intersects with the average of the two points (avgx, avgy)
		//y= mx+b ; b= y-mx;
		final double m = -1.0d/m_between;
		final double avgx = (x1 + x2) / 2.0d,
			   avgy = (y1 + y2) / 2.0d;
		final double b = avgy - m*avgx;
		
		//Determine which of the points is "less than" or under the new
		//perpendicular line.
		
		//(ltx, lty) = the point which is less than the line
		//(gtx, gty) = the point which is greater than the line
		final int ltx, lty, gtx, gty;
		if(y1 < y2)
		{
			ltx = x1;
			lty = y1;
			gtx = x2;
			gty = y2;
		}
		else
		{
			ltx = x2;
			lty = y2;
			gtx = x1;
			gty = y1;
		}
		
		//Fields used within following loop:
		final long length = toSort.length; //micro-optimization
		
		int[] toSort_i;			//micro-optimization if at all
		long dist; 				//The result of the distance function
		int[] temp;		//Used for swapping indices within toSort
		
		//Pseudocode
		//For i from [0, toSort.Length)
		//	point = toSort[i]
		//	If the point is underneath the perpendicular line mx+b
		//		dist = Distance between point and the "lesser point"
		//	Else
		//		dist = Disxtance between point and the "greater point"
		//	If dist == 0
		//		Exchange toSort[zeropart] and toSort[i]
		//		distances[i] = distances[zeropart]
		//		distances[zeropart] = 0;
		//		zeropart = zeropart + 1
		//	Else
		//		distances[i] = dist
		
		for(int i = 0; i < length; i++)
		{
			toSort_i = toSort[i];
			//Determine which side of inequality toSort[i] is on
			if ( toSort_i[1] < m*toSort_i[0] + b)
			{
				//Under line
				dist = distance(toSort_i, ltx, lty);
			}
			else
			{
				//Above line
				dist = distance(toSort_i, gtx, gty);
			}
			
			if(dist != 0)
				distances[i] = dist;
			else
			{
				//Exchange distances[i] with distances[zeropart]
				//In order to add it to the zero partition
				distances[i] = distances[zeropart];
				distances[zeropart] = 0;
				
				//exchange toSort[i] with toSort[zeropart];
				temp = toSort[zeropart];
				toSort[zeropart] = toSort_i;
				toSort[i] = temp;
				zeropart++;
			}
		}
		return zeropart;
	}
	
	//Top-Down Merge Sort from Wikipedia, MODIFIED to fit the circumstances
	//https://en.wikipedia.org/wiki/Merge_sort
	public static void TopDownMergeSort(long distances[], int[][]toSort, int zeropart, int length)
	{
		long [] B = new long[distances.length];
		int[][] B2 = new int[toSort.length][];
		System.arraycopy(distances, 0, B, 0, length); 	//System.arraycopy is faster than copying elements because it runs natively,
		System.arraycopy(toSort, 0, B2, 0, length);		//arraycopy only works on primitives and apparently pointers to primitives!
		
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
	
	
	
	
	private static int[][] readInData(String inputFileName) {
		ArrayList<int[]> points = new ArrayList<int[]>();
		Scanner in;
		Pattern p = Pattern.compile("\\d+");
		int counter = 0;
		try {
			in = new Scanner(new File(inputFileName));
			// read the two points:
			x1 = Integer.parseInt(in.next());
			y1 = Integer.parseInt(in.next());
			x2 = Integer.parseInt(in.next());
			y2 = Integer.parseInt(in.next());
			in.nextLine(); // skip the rest of the line
			while (in.hasNextLine()) {
				String point = in.nextLine();
				Matcher m = p.matcher(point);
				int[] arrayPoint = new int[3];
				m.find();
				arrayPoint[0] = Integer.parseInt(m.group()); // x coord
				m.find();
				arrayPoint[1] = Integer.parseInt(m.group()); // y coord
				m.find();
				arrayPoint[2] = Integer.parseInt(m.group()); // timestamp
				points.add(arrayPoint);
				counter++;
			}
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return points.toArray(new int[counter][3]); // convert to array of int
													// arrays
	}
	
	private static void writeOutResult(int[][] sorted, String outputFilename) {
		try {
			PrintWriter out = new PrintWriter(outputFilename);
			for (int[] point : sorted) {
				out.println(point[0] + " " + point[1] + " " + point[2]);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// for test printing:
	private static void printArray(int[][] points, int n) {
		for (int i = 0; i < n; ++i) {
			System.out.println("x = " + points[i][0] + ", y = " + points[i][1]
					+ ", t = " + points[i][2]);
		}
	}

	
	
	

}
