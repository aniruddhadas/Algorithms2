import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class CircularSuffixArrayTests{
    @Test
    public void CircularSuffixArray_length_s() {
        CircularSuffixArray c = new CircularSuffixArray("foobar!");
        assertEquals(7, c.length());
    }
    
    @Test
    public void CircularSuffixArray_index_i() {
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        assertEquals(0, c.index(3));
    }
    
    @Test
    public void CircularSuffixArray_index_i_1() {
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        assertEquals(c.index(0), 11);
        assertEquals(c.index(1), 10);
        assertEquals(c.index(2), 7);
        assertEquals(c.index(3), 0);
        assertEquals(c.index(4), 3);
        assertEquals(c.index(5), 5);
        assertEquals(c.index(6), 8);
        assertEquals(c.index(7), 1);
        assertEquals(c.index(8), 4);
        assertEquals(c.index(9), 6);
        assertEquals(c.index(10), 9);
        assertEquals(c.index(11), 2);
        
    }
}
