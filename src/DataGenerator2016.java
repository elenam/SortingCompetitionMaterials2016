
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

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
	
	static int timestamp = 0; 

	public static void main(String [] args) {
		int defaultGridSide = 100000;
		int defaultN = 10000000;
		int defaultWalkMin = 100;
		int defaultWalkMax = 1000;
		int defaultMaxSpeed = 100; 
		
		
		int gridSide = defaultGridSide;
		int n = defaultN;
		int walkMin = defaultWalkMin;
		int walkMax = defaultWalkMax;
		// the angle is always changed between 0 and 360
		
		
		
		// start at the center 
		int startX = gridSide / 2;
		int startY = gridSide / 2;
		
		// generate a direction
		// generate number of points
		// generate speed
		
		// keep generating walks until the timestamp is over the needed number of points
		
		
		// Test prints:
		//int[][] points = walk(50, 50, Math.toRadians(90), 5, 100);
		int[][] points = walk(50, 50, Math.toRadians(0), 5, 100);
		printArray(points);
		
		
	}
	
	/*
	 * direction: heading direction, in radians 
	 */
	private static int [][] walk(int xStart, int yStart, double direction, int speed, int n) {
		int [][] points = new int[n][3];
		// the first point is based on the start point:
		points[0][0] = generateX(xStart, direction, speed);
		points[0][1] = generateY(yStart, direction, speed);
		points[0][2] = timestamp++;
		for (int i = 1; i < n; ++i) {
			points[i][0] = generateX(points[i-1][0], direction, speed);
			points[i][1] = generateY(points[i-1][1], direction, speed);
			points[i][2] = timestamp++;
		}
		return points;
	}
	
	private static int generateX(int x, double direction, int speed) {
		return (int) Math.floor(x + speed * Math.cos(direction));
	}
	
	private static int generateY(int y, double direction, int speed) {
		return (int) Math.floor(y + speed * Math.sin(direction));
	}
	
	private static void printArray(int[][] points) {
		for (int i = 0; i < points.length; ++i) {
			System.out.println("x = " + points[i][0] + ", y = " + points[i][1] + ", t = " + points[i][2]);
		}
	}

}
