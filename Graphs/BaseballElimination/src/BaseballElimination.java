import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BaseballElimination {
    private static final int WIN = 0;
    private static final int LOSS = 1;
    private static final int LEFT = 2;

    private final HashMap<String, Integer> teamIndex;
    private String[] indexToTeam;
    private int[][] wlr;
    private int maxWin;
    private int[][] g;
    private int numberOfTeams;
    private int s;
    private int t;
    private int rightValue;


    public BaseballElimination(String filename) {                   // create a baseball division from given filename in format specified below
        teamIndex = new HashMap<>();
        parseInput(filename);
    }

    private void parseInput(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        indexToTeam = new String[numberOfTeams];
        wlr = new int[numberOfTeams][3];
        g = new int[numberOfTeams][numberOfTeams];
        int cnt = 0;
        while (in.hasNextLine()) {
            try {
                String name = in.readString();
                teamIndex.put(name, cnt);
                indexToTeam[cnt] = name;
                for (int i = 0; i < 3; i++) {
                    wlr[cnt][i] = in.readInt();
                }
                maxWin = Math.max(maxWin, wlr[cnt][WIN]);
                for (int i = 0; i < numberOfTeams; i++) {
                    g[cnt][i] = in.readInt();
                }
                cnt++;
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    public int numberOfTeams() {                       // number of teams
        return numberOfTeams;
    }

    public Iterable<String> teams() {                  // all teams
        return teamIndex.keySet();
    }

    public int wins(String team) {                     // number of wins for given team
        validateTeam(team);
        return wlr[teamIndex.get(team)][WIN];
    }

    public int losses(String team) {                   // number of losses for given team
        validateTeam(team);
        return wlr[teamIndex.get(team)][LOSS];
    }

    public int remaining(String team) {                // number of remaining games for given team
        validateTeam(team);
        return wlr[teamIndex.get(team)][LEFT];
    }

    public int against(String team1, String team2) {   // number of remaining games between team1 and team2
        validateTeam(team1);
        validateTeam(team2);
        return g[teamIndex.get(team1)][teamIndex.get(team2)];
    }

    public boolean isEliminated(String team) {             // is given team eliminated?
        validateTeam(team);
        if (numberOfTeams == 1)
            return false;
        if (wlr[teamIndex.get(team)][WIN] + wlr[teamIndex.get(team)][LEFT] < maxWin)
            return true;
        FlowNetwork G = constructGraph(team);
        FordFulkerson fordFulkerson = new FordFulkerson(G, s, t);
        return fordFulkerson.value() < rightValue;
    }

    public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
        validateTeam(team);
        if (!isEliminated(team))
            return null;
        if (isEliminated(team) && wlr[teamIndex.get(team)][WIN] + wlr[teamIndex.get(team)][LEFT] < maxWin) {
            for (int i = 0; i < wlr.length; i++) {
                if (wlr[i][WIN] == maxWin) {
                    LinkedList<String> result = new LinkedList<>();
                    result.add(indexToTeam[i]);
                    return result;
                }
            }
        }
        LinkedList<String> result = new LinkedList<>();
        FlowNetwork G = constructGraph(team);
        FordFulkerson fordFulkerson = new FordFulkerson(G, s, t);
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != s) {
                if (fordFulkerson.inCut(i)) {
                    result.add(indexToTeam[i]);
                }
            }
        }
        return result;
    }

    private FlowNetwork constructGraph(String team) {
        int V = 1 + (numberOfTeams() - 1) * (numberOfTeams() - 2) / 2 + numberOfTeams() - 1 + 1;
        FlowNetwork flowNetwork = new FlowNetwork(V);
        s = teamIndex.get(team);
        t = V - 1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != s)
                flowNetwork.addEdge(new FlowEdge(i, t, wlr[s][WIN] + wlr[s][LEFT] - wlr[i][WIN]));
        }
        int gameVerticesIndex = numberOfTeams;
        rightValue = 0;
        for (int i = 0; i < numberOfTeams - 1; i++) {
            if (i == s) continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == s) continue;
                flowNetwork.addEdge(new FlowEdge(s, gameVerticesIndex, g[i][j]));
                rightValue += g[i][j];
                flowNetwork.addEdge(new FlowEdge(gameVerticesIndex, i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameVerticesIndex, j, Double.POSITIVE_INFINITY));
                gameVerticesIndex++;
            }
        }
//        System.out.println(gameVerticesIndex == V - 1);
        return flowNetwork;
    }

    private void validateTeam(String team) {
        if (!teamIndex.keySet().contains(team))
            throw new IllegalArgumentException("Wrong team input");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
//        System.out.println(division.numberOfTeams());
//        for (String team : division.teams()) {
//            System.out.println(division.remaining(team));
//        }
//        for (int i = 0; i < division.numberOfTeams(); i++) {
//            for (int j = 0; j < division.numberOfTeams(); j++) {
//                System.out.print(division.g[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println(division.constructGraph("Atlanta").toString());
//        System.out.println(division.isEliminated("Philadelphia"));
        System.out.println(division.constructGraph("New_York").toString());
        System.out.println(division.isEliminated("New_York"));
        for (String i : division.certificateOfElimination("Montreal")) {
            System.out.println(i);
        }
//        System.out.println(division.isEliminated("Montreal"));
    }
}
