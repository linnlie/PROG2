import javafx.scene.control.Button;

public class CustomButton extends Button{
    
    public CustomButton(String text, double x, double y){
        super(text);
        setLayoutX(x);
        setLayoutY(y);
    }

    public void setButtonText(String text){
        super.setText(text);
    }
}
