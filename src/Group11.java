//package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group11 {
	private static int x1;
	private static int y1;
	private static int x2;
	private static int y2;
	public static double threshold = 0.0000000001;

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
		
		
//		 // Testing (assuming data2.txt points)
//		int[] test1 = {54561, 49402, 24294};
//		int[] test2 = {79194, 23251, 68245};
//		System.out.println(distance(test1));
//		System.out.println(distance(test2));
//		System.out.println(distance(test1) - distance(test2));
//		System.out.println((test2)[2]);
//		System.out.println((test1)[2]);
		
		
	}
     
	
	public static void mergesort(int[][] values) {
		    mergesortRange(values, 0, values.length);
		  }

		  private static void mergesortRange(int[][] values, int startIndex, int endIndex) {
		    int rangeSize = endIndex - startIndex;
		    if (needsSorting(rangeSize)) {
		      int midPoint = (startIndex + endIndex) / 2;
		      mergesortRange(values, startIndex, midPoint);
		      mergesortRange(values, midPoint, endIndex);
		      mergeRanges(values, startIndex, midPoint, endIndex);
		    }
		  }

		  private static void mergeRanges(int[][] values, int startIndex, int midPoint,
					int endIndex) {
			
				final int rangeSize = endIndex - startIndex;
				int[][] destination = new int[rangeSize][3];
				int firstIndex = startIndex;
				int secondIndex = midPoint;
				int copyIndex = 0;

				double diff = distance(values[firstIndex]) - distance(values[secondIndex]);
				if (Math.abs(diff) < threshold){
					diff = 0.0;
				}
				while (firstIndex < midPoint && secondIndex < endIndex) {
					if (Math.abs(diff) > 0.0){
						if (distance(values[firstIndex]) < distance(values[secondIndex])) {
							
							destination[copyIndex] = values[firstIndex];
							++firstIndex;
						} 
						
						else {
							destination[copyIndex] = values[secondIndex];
							++secondIndex;
						
						}
					} else if (diff == 0.0){
						if(values[firstIndex][2] < values[secondIndex][2]){
							destination[copyIndex] = values[firstIndex];
							++firstIndex;
						}
						else {
							destination[copyIndex] = values[secondIndex];
							++secondIndex;
						}
					}
					++copyIndex;
				}
				while (firstIndex < midPoint) {
					destination[copyIndex] = values[firstIndex];
					++copyIndex;
					++firstIndex;
				}
				while (secondIndex < endIndex) {
					destination[copyIndex] = values[secondIndex];
					++copyIndex;
					++secondIndex;
				}
				for (int i = 0; i < rangeSize; ++i) {
					values[i + startIndex] = destination[i];
				}
			}

		  private static boolean needsSorting(int rangeSize) {
		    return rangeSize >= 2;
		  }
	
	private static int[][] sort(int[][] toSort) {
		mergesort(toSort);
		return toSort;
		
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
	
	private static int[][] deepClone(int[][] toClone) {
		int[][] clone = new int[toClone.length][];
		
		int i = 0;
		for (int[] point: toClone) {
			clone[i++] = point.clone();
		}
		
		return clone;
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
		double deltaX1 = x1 - point[0];
		double deltaY1 = y1 - point[1];
		// beware of integer overflow! d1 has to be a double to accommodate large numbers
		double d1 = Math.sqrt(deltaX1 * deltaX1 + deltaY1 * deltaY1);
		if (d1 < threshold) {
			d1 = 0.0;
		}

		// distance to the second point (set to 0 if too small):
		double deltaX2 = x2 - point[0];
		double deltaY2 = y2 - point[1];
		double d2 = Math.sqrt(deltaX2 * deltaX2 + deltaY2 * deltaY2);
		if (d2 < threshold) {
			d2 = 0.0;
		}

		// the smaller of the two:
		if (d1 <= d2){
			return d1;
		}
		return d2;
	}

}
