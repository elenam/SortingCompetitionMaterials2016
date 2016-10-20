import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group4 {
	private static int x1;
	private static int y1;
	private static int x2;
	private static int y2;
	private static double slope;
	private static double intersection;
	private static double threshold = 0.0000000001;
	private static int mergeThreshold = 8;

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
		setLine();
		//Arrays.sort(toSort, new PointComparator());
		//shellSort(toSort);
		//quickSort(toSort, 0, toSort.length - 1);
		//return mergeSort(toSort);
		return mergeSort2(toSort);
		//return toSort;
	}
	
	private static int[][] mergeSort(int[][] toSort) {
		if (toSort.length <= 1) {
			return toSort;
		}
		
		
		//Split array in half
		
		int[][] first = new int[toSort.length/2][];
		int[][] second = new int[toSort.length - first.length][];
		System.arraycopy(toSort, 0, first, 0, first.length);
		System.arraycopy(toSort, first.length, second, 0, second.length);
		
		// Sort each half
		mergeSort(first);
		mergeSort(second);
		
		// Merge the halves together, overwriting the original array
		merge(first, second, toSort);
		
		return toSort;
	}
	
	private static int[][] mergeSort2(int[][] toSort) {
		if (toSort.length <= 1) {
			return toSort;
		}
		
		
		//Split array in half
		
		int[][] first = new int[toSort.length/2][];
		int[][] second = new int[toSort.length - first.length][];
		System.arraycopy(toSort, 0, first, 0, first.length);
		System.arraycopy(toSort, first.length, second, 0, second.length);
		
		// Sort each half
		if (first.length >= mergeThreshold){
		mergeSort2(first);
		mergeSort2(second);
		} else {
			insertionSort(first);
			insertionSort(second);
		}
		
		// Merge the halves together, overwriting the original array
		merge(first, second, toSort);
		
		return toSort;
	}
	
	private static void merge(int[][] first, int[][] second, int[][] result) {
		// Merge both halves into the result array
		int indexFirst = 0;
		int indexSecond = 0;
		
		int j = 0;
		
		while (indexFirst < first.length && indexSecond < second.length) {
			if (distance(first[indexFirst]) <= distance(second[indexSecond])) {
				result[j] = first[indexFirst];
				indexFirst++;
			} else {
				result[j] = second[indexSecond];
				indexSecond++;
			}
			j++;
		}
		
		System.arraycopy(first, indexFirst, result, j, first.length - indexFirst);
		System.arraycopy(second, indexSecond, result, j, second.length - indexSecond);
	}
	
	private static void insertionSort(int[][] toSort){
		for (int i = 1; i < toSort.length; i++){
			int j = i;
			int[] temp = toSort[i];
			while ((j > 0) && (distance(toSort[j-1]) > distance(temp))) {
				toSort[j] = toSort[j-1];
				j--;
			}
			toSort[j] = temp;
		}
	}
	
	
	


	private static void quickSort(int[][] toSort, int low, int high){
		
		if (low >= high){
			return;
		}
		
		// choose a pivot
		int middle = low + (high - low)/2;
		//int middle = low + (int) (Math.random()*(high-low+1));
		int[] pivot = toSort[middle];
		
		//make left < pivot and right > pivot
		int i = low, j = high;
		while (i <= j) {
			double pivotDistance = lineDistance(pivot);
			while (lineDistance(toSort[i]) < pivotDistance){
				i++;
			}
			while (lineDistance(toSort[j]) > pivotDistance){
				j--;
			}
			
			if (i <= j) {
				int[] temp = toSort[i];
				toSort[i] = toSort[j];
				toSort[j] = temp;
				i++;
				j--;
			}
		}
		
		//recursion
		if (low < j) {
			quickSort(toSort, low, j);
		}
		if (high > i) {
			quickSort(toSort, i, high);
		}
	}


	private static void shellSort(int[][] toSort){
		int inner, outer;
		int[] temp;

		setLine();

		int h = 1;
		while (h <= toSort.length / 3) {
			h = h * 3 + 1;
			//		      System.out.println("gets here");
		}
		while (h > 0) {
			for (outer = h; outer < toSort.length; outer++) {
				//		    	  System.out.println("now we are here");
				temp = toSort[outer];
				inner = outer;
				//		        System.out.println(outer);

				while (inner > h - 1 && lineDistance(toSort[inner - h]) >= lineDistance(temp)) {
					//		        	System.out.println("Finally here");
					toSort[inner] = toSort[inner - h];
					inner -= h;
					//		          System.out.println(inner);
				}
				toSort[inner] = temp;
			}
			h = (h - 1) / 3;
		}

	}

	private static void setLine() {
		//if x1 is greater than x2, switch the points
		if (x1 > x2) {
			int tempx2 = x2;
			int tempy2 = y2;
			x2 = x1;
			y2 = y1;
			x1 = tempx2;
			y1 = tempy2;
		}
		//slope is the inverse of the slope between the points
		slope = (double) -(x1 - x2)/(y1 - y2);
		intersection = (double) ((y1 + y2)/2 - slope*((x1 + x2)/2));
		//System.out.println(slope);
		//System.out.println(intersection);
	}

	private static double lineDistance(int[] point) {
		double distance;

		//if the point is above the line, then it must be closer to x1, y1
		if (slope*point[0] + intersection < point[1]){
			// distance to the first point (set to 0 if too small):
			double deltaX = x1 - point[0];
			double deltaY = y1 - point[1];
			// beware of integer overflow! d1 has to be a double to accommodate large numbers
			distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		} else {
			// distance to the first point (set to 0 if too small):
			double deltaX = x2 - point[0];
			double deltaY = y2 - point[1];
			// beware of integer overflow! d1 has to be a double to accommodate large numbers
			distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		}


		if (distance < threshold) {
			distance = 0.0;
		}
		
		return distance;

	}

















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
