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

        //här skapa jag en lista som innehåller alla underrubriker för "file" menyn, blir mindre rader kod vilket är fint
        List<String> menuNamnen = List.of("New Map", "Open", "Save", "Save Image", "Exit");
        //här skapas en meny bar
        MenuBar menuBar = new MenuBar();
        //här skapas en vbox som håller menybaren, som jag fattar det så stackar den alla childs vertikalt på varandra
        VBox vBox = new VBox(menuBar);

        //här skapar jag en ny flowpane som håller alla ui element
        FlowPane buttonPane = new FlowPane();
        //här sätter jag mellanrummet/spacing mellan ui elementen i detta fall knapparna både vertikalt och horizontelt
        buttonPane.setHgap(20);
        buttonPane.setVgap(20);

        //här sätter jag den "önskade" bredden på flowpanen
        buttonPane.setPrefWrapLength(800);
        //här lägger jag till flowpanen till vboxen
        vBox.getChildren().add(buttonPane);

        //sätter scenen till stage
        //här sätter jag texten som står högst uppe i vänstra hörnet på uppgiftsbeskrivningen, lite osäker på om det behövs vara exakt samma men aja


        Menu fileMenu = new Menu("File"); //Skapar & sätter Meny-texten till File

        //Skapar å lägger till MenuItems till fileMenu, precis som ni hade gjort innan men med hjälp av listan där uppe och en for loop
        for(String rubrikNamn : menuNamnen){
            MenuItem menuItem = new MenuItem(rubrikNamn);
            menuItem.setOnAction(this::handleFileMenyn);
            fileMenu.getItems().add(menuItem);
        }

        //lägger till filemenu tiill menubar
        menuBar.getMenus().add(fileMenu);

        //sätter bredd och höjd för stage
        primaryStage.setWidth(1000);
        primaryStage.setHeight(400);

        //här skapade jag en lista med knapper för insåg hur fult de kommer bli add behöva addera alla knapper separat till pane
        List<CustomButton> buttons = List.of(
                new CustomButton("Find Path", 0, 0),
                new CustomButton("Show Connection", 0, 0),
                new CustomButton("New Place", 0, 0),
                new CustomButton("New Connection", 0, 0),
                new CustomButton("Change Connection", 0, 0)
        );

        buttonPane.getChildren().addAll(buttons); //lägger in knapperna i pane

        //här adderar jag en eventhandlar till varje separat knapp som är tillagd i listan, så man får feedback på att de funkar, också najs med lista här för annars hade man behövt addera en ny eventhandlar till varje knapp vilket blir snuskigt
        for (CustomButton button : buttons) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Knapp " + button.getText() + " har klickat på :)");
                }
            });
        }
        //skapar en egen pane för själva bilden så att den inte lagras på knapparna (finns säkert något bättre sätt man kan göra det aka kolla i Linns anteckningar kanske)
        VBox imagePane = new VBox();

        //skapar ramen för bilden vi ska ladda
        imageView = new ImageView();

        //adderar ramen till bildens pane
        imagePane.getChildren().add(imageView);

        //skapar en parent pane som håller menupanen/file menyn, pane för knapparna, och bilden som ska laddas
        VBox root = new VBox();
        root.getChildren().addAll(vBox, buttonPane, imagePane);
        //här skapas en scene med vbox som root och måtten/storleken
        Scene scene = new Scene(root, 960, 600);
        primaryStage.setScene(scene);
        //visar fönstret/stage
        primaryStage.show();
        primaryStage.setTitle("PathFinder");

    }

    public void handleFileMenyn(ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        String menuNamn = menuItem.getText();

        switch(menuNamn){
            case "New Map":
                if (!imageLoaded) {
                    //vet inte om de är så mycket att förklara men
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
