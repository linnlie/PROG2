import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.*;

public class Graphics extends Application{

    public static void main(String[]args){
        launch(args);
    }

    public void start(Stage primaryStage){
        MenuBar menuBar = new MenuBar(); //Skapar instans av MenuBar
        VBox vBox = new VBox(menuBar); //Skapar VBox som rymmer MenuBar för att få menyn liggandes
        Scene scene = new Scene(vBox, 960, 600); //root node, width, height. Root node är typ parent till allt annat i scenen, så kanske ska bytas ut mot typ BorderPane
        primaryStage.setScene(scene); //Sätter scenen
        primaryStage.show(); //Öppnar scenen

        //Lägger till Menu-instans till MenuBar
        Menu fileMenu = new Menu("File"); //Skapar & sätter Meny-texten till File
        menuBar.getMenus().add(fileMenu);

        //Skapar å lägger till MenuItems till fileMenu
        MenuItem menuItemOne = new MenuItem("New Map");
        MenuItem menuItemTwo = new MenuItem("Open");
        MenuItem menuItemThree = new MenuItem("Save");
        MenuItem menuItemFour = new MenuItem("Save Image");
        fileMenu.getItems().add(menuItemOne);
        fileMenu.getItems().add(menuItemTwo);
        fileMenu.getItems().add(menuItemThree);
        fileMenu.getItems().add(menuItemFour);
    }
}
