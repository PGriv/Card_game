import javafx.application.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import java.util.ArrayList;
import java.util.Collections;


public class Main extends Application {
    int x=10;
    int y=10;
    int height = 50;
    int width  = 50;
    int columns = 4 , rows = 3;
    Rectangle[][] rec;
    Boolean[][] isClosed;
    Color[][] cards;
    int counter=0;
    int movesCounter=0;
    StateOfGame stateOfGame;
    Rectangle previous;
    int cardsFound = 0;
    Text message;
    

    @Override
    public void start(Stage stage) {

        double width = 100;
        double height = 100;

        rec = new Rectangle[rows][];
        for (int i=0; i<rows; i++){
            rec[i] = new Rectangle[columns];
            for(int j=0; j<columns; j++){
                rec[i][j] = new Rectangle(30+j*(width+30),
                        30+i*(height+30),width,height);
                rec[i][j].setArcWidth(40);
                rec[i][j].setArcHeight(60);
            }
        }
        Color[] colors = new Color[] {Color.web("ee6055"), Color.web("60d394"),
                Color.web("aaf683"), Color.web("ffd97d"),
                Color.web("ff9b85"), Color.web("90e0ef")};

        ArrayList<Integer> shuffle = new ArrayList<>();
        for (int i=0; i<rows*columns/2; i++){
            shuffle.add(i);
            shuffle.add(i);
        }

        Collections.shuffle(shuffle);
        System.out.println(shuffle);

        cards = new Color[rows][];
        isClosed = new Boolean[rows][];

        for (int i = 0; i<rows; i++){
            cards[i] = new Color[columns];
            isClosed[i]= new Boolean[columns];
            for (int j = 0; j<columns; j++){
                    cards[i][j] = colors[shuffle.get(columns*i+j)];
                    isClosed[i][j] = true;
            }
        }
        stateOfGame = StateOfGame.CLOSED;
        for (int i=0; i<rows; i++){
            for (int j=0; j<columns; j++){
                int Xlocation = i;
                int Ylocation = j;
                rec[i][j].setOnMouseClicked(e->click(Xlocation,Ylocation));
            }
        }
        message = new Text("Moves: "+movesCounter);
        message.setFont(Font.font(80));
        message.setX(140);
        message.setY(550);


        Pane pane = new Pane();
        for (int i=0; i<rec.length; i++)
            for (int j=0; j<rec[i].length; j++)
                pane.getChildren().addAll(rec[i][j]);
        pane.getChildren().add(message);

        pane.setPrefWidth(560);
        pane.setPrefHeight(640);

        pane.setBackground(new Background(new BackgroundFill(Color.web("f8f9fa"), new CornerRadii(0), Insets.EMPTY)));
        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.setTitle("MEMORY CARD GAME");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public void click(int X, int Y){
        if (isClosed[X][Y]){
            movesCounter+=1;
            message.setText("Moves: " + movesCounter);

            if(stateOfGame == StateOfGame.CLOSED){
                rec[X][Y].setFill(cards[X][Y]);
                previous = rec[X][Y];
                stateOfGame = StateOfGame.OPEN;
            }else {
                rec[X][Y].setFill(cards[X][Y]);

                if(cards[X][Y]!=previous.getFill()){
                    Thread thread = new Thread(()->{
                        try {
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        Platform.runLater(()->{
                            previous.setFill(Color.BLACK);
                            rec[X][Y].setFill(Color.BLACK);
                        });
                    });
                    thread.start();

                }
                else {
                    cardsFound+=2;
                    if(cardsFound==rows*columns){
                        message.setFont(Font.font(50));
                        message.setText("Game Over (total " + movesCounter + " moves)");
                        message.setX(30);
                    }
                stateOfGame = StateOfGame.CLOSED;

                }
            }
        }
    }
}