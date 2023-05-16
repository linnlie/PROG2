import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.List;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;
import java.io.*;
import java.util.*;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.geometry.*;

import javafx.scene.control.ButtonBar.ButtonData;


public class Graphics <T> extends Application{
    private Rectangle mapBackground;
    private boolean imageLoaded = false;
    private ImageView imageView;
    private String imageUrl = "europa.gif";
    private ListGraph listGraph = new ListGraph();
    private TextField nameField;
    private boolean hasSaved = false;
    public Scene scene;

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
            menuItem.setOnAction(this::handleFileMenu);
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
        primaryStage.setWidth(635);
        primaryStage.setHeight(818);

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
            button.setOnAction(new EventHandler<ActionEvent>() { //Allt inom {} är den nya metoden EventHandler
                @Override
                public void handle(ActionEvent event) {
                    handleButtons(event);
                    //System.out.println("Knapp " + button.getText() + " har klickat på :)");
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
        scene = new Scene(root, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("PathFinder");

        //primaryStage.setOnCloseRequest(new ExitHandler());
        primaryStage.setOnCloseRequest(event -> exit(event));
    }

    public void handleFileMenu(ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        String menuNamn = menuItem.getText();

        switch(menuNamn){
            case "New Map":
                if (!imageLoaded) {
                    Image image = new Image(imageUrl);
                    imageView.setImage(image);
                    imageLoaded = true;
                }
                System.out.println("New Map menu item clicked!");
                break;
            case "Open":
                open();
                System.out.println("Open menu item clicked!");
                break;
            case "Save":
                save();
                System.out.println("Save menu item clicked!");
                break;
            case "Save Image":
                saveImage();
                System.out.println("Save Image menu item clicked!");
                break;
            case "Exit":
                WindowEvent closeEvent = new WindowEvent(scene.getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST);
                exit(closeEvent);
                System.out.println("Exit menu item clicked!");
                break;
            default:
                break;
        }
    }

    public void handleButtons (ActionEvent event){
        CustomButton button = (CustomButton) event.getSource();
        String name = button.getText();

        switch (name){
            case "Find Path":
                System.out.println("Find Path clicked");
                break;
            case "Show Connection":
                showConnection();
                System.out.println("Show Connection print");
                break;
            case "New Place":
                System.out.println("New Place print");
                break;
            case "New Connection":
                System.out.println("New Connection print");
                break;
            case "Change Connection":
                System.out.println("Change Connection clicked");
                break;
        }
    }

//    private void saveExpriement(String url, Graph<T> graph){
//        try {
//            String filePath = "europa.graph";
//            FileWriter fileWriter = new FileWriter(filePath);
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//
//            //skriv url till bild filen
//            bufferedWriter.write(url);
//            bufferedWriter.newLine();
//
//            //skriva ut noderna
//            for(T t : graph.getNodes()){
//                bufferedWriter.write(t.getName)
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }

    private void save(){
        try {
            String filePath = "europa.graph"; //Referens till filen vi ska skriva till
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)); //Skapar ny med filePath
            writer.write("file:" + imageUrl); //Skriver ut url:en
            StringBuilder sb = new StringBuilder(); //Skapar stringBuilder som är tom just nu
            
            Set<T> nodeSet = listGraph.getNodes(); ///Hämtar alla noder
            for (T node : nodeSet){ //Går igenom varje nod i nod-Settet
                sb.append(nameField + ";"); //Lägger till nodens namn; i stringBuildern
                //Användaren skriver in ett namn, kommer nog behöva hitta det i en lista. Ändra alltså nameField sen.
                //Sedan ska vi också lägga till koordinaterna
            }
            sb.deleteCharAt(nodeSet.size()); //size - 1? Vill ta bort sista ;
            writer.write(sb.toString()); //Skriver ut stringBuildern i filen

            // //Hittar och skriver ut förbindelserna
            // Collection <Edge<T>> edges = listGraph.getEdgesFrom();
            // for (T node : nodeSet){
            //     //Loopa igenom varje edge, skriv ut från-noden, till-noden, namnet å vikten
            // }

            writer.close();
        } catch (IOException e){
            throw new RuntimeException("Error: No such file found.");
        }
    }

    private boolean harOsparadeChanges(){
        return false; //skrev dit detta för metoden va tom /Linn
    }

    private void open(){ //Övningsuppgift 4 använder en map för att konvertera String till Node, kanske behövs???
        if (harOsparadeChanges()) {
            // Visa dialogruta för att fråga användaren om personen vill spara ändringarna
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Osparade ändringar");
            alert.setHeaderText("Det finns osparade ändringar i kartan");
            alert.setContentText("Vill du spara ändringarna innan du öppnar en ny karta?");
    
            //gör knapparna till dialog fönstret, ButtonType är typen av knappar som finns i ett alert dialogfönster, trodde det funka med vanliga knappar först men tydligen inte, detta är en subtyp till button
            //ButtonType saveButton = new ButtonType("Spara"); //denna behövs icke
            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Avbryt", ButtonData.CANCEL_CLOSE);
    
            alert.getButtonTypes().setAll(okButton, cancelButton); //sätter alla knappar i dialogfönstret //tog bort saveButton härifrån
    
            Optional<ButtonType> result = alert.showAndWait(); //väntar med att sätta knapp typen tills användaren trycker på någon, rätt häftigt ändå
            
            switch(result.get().getText()){
                //spara de osparade ändringarna
                case "Spara":
                    save();
                    break;
                case "OK":
                //förkasta de osparade ändringarna och det nya "dokumentet" öppnas
                default:
                //avbryt 
                return;
            }
        }
        
        if (!hasSaved){ //Om användaren inte har sparad info så skicka felmeddelande
            System.out.println("Error: No saved information.");
        } else { //Annars öppna europa.graph
            try {
                FileReader file = new FileReader("europa.graph"); //Referens till filen
                BufferedReader reader = new BufferedReader(file); //Läser in filen rad för rad

                //Återskapar objekt från filen:
                String line; //Tom sträng
                reader.readLine(); //läser första raden file:europa.gif
                reader.readLine(); //Läser andra raden med uppradningen av alla städer
                //Alla rader efter detta ser ut ex: Stockholm;Oslo;Train;3
                while ((line = reader.readLine()) != null){ //läser en rad i taget genom hela filen tills den tar slut
                    String[] parts = line.split(";"); //Delar upp noderna genom ;
                    T cityStart = (T) parts[0]; //Omvandlar String till T
                    T cityEnd = (T) parts[1];
                    String tag = parts[2];
                    int weight = Integer.parseInt(parts[3]); //Omvandlar vikten från string till int

                    //Lägger till noderna i grafen
                    listGraph.add(cityStart);
                    listGraph.add(cityEnd);

                    //Skapar en koppling mellan dem
                    listGraph.connect(cityStart, cityEnd, tag, weight);
                }
                file.close();
                reader.close();
            } catch (IOException e){
                throw new RuntimeException("Error: No such file found.");
            }
        }
    }

    public void saveImage(){
        try{
            WritableImage image = scene.snapshot(null); //skapar bild av scenen - snapshot kan bara vara WritableImage (som är en bildtyp för att representera bilder i minnet)
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null); //bilden omvandlas till bufferedimage via swingFXUtils, vilket krävs pga imageio kan bara hantera bufferedimages
            ImageIO.write(bufferedImage, "png", new File("capture.png")); //imageIO kan spara bilder i olika format
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "IO-error " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void exit(WindowEvent event){
        if(hasSaved){
            //Stage stageToClose = (Stage) scene.getWindow();
            //stageToClose.close();
            System.exit(0);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Unsaved changes, exit anyway?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get().equals(ButtonType.CANCEL)){
                event.consume(); //"konsumerar" aka avbryter eventet, så stängningen kommer ej ske
            } else {
                Stage stageToClose = (Stage) scene.getWindow();
                stageToClose.close();
            }
        }
    }

    public void showConnection(){
        Set<City> nodes = listGraph.getNodes(); //Hämtar alla noder
        City cityStart = new City("temp1"); //Kanske ska flyttas ut till instansvariabel?
        City cityEnd = new City("temp2"); //Måste ändra sen så att man hämtar de faktiskta noderna?

        if (cityStart == null || cityEnd == null){   //Om det inte finns två markerade platser i kartan visas felmeddelande
            showError("Error: Select two cities.");
            return;
        } else if (listGraph.getEdgeBetween(cityStart, cityEnd) == null){ //Om det inte finns förbindelse mellan platserna visas felmeddelande
            showError("Error: No connection between citites.");
            return;
        } 

        //Visar ett fönster med uppgifter om förbindelsen.
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle("Connection");
        alert.setHeaderText("Connection from blablabla to buuu");

        //Gör första HBox:en
        Label name = new Label("Name: ");
        TextField nameField = new TextField();
        HBox hboxOne = new HBox(8); //sätter padding horisontellt
        hboxOne.getChildren().addAll(name, nameField);

        //Gör andra HBox:en
        Label time = new Label("Time: ");
        TextField timeField = new TextField();
        HBox hboxTwo = new HBox(13);
        hboxTwo.getChildren().addAll(time, timeField);

        //Lägg till de i en VBox
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hboxOne, hboxTwo);
        vbox.setAlignment(Pos.CENTER);

        //placerar de i mitten av en BorderPane
        // BorderPane borderPane = new BorderPane();
        // borderPane.setCenter(vbox);
        // borderPane.setAlignment(vbox, Pos.CENTER);

        alert.getDialogPane().setContent(vbox);
        alert.showAndWait();
    }

    private void showError(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
