/**
 * Skye Antinozzi and Jocelyn Bayer
 * Final Sorting Competition Submission
 *
 * This solution for the sorting competition uses many optimization techniques found from research and
 * experience. Some optimizations include:
 *  • Using while loops instead of for loops
 *  • Using post-fix decrement
 *  • Conditions based on zero rather than an upper-bound
 *  • Removal of method calls by inlining
 *  • Removal of method calls (such as sqrt(double)) by clever math
 *  • Removal of floating point values
 *
 * In addition to optimizations seen in code, under-the-hood optimizations have been made
 * to work closely with the JIT compiler. Most notably, the compiler does very well when clear copying
 * patterns are made. In lines 170-172 the compiler optimizes the copying of in-sequence object
 * data back to the array that was passed to the method.
 *
 * To calculate distances the sqrt() method was removed. This allowed for distances to be calculated much faster.
 * In addition, the distance() method is no longer called. To remove the overhead of calling distance()
 * twice per comparison the decision was made to inline the method twice within the comparison method.
 *
 * The sorting routine used is the Timsort/Traditional Mergesort defined by the Java API. This implementation
 * will almost always beat the Java level implementation in runtime as it is implemented on a native level.
 * This means that instead of working through the JVM to execute code, code recognized by the machine
 * can be directly run on the hardware.
 *
 * The algorithm was decided to be used after trying a number of different sorting routines.
 *  • The first was a Mergesort routine written by Nic Mcphee. It did not perform well.
 *  • The next was the Java API Arrays.sort(T[], Comparator<? super T>) method (which Elena had used,
 *    an attempt was made to make it run faster by modifying the distance() method but this did not give
 *    the performance jump desired)
 *  • Finally, the Java API Arrays.sort(Object[]) was the sorting routine that gave the most promising results.
 *
 * A question to be answered was why was the Arrays.sort(T[], Comparator) used by Elena was much slower
 * than the Arrays.sort(Object[]). The answer seems to be with overhead. Elena's sort used generics and
 * also passed a Comparator rather than having Object simply extend them as was the case with sort(Object[]).
 * In addition, the under-the-hood implementation may be doing more data-accessing from generic and interface
 * implementation overhead.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group2 {
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

		// Create a number of Points that will encapsulate each Point's information
		Point[] points = new Point[toSort.length];

		// Store points.length to avoid points.length repetitive resolution
		int i = points.length;

		// Create all point objects
		while(true) {

			// Track created point objects
			--i;

			// When all point objects have been created begin sorting
			if(i < 0) {

				// Sort the data
				Arrays.sort(points);

				// Track copied object data
				i = toSort.length;

				// Copy over all Point object data into 2D array
				while(true) {
					// Track copied object data
					--i;

					// If all Point objects have been copied
					if(i < 0) {
						return toSort;
					}

					// Copy over the Point object data (x, y, timestamp) back into the 2D array that was passed in
					toSort[i][2] = points[i].t;
					toSort[i][1] = points[i].y;
					toSort[i][0] = points[i].x;

				}//end copy object data to 2D array
			}//end start sorting when all point objects have been created

			// Construct a new point based on the input data
			points[i] = new Point(toSort[i][0], toSort[i][1], toSort[i][2]);

		}//end create point objects
	}//end toSort

	private static class Point implements Comparable<Point>{

		// Default access modifier for quick access to x, y and timestamp
		final int x, y, t;

		// These variables are reused on each comparison.
		// By making them static they are only initialized once
		// rather than on each comparison.
		// These can be longs instead of doubles as no floating point
		// values are used.
		static long deltaX, deltaY, d1, d2, firstDistance, secondDistance, diff;

		// Creates a point with the specified x position, y position and time stamp
		public Point(int xPoint, int yPoint, int tPoint){
			x = xPoint;
			y = yPoint;
			t = tPoint;
		}//end sortPoint

		// Compares this point to the specified point by their respective
		// distances to the reference points. If the distance from reference
		// points is the same between the two points then the timestamp
		// is used to resolve the conflict.
		//
		// There are no method calls in this function. Rather
		// the distance calculations from the distance method are inlined
		// within this method.
		public int compareTo(Point p){

			// !!!Calculate first point!!!
			deltaX = x1 - x;
			deltaY = y1 - y;

			d1 = deltaX * deltaX + deltaY * deltaY;

			//if(d1 < threshold * threshold)
			//    d1 = 0;

			deltaX = x2 - x;
			deltaY = y2 - y;

			d2 = deltaX * deltaX + deltaY * deltaY;

			//if(d2 < threshold * threshold)
			//    d2 = 0;

			firstDistance = (d1 <= d2 ? d1 : d2);
			// !!!End calculate first point!!!

			// !!!Calculate second point!!!
			deltaX = x1 - p.x;
			deltaY = y1 - p.y;

			d1 = deltaX * deltaX + deltaY * deltaY;

			//if(d1 < threshold * threshold)
			//    d1 = 0;

			deltaX = x2 - p.x;
			deltaY = y2 - p.y;

			d2 = deltaX * deltaX + deltaY * deltaY;

			//if(d2 < threshold * threshold)
			//    d2 = 0;

			secondDistance = (d1 <= d2 ? d1 : d2);
			// !!!End calculate second point!!!

			diff = firstDistance - secondDistance;

			// Determine which reference point is the closest
			//if (Math.abs(diff) < threshold) {

			// The distances are equal
			if(diff == 0)
				return t - p.t;
			// The second point is closer
			if (diff < 0)
				return -1;
			// The first point is closer
			return 1;

		}//end compareTo

	}//end inner class Point


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

	private static double distance(int[] point) {

		// distance to the first point (set to 0 if too small):
		double deltaX = x1 - point[0];
		double deltaY = y1 - point[1];

		// beware of integer overflow! d1 has to be a double to accommodate large numbers
		double d1 = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		if (d1 < threshold) {
			d1 = 0;
		}

		// distance to the second point (set to 0 if too small):
		deltaX = x2 - point[0];
		deltaY = y2 - point[1];
		double d2 = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		if (d2 < threshold) {
			d2 = 0;
		}

		// the smaller of the two:
		if (d1 <= d2)
			return d1;

		return d2;
	}

	/**
	 *
	 * @author elenam
	 *
	 * The comparator compares two points (each represented as an array of
	 * three integer values) by their distance to the closer of two given
	 * points. The coordinates of the two given points are in x1, y1 and x2, y2
	 * respectively.
	 *
	 * If the difference between the two distances is smaller than the threshold,
	 * the two points are considered to be equal by the distance,
	 * and are ordered based on their timestamps.
	 */
	// the inner class has to be static because it is used in a static method
	private static class PointComparator implements Comparator<int[]> {
		@Override
		public int compare(int[] point1, int[] point2) {
			double diff = distance(point1) - distance(point2);
			if (Math.abs(diff) < threshold) {
				return point1[2] - point2[2]; // the two distances are equal
			}
			if (diff < 0) {
				return -1;
			} else {
				return 1;
			}
		}

	}

}
