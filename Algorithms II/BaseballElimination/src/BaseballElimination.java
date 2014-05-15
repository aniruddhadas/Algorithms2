import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class BaseballElimination {
    private int[] w, l, r;
    private HashMap<String, Integer> team;
    private int[][] g;
    private String bestTeam;
    private ArrayList<ArrayList<String>> outcomes;
    private int currentElimationCount = 0;

    public BaseballElimination(String filename) {

        In reader = new In(filename);
        int size = reader.readInt();

        w = new int[size];
        l = new int[size];
        r = new int[size];
        g = new int[size][];

        outcomes = new ArrayList<ArrayList<String>>(size);
        team = new HashMap<String, Integer>();
        int winsOfbestTeam = -1;
        // 4
        // Atlanta 83 71 8 0 1 6 1
        // Philadelphia 80 79 3 1 0 0 2
        // New_York 78 78 6 6 0 0 0
        // Montreal 77 82 3 1 2 0 0

        for (int i = 0; i < size; i++) {
            String currTeam = reader.readString();
            team.put(currTeam, i);
            w[i] = reader.readInt();
            l[i] = reader.readInt();
            r[i] = reader.readInt();
            outcomes.add(i, null);
            if (winsOfbestTeam < w[i]) {
                winsOfbestTeam = w[i];
                bestTeam = currTeam;
            }

            // to-do fill g matrix
            g[i] = new int[size];
            for (int j = 0; j < size; j++) {
                // read ints and fill g
                g[i][j] = reader.readInt();
            }
        }

        // do proactive trivial elimination
        for (String team : this.team.keySet()) {
            // subset R of teams that eliminates given team; null if not
            // eliminated
            // step 1. trival elimination
            int maxWins = this.wins(team) + this.remaining(team);
            // ArrayList<String> eliminatingTeam = new ArrayList<String>();
            if (maxWins < this.wins(this.bestTeam)) {
                // eliminatingTeam.add(this.bestTeam);
                ArrayList<String> outcome = new ArrayList<String>();
                outcome.add(this.bestTeam);
                SetOutcome(team, outcome);
                this.currentElimationCount++;
            }
        }
    }

    public int numberOfTeams() {
        // number of teams
        return this.team.size();
    }

    public Iterable<String> teams() {
        // all teams
        return this.team.keySet();
    }

    public int wins(String team) {
        // number of wins for given team
        return this.w[this.GetTeamIndex(team)];
    }

    public int losses(String team) {
        // number of losses for given team
        return this.l[this.GetTeamIndex(team)];
    }

    public int remaining(String team) {
        // number of remaining games for given team
        return this.r[this.GetTeamIndex(team)];
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        return g[this.GetTeamIndex(team1)][this.GetTeamIndex(team2)];
    }

    public boolean isEliminated(String team) {
        return this.certificateOfElimination(team) != null;
    }

    public Iterable<String> certificateOfElimination(String team) {
        ArrayList<String> outcome = GetOutcome(team);

        if (outcome != null) {
            return outcome.size() == 0 ? null : outcome;
        }

        // step 2. non trivial elimination
        outcome = this.NonTrivialElimination(team);
        SetOutcome(team, outcome);

        // has been eliminated?
        if (outcome.size() > 0) {
            this.currentElimationCount++;
        }

        return outcome.size() == 0 ? null : outcome;
    }

    private boolean hasBeenEliminated(String team) {
        ArrayList<String> outcome = this.GetOutcome(team);
        return outcome != null && outcome.size() > 0;
    }

    private ArrayList<String> GetOutcome(String team) {
        // check if outcomes already contains an entry if yes then return the
        // values from outcomes
        return outcomes.get(this.GetTeamIndex(team));
    }

    private void SetOutcome(String team, ArrayList<String> outcome) {
        outcomes.set(this.GetTeamIndex(team), outcome);
    }

    private ArrayList<String> NonTrivialElimination(String team) {
        HashMap<String, Integer> floNetworkNodes = new HashMap<String, Integer>();
        HashMap<Integer, String> thirdLayer = new HashMap<Integer, String>();
     
        int n = this.team.keySet().size() - (this.currentElimationCount + 1);  // adding 1 for "team" exclude yourself
        int V = n + n * (n - 1) / 2 + 2;
        int maxWins = this.wins(team) + this.remaining(team);

        FlowNetwork network = new FlowNetwork(V);

        int v = 1;

        // add the pairings
        for (String teamA : this.team.keySet()) {
            // find team pairs
            // team A has been eliminated skip
            // team B has been eliminated skip
            // team A and team B are the same skip
            if (this.hasBeenEliminated(teamA) || teamA.equals(team)) {
                continue;
            }

            Integer teamAv = floNetworkNodes.get(teamA);

            if (teamAv == null) {
                v++;
                floNetworkNodes.put(teamA, v);
                teamAv = v;
                thirdLayer.put(teamAv, teamA);
            }

            for (String teamB : this.team.keySet()) {
                // skip conditions
                String flowKey1 = teamB + "-" + teamA;
                String flowKey2 = teamA + "-" + teamB;

                if (floNetworkNodes.containsKey(flowKey1)
                        || floNetworkNodes.containsKey(flowKey2)) {
                    // already did first order
                    continue;
                }

                if (this.hasBeenEliminated(teamB) || teamA.equals(teamB) || teamB.equals(team)) {
                    continue;
                }

                Integer pairOfABv = ++v;

                floNetworkNodes.put(flowKey1, v);

                // 1. SOURCE to layer 2
                int capacity = this.g[this.GetTeamIndex(teamA)][this
                        .GetTeamIndex(teamB)];
                FlowEdge flowEdge = new FlowEdge(0, v, capacity);
                network.addEdge(flowEdge);

                // 2. INFINITE Layer 2 to layer 3
                // try get layer 3 id's
                Integer teamBv = floNetworkNodes.get(teamB);

                if (teamBv == null) {
                    v++;
                    floNetworkNodes.put(teamB, v);
                    teamBv = v;
                    thirdLayer.put(teamBv, teamB);
                }

                // infinite layer connection
                flowEdge = new FlowEdge(pairOfABv, teamAv, Double.MAX_VALUE);
                network.addEdge(flowEdge);

                flowEdge = new FlowEdge(pairOfABv, teamBv, Double.MAX_VALUE);
                network.addEdge(flowEdge);
            }

            // teamA current wins
            int teamACurrentWins = this.wins(teamA);
            int finalCapacity = maxWins - teamACurrentWins;
            this.AssertFinalCapacity(finalCapacity);

            network.addEdge(new FlowEdge(teamAv, 1, finalCapacity));
        }

        // 2. construct ford fulkerson
        FordFulkerson fordFulkerson = new FordFulkerson(network, 0, 1);

        // 3. incut
        ArrayList<String> outcome = new ArrayList<String>();
        for (int i = 0; i < V; i++) {
            if (fordFulkerson.inCut(i)) {
                String eliminatingTeam = thirdLayer.get(i);
                if (eliminatingTeam != null) {
                    outcome.add(eliminatingTeam);
                }
            }
        }

        return outcome;
    }

    private void AssertFinalCapacity(int finalCapacity) {
        if (finalCapacity < 0) {
            Integer result = 1 / 0;
        }
    }

    private int GetTeamIndex(String team) {
        Integer i = this.team.get(team);

        if (i == null) {
            throw new IllegalArgumentException();
        }

        return i;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
