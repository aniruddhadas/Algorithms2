import java.util.Arrays;

public class BurrowsWheeler {
	private static final int R = 256;
	private static char terminator = '\0';
	private static char[] encode(char[] dataTemp) {
//		// TODO: fix this crap!
//		char[] data = Arrays.copyOfRange(dataTemp, 0, dataTemp.length + 1);
//
//		data[data.length - 1] = terminator;
//
//		String dataString = new String(data);
//
//		CircularSuffixArray circularSuffixArray = new CircularSuffixArray(
//				dataString);
//		// char[] returnVal = new char[data.length];
//
//		for (int counter = 0; counter < data.length; counter++) {
//
//			int index = circularSuffixArray.index(counter) - 1;
//			if (circularSuffixArray.index(counter) == 0) {
//				// returnVal[counter] = data[data.length - 1];
//				BinaryStdOut.write(data[data.length - 1]);
//			} else {
//				// returnVal[counter] = data[index];
//				BinaryStdOut.write(data[index]);
//			}
//		}

		// rewriting code
		String dataString = BinaryStdIn.readString();
		int n = dataString.length();
		CircularSuffixArray circularSuffixArray = new CircularSuffixArray(dataString);
		int first;
		for (first = 0; first < circularSuffixArray.length(); first++)
			if (circularSuffixArray.index(first) == 0)
				break;
		BinaryStdOut.write(first);
		for (int i = 0; i < circularSuffixArray.length(); i++)
			BinaryStdOut.write(dataString.charAt((circularSuffixArray.index(i) + n - 1) % n));
		
		// close output stream
		BinaryStdOut.close();
		// return returnVal;
		return null;
	}

	// private static void NotImplemented() {
	// // TODO Auto-generated method stub
	// int result = 1 / 0;
	// if (result == 0)
	// {
	// //
	// }
	// }

	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {
		//String s = BinaryStdIn.readString();
		//char[] input = s.toCharArray();
		encode(null);
	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing
	// to standard output
	public static void decode() {
		//String s = BinaryStdIn.readString();
		//char[] input = s.toCharArray();
		decode(null);
	}

	private static char[] decode(char[] t) {
//		// keep t
//		char[] index = t.clone();
//		// sort t should be called index
//		Arrays.sort(index);
//
//		int len = t.length;
//		// allocate next
//		int[] next = new int[len];
//		boolean[] marked = new boolean[len];
//
//		// char[] result = new char[len - 1];
//
//		// for each char in index lookup in the transform see if the there is a
//		// value that is unmarked and use its position to set that
//		// to next for the current char keep scanning t to find out what is the
//		// next available index of i in t that hasn't already been voided
//		for (int i = 0; i < len; i++) {
//			// scanning
//			for (int j = 0; j < len; j++) {
//				// found match
//				if (t[j] == index[i]) {
//					// marked, need to find another candidate
//					if (marked[j]) {
//						// keep scanning j
//						continue;
//					}
//
//					// set next[i] to j
//					// mark
//					marked[j] = true;
//					next[i] = j;
//					break;
//				}
//			}
//		}
//
//		// rebuild the original string. follow the points in the next and keep
//		// readin next and output from index
//		// keep going till you have hit length
//		int current = 0;
//		for (int i = 0; i < len - 1; i++) {
//			// result[i] = index[next[current]];
//			BinaryStdOut.write(index[next[current]]);
//			current = next[current];
//		}
//
//		// close output stream
//		BinaryStdOut.close();
//
//		// find the marker in the result and put that at the beginning
//		// return result;
//		return null;
		
		
		int first = BinaryStdIn.readInt();
		String t1 = BinaryStdIn.readString();
		int n = t1.length();
		int[] count = new int[R + 1], next = new int[n];
		for (int i = 0; i < n; i++)
			count[t1.charAt(i) + 1]++;
		for (int i = 1; i < R + 1; i++)
			count[i] += count[i - 1];
		for (int i = 0; i < n; i++)
			next[count[t1.charAt(i)]++] = i;
		for (int i = next[first], c = 0; c < n; i = next[i], c++)
			BinaryStdOut.write(t1.charAt(i));
		BinaryStdOut.close();
		return null;
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new RuntimeException("Illegal command line argument");
	}
}