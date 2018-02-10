package gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import model.*;
import sun.invoke.empty.Empty;


/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the model
 * and receives updates from it.
 *
 * @author Sean Strout @ RIT CS
 * @author Calvin Wu
 * @author Nahjee Sowah
 */
public class LasersGUI extends Application implements Observer {
    /**
     * The UI's connection to the model
     */

    private LasersModel model;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button    the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }


    private BorderPane border = new BorderPane();

    /**
     * Initializes the stage
     *
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        // TODO
        border.setTop(this.makeTop());
        border.setCenter(this.makeCenter());
        border.setBottom(this.makeBottom());
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setScene(new Scene(border));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO
        init(primaryStage);  // do all your UI initialization here

        primaryStage.setTitle("Lasers");
        primaryStage.show();
    }

    /**
     * makeTop creates and returns a flow pane with a label.
     *
     * @return flow pane that can be added to a region
     */

    private FlowPane makeTop() {
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        Label aLabel = new Label(model.getOutput());
        flowPane.getChildren().add(aLabel);
        return flowPane;
    }

    ArrayList<Button> buttons = new ArrayList<>();
    private int row = 0;
    private int col = 0;

    /**
     * makeCenter creates and returns a grid pane of shapes.
     *
     * @return grid pane that can be added to a region
     */
    public GridPane makeCenter() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        int gap = 5;
        grid.setVgap(gap); // gap between grid cells
        grid.setHgap(gap);
        int pad = 10;
        grid.setPadding(new Insets(pad, pad, pad, pad)); // t,r,b,t
        for (row = 0; row < model.rowdim; row++) {
            for (col = 0; col < model.coldim; col++) {
                Button b = new Button("   ");
                buttons.add(b);
                if (model.table[row][col] == '.') {
                    setButtonBackground(b, "white.png");
                }
                else if (model.table[row][col] == '0') {
                    setButtonBackground(b, "pillar0.png");
                }
                else if (model.table[row][col] == '1') {
                    setButtonBackground(b, "pillar1.png");
                }
                else if (model.table[row][col] == '2') {
                    setButtonBackground(b, "pillar2.png");
                }
                else if (model.table[row][col] == '3') {
                    setButtonBackground(b, "pillar3.png");
                }
                else if (model.table[row][col] == '4') {
                    setButtonBackground(b, "pillar4.png");
                }
                else if (model.table[row][col] == 'X') {
                    setButtonBackground(b, "pillarX.png");
                }
                b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    private int xcol = col;
                    private int xrow = row;

                    @Override
                    public void handle(MouseEvent event) {
                        model.choose(xrow, xcol);
                    }
                });
                grid.add(b, col, row);
            }
        }
        grid.setGridLinesVisible(false);
        return grid;
    }

    public boolean hasRestarted; // Used for restart button

    /**
     * makeBottom creates and returns a tile pane of buttons.
     *
     * @return tile pane that can be added to a region
     */
    private TilePane makeBottom() {
        TilePane tilePane = new TilePane();
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setOrientation(Orientation.HORIZONTAL);
        Button btncheck = new Button("Check");
        btncheck.setOnMouseClicked(event -> {
            hasRestarted = false;
            model.verify2();
        });
        Button btnhint = new Button("Hint");
//        btnhint.setOnMouseClicked(event -> model.btnhint());
        Button btnsolve = new Button("Solve");
//        btnsolve.setOnMouseClicked(event -> model.solve());
        Button btnrestart = new Button("Restart");
        btnrestart.setOnMouseClicked(event -> {
            hasRestarted = true;
            model.restart();
        });
        Button btnload = new Button("Load");
        btnload.setOnMouseClicked(event -> {
            FileChooser chooser;
            chooser = new FileChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File input = chooser.showOpenDialog(null);
            if (input != null) {
                try {
                    model = new LasersModel(input.getAbsolutePath());
                    model.addObserver(this);
                    buttons=new ArrayList<Button>();
                    row=0;
                    col=0;
                    border.setCenter(makeCenter());
                    update(null, null);
                } catch (FileNotFoundException ignored) {
                    ignored.printStackTrace();
                }
            }
        });
        tilePane.getChildren().addAll(btncheck, btnhint, btnsolve, btnrestart, btnload);
        return tilePane;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO
        border.getScene().getWindow().sizeToScene();
        Pane top = (Pane) border.getTop();
        Label aLabel = (Label) top.getChildren().get(0);
        aLabel.setText(model.getOutput());
        System.out.println("Changed and notified");
        for (int i = 0; i < buttons.size(); i++) {
            int y = i % model.coldim;
            int x = i / model.coldim;
            if (!model.isInvalid) {
                if (model.table[x][y] == 'L') {
                    setButtonBackground(buttons.get(i), "laser.png");
                }
                else if (model.table[x][y] == '.') {
                    setButtonBackground(buttons.get(i), "white.png");
                }
                else if (model.table[x][y] == '*') {
                    setButtonBackground(buttons.get(i), "beam.png");
                }
            }
            else if (model.invalidSpot[0] == x && model.invalidSpot[1] == y && !hasRestarted) {
                if (model.table[x][y] == 'L') {
                    setButtonBackground(buttons.get(i), "redLaser.png");
                }
                else if (model.table[x][y] == '.') {
                    setButtonBackground(buttons.get(i), "red.png");
                }
                else if (model.table[x][y] == '*') {
                    setButtonBackground(buttons.get(i), "redBeam.png");
                }
                else if (model.table[x][y] == '0') {
                    setButtonBackground(buttons.get(i), "redPillar0.png");
                }
                else if (model.table[x][y] == '1') {
                    setButtonBackground(buttons.get(i), "redPillar1.png");
                }
                else if (model.table[x][y] == '2') {
                    setButtonBackground(buttons.get(i), "redPillar2.png");
                }
                else if (model.table[x][y] == '3') {
                    setButtonBackground(buttons.get(i), "redPillar3.png");
                }
                else if (model.table[x][y] == '4') {
                    setButtonBackground(buttons.get(i), "redPillar4.png");
                }
                else if (model.table[x][y] == 'X') {
                    setButtonBackground(buttons.get(i), "red.png");
                }
            }
            else {
                if (model.table[x][y] == 'L') {
                    setButtonBackground(buttons.get(i), "laser.png");
                }
                else if (model.table[x][y] == '.') {
                    setButtonBackground(buttons.get(i), "white.png");
                }
                else if (model.table[x][y] == '*') {
                    setButtonBackground(buttons.get(i), "beam.png");
                }
                else if (model.table[x][y] == '0') {
                    setButtonBackground(buttons.get(i), "pillar0.png");
                }
                else if (model.table[x][y] == '1') {
                    setButtonBackground(buttons.get(i), "pillar1.png");
                }
                else if (model.table[x][y] == '2') {
                    setButtonBackground(buttons.get(i), "pillar2.png");
                }
                else if (model.table[x][y] == '3') {
                    setButtonBackground(buttons.get(i), "pillar3.png");
                }
                else if (model.table[x][y] == '4') {
                    setButtonBackground(buttons.get(i), "pillar4.png");
                }
                else if (model.table[x][y] == 'X') {
                    setButtonBackground(buttons.get(i), "pillarX.png");
                }
            }
        }
    }
}
