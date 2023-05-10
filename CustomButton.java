import javafx.scene.control.Button;

public class CustomButton extends Button {

    //konstuktor som tar en texten på knappen och x samt y kordinaten i pane
    public CustomButton(String text, double x, double y) {
        super(text); //anropa superklassens konstruktor för att sätta texten på knappen
        //sätter x och y kordinaterna i pane för knappen
        setLayoutX(x);
        setLayoutY(y);
    }

    //metod för att ändra texten på knappen om man vill göra det :)
    public void setButtonText(String text) {
        super.setText(text); //anropa superklassens setText metoden för att sätta texten på knappen
    }
}
