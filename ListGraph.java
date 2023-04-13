public class ListGraph {
    private final Map<City, Set<Edge>> nodes = new HashMap<>();

    public void add(City city) {
        nodes.putIfAbsent(city, new HashSet<>());
    }

    public void remove(City city){

    }

    public void connect(City a, City b, String name, int distance){

    }

    public void disconnect(City a, City b){

    }

    public void setConnectionWeight(City a, City b, int newDistance){

    }

    public HashMap<City, Set<Egde>> getNodes(){
        return null;
    }

    public Set<Edge> getEdgesFrom(City city){
        return null;
    }

    public Edge getEdgeBetween(City a, City b){
        return null;
    }

    public String toString(){
        return null;
    }
    public boolean pathExists(City a, City b){
        return null;
    }

    public List<Edge> getPath(City a, City b){
        return null;
    }

}

