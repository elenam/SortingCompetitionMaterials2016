
public class Mergesort  {

	static int x = 0;
	static int y = 0;
	
	public static void sort(int[][] values, int xIn, int yIn) {
		x = xIn;
		y = yIn;
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

	public static double distance(int[] array){
		double dist = Math.sqrt(array[0] * x + array[1] * y);
		if (dist < Group11.threshold){
			dist = 0.0;
		}
		dist = (double)Math.round(dist * 10000000000d) / 10000000000d;
		return dist;
	}

	private static void mergeRanges(int[][] values, int startIndex, int midPoint,
			int endIndex) {
		/*
		 * Assume that the two ranges are sorted:
		 *   (forall i | startIndex <= i <= j < midPoint : values[i] <= values[j])
		 *   (forall i | midPoint <= i <= j < endIndex : values[i] <= values[j])
		 * then merge them into a single sorted array, copy that back, and return.
		 */
		final int rangeSize = endIndex - startIndex;
		int[][] destination = new int[rangeSize][3];
		int firstIndex = startIndex;
		int secondIndex = midPoint;
		int copyIndex = 0;
		while (firstIndex < midPoint && secondIndex < endIndex) {
			if (distance(values[firstIndex]) < distance(values[secondIndex])) {
				destination[copyIndex] = values[firstIndex];
				++firstIndex;
			} else {
				destination[copyIndex] = values[secondIndex];
				++secondIndex;
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

}
