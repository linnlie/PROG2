import java.util.*;

// PROG2 VT2023, Inlämningsuppgift, del 1 
// Grupp 385
// Linn Li lili6794
// Linus Levén lile6018
// Nora Wennerberg nowe9092

public class ListGraph<T> implements Graph<T> {
    private final Map<T, Set<Edge>> nodes = new HashMap<>();

    public void add(T city) {
        nodes.putIfAbsent(city, new HashSet<>());
    }

    public void remove(T city) {
        if (nodes.containsKey(city)) {
            nodes.remove(city);
        } else {
            System.out.println("Error: No such city found.");
        }
    }

    public void connect(T cityA, T cityB, String name, int distance) {
        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB)) {
            throw new NoSuchElementException("One or more cities not found.");
        }

        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be a negative number.");
        }

        if (getEdgeBetween(cityA, cityB) != null) {
            throw new IllegalStateException("Connection already exists between " + a + " and " + b + ".");
        }

        Set<Edge> aEdges = nodes.get(cityA);
        Set<Edge> bEdges = nodes.get(cityB);

        //Gör grafen oriktad, pga skapar kant från a till b samt kant från b till a
        aEdges.add(new Edge(cityB, name, distance));
        bEdges.add(new Edge(cityA, name, distance));

        setConnectionWeight(cityA, cityB, distance);
    }

    public void disconnect(T cityA, T cityB) {
        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB)) {
            throw new NoSuchElementException("One or more cities not found.");
        }

        Edge edge = getEdgeBetween(cityA, cityB);
        if (edge == null) {
            throw new IllegalStateException("No edge between " + cityA + " and " + cityB + ".");
        }

        Set<Edge> aEdges = nodes.get(cityA);
        Set<Edge> bEdges = nodes.get(cityB);

        aEdges.remove(edge);
        bEdges.remove(edge);
    }

    public void setConnectionWeight(T cityA, T cityB, int newDistance) {
        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB)) { ||!pathExists(cityA, cityB)){
            throw new NoSuchElementException("Error: No such city or connection.");
        } else if (getEdgeBetween(cityA, cityB) < 0) {
            throw new IllegalArgumentException("Error: Wheight is negative");
        } else {
            connect(cityA, cityB, "stad", newDistance);
        }
        }
    }

    public Set<T> getNodes() {
        Set<T> nodesCopy = new HashSet<T>();
        nodesCopy.addAll(nodes.keySet());
        return nodesCopy;
    }

    public Collection<Edge<T>> getEdgesFrom(T city) {
        Set<Edge> edges = new HashSet<>(); //Alla kanter från noden

        if (!nodes.containsKey(city)){
            throw new NoSuchElementException("Error: No such city.");
        } else {
            for (Edge e : nodes.get(city)){ //Lägger till kanterna från city i edges
                edges.add(e);
            }
        }
        return new HashSet<>(edges); //Returnerar kopia av samlingen av alla kanter

    }

    public Edge getEdgeBetween(T cityFrom, T cityTo) {
        if (!nodes.containsKey(cityFrom) || !nodes.containsKey(cityTo)){
            throw new NoSuchElementException("Error: No such city found.");
        } else if (pathExists(cityFrom, cityTo)){
            for (Edge edge : nodes.get(cityFrom)){
                if (edge.getDestination().equals(cityTo)){
                    return edge;
                }
            }
        }
        return null;
    }

    public String toString() {
        return null;
    }

    public boolean pathExists(T cityA, T cityB) {
        return false;
    }

    public List<Edge<T>> getPath(T cityA, T cityB) {
        return null;
    }
}