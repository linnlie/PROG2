import java.util.Objects;

// PROG2 VT2023, Inlämningsuppgift, del 1 // Grupp 385
// Linn Li lili6794
// Linus Levén lile6018
// Nora Wennerberg nowe9092

public class Edge<T> {
    public int weight;
    public String name;
    private final T destination;

    public Edge(T destination, String name, double weight) {
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException();
        }
        this.weight = (int)weight;
    }
    public T getDestination(){
        return this.destination;
    }

    public int getWeight(){
        return weight;
    }

    public void setWeight(int weight){
        if (weight < 0) {
            throw new IllegalArgumentException("Aja baja vikten kan inte vara negativ!");
        }
        this.weight = weight;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return "Edge{" + "destination " + this.destination + ", name=" + this.name + '\'' + ", distance" + this.weight + "}";
    }
}

