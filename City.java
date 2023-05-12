import java.util.Objects;

public class City{

    private final String cityName;
    private final int x;
    private final int y;

    public City(String cityName, int x, int y){
        this.cityName = cityName;
        this.x = x;
        this.y = y;
    }

    public String getName(){
        return this.cityName;
    }

    public boolean equals(Object other){
        if(other instanceof City city){
            return cityName.equals(city.cityName);
        }
        return false;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int hashCode(){
        return Objects.hash(cityName);
    }

    public String toString(){
        return this.cityName;
    }
}