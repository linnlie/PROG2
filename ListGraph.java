import java.util.*;

// PROG2 VT2023, Inlämningsuppgift, del 1 
// Grupp 385
// Linn Li lili6794
// Linus Levén lile6018
// Nora Wennerberg nowe9092

public class ListGraph<T> implements Graph<T> {
    private final Map<City, Set<Edge>> nodes = new HashMap<>();

    public void add(City city) {
        nodes.putIfAbsent(city, new HashSet<>());
    }

    public void remove(City city){
        if (nodes.containsKey(city)){
            nodes.remove(city);
        } else {
            System.out.println("Error: No such city found.");
        }
    }

    public void connect(City a, City b, String name, int distance) {
        if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
            throw new NoSuchElementException("One or more cities not found.");
        }

        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be a negative number.");
        }

        if (getEdgeBetween(a, b) != null) {
            throw new IllegalStateException("Connection already exists between " + a + " and " + b + ".");
        }

        Set<Edge> aEdges = nodes.get(a);
        Set<Edge> bEdges = nodes.get(b);

        //Gör grafen oriktad, pga skapar kant från a till b samt kant från b till a
        aEdges.add(new Edge(b, name, distance));
        bEdges.add(new Edge(a, name, distance));

        setConnectionWeight(a, b, distance);
    }

    public void disconnect(City a, City b) {
        if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
            throw new NoSuchElementException("One or more cities not found.");
        }

        Edge edge = getEdgeBetween(a, b);
        if (edge == null) {
            throw new IllegalStateException("No edge between " + a + " and " + b + ".");
        }

        Set<Edge> aEdges = nodes.get(a);
        Set<Edge> bEdges = nodes.get(b);

        aEdges.remove(edge);
        bEdges.remove(edge);
    }

    public void setConnectionWeight(City a, City b, int newDistance) {
        if (!nodes.containsKey(a) || !nodes.containsKey(b) || !pathExists(a, b)){
            throw new NoSuchElementException("Error: No such city or connection.");
        } else if (getEdgeBetween(a, b) < 0){
            throw new IllegalArgumentException("Error: Wheight is negative");
        } else {
            Edge edge = getEdgeBetween(a, b);
            edge.setWeight(newDistance);
        }
    }

    public HashMap<City, Set<Edge>> getNodes() {
        return null;
    }

    public Set<Edge> getEdgesFrom(City city) {

    }

    public Edge getEdgeBetween(City a, City b) {
        return null;
    }

    public String toString() {
        return null;
    }

    public boolean pathExists(City a, City b) {
        return false;
    }

    public List<Edge> getPath(City a, City b) {
        return null;
    }

}


