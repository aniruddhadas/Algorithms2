
public class Outcast {
    private WordNet wordNet;
    
    public Outcast(WordNet wordnet) 
    {
        this.wordNet = wordnet;
    }
    
    public String outcast(String[] nouns) 
    {
        int maxDist = 0;
        String nounMaxAway = "";
        for (String nounMe:nouns)
        {
            int currentDist = 0;
            for (String nounOther:nouns)
            {
                currentDist = 
                        currentDist 
                        + 
                        wordNet.distance(nounMe, nounOther);
            }
            
            if (currentDist > maxDist)
            {
                maxDist = currentDist;
                nounMaxAway = nounMe;
            }
        }
        return nounMaxAway;
    }
    
    public static void main(String[] args) 
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) 
        {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
