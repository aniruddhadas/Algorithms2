import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WordNet {
	private Map<String, Bag<Integer>> nounDict = new HashMap<String, Bag<Integer>>();
	private Map<Integer, String> synSetIdToNouns = new HashMap<Integer, String>();

	private Digraph g;

	// constructor takes the name of the two input files
	// The constructor should take time linearithmic (or better) in the input size
	public WordNet(String synsets, String hypernyms) {

		// eg: 21,A a,the 1st letter of the Roman alphabet
		// for synset get the first 2 element and split by comma
		// in the synset split the 2nd column and
		In synsetInputFile = new In(synsets);
		String line = synsetInputFile.readLine();
		while (line != null) {

			String[] result = line.split(",");
			int synSetid = Integer.parseInt(result[0]);
			this.synSetIdToNouns.put(synSetid, result[1]);

			String[] nouns = result[1].split("\\s+");
			for (String noun : nouns) {
				Bag<Integer> synBag;
				if (!this.nounDict.containsKey(noun)) {
					synBag = new Bag<Integer>();
					this.nounDict.put(noun, synBag);
				} else {
					synBag = this.nounDict.get(noun);
				}
				synBag.add(synSetid);
			}

			line = synsetInputFile.readLine();
		}

		// build the digraph from hypernym
		In hypernymInputFile = new In(hypernyms);
		line = hypernymInputFile.readLine();
		List<String> allLines = new ArrayList<String>();
		while (line != null) {
			allLines.add(line);
			line = hypernymInputFile.readLine();
		}

		int V = this.nounDict.size();
		// construct digraph
		this.g = new Digraph(V);

		for (String line2 : allLines) {
			String[] adjList = line2.split(",");
			int v = Integer.parseInt(adjList[0]);
			for (int counter = 1; counter < adjList.length; counter++) {
				int w = Integer.parseInt(adjList[counter]);
				this.g.addEdge(v, w);
			}
		}
		
		Topological topological = new Topological(this.g);
		if (!topological.hasOrder())
		{
			throw new IllegalArgumentException();
		}
		
//		int firstRoot = -1;
//		
//		// check to see if there is more than one root
//		for (int v : topological.order())
//		{
//			// lookup up v, and check to see if it has items in the adjecent list
//			// if it does not then we have a root
//			if (this.g.adj(v).iterator().hasNext())
//			{
//				// continue, this is not a root
//				continue;
//			}
//			
//			// got a root
//			// if we did not find one yet, then this is our root (it should also be the last item)
//			if (firstRoot == -1)
//			{
//				firstRoot = v;
//				continue;
//			}
//			
//			if (firstRoot != v)
//			{
//				throw new IllegalArgumentException(); 
//			}
//		}
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return this.nounDict.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return this.nounDict.containsKey(word);
	}

	private String lastNounA;
	private String lastNounB;
	private boolean lastSapFail;
	private int lastSapLength;
	private String lastSapSynset;

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		this.EnsureSap(nounA, nounB);
		return this.lastSapLength;
	}

	// a synset (second field of synsets.txt)
	// that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		this.EnsureSap(nounA, nounB);
		return this.lastSapSynset;
	}

	// a synset (second field of synsets.txt)
	// that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	// The methods distance() and sap() should run in time linear in the size of the WordNet digraph
	private void EnsureSap(String nounA, String nounB) {
		if ((this.lastNounA == nounA && this.lastNounB == nounB)
				|| (this.lastNounA == nounB && this.lastNounB == nounA)) {
			if (this.lastSapFail) {
				throw new IllegalArgumentException();
			}
			return;
		}

		this.lastNounA = nounA;
		this.lastNounB = nounB;
		this.lastSapFail = false;

		SAP sap = new SAP(this.g);
		Bag<Integer> v = this.nounDict.get(nounA);
		Bag<Integer> w = this.nounDict.get(nounB);

		if (v == null || w == null) {
			this.lastSapFail = true;
			throw new IllegalArgumentException();
		}

		int ancestor = sap.ancestor(v, w);
		this.lastSapSynset = this.synSetIdToNouns.get(ancestor);
		this.lastSapLength = sap.length(v, w);
	}
}
