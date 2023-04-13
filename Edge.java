public class Edge {
    public int weight;
    public String name;
    private final City destination;

    public Edge(City destination, String name, double weight) {
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException();
        }
        this.weight = (int)weight;
    }
    public City getDestination(){
        return null;
    }

    public int getWeight(){
        return 1;
    }

    public void setWeight(int distance){

    }

    public String getName(){
        return null;
    }

    public String toString(){
        return null;
    }
}
