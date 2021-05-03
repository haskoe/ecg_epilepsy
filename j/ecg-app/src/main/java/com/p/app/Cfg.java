package com.p.app;

// Java implementation of the approach
import java.util.TreeSet;

public class Cfg {

	// Pair class for the value and its index
	public static class Pair implements Comparable<Pair> {
		private double value, index;

		// Constructor
		public Pair(double v, double p) {
			value = v;
			index = p;
		}

		// This method will be used by the treeset to
		// search a value by index and setting the tree
		// nodes (left or right)
		@Override
		public int compareTo(Pair o) {

			// Two nodes are equal only when
			// their indices are same
			if (index == o.index) {
				return 0;
			} else if (value == o.value) {
				return Double.compare(index, o.index);
			} else {
				return Double.compare(value, o.value);
			}
		}

		// Function to return the value
		// of the current object
		public double value() {
			return value;
		}

		// Update the value and the position
		// for the same object to save space
		public void renew(double v, double p) {
			value = v;
			index = p;
		}

		@Override
		public String toString() {
			return "abe";
		}
	}

	// Function to print the median for the current window
	private static double printMedian(TreeSet<Pair> minSet, TreeSet<Pair> maxSet, int window) {
		if (window % 2 == 0) {
			return (minSet.last().value() + maxSet.first().value()) / 2.0;
		}
		return minSet.size() > maxSet.size() ? minSet.last().value() : maxSet.first().value();
	}

	// Function to find the median
	// of every window of size k
	public static double[] findMedian(double arr[], int k) {
		double[] result = new double[arr.length];

		TreeSet<Pair> minSet = new TreeSet<>();
		TreeSet<Pair> maxSet = new TreeSet<>();

		// To hold the pairs, we will keep renewing
		// these instead of creating the new pairs
		Pair[] windowPairs = new Pair[k];

		for (int i = 0; i < k; i++) {
			windowPairs[i] = new Pair(arr[i], i);
		}

		// Add k/2 items to maxSet
		for (int i = 0; i < k / 2; i++) {
			maxSet.add(windowPairs[i]);
		}

		for (int i = k / 2; i < k; i++) {

			// Below logic is to maintain the
			// maxSet and the minSet criteria
			if (arr[i] < maxSet.first().value()) {
				minSet.add(windowPairs[i]);
			} else {
				minSet.add(maxSet.pollFirst());
				maxSet.add(windowPairs[i]);
			}
		}

		int n = 0;
		result[n++] = printMedian(minSet, maxSet, k);

		for (int i = k; i < arr.length; i++) {

			// Get the pair at the start of the window, this
			// will reset to 0 at every k, 2k, 3k, ...
			Pair temp = windowPairs[i % k];
			if (temp.value() <= minSet.last().value()) {

				// Remove the starting pair of the window
				minSet.remove(temp);

				// Renew window start to new window end
				temp.renew(arr[i], i);

				// Below logic is to maintain the
				// maxSet and the minSet criteria
				if (temp.value() < maxSet.first().value()) {
					minSet.add(temp);
				} else {
					minSet.add(maxSet.pollFirst());
					maxSet.add(temp);
				}
			} else {
				maxSet.remove(temp);
				temp.renew(arr[i], i);

				// Below logic is to maintain the
				// maxSet and the minSet criteria
				if (temp.value() > minSet.last().value()) {
					maxSet.add(temp);
				} else {
					maxSet.add(minSet.pollLast());
					minSet.add(temp);
				}
			}

			result[n++] = printMedian(minSet, maxSet, k);
		}
		return result;
	}

	// // Driver code
	// public static void main(String[] args)
	// {
	// int[] arr = new int[] { 0, 9, 1, 8, 2,
	// 7, 3, 6, 4, 5 };
	// int k = 3;

	// findMedian(arr, k);
	// }
}
