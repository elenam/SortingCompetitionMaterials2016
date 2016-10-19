import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class DataGenerator2016 {
	/**
	 * See Readme for an overview of the data generating process. The result is
	 * being written to the file given as the first command line argument.
	 * 
	 * @param args
	 * 
	 * 
	 * 
	 *            Author: Elena Machkasova
	 */

	private static int timestamp = 0;
	private static int gridSide;
	

	public static void main(String[] args) {
		int defaultGridSide = 100000;
		int defaultN = 1000000;
		int defaultWalkMin = 10;
		int defaultWalkMax =  500;
		int defaultMaxSpeed = 100;
		String filename = "nofile";
		
		// Reading main args
		
		if (args.length >= 1) {
			filename = args[0];
		}

		gridSide = 200000; // global variable (need to use in a function)
		int n = 1000000;
		int walkMin = 1;
		int walkMax = 1000;
		int maxSpeed = 100;
		// the angle is always between 0 and 360
		
		// generate two points:
		int x1 = (int) (Math.random() * gridSide); 
		int y1 = (int) (Math.random() * gridSide); 
		int x2 = (int) (Math.random() * gridSide); 
		int y2 = (int) (Math.random() * gridSide); 
		
		//int walks = 0; // for stats purposes

		// start at the center
		int startX = gridSide / 2;
		int startY = gridSide / 2;

		int[][] points = new int[n][3];
		points[0][0] = startX;
		points[0][1] = startY;
		points[0][2] = timestamp++;

		// keep generating walks until the timestamp is over the needed number
		// of points
		while (timestamp < n) {
			// generate length of a walk
			int length = walkMin
					+ (int) (Math.random() * (walkMax - walkMin));
			// make sure we are not going over n:
			if (length + timestamp > n)
				length = n - timestamp;
			// store the current number of points for copying
			int oldLength = timestamp;
			
			// generate a direction
			double angle = Math.random() * 360;
			double direction = Math.toRadians(angle);

			// generate speed
			int speed = (int) (Math.random() * maxSpeed);

			int[][] walkPoints = walk(points[timestamp - 1][0],
					points[timestamp - 1][1], direction, speed, length);
			
			// copy walk points into points
			for (int i = 0; i < length; ++i) {
				points[oldLength + i][0] = walkPoints[i][0]; // copy x
				points[oldLength + i][1] = walkPoints[i][1]; // copy y
				points[oldLength + i][2] = walkPoints[i][2]; // copy timestamp
			}
			
			//System.out.println("The walk at the angle " + angle + "at the speed " + speed + " ends at point " + timestamp);
			//++walks;
		}
		
		//System.out.println("The total number of walks is " + walks);
		

		// Test prints:
		//int[][] points = walk(50, 50, Math.toRadians(90), 5, 100);
		//int[][] points = walk(50, 50, Math.toRadians(0), 5, 100);
		//printArray(points);
		
		// the output goes to the standard output (console)
		if (filename.equals("nofile")) {
			writeOutputStandardOut(x1, y1, x2, y2, points);
		} else { // output goes to a file
			try {
				writeOutputFile(new PrintWriter(filename), x1, y1, x2, y2, points);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * direction: heading direction, in radians 
	 */
	private static int [][] walk(int xStart, int yStart, double direction, int speed, int n) {
		int [][] walkPoints = new int[n][3];
		// the first point is based on the start point:
		walkPoints[0][0] = generateX(xStart, direction, speed);
		walkPoints[0][1] = generateY(yStart, direction, speed);
		walkPoints[0][2] = timestamp++;
		for (int i = 1; i < n; ++i) {
			walkPoints[i][0] = generateX(walkPoints[i-1][0], direction, speed);
			walkPoints[i][1] = generateY(walkPoints[i-1][1], direction, speed);
			walkPoints[i][2] = timestamp++;
		}
		return walkPoints;
	}
	
	private static int generateX(int x, double direction, int speed) {
		return wrapAround((int) Math.floor(x + speed * Math.cos(direction)));
	}
	
	private static int generateY(int y, double direction, int speed) {
		return wrapAround((int) Math.floor(y + speed * Math.sin(direction)));
	}
	
	private static int wrapAround(int coord) {
		if (coord < 0) {
			return coord + gridSide;
		}
		if (coord > gridSide) {
			return coord - gridSide;
		}
		else return coord;
	}
	
	private static void printArray(int[][] points) {
		for (int i = 0; i < points.length; ++i) {
			System.out.println("x = " + points[i][0] + ", y = " + points[i][1] + ", t = " + points[i][2]);
		}
	}
	
	private static void writeOutputStandardOut(int x1, int y1, int x2, int y2, int[][] points) {
		System.out.println(x1 + " " + y1);
		System.out.println(x2 + " " + y2);
		for (int i = 0; i < points.length; ++i) {
			System.out.print(points[i][0] + " ");
			System.out.print(points[i][1] + " ");
			System.out.println(points[i][2]);
		}
	}

	private static void writeOutputFile(PrintWriter out, int x1, int y1, int x2, int y2, int[][] points) {
		out.println(x1 + " " + y1);
		out.println(x2 + " " + y2);
		for (int i = 0; i < points.length; ++i) {
			out.print(points[i][0] + " ");
			out.print(points[i][1] + " ");
			out.println(points[i][2]);
		}
		out.close();
	}

}
