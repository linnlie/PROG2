public class Edge {
    public int weight;
    public string name;
    private final City destination;

    public Edge(City destination, String name, double weight) {
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException();
        }
        this.weight = weight;
    }
    public City getDestination(){
        return null;
    }

    public int getWeight(){
        return null;
    }

    public void setWeight(int distance){

    }

    public string getName(){
        return null;
    }

    public string toString(){
        return null;
    }
}
