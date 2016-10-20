import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *  Ben Sixel and Jack Ziegler
 */

public class Group9 {
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
		
		int[][] toSort = deepClone(points);
				
		int[][] sorted = sort(toSort);
		
		toSort = deepClone(points);
		
		Thread.sleep(10); //to let other things finish before timing; adds stability of runs

		long start = System.currentTimeMillis();
		
		sorted = sort(toSort);
		
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);

		writeOutResult(sorted, outFileName);		
	}
	

	// YOUR SORTING METHOD GOES HERE. 
	// You may call other methods and use other classes. 
	// Note: you may change the return type of the method. 
	// You would need to provide your own function that prints your sorted array to 
	// a while in the exact same format that my program outputs
	private static int[][] sort(int[][] toSort) {
		//Puddle.setThreshold(threshold);
		
		// Puddles are just a codename, for fun. Each represents a single point on the "board."
		Puddle[] points = new Puddle[toSort.length];
		
		int[] p; // Storing the values seems to improve speed. Not sure why, but we got on an abstraction kick and it worked.
		for (int i = 0; i < toSort.length; i++) { // Populate all the Puddles.
			p = toSort[i];
			points[i] = new Puddle(p[0], p[1], p[2]);
		}
		
		Arrays.sort(points);
		
		// Copy the newly sorted values back into toSort.
		Puddle pud;
		for (int i = 0; i < toSort.length; i++) {
			pud = points[i];			
			toSort[i][0] = pud.x;
			toSort[i][1] = pud.y;
			toSort[i][2] = pud.n;
		}
		
		
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
	
	private static int[][] deepClone(int[][] toClone) {
		int[][] clone = new int[toClone.length][];
		
		int i = 0;
		for (int[] point: toClone) {
			clone[i++] = point.clone();
		}
		
		return clone;
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
	
	// We created a custom comparator. Honestly we stole this idea by knowing that a different
	// group was doing this faster than we were. We tried a half dozen different things until
	// we decided this was the fastest. Why? Because.
	private static double distance(int xco, int yco) {
	// distance to the first point (set to 0 if too small):
	double deltaX = x1 - xco;
	double deltaY = y1 - yco;
	// beware of integer overflow! d1 has to be a double to accommodate large numbers
	double d1 = deltaX * deltaX + deltaY * deltaY;
	if (d1 < (threshold * threshold)) {
		d1 = 0.0;
	}

	// distance to the second point (set to 0 if too small):
	deltaX = x2 - xco;
	deltaY = y2 - yco;
	double d2 = deltaX * deltaX + deltaY * deltaY;
	if (d2 < (threshold * threshold)) {
		d2 = 0.0;
	}

	// the smaller of the two:
	if (d1 <= d2)
		return d1;
	return d2;
}
	
	
	
	// Puddles are an alias for an individual point. It contains all the necessary information to
	// produce a sorted Array, once populated.
	private static class Puddle implements Comparable<Puddle> {
		private int x;
		private int y;
		private int n;
		private double dist;
		
		//private static double threshold;
		
		// Leftover code from various attempts to improve efficiency.
//		public Puddle(int[] point) {
//			x = point[0];
//			y = point[1];
//			n = point[2];
//			//dist = distance(point);
//			dist = distance(x, y);
//		}
		
		public Puddle(int xco, int yco, int pos) {
			x = xco;
			y = yco;
			n = pos;
			//dist = distance(point);
			dist = distance(x, y);
		}

		public int compareTo(Puddle that) {
			double diff = this.dist - that.dist;
			
			if (Math.abs(diff) < (threshold * threshold)) {
				return this.n - that.n;
			}
			
			//return (diff > 0) ? 1 : -1;
			if (diff > 0) {
				return 1;
			} else {
				return -1;
			}
			
		}
		
		// We tried storing the threshold, didn't make any difference.
//		public static void setThreshold(double t) {
//			//threshold = t * t;
//		}
		
	}
}
