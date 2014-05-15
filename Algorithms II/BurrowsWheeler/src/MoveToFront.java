public class MoveToFront {
	private final static int R = 256;
	
    // apply move-to-front encoding, reading from standard input and writing to standard output
    private static char[] encode(char[] in)
    {
    	//return new char[] {65,66,82,02,68,01,69,01,04,04,02,38};
    	// this will get deleted later
    	char[] r = InitializeR();
    	char[] result = new char[in.length];
    	
    	// now we will go through each element and update the values
    	for(int i=0;i<in.length;i++)
    	{
    		// get current position
    		char currentPosition = r[in[i]];
	
    		// anything less than current position 
    		// scanning from r[0]..r[255] that has a value 
    		// less than currentPosition will get incrmented by 1
    		// we are done when we increment currentPosition items
    		
    		int remainingUpdates = currentPosition;
    		
    		for (int j = 0; j < r.length && remainingUpdates > 0; j++)
    		{
    			if (r[j] < currentPosition)
    			{
    				// increment by 1
    				r[j]++;
    				remainingUpdates--;
    			}
    		}
    		
    		r[in[i]] = 0;
    		result[i] = currentPosition;
    		BinaryStdOut.write(currentPosition);
    	}
    	
    	// close output stream
        BinaryStdOut.close();
    	return result;
    }

	private static char[] InitializeR() {
		char[] r = new char[R];
    	// initialize r
    	for(char i=0;i<R;i++)
    	{
    		r[i] = i;
    	}
		return r;
	}
	
    // apply move-to-front decoding, reading from standard input and writing to standard output
    private static char[] decode(char[] in){
    	char[] r = InitializeR();
    	char[] result = new char[in.length];
    	for(int i=0;i<in.length;i++)
    	{
    		// lookup value from passed in index
        	char currentPostion = in[i];
        	char currentValue = r[currentPostion];
        	// shift all values less than position up
        	for(int j=currentPostion; j>0; j--)
        	{
        		r[j] = r[j-1];
        	}
        	
        	// move to front the value
        	r[0] = currentValue;
        	// set result
        	result[i] = currentValue;
        	BinaryStdOut.write(currentValue);
    	}
    	
    	// close output stream
        BinaryStdOut.close();
    	return result;
    }
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        encode(input);
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        decode(input);
    }
    
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new RuntimeException("Illegal command line argument");
    }
}