import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sorting Competition 2016
 * 
 * @author Laverne Schrock, Tony Song
 *
 */
public class Group5 {

	private static int x1;
	private static int y1;
	private static int x2;
	private static int y2;
	private static double threshold = 0.0000000001;

	public static void main(String[] args) throws InterruptedException {
		if (args.length < 2) {
			System.out.println("Please run with two command line arguments: input and output file names");
			System.exit(0);
		}

		String inputFileName = args[0];
		String outFileName = args[1];

		int[][] points = readInData(inputFileName);

		int[][] toSort = points.clone();

		DataPoint[] sorted = sort(toSort);

		toSort = points.clone();

		Thread.sleep(10); // to let other things finish before timing; adds
							// stability of runs

		long start = System.currentTimeMillis();
		sorted = sort(toSort);

		long end = System.currentTimeMillis();

		System.out.println(end - start);

		writeOutResult(sorted, outFileName);

	}

	
	/**
	 * 
	 * @param toSort
	 * @return a two dimensional integer array
	 */
	private static DataPoint[] sort(int[][] toSort) {
		
		// Copy and convert the initial array into our array
		DataPoint points[] = new DataPoint[toSort.length];
		for (int i = 0; i < toSort.length; i++) {
			points[i] = new DataPoint(toSort[i][0], toSort[i][1], toSort[i][2]);
		}

		Arrays.sort(points);

		return points;
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

	private static void writeOutResult(DataPoint[] sorted, String outputFilename) {
		try {
			PrintWriter out = new PrintWriter(outputFilename);
			for (DataPoint point : sorted) {
				out.println(point.x + " " + point.y + " " + point.timeStamp);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Returns the squared distance to the closer reference point
	 * @param x
	 * @param y
	 * @return the squared distance
	 */
	private static double fastDistance(int x, int y) {
		// distance to the first point (set to 0 if too small):
		double deltaX = x1 - x;
		double deltaY = y1 - y;
		// beware of integer overflow! d1 has to be a double to accommodate
		// large numbers **
		double d1 = deltaX * deltaX + deltaY * deltaY;
		if (d1 < threshold * threshold) {
			d1 = 0.0;
		}

		// distance to the second point (set to 0 if too small):
		deltaX = x2 - x;
		deltaY = y2 - y;
		double d2 = deltaX * deltaX + deltaY * deltaY;
		if (d2 < threshold * threshold) {
			d2 = 0.0;
		}

		// the smaller of the two:
		if (d1 <= d2)
			return d1;
		return d2;
	}
	
	/**
	 * An inner class< to store each data point with a calculated distance.
	 * <em>compareTo</em> method is implemented to be used with the sorting method 
	 * in the <strong>Arrays</strong> class.
	 */
	private static class DataPoint implements Comparable<DataPoint> {
		int x;
		int y;
		int timeStamp;
		double distance;

		public DataPoint(int x, int y, int timeStamp) {
			this.x = x;
			this.y = y;
			this.timeStamp = timeStamp;
			this.distance = fastDistance(x, y);
		}

		@Override
		public int compareTo(DataPoint that) {
			double diff = this.distance - that.distance;

			if (Math.abs(diff) < threshold) {
				return this.timeStamp - that.timeStamp; // the two distances are
														// equal
			}
			if (diff < 0) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
