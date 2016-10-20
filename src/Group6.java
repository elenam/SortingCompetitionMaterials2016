import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group6 {
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

    private static int[][] deepClone(int[][] toClone) {
        int[][] clone = new int[toClone.length][];

        int i = 0;
        for (int[] point: toClone) {
            clone[i++] = point.clone();
        }

        return clone;
    }


    private static int[][] sort(int[][] toSort) {
        threshold = threshold * threshold;
        Banana[] bananas = new Banana[toSort.length];
        for(int i = 0; i < toSort.length; i++) {
            bananas[i] = new Banana(toSort[i][0], toSort[i][1], toSort[i][2], dist(toSort[i][0], toSort[i][1]));
        }

        Arrays.sort(bananas);

        for(int i = 0; i < toSort.length; i++) {
            int[] temp = {bananas[i].x, bananas[i].y, bananas[i].time};
            toSort[i] = temp;
        }
        return toSort;
    }

    private static double dist(double x, double y) {
        double one = ((x1 - x) * (x1 - x)) + ((y1 - y) * (y1 - y));
        double two = ((x2 - x) * (x2 - x)) + ((y2 - y) * (y2 - y));

        one = (one < threshold) ? 0.0 : one;
        two = (two < threshold) ? 0.0 : two;

        return (one <= two) ? one : two;
    }


    private static class Banana implements Comparable<Banana> {
        int x;
        int y;
        int time;
        double distance;

        public Banana(int x, int y, int time, double distance) {
            this.x = x;
            this.y = y;
            this.time = time;
            this.distance = distance;
        }

        public String toString() {
            return "" + x + " " + y + " " + time + " " + distance;
        }

        public int compareTo(Banana banana) {
            double diff = this.distance - banana.distance;
            if(Math.abs(diff) < threshold) return 0;
            return (diff < 0) ? -1 : 1;
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

    private static double distance(int[] point) {
        // distance to the first point (set to 0 if too small):
        double deltaX = x1 - point[0];
        double deltaY = y1 - point[1];
        // beware of integer overflow! d1 has to be a double to accommodate large numbers
        double d1 = deltaX * deltaX + deltaY * deltaY;
        if (d1 < (threshold * threshold)) {
            d1 = 0.0;
        }

        // distance to the second point (set to 0 if too small):
        deltaX = x2 - point[0];
        deltaY = y2 - point[1];
        double d2 = deltaX * deltaX + deltaY * deltaY;
        if (d2 < (threshold * threshold)) {
            d2 = 0.0;
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
                return 0; // the two distances are equal
            }
            if (diff < 0) {
                return -1;
            } else {
                return 1;
            }
        }

    }
}
