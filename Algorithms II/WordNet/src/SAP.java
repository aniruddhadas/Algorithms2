import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph g;
    public SAP(Digraph g) {
        this.g = new Digraph(g);
    }

    // int lastSap = -1;
    // int lastV = -1;
    // int lastW = -1;
    // int theRoot = INFINITY;
    
    BreadthFirstDirectedPaths lastBfsV;
    BreadthFirstDirectedPaths lastBfsW;

    public int length(int v, int w) {
        List<Integer> listv = new ArrayList<Integer>();
        listv.add(v);

        List<Integer> listw = new ArrayList<Integer>();
        listw.add(w);
        return length(listv, listw);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int sap = ancestor(v, w);

        if (sap == -1) {
            return -1;
        }

        return (this.lastBfsV.distTo(sap) + this.lastBfsW.distTo(sap));
    }

    public int ancestor(int v, int w) {
        // if((v == lastV && w == lastW)||(v == lastW && w == lastV)){
        // return lastSap;
        // }

        // cache
        // this.lastV = v;
        // this.lastW = w;
        
        List<Integer> listv = new ArrayList<Integer>();
        listv.add(v);

        List<Integer> listw = new ArrayList<Integer>();
        listw.add(w);

        return this.ancestor(listv, listw);
    }

    // All methods (and the constructor) should take time at most proportional to E + V in the worst case, 
    // where E and V are the number of edges and vertices in the digraph, respectively. 
    // Your data type should use space proportional to E + V. 
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for(int vertex:v){
            if (vertex < 0 || vertex >= this.g.V()) throw new IndexOutOfBoundsException();    
        }
        for(int vertex:w){
            if (vertex < 0 || vertex >= this.g.V()) throw new IndexOutOfBoundsException();    
        }
        
        // TO-DO: can we cache the list of V and W?
        // so that we can short circuit
        this.lastBfsV = new BreadthFirstDirectedPaths(this.g, v);
        this.lastBfsW = new BreadthFirstDirectedPaths(this.g, w);
        
        int shortestAncestor = -1;
        int minDist = INFINITY;
        for (int vertex = 0; vertex < g.V(); vertex++){
            if (this.lastBfsV.hasPathTo(vertex) && this.lastBfsW.hasPathTo(vertex))
            {
                int sumDist = this.lastBfsV.distTo(vertex) 
                        + this.lastBfsW.distTo(vertex);
                if(sumDist < minDist)
                {
                    shortestAncestor = vertex;
                    minDist = sumDist;
                }
            }
        }
        return shortestAncestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
