import java.util.Objects;

// PROG2 VT2023, Inl ̈amningsuppgift, del 1 // Grupp 385
// Linn Li lili6794
// Linus Levén lile6018
// Nora Wennerberg nowe9092

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
        return this.destination;
    }

    public int getWeight(){
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "Edge{" + "destination " + this.destination + ", name=" + this.name + '\'' + ", distance" + this.weight + "}";
    }
}

