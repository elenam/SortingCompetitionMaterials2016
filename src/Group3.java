import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Daniel W. and Ben G. 

public class Group3 {
	//Group 3
	private static int x1;
	private static int y1;
	private static int x2;
	private static int y2;
	
	private static double threshold = 0.0000000001;
	
	private static double halfway;

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
	
	
	private static int[][] sort(int[][] toSort) {
		//Arrays.sort(toSort, new PointComparator());
		MergeSortRange(toSort, 0, toSort.length, new PointComparator());
		return toSort;
		
	}
	
	
	 public static void MergeSortRange(int[][] toSort, int start, int end, Comparator<int[]> comp) {
		 if (end - start < 2) {
			 // do nothing
		 } else {
			 int middle = (end + start) / 2;
			 MergeSortRange(toSort, start, middle, comp);
			 MergeSortRange(toSort, middle, end, comp);
			 MergeRange(toSort, start, middle, end, comp);
			 
		 }
	    }
	     
	    private static void MergeRange(int[][] toSort, int start, int middle, int end, Comparator<int[]> comp){
			//System.out.println("merge range is called");
	    	int[][] temp = new int[end - start][3];
			int leftPointer = start;
			int rightPointer = middle;
			for(int i = 0; i < end - start; i++){
				if(rightPointer == end){
					temp[i] = toSort[leftPointer];
					leftPointer++;
				} else if(leftPointer == middle || comp.compare(toSort[rightPointer], toSort[leftPointer]) < 0){
					temp[i] = toSort[rightPointer];
					rightPointer++;
				} else {
					temp[i] = toSort[leftPointer];
					leftPointer++;
				}
			}
			for(int i = 0; i < end - start; i++){
				toSort[i + start] = temp[i];
			}
	    }
	    
	// YOUR SORTING METHOD GOES HERE. 
	// You may call other methods and use other classes. 
	// Note: you may change the return type of the method. 
	// You would need to provide your own function that prints your sorted array to 
	// a while in the exact same format that my program outputs

//	private static void MergeSortRange(int[][] toSort, int start, int end, Comparator<int[]> comp){
//		System.out.println("Start: " + start);
//		System.out.println("End: " + end);
//		
//		try{
//		Thread.sleep 2d array java(100);
//		} catch (Exception e){}
//		
//		
//		if(end - start <= 1){
//			//do nothing
//			System.out.println("End of calls");
//		} else {
//			//TODO woop woop TODO
//			//There is a huge error right here
//			//When the parameters are 1 and 3, due to rounding, the range of the second half is (1, 3)
//			//Yes, cut it in half and half is identical to the original
//			//downright Lovecraftian
//			
//			MergeSortRange(toSort, start, (end/2), comp);
//			MergeSortRange(toSort, (end/2), end, comp);
//			MergeRange(toSort, start, end/2, end, comp);
//		}
//	}
//	
//	private static void MergeRange(int[][] toSort, int start, int middle, int end, Comparator<int[]> comp){
//		int[][] temp = new int[end - start][3];
//		int leftPointer = start;
//		int rightPointer = middle;
//		for(int i = 0; i < end - start; i++){
//			if(rightPointer == end){
//				temp[i] = toSort[leftPointer];
//				leftPointer++;
//			} else if(leftPointer == middle || comp.compare(toSort[rightPointer], toSort[leftPointer]) < 0){
//				temp[i] = toSort[rightPointer];
//				rightPointer++;
//			} else {
//				temp[i] = toSort[leftPointer];
//				leftPointer++;
//			}
//		}
//		for(int i = 0; i < end - start; i++){
//			toSort[i + start] = temp[i];
//		}
//		
//		System.out.println("MergeRange was called!");
//	}
	
	
	//OLD CODE
	/*
	private static int[][] MergeSort(int[][] toSort, Comparator<int[]> comp){
		if(toSort.length <= 1){
			return toSort;
		}
		return Merge(
				MergeSort(Arrays.copyOfRange(toSort, 0, toSort.length/2), comp),
				MergeSort(Arrays.copyOfRange(toSort, toSort.length/2, toSort.length), comp),
				comp);
	}
	
	private static int[][] Merge(int[][] left, int[][] right, Comparator<int[]> comp){
		int[][] output = new int[left.length + right.length][3];
		
		int leftProgress = 0;
		int rightProgress = 0;
		int totalProgress = 0;
		while(totalProgress < output.length){
			if(leftProgress == left.length){
				for(int i = rightProgress; i < right.length; i++){
					output[totalProgress] = right[i];
					totalProgress++;
				}
				break;
			}
			if(rightProgress == right.length){
				for(int i = leftProgress; i < left.length; i++){
					output[totalProgress] = left[i];
					totalProgress++;
				}
				break;
			}
			
		}
		
		return output;
	}
	//*/
	

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
			
			halfway = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
			
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
			d1 = 0.0;
		}

		// distance to the second point (set to 0 if too small):
		deltaX = x2 - point[0];
		deltaY = y2 - point[1];
		double d2 = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		if (d2 < threshold) {
			d2 = 0.0;
		}

		// the smaller of the two:
		if (d1 <= d2)
			return d1;
		return d2;
	}
	
	private static int[][] deepClone(int[][] toClone) {
		int[][] clone = new int[toClone.length][];
		
		int i = 0;
		for (int[] point: toClone) {
			clone[i++] = point.clone();
		}
		
		return clone;
	}
	
	private static double pointDistance(int[] point1, int[] point2){
		return Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2));
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
