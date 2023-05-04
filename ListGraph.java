import java.util.*;

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

    public void remove(T cityToRemove) {
        if (!nodes.containsKey(cityToRemove)) {
            throw new NoSuchElementException("No such city found");
        }

        //lägger förbindelserna från cityToRemove i ett set
        Set<Edge<T>> edgesFrom = nodes.get(cityToRemove);
        
        if(edgesFrom != null){
            for(Edge<T> edge : edgesFrom){
                if(edge.getDestination() != cityToRemove){
                    Set<Edge<T>> edgesFromNeighbour = nodes.get(edge.getDestination());
                    edgesFromNeighbour.removeIf(e -> e.getDestination() == cityToRemove);
                }
            }
            nodes.remove(cityToRemove);
        }
    }

    public void connect(T cityA, T cityB, String name, int weight) {
        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB)) {
            throw new NoSuchElementException("One or more cities not found.");
        }

        if (weight < 0) {
            throw new IllegalArgumentException("Time cannot be a negative number.");
        }

        if (getEdgeBetween(cityA, cityB) != null) {
            throw new IllegalStateException("Connection already exists between " + cityA + " and " + cityB + ".");
        }

        Set<Edge<T>> cityAEdges = nodes.get(cityA);
        Set<Edge<T>> cityBEdges = nodes.get(cityB);

        //Gör grafen oriktad, pga skapar kant från a till b samt kant från b till a
        cityAEdges.add(new Edge<>(cityB, name, weight));
        cityBEdges.add(new Edge<>(cityA, name, weight));

        setConnectionWeight(cityA, cityB, weight);
    }

    public void disconnect(T cityA, T cityB) {
        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB)) {
            throw new NoSuchElementException("One or more cities not found.");
        }

        Edge edgeBetweenAandB = getEdgeBetween(cityA, cityB);
        Edge edgeBetweenBandA = getEdgeBetween(cityB, cityA);
        if (edgeBetweenAandB == null || edgeBetweenAandB == null) {
            throw new IllegalStateException("Edge not found.");
        }

        Set<Edge<T>> cityAEdges = nodes.get(cityA);
        Set<Edge<T>> cityBEdges = nodes.get(cityB);

        cityAEdges.remove(edgeBetweenAandB);
        cityBEdges.remove(edgeBetweenBandA);
    }

    public void setConnectionWeight(T cityA, T cityB, int newWeight) {
        if (!nodes.containsKey(cityA) || !nodes.containsKey(cityB) || !pathExists(cityA, cityB)){
            throw new NoSuchElementException("Error: No such city or connection.");
        } else if (getEdgeBetween(cityA, cityB).getWeight() < 0) {
            throw new IllegalArgumentException("Error: Weight is negative");
        } else {
            Edge edgeOne = getEdgeBetween(cityA, cityB);
            edgeOne.setWeight(newWeight);
            Edge edgeTwo = getEdgeBetween(cityB, cityA);
            edgeTwo.setWeight(newWeight);
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
            return false;
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

    public List<Edge<T>> getPath(T start, T destination) {
        Map<T, T> edges = new HashMap<>(); //hålla reda på vilken nod som kom från vilken annan nod
        Map<T, Integer> weights = new HashMap<>(); //håller reda på avståndet mellan varje nod och startnoden
        LinkedList<T> queue = new LinkedList<>(); //en länkad lista som håller reda på vilka noder som ska besökas härnäst(vet inte hur de stavas)
        LinkedList<Edge<T>> path = new LinkedList<>(); //håller reda på vilka edges som ingår i den kortaste vägen aka shortest path

        edges.put(start, null); //lägger till startnoden i "edges" kartan, null eftersom det inte finns någon nod innan
        weights.put(start, 0); //sätt avstånden till startnoden till 0
        queue.add(start); //lägger till startnoden i kön

        while(!queue.isEmpty()) {
            T currentDestination = queue.pollFirst(); //tar ut första noden i kön

            //loopa igenom alla edges som utgår från noden
            for(Edge<T> edge : nodes.get(currentDestination)) {
                T nextDestination = edge.getDestination(); //hämtar destinations noden av den nuvarande edgen
                int timeToDestination = weights.get(currentDestination) + edge.getWeight(); //räknar ut distansen till destinations noden
                if(!weights.containsKey(nextDestination) || timeToDestination < weights.get(nextDestination)) {
                    //uppdaterar kartläggningen om den nya vägen är kortare
                    edges.put(nextDestination, currentDestination); //sätter parent av destinations noden till den nuvarande noden
                    weights.put(nextDestination, timeToDestination); //uppdaterar distansen till destinations noden
                    queue.add(nextDestination); //lägger till destinations noden till kön
                }
            }
        }

        if(!weights.containsKey(destination)) {
            return null; //om det inte finns någpn väg till destinations noden så retunerar vi null
        }

        T currentNode = destination; //börjar på destinations noden
        //lägger till alla edges från destinationen till startnoden i den länkade listan över den kortaste vägen
        while(!currentNode.equals(start)) {
            T nextNode = edges.get(currentNode);
            Edge<T> edge = getEdgeBetween(nextNode, currentNode);
            path.addFirst(edge);
            currentNode = nextNode;
        }

        return path; //retunerar en länkad lista över den kortaste vägen :)
    }
}

