import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;

public class Graphics extends Application{
    private Rectangle mapBackground;
    private boolean imageLoaded = false;
    private ImageView imageView;

    public static void main(String[]args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //lista med underrubriker för "file" menyn
        List<String> menuNamnen = List.of("New Map", "Open", "Save", "Save Image", "Exit");
        MenuBar menuBar = new MenuBar();
        VBox vBox = new VBox(menuBar);

        Menu fileMenu = new Menu("File"); //Skapar & sätter Meny-texten till File

        //loopar igenom listan av menynamnen, skapar en menuitem för varje namn och lägger till eventhanterare via handlefilemenu + lägger till i filemenyn
        for(String rubrikNamn : menuNamnen){
            MenuItem menuItem = new MenuItem(rubrikNamn);
            menuItem.setOnAction(this::handleFileMenyn);
            fileMenu.getItems().add(menuItem);
        }

        //lägger till filemenu tiill menubar
        menuBar.getMenus().add(fileMenu);

        FlowPane buttonPane = new FlowPane();
        //sätter mellanrum/spacing mellan ui elementen (knapparna) både vertikalt och horisontellt
        buttonPane.setHgap(20);
        buttonPane.setVgap(20);

        //här sätter jag den "önskade" bredden på flowpanen
        buttonPane.setPrefWrapLength(800);

        vBox.getChildren().add(buttonPane);

        //sätter bredd och höjd för stage
        primaryStage.setWidth(1000);
        primaryStage.setHeight(400);

        //lista med meny knappar
        List<CustomButton> buttons = List.of(
                new CustomButton("Find Path", 0, 0),
                new CustomButton("Show Connection", 0, 0),
                new CustomButton("New Place", 0, 0),
                new CustomButton("New Connection", 0, 0),
                new CustomButton("Change Connection", 0, 0)
        );

        buttonPane.getChildren().addAll(buttons); //lägger in knapparna i pane

        //här adderar jag en eventhandlar till varje separat knapp som är tillagd i listan
        for (CustomButton button : buttons) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Knapp " + button.getText() + " har klickat på :)");
                }
            });
        }

        //skapar en egen pane för själva bilden så att den inte lagras på knapparna
        VBox imagePane = new VBox();
        imageView = new ImageView(); //ramen för bilden
        imagePane.getChildren().add(imageView); //lägger till ramen till bildens pane

        //skapar en parent pane som håller menupanen/file menyn, pane för knapparna, och bilden som ska laddas
        VBox root = new VBox();
        root.getChildren().addAll(vBox, buttonPane, imagePane);

        //här skapas en scene med vbox som root och måtten/storleken
        Scene scene = new Scene(root, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("PathFinder");
    }

    public void handleFileMenyn(ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        String menuNamn = menuItem.getText();

        switch(menuNamn){
            case "New Map":
                if (!imageLoaded) {
                    String imageUrl = "europa.gif";
                    Image image = new Image(imageUrl);
                    imageView.setImage(image);
                    imageLoaded = true;
                }
                System.out.println("New Map menu item clicked!");
                break;
            case "Open":

                System.out.println("Open menu item clicked!");
                break;
            case "Save":

                System.out.println("Save menu item clicked!");
                break;
            case "Save Image":

                System.out.println("Save Image menu item clicked!");
                break;
            case "Exit":

                System.out.println("Exit menu item clicked!");
                System.exit(0);
                break;
            default:
                break;
        }
    }
}
