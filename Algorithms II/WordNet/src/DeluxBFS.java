// https://class.coursera.org/algs4partII-001/forum/search?q=DeluxeBFS&all_forums=Search+All+Forums

// this class should work like BreadthFirstDirectedPaths but it should expose the following or a way to determine 
// the following.
//   given a enumerable sources of w and v runs BFDP(w) and BFDP(w)
//   then for all the vertexes touched by v and w (the paths) determines which vertex is
//   common to both paths (all marked) of v and w, with the closes distance.
//   no need to go thru all vertexes (V) in the graph because v and w will likely not 
//   touch all vertexes (V).
//   
//   1. should also cache results for SAP and the distance (or length) of SAP from v and w.
//   2. if the same set of v or w come in then we should be able to leverage cached results
//   3. if a new set of v or w come in then there should be a cached paths array that we can use
//      to seek into the underlying arrays to clean them up (reset them to initial state) for this new BFS.
//   
public class DeluxBFS {

}
