package sample;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Math.abs;

public class KoguSeda extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();

        Text title = new Text("KoguSeda!");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        title.setX(325);
        title.setY(75);
        title.setFill(Color.AQUAMARINE);

        Button button1 = new Button("Mängi");
        button1.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        button1.setLayoutX(425);
        button1.setLayoutY(100);
        // nuppu vajutamisega vahetatakse Stseeni
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Group group = new Group();

                Rectangle rectangle = new Rectangle(16, 16, Color.RED);
                rectangle.setLayoutY(150);
                rectangle.setLayoutX(120);

                Circle circle = new Circle(2.5, Color.YELLOW);
                circle.setCenterX(675);
                circle.setCenterY(250);

                Image image = new Image("file:map.png");
                ImageView imageView = new ImageView();
                imageView.setImage(image);
                imageView.setBlendMode(BlendMode.MULTIPLY);
                // kasutame meetodit, selleks et koostada vaenlased
                Circle circle1 = vaenlane(265, 160, 570, 160);
                Circle circle2 = vaenlane(265, 220, 570, 220);
                Circle circle3 = vaenlane(570, 190, 265, 190);
                Circle circle4 = vaenlane(570, 250, 265, 250);
                group.getChildren().addAll(rectangle, imageView, circle, circle1, circle2, circle3, circle4);

                Scene scene = new Scene(group, 800, 480);
                // meetod, mis kontrollib kuidas tegelane liigub ning kuidas tegelane suhtleb teiste objektidega
                tegelaseLiikumine(scene, rectangle, primaryStage, circle1, circle2, circle3, circle4);
                primaryStage.setScene(scene);
            }
        });
        // juurdepääs allikale ning meedia esitamine
        Media musicFile = new Media("file:///C:/Users/Artur/Music/game_music.mp3");
        MediaPlayer mediaPlayer = new MediaPlayer(musicFile);

        Button buttonPlay = new Button("Music");
        buttonPlay.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");

        buttonPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.play();
            }
        });
        // valmistab uut lavat ning näitab vihje tekstina
        Button button2 = new Button("Vihje");
        button2.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        button2.setLayoutX(430);
        button2.setLayoutY(160);
        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BorderPane borderPane = new BorderPane();
                VBox vBox = new VBox();
                Text text = new Text();

                /*Text text1 = new Text("Mängu eesmärg on takistustest läbi saada ning koguda kuldse mündi.");
                Text text2 = new Text("Peab leidma ebatavalist taktikat, et takistustest läbi saada");
                Text text3 = new Text("ning kuidas seda teha, sul endal tuleb lahendust leida!");*/

                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("reeglid.txt")))) {
                    String reeglid = "";

                    String rida = br.readLine();

                    while (rida != null) {
                        reeglid += rida;
                        reeglid += "\n";
                        rida = br.readLine();
                    }
                    text.setText(reeglid.trim());
                    text.setFont(Font.font ("Verdana", 16));
                }
                catch (IOException exception) {
                    throw new RuntimeException("Sllist faili ei ole süsteemis olemas!");
                }

                vBox.getChildren().addAll(text);
                borderPane.setTop(vBox);

                Scene scene = new Scene(borderPane, 570 ,70);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });

        Button button3 = new Button("Pane kirja");
        button3.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        button3.setLayoutX(405);
        button3.setLayoutY(250);
        button3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BorderPane borderPane = new BorderPane();

                TextField tekst = new TextField();
                tekst.setText("Pane ennast kirja: ");
                borderPane.setTop(tekst);

                // sündmuse lisamine tekstiväljale (klahvisündmus reageerib
                // ainult enter-i vajutamisele)
                tekst.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.ENTER) {
                            Salvestaja salvestaja = new Salvestaja();
                            try {
                                salvestaja.salvestaMängija("nimekiri.txt", tekst.getCharacters().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                Scene scene = new Scene(borderPane, 375 ,50);
                Stage primaryStage = new Stage();
                primaryStage.setTitle("Registreerimine");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });

        Rectangle rectangle = new Rectangle(450, 20);
        rectangle.setX(50);
        rectangle.setY(460);
        rectangle.setFill(Color.AQUAMARINE);

        Image image = new Image(new FileInputStream("C:\\Users\\Artur\\IdeaProjects\\GraafilineProjekt\\triangle.gif"));
        ImageView imageView = new ImageView(image);
        imageView.setX(50);
        imageView.setY(25);
        imageView.setFitHeight(455);
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        group.getChildren().addAll(imageView, button1, button2, rectangle, title, buttonPlay, button3);
        ringiUleminek(group, 25, 455, 375, 240);
        ringiUleminek(group, 775, 25, 425, 240);
        punkt(group);
        ruuduUleminek(group);

        primaryStage.setTitle("KoguSeda");
        primaryStage.setScene(new Scene(group, 800, 480));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }

    private Node ruuduUleminek(Group group) {
        Rectangle rectangle = new Rectangle(25, 25);
        rectangle.setFill(Color.RED);

        Path path = new Path();
        MoveTo moveTo = new MoveTo(25, 25); //Moving to the starting point
        LineTo line = new LineTo(775, 455);
        path.getElements().add(moveTo);
        path.getElements().add(line);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(rectangle);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        group.getChildren().add(rectangle);
        return group;
    }

    public Node ringiUleminek(Group group, double x1, double y1, double x2, double y2) {
        Circle circle = new Circle(12.5, Color.BLUE);

        Path path = new Path();
        MoveTo moveTo = new MoveTo(x1, y1); //Moving to the starting point
        LineTo line = new LineTo(x2, y2);
        path.getElements().add(moveTo);
        path.getElements().add(line);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(circle);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        group.getChildren().add(circle);
        return group;
    }

    public Node punkt(Group group) {
        Circle circle = new Circle(5);
        circle.setFill(Color.YELLOW);
        circle.setCenterX(775);
        circle.setCenterY(455);

        FadeTransition ft = new FadeTransition(Duration.millis(1000), circle);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        group.getChildren().add(circle);
        return group;
    }

    public void tegelaseLiikumine(Scene scene, Node rectangle, Stage primaryStage,
                                  Circle circle1, Circle circle2, Circle circle3, Circle circle4) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // Tegelane saab liikuda ainult sellisel juhul kui ta ei lähe piirist välja
                switch (event.getCode()) {
                    case DOWN:
                        if ((rectangle.getLayoutX() <= 267 && rectangle.getLayoutY() + 7 <= 339) ||
                                (rectangle.getLayoutX() >= 260 && rectangle.getLayoutY() + 7 <= 304 && rectangle.getLayoutX() <= 568) ||
                                (rectangle.getLayoutX() >= 568 && rectangle.getLayoutY() + 7 <= 143) ||
                                (rectangle.getLayoutX() >= 624 && rectangle.getLayoutY() + 7 <= 332))
                            rectangle.setLayoutY(rectangle.getLayoutY() + 7); break;
                    case UP:
                        if ((rectangle.getLayoutX() >= 78 && rectangle.getLayoutX() <= 155 && rectangle.getLayoutY() >= 115 + 7) ||
                                (rectangle.getLayoutY() >= 304 + 7) ||
                                (rectangle.getLayoutX() >= 260 && rectangle.getLayoutX() <= 568 && rectangle.getLayoutY() >= 150 + 7) ||
                                (rectangle.getLayoutX() >= 554 && rectangle.getLayoutY() >= 115 + 7))
                            rectangle.setLayoutY(rectangle.getLayoutY() - 7); break;
                    case RIGHT:
                        if ((rectangle.getLayoutX() <= 155 - 7) ||
                                (rectangle.getLayoutY() >= 304 && rectangle.getLayoutX() <= 267 - 7) ||
                                (rectangle.getLayoutY() <= 305 && rectangle.getLayoutX() >= 260 && rectangle.getLayoutX() <= 568 - 7) ||
                                (rectangle.getLayoutY() <= 143 && rectangle.getLayoutX() <= 722 - 7 && rectangle.getLayoutX() >= 554) ||
                                (rectangle.getLayoutX() >= 624 && rectangle.getLayoutX() <= 722 - 7))
                            rectangle.setLayoutX(rectangle.getLayoutX() + 7); break;
                    case LEFT:
                        if ((rectangle.getLayoutX() - 7 >= 78 && rectangle.getLayoutX() <= 155) ||
                                (rectangle.getLayoutY() >= 304 && rectangle.getLayoutX() <= 568 && rectangle.getLayoutX() - 7 >= 78) ||
                                (rectangle.getLayoutX() - 7 >= 260 && rectangle.getLayoutX() <= 568 && rectangle.getLayoutY() >= 150) ||
                                (rectangle.getLayoutX() - 7>= 554 && rectangle.getLayoutY() <= 143) ||
                                (rectangle.getLayoutX() - 7 >= 624))
                            rectangle.setLayoutX(rectangle.getLayoutX() - 7); break;
                }
                // kui tegelane jõudis mündini
                if (rectangle.getLayoutX() >= 660 &&  rectangle.getLayoutX() <= 675
                        && rectangle.getLayoutY() >= 230 && rectangle.getLayoutY() <= 250) {
                    primaryStage.close();
                    // mängu lava pannakse kinni ja tehakse uut lava koos õnnitlus tekstiga
                    BorderPane borderPane = new BorderPane();
                    Text text = new Text("Oled võitnud!");
                    borderPane.setCenter(text);

                    Scene scene = new Scene(borderPane, 375, 50);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                }

                Bounds bounds1 = circle1.localToScreen(circle1.getLayoutBounds());
                Bounds bounds2 = circle2.localToScreen(circle2.getLayoutBounds());
                Bounds bounds3 = circle3.localToScreen(circle3.getLayoutBounds());
                Bounds bounds4 = circle4.localToScreen(circle4.getLayoutBounds());
                Bounds bounds5 = rectangle.localToScreen(rectangle.getLayoutBounds());

                int minx1 = (int) bounds1.getMinX();
                int miny1 = (int) bounds1.getMinY();
                int minx2 = (int) bounds2.getMinX();
                int miny2 = (int) bounds2.getMinY();
                int minx3 = (int) bounds3.getMinX();
                int miny3 = (int) bounds3.getMinY();
                int minx4 = (int) bounds4.getMinX();
                int miny4 = (int) bounds4.getMinY();
                int minx5 = (int) bounds5.getMinX();
                int miny5 = (int) bounds5.getMinY();

                int maxx1 = (int) bounds1.getMaxX();
                int maxy1 = (int) bounds1.getMaxY();
                int maxx2 = (int) bounds2.getMaxX();
                int maxy2 = (int) bounds2.getMaxY();
                int maxx3 = (int) bounds3.getMaxX();
                int maxy3 = (int) bounds3.getMaxY();
                int maxx4 = (int) bounds4.getMaxX();
                int maxy4 = (int) bounds4.getMaxY();
                int maxx5 = (int) bounds5.getMaxX();
                int maxy5 = (int) bounds5.getMaxY();

                if (abs(minx1 - maxx5) <= 10 && abs(miny1 - maxy5) <= 10)
                    primaryStage.close();
                else if (abs(minx2 - maxx5) <= 10 && abs(miny2 - maxy5) <= 10)
                    primaryStage.close();
                else if (abs(minx3 - maxx5) <= 10 && abs(miny3 - maxy5) <= 10)
                    primaryStage.close();
                else if (abs(minx4 - maxx5) <= 10 && abs(miny4 - maxy5) <= 10)
                    primaryStage.close();

                else if (abs(maxx1 - minx5) <= 10 && abs(maxy1 - miny5) <= 10)
                    primaryStage.close();
                else if (abs(maxx2 - minx5) <= 10 && abs(maxy2 - miny5) <= 10)
                    primaryStage.close();
                else if (abs(maxx3 - minx5) <= 10 && abs(maxy3 - miny5) <= 10)
                    primaryStage.close();
                else if (abs(maxx4 - minx5) <= 10 && abs(maxy4 - miny5) <= 10)
                    primaryStage.close();
            }
        });
    }

    public Circle vaenlane(double x1, double y1, double x2, double y2) {
        Circle circle = new Circle(5, Color.BLUE);

        Path path = new Path();
        MoveTo moveTo = new MoveTo(x1, y1); //Moving to the starting point
        LineTo line = new LineTo(x2, y2);
        path.getElements().add(moveTo);
        path.getElements().add(line);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setNode(circle);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        return circle;
    }
}