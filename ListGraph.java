import java.util.*;
import java.util.PriorityQueue;

// PROG2 VT2023, Inlämningsuppgift, del 1 
// Grupp 385
// Linn Li lili6794
// Linus Levén lile6018
// Nora Wennerberg nowe9092

public class ListGraph<T> implements Graph<T> {
    private final Map<T, Set<Edge<T>>> nodes = new HashMap<T, Set<Edge<T>>>();

    public void add(T city) {
        nodes.putIfAbsent(city, new HashSet<Edge<T>>());
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
            throw new IllegalStateException("Connection already exists between " + cityA + " and " + cityB + ".");
        }

        Set<Edge<T>> aEdges = nodes.get(cityA);
        Set<Edge<T>> bEdges = nodes.get(cityB);

        //Gör grafen oriktad, pga skapar kant från a till b samt kant från b till a
        aEdges.add(new Edge<>(cityB, name, distance));
        bEdges.add(new Edge<>(cityA, name, distance));

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

        Set<Edge<T>> aEdges = nodes.get(cityA);
        Set<Edge<T>> bEdges = nodes.get(cityB);

        aEdges.remove(edge);
        bEdges.remove(edge);
    }

    public void setConnectionWeight(T cityA, T cityB, int newDistance) {
        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB) || !pathExists(cityA, cityB)){
            throw new NoSuchElementException("Error: No such city or connection.");
        } else if (getEdgeBetween(cityA, cityB).getWeight() < 0) {
            throw new IllegalArgumentException("Error: Weight is negative");
        } else {
            Edge edge = getEdgeBetween(cityA, cityB);
            edge.setWeight(newDistance);
        }
    }

    public Set<T> getNodes() {
        Set<T> nodesCopy = new HashSet<T>();
        nodesCopy.addAll(nodes.keySet());
        return nodesCopy;
    }

    public Collection<Edge<T>> getEdgesFrom(T city) {
        Set<Edge<T>> edges = new HashSet<Edge<T>>(); //Alla kanter från noden

        if (!nodes.containsKey(city)){
            throw new NoSuchElementException("Error: No such city.");
        } else {
            for (Edge<T> e : nodes.get(city)){ //Lägger till kanterna från city i edges
                edges.add(e);
            }
        }
        return new HashSet<>(edges); //Returnerar kopia av samlingen av alla kanter

    }

    public Edge getEdgeBetween(T cityFrom, T cityTo) {
        if (!nodes.containsKey(cityFrom) || !nodes.containsKey(cityTo)){
            throw new NoSuchElementException("Error: No such city found.");
        } else if (pathExists(cityFrom, cityTo)){
            for (Edge<T> edge : nodes.get(cityFrom)){
                if (edge.getDestination().equals(cityTo)){
                    return edge;
                }
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (T city : nodes.keySet()){ //Går igenom alla nycklar (städer)
            stringBuilder.append(city).append(":").append(nodes.get(city)).append("\n");
        }
        return stringBuilder.toString();
    }

    public boolean pathExists(T cityA, T cityB) {
        Set<T> visited = new HashSet<>();

        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB)){
            throw new NoSuchElementException("Error: One or more cities not found.");
        }

        depthFirstSearch(cityA, cityB, visited);
        return visited.contains(cityB); //returnerar true om dfs har besökt cityB (en path har hittats), annars false
    }

    public void depthFirstSearch(T current, T destination, Set<T> visited){
        visited.add(current);
        if(current.equals(destination)){
            return;
        } for (Edge<T> edge: nodes.get(current)){
            if(!visited.contains(edge.getDestination())){
                depthFirstSearch(edge.getDestination(), destination, visited);
            }
        }
    }

    public List<Edge<T>> getPath(T cityA, T cityB) {
        if (!pathExists(cityA, cityB)){ //kollar om det redan finns en väg mellan noderna(city)
            return null;
        }
        PriorityQueue<Path<T>> paths = new PriorityQueue<>(); //använder priorityqueue istället för queue eftersom den är sorterad baserat på en given prioritet av elementen den lagrar. 
        //vilket för att vi kan använda distansen av en väg mellan noder som prioritet för att sortera kön.
        Set<T> visited = new HashSet<>(); //ett set för att hålla reda på vilka städer/noder man besökt
        
        Path<T> firstPath = new Path<>(cityA); //skapar en ny väg som börjar från och med cityA noden. 
        
    }
}

