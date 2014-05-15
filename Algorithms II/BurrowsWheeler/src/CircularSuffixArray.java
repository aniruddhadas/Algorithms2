import java.util.Arrays;
import java.util.Comparator;


public class CircularSuffixArray {
    private String initString;
    //private int[][] suffixArray;
    private Integer[] index;
    public CircularSuffixArray(String s)  
    {
        // circular suffix array of s
        this.initString = s;
        
        //this.suffixArray = new int[this.length()][];
        
        this.index =  new Integer[this.length()];
        for(int i=0;i<s.length();i++){
        	// initialize index
//        	this.suffixArray[i] = new int[this.length()];
        	this.index[i] = i;
//        	for(int j=0;j<s.length();j++){
//        		this.suffixArray[i][j] = (i+j) % s.length();
//            }
        }
        
		// sorting
        Arrays.sort((Integer[]) index, new SuffixComparator(s));
    }
    
    public int length()
    {
        // length of s
        return this.initString.length();
    }
    
    public int index(int i){
        // returns index of ith sorted suffix
        return this.index[i];
    }
    
    private class SuffixComparator implements Comparator<Integer> {
        //private int[][] suffixArray;
        String initString;
        //contructor to set the column to sort on.
        SuffixComparator(String initString) {
          //this.suffixArray = suffixArray;
          this.initString = initString;
        }

        // Implement the abstract method which tells
        // how to order the two elements in the array.
        public int compare(int o1, int o2) 
        {
        	
//        	int[] row1 = new int[initString.]
//        	for(int j=0;j<initString.length();j++){
//        		
//        	} 
//        	int[] suffixArray1 = (i+j) % s.length();
//        	//int[] row1 = suffixArray[o1];
//        	//int[] row2 = suffixArray[o2];
//        	// not start going column by column
        	for(int counter=0;counter<initString.length();counter++)
        	{
        		// go to the first column that breaks the tie
        		char char1 = initString.charAt((o1+counter) % initString.length());
        		char char2 = initString.charAt((o2 + counter) % initString.length());
        		if(char1 == char2){
        			continue;
        		}
        		else
        		{
        			if(char1 < char2)
        			{
        				return -1;
        			}
        			else{
        				return +1;
        			}
        		}
        	}
        	return -1;
        }

    	@Override
    	public int compare(Integer o1, Integer o2) {
    		// TODO Auto-generated method stub
    		return compare((int) o1, (int) o2);
    	}
    }
}
