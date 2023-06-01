import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.List;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import java.io.*;
import java.util.*;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

import javafx.scene.control.ButtonBar.ButtonData;

public class Graphics<T> extends Application {
    private Rectangle mapBackground;
    private boolean imageLoaded = false;
    private ImageView imageView;
    private String imageUrl = "europa.gif";
    private ListGraph listGraph = new ListGraph();
    private TextField nameField;
    private boolean hasSaved = true;
    private Scene scene;
    private Stage primaryStage;

    private Pane imagePane;
    private CustomButton newPlaceButton;
    private CustomButton findPathButton;
    private CustomButton showConnectionButton;
    private CustomButton newConnectionButton;
    private CustomButton changeConnectionButton;
    private ArrayList<City> placesList = new ArrayList<>();
    private City place1;
    private City place2;

    private List<CustomButton> buttons;
    private VBox root = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // lista med underrubriker för "file" menyn
        List<String> menuNamnen = List.of("New Map", "Open", "Save", "Save Image", "Exit");
        MenuBar menuBar = new MenuBar();
        VBox vBox = new VBox(menuBar);

        Menu fileMenu = new Menu("File"); // Skapar & sätter Meny-texten till File

        // loopar igenom listan av menynamnen, skapar en menuitem för varje namn och
        // lägger till eventhanterare via handlefilemenu + lägger till i filemenyn
        for (String rubrikNamn : menuNamnen) {
            MenuItem menuItem = new MenuItem(rubrikNamn);
            menuItem.setOnAction(this::handleFileMenu);
            fileMenu.getItems().add(menuItem);
        }

        // lägger till filemenu tiill menubar
        menuBar.getMenus().add(fileMenu);

        FlowPane buttonPane = new FlowPane();
        // sätter mellanrum/spacing mellan ui elementen (knapparna) både vertikalt och horisontellt
        buttonPane.setHgap(20);
        buttonPane.setVgap(20);

        // här sätter jag den "önskade" bredden på flowpanen
        buttonPane.setPrefWrapLength(800);

        vBox.getChildren().add(buttonPane);

        // sätter bredd och höjd för stage
        primaryStage.setWidth(635);
        primaryStage.setHeight(818);

        // lista med meny knappar
        buttons = List.of(
            findPathButton = new CustomButton("Find Path", 0, 0),
            showConnectionButton = new CustomButton("Show Connection", 0, 0),
            newPlaceButton = new CustomButton("New Place", 0, 0),
            newConnectionButton = new CustomButton("New Connection", 0, 0),
            changeConnectionButton = new CustomButton("Change Connection", 0, 0));

        buttonPane.getChildren().addAll(buttons); // lägger in knapparna i pane

        // här adderar jag en eventhandlar till varje separat knapp som är tillagd i
        // listan
        /*
         * for (CustomButton button : buttons) {
         * button.setOnAction(new EventHandler<ActionEvent>() { //Allt inom {} är den
         * nya metoden EventHandler
         * 
         * @Override
         * public void handle(ActionEvent event) {
         * handleButtons(event);
         * //System.out.println("Knapp " + button.getText() + " har klickat på :)");
         * }
         * });
         * }
         */
        for (CustomButton button : buttons) {
            button.setOnAction(this::handleButtons);
            button.setDisable(true);
        }

        // skapar en egen pane för själva bilden så att den inte lagras på knapparna
        imagePane = new Pane();
        imageView = new ImageView();
        imagePane.getChildren().add(imageView);

        root.getChildren().addAll(vBox, buttonPane, imagePane);

        // här skapas en scene med vbox som root och måtten/storleken
        scene = new Scene(root, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("PathFinder");

        // primaryStage.setOnCloseRequest(new ExitHandler());
        primaryStage.setOnCloseRequest(event -> exit(event));
    }

    private void handleFileMenu(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String menuNamn = menuItem.getText();

        switch (menuNamn) {
            case "New Map":
                if (!imageLoaded) {
                    Image image = new Image(imageUrl);
                    imageView.setImage(image);
                    imageLoaded = true;
                    for (CustomButton b : buttons) {
                        b.setDisable(false);
                    }
                    hasSaved = false;
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


    private void handleButtons(ActionEvent event) {
        CustomButton button = (CustomButton) event.getSource();
        String name = button.getText();

        switch (name) {
            case "Find Path":
                System.out.println("Find Path clicked");
                break;
            case "Show Connection":
                showConnection();
                System.out.println("Show Connection print");
                break;
            case "New Place":
                System.out.println("New Place print");
                newPlaceButton.setOnAction(new NewPlaceHandler());
                break;
            case "New Connection":
                newConnection();
                System.out.println("New Connection print");
                break;
            case "Change Connection":
                changeConnection();
                System.out.println("Change Connection clicked");
                break;
        }
    }


    private void save() {
        try {
            String filePath = "europa.graph"; // Referens till filen vi ska skriva till
            PrintWriter writer = new PrintWriter(new FileWriter(filePath)); // Skapar ny med filePath
            writer.println("file:" + imageUrl); // Skriver ut url:en
            StringBuilder sb = new StringBuilder(); // Skapar stringBuilder som är tom just nu

            Set<City> nodeSet = listGraph.getNodes(); /// Hämtar alla noder
            for (City node : nodeSet) { // Går igenom varje nod i nod-Settet
                sb.append(node.getName()).append(";").append(node.getX()).append(";").append(node.getY()).append(";");
            }
            writer.println(sb.toString()); // Skriver ut stringBuildern i filen

            for (City node : nodeSet) { // Går igenom varje stad. Ex: Stockholm, London, Oslo
                for (Object obj : listGraph.getEdgesFrom(node)) { // Går igenom alla dess kanter
                    Edge edge = (Edge) obj;
                    City destination = (City) edge.getDestination();
                    int weight = edge.getWeight();
                    String edgeName = edge.getName();
                    writer.println(node.getName() + ";" + destination.getName() + ";" + edgeName + ";" + weight);
                }
            }

            writer.close();
            hasSaved = true;
        } catch (IOException e) {
            throw new RuntimeException("Error: No such file found.");
        }
    }


    private void open() { // Övningsuppgift 4 använder en map för att konvertera String till Node, kanske behövs???
        checkUnsavedChanges();


        imagePane.getChildren().clear();
        placesList.clear();
        findPathButton.setDisable(false);
        showConnectionButton.setDisable(false);
        newPlaceButton.setDisable(false);
        newConnectionButton.setDisable(false);
        changeConnectionButton.setDisable(false);
        place1 = null;
        place2 = null;

        hasSaved = false;

        try { //Återskapa objekt från sparade filen europa.graph
            Map<String, City> map = new HashMap<>(); //Namnet på staden är nyckeln
            FileReader file = new FileReader("europa.graph"); // Referens till filen
            BufferedReader reader = new BufferedReader(file); // Läser in filen rad för rad

            String line = reader.readLine(); // läser första raden file:europa.gif
            String[] fileParts = line.split(":");
            String url = fileParts[1];
            Image image = new Image(url);
            imageView.setImage(image);
            imagePane.getChildren().add(imageView);
            imageLoaded = true;

            line = reader.readLine();// Läser andra raden med uppradningen av alla städer
            String[] subParts = line.split(";"); //Formatet på meningen: cityAName, x1, y1; cityBName, x2, y2
            for (int i = 0; i < subParts.length; i += 3){
                String cityName = subParts[i];
                double x = Double.parseDouble(subParts[i+1]);
                double y = Double.parseDouble(subParts[i+2]);

                City city = new City(cityName, x ,y);
                listGraph.add(city);
                placesList.add(city);
                map.put(cityName, city);
                imagePane.getChildren().add(city);//Lägger till city i scenen
                city.setId(cityName); 
                //fortsätt här
            }
    
            // Alla rader efter detta ser ut ex: Stockholm;Oslo;Train;3
            while ((line = reader.readLine()) != null) { // läser en rad i taget genom hela filen tills den tar slut
                String[] parts = line.split(";"); // Delar upp noderna genom ;
                String cityStartName = parts[0]; // Omvandlar String till T
                String cityEndName = parts[1];
                String tag = parts[2];
                int weight = Integer.parseInt(parts[3]); // Omvandlar vikten från string till int

                City cityStart = map.get(cityStartName);
                City cityEnd = map.get(cityEndName);

                createLine(cityStart, cityEnd);
            }
            file.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: No such file found.");
        }
    }


    private void saveImage() {
        try {
            // skapar bild av scenen - snapshot kan bara vara WritableImage (som är en bildtyp för att representera bilder i minnet)
            WritableImage image = scene.snapshot(null); 
            // bilden omvandlas till bufferedimage via swingFXUtils, vilket krävs pga imageio kan bara hantera bufferedimages
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null); 
            ImageIO.write(bufferedImage, "png", new File("capture.png")); // imageIO kan spara bilder i olika format
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "IO-error " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void exit(WindowEvent event) {
        if (hasSaved) {
            // Stage stageToClose = (Stage) scene.getWindow();
            // stageToClose.close();
            System.exit(0);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Warning");
            alert.setContentText("Unsaved changes, exit anyway?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.CANCEL)) {
                event.consume(); // "konsumerar" aka avbryter eventet, så stängningen kommer ej ske
            } else if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                Stage stageToClose = (Stage) scene.getWindow();
                stageToClose.close();
            }
        }
    }


    private class NewPlaceHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            newPlaceButton.setDisable(true);
            imagePane.setCursor(Cursor.CROSSHAIR);
            imagePane.setOnMouseClicked(new MapClickHandler());
        }
    }


    private class MapClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            double x = event.getX();
            double y = event.getY();

            TextInputDialog newPlaceName = new TextInputDialog();
            newPlaceName.setTitle("New Place");
            newPlaceName.setHeaderText("Enter the following: ");
            newPlaceName.setContentText("Name of new place:");
            var result = newPlaceName.showAndWait();

            if (result.isPresent()) {
                String name = newPlaceName.getEditor().getText();
                City newPlace = new City(name, x, y);

                // addera nya platsen till grafen
                placesList.add(newPlace);
                imagePane.getChildren().add(newPlace);
                newPlace.setId(name);

                // Av linn: innan lades den bara till i linus arraylist, inte i nodes grafen i
                // listgraph som våra listgraph metoder använder
                listGraph.add(newPlace);

                Label labelNewPlace = new Label(name);
                labelNewPlace.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 20));
                labelNewPlace.setTextFill(Color.BLACK);
                labelNewPlace.setLayoutX(x);
                labelNewPlace.setLayoutY(y + 8);
                imagePane.getChildren().add(labelNewPlace);

                // nu vet vi att användaren har lagt till en plats, så saved är false
                hasSaved = false;

                labelNewPlace.setDisable(true);
                newPlace.setOnMouseClicked(new PlaceClickHandler());

            }

            newPlaceButton.setDisable(false);
            imagePane.setCursor(Cursor.DEFAULT);
            imagePane.setOnMouseClicked(null);
        }
    }


    private class PlaceClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            City place = (City) event.getSource();

            if (place.isSelected()) {
                place.setSelected(false); // ifall den redan är selected blir den unselected (blå) av detta klick
                if (place == place1) {
                    place1 = null; // gör att man inte kan skapa ny place på detta ställe
                } else {
                    place2 = null;
                }
            } else { // ifall den inte är selected
                if (place1 == null) {
                    place1 = place;
                    place.setSelected(true);
                } else if (place2 == null) {
                    place2 = place;
                    place.setSelected(true);
                }
            }
        }
    }


    private class FindPathHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            if (place1 == null || place2 == null) {
                showError("Two places must be selected!");
            } else if (!listGraph.pathExists(place1, place2)) {
                showError("There is no path between these two places!");
            } else {
                List<Edge<City>> listOfPath = listGraph.getPath(place1, place2);

                Alert alrt = new Alert(AlertType.INFORMATION);
                alrt.setTitle("Message");
                alrt.setHeaderText("The Path from  " + place1.getName() + " to " + place2.getName() + ":");
                String innehåll = "";
                int totalWeight = 0;

                for (Edge<City> nuvaranceEdge : listOfPath) {
                    String destination = nuvaranceEdge.getDestination().getName();
                    String edgeName = nuvaranceEdge.getName();
                    int edgeWeight = nuvaranceEdge.getWeight();
                    innehåll += "to " + destination + " by " + edgeName + " takes " + edgeWeight + "\n";
                    totalWeight += edgeWeight;
                }

                innehåll += "Total " + totalWeight;
                TextArea textArea = new TextArea(innehåll);
                alrt.getDialogPane().setContent(textArea);
                textArea.setEditable(false);
                alrt.showAndWait();
            }
        }
    }


    private void newConnection() {
        // ifall användaren ej valt två platser
        if (place1 == null || place2 == null) {
            showError("Two places must be connected!");
            return;
        }

        // ifall connection redan finns mellan de valda platserna
        if (listGraph.pathExists(place1, place2)) {
            showError("Connection already exists!");
            return;
        }

        // skapar dialogrutan
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        HBox hBox1 = new HBox(6); // skapar två hboxar (två "rader"), med padding mellan dess children (komponenter)
        HBox hBox2 = new HBox(12);
        VBox vBox = new VBox(5);
        vBox.getChildren().addAll(hBox1, hBox2); // skapar vbox som lägger hboxarna efter varandra vertikalt
        confirmation.getDialogPane().setContent(vBox); // lägger till vboxen i alert

        confirmation.setHeaderText("Connection from " + place1.getName() + " till " + place2.getName());

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        hBox1.getChildren().addAll(nameLabel, nameField);

        Label timeLabel = new Label("Time:");
        TextField timeField = new TextField();
        hBox2.getChildren().addAll(timeLabel, timeField);

        Optional<ButtonType> result = confirmation.showAndWait();

        // hantera användarens input på dialogrutan
        if (result.isPresent() && result.get().equals(ButtonType.CANCEL)) {
            return;
        } else if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            String name = nameField.getText();
            String time = timeField.getText();
            if (name.isBlank() || name.isEmpty()) {
                showError("Name cannot be empty!");
                return;
            } else if (!time.matches("\\d+")) { // ifall strängen inte bara innehåller siffror
                showError("Time must be in numbers!");
                return;
            } else {
                // lägger till förbindelsen i städernas sets via connect-metoden
                listGraph.connect(place1, place2, name, Integer.valueOf(time));

                // skapar den grafiska förbindelsen (linjen)
                createLine(place1, place2);

                hasSaved = false;
            }
        }
    }


    private void showConnection() {
        Set<City> nodes = listGraph.getNodes(); // Hämtar alla noder
        Edge edge = listGraph.getEdgeBetween(place1, place2);

        checkConnection();

        // Visar ett fönster med uppgifter om förbindelsen.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Connection");
        alert.setHeaderText("Connection from " + place1.getName() + " to " + place2.getName());

        // Gör första HBox:en
        Label name = new Label("Name: ");
        TextField nameField = new TextField(edge.getName());
        nameField.setEditable(false);
        HBox hboxOne = new HBox(8); // sätter padding horisontellt
        hboxOne.getChildren().addAll(name, nameField);

        // Gör andra HBox:en
        Label time = new Label("Time: ");
        TextField timeField = new TextField(Integer.toString(edge.getWeight()));
        timeField.setEditable(false);
        HBox hboxTwo = new HBox(13);
        hboxTwo.getChildren().addAll(time, timeField);

        // Lägg till de i en VBox
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hboxOne, hboxTwo);
        vbox.setAlignment(Pos.CENTER);

        alert.getDialogPane().setContent(vbox);
        alert.showAndWait();
    }


    private void changeConnection() {
        Set<City> nodes = listGraph.getNodes(); // Hämtar alla noder
        Edge edge = listGraph.getEdgeBetween(place1, place2);

        checkConnection();

        // Visar ett fönster med uppgifter om förbindelsen.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Connection");
        alert.setHeaderText("Connection from " + place1.getName() + " to " + place2.getName());

        // Gör första HBox:en
        Label name = new Label("Name: ");
        TextField nameField = new TextField(edge.getName());
        nameField.setEditable(false);
        HBox hboxOne = new HBox(8); // sätter padding horisontellt
        hboxOne.getChildren().addAll(name, nameField);

        // Gör andra HBox:en
        Label time = new Label("Time: ");
        TextField timeField = new TextField();
        HBox hboxTwo = new HBox(13);
        hboxTwo.getChildren().addAll(time, timeField);

        // Lägg till de i en VBox
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hboxOne, hboxTwo);
        vbox.setAlignment(Pos.CENTER);

        alert.getDialogPane().setContent(vbox);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK && !timeField.getText().isEmpty()) { 
            // Om användaren klickat på OK och skrivit in ny tid
            listGraph.setConnectionWeight(place1, place2, Integer.parseInt(timeField.getText()));
            listGraph.setConnectionWeight(place2, place2, Integer.parseInt(timeField.getText())); 
            hasSaved = false;
        } else if (result.isPresent() && result.get() == ButtonType.OK && timeField.getText().isEmpty()) {
            showError("You have to write a new time!");
        }
    }


    private void checkConnection() {
        if (place1 == null || place2 == null) { // Om det inte finns två markerade platser i kartan visas felmeddelande
            showError("Error: Select two cities.");
            return;
        } else if (!listGraph.pathExists(place1, place2)) { // Om det inte finns förbindelse mellan platserna visas felmeddelande
            showError("Error: No connection between citites.");
            return;
        }
    }


    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


    private void createLine(City a, City b){
        Line connectionLine = new Line(a.getX(), a.getY(), b.getX(), b.getY());
        connectionLine.setStroke(Color.BLACK);
        connectionLine.setStrokeWidth(3.0);
        imagePane.getChildren().add(connectionLine);
    }

    private void checkUnsavedChanges(){
        if (hasSaved == false) {
            // Visa dialogruta för att fråga användaren om personen vill spara ändringarna
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Unsaved changes, continue anyways?");

            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(okButton, cancelButton); // sätter alla knappar i dialogfönstret tog bort saveButton härifrån

            Optional<ButtonType> result = alert.showAndWait(); 
            // väntar med att sätta knapp typen tills användare trycker på någon, rätt häftigt ändå

            switch (result.get().getText()) {
                // spara de osparade ändringarna
                case "Spara":
                    save();
                    break;
                case "OK":
                    // förkasta de osparade ändringarna och det nya "dokumentet" öppnas
                default:
                    // avbryt
                    return;
            }
        }
    }
}