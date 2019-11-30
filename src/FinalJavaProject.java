

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class FinalJavaProject extends Application {
    Pane pane;
    ImageView blueSnowmobile;
    ImageView greenSnowmobile;
    ImageView purpleSnowmobile;
    ImageView redSnowmobile;
/*-----------------------Text Fields & Misc Variables--------------*/
    private ToggleGroup cars;
    private TextField betAmount;
    private TextField userChoice;
    boolean userPicked = false;
    String carChosen;
    /* This text field is used to display information to the user of the program.*/
    private TextField announcer;
    private boolean isRacing = false;
    int number = 0;

/*------------------------Create Queue to store cars as they cross the finish line---------------------*/
    Queue<String> winnersQueue = new LinkedList<String>();
/*-----------------------------------------------------------------------------*/    
    @Override
    public void start(Stage primaryStage) {
/*------Background Image and Car images ----------*/
        pane = new Pane();
        initializeBackground();
        initializeCars();
        initializeLabels();
        initializeButtons();
        initializeRadioButtons();
        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.setTitle("Snowmobile Racing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void initializeCars(){
        Image blueCar = new Image("https://i.imgur.com/ROhXt8Z.jpg");
        Image greenCar = new Image("https://i.imgur.com/XXVgpJV.jpg");
        Image purpleCar = new Image("https://i.imgur.com/tT19est.jpg");
        Image redCar = new Image("https://i.imgur.com/Bq37Kzv.jpg");

        blueSnowmobile = new ImageView(blueCar);
        greenSnowmobile = new ImageView(greenCar);
        purpleSnowmobile = new ImageView(purpleCar);
        redSnowmobile = new ImageView(redCar);

        /*----------------Create Four Snowmobiles-----------------------------------*/
        blueSnowmobile.setX(50); //Blue is Car #1
        blueSnowmobile.setY(270);
        blueSnowmobile.setFitHeight(30);
        blueSnowmobile.setFitWidth(50);
        pane.getChildren().add(blueSnowmobile);

        greenSnowmobile.setX(50); //Green is Car #2
        greenSnowmobile.setY(320);
        greenSnowmobile.setFitHeight(30);
        greenSnowmobile.setFitWidth(50);
        pane.getChildren().add(greenSnowmobile);

        purpleSnowmobile.setX(50); //Purple is Car #3
        purpleSnowmobile.setY(370);
        purpleSnowmobile.setFitHeight(30);
        purpleSnowmobile.setFitWidth(50);
        pane.getChildren().add(purpleSnowmobile);

        redSnowmobile.setX(50); //Red is Car #4
        redSnowmobile.setY(420);
        redSnowmobile.setFitHeight(30);
        redSnowmobile.setFitWidth(50);
        pane.getChildren().add(redSnowmobile);
    }

    public void initializeBackground(){
        Image backgroundImage = new Image("https://i.imgur.com/i3XRlZu.png");
        ImageView background = new ImageView(backgroundImage);
        background.setFitHeight(600);
        background.setFitWidth(600);
        pane.getChildren().add(background);
    }

    public void initializeLabels(){
        Label betLabel = new Label("Amount to Bet: ");
        betLabel.setLayoutX(188);
        betLabel.setLayoutY(550);
        betLabel.setFont(Font.font("Gill Sans MS",FontWeight.BOLD, 14));
        Label carNumberLabel = new Label("Car #: ");
        carNumberLabel.setLayoutX(450);
        carNumberLabel.setLayoutY(550);
        carNumberLabel.setFont(Font.font("Gill Sans MS",FontWeight.BOLD, 14));
        betAmount =  new TextField();
        betAmount.setLayoutX(290);
        betAmount.setLayoutY(550);
        betAmount.setMaxWidth(60);
        announcer = new TextField();
        announcer.setLayoutX(15);
        announcer.setLayoutY(510);
        announcer.setFont(Font.font("Gill Sans MS",FontWeight.BOLD, 14));
        announcer.setText("Please enter the amount you wish to bet and "
                + "the car you wish to bet on.");
        announcer.setMinWidth(545);
        announcer.setEditable(false);
        pane.getChildren().add(announcer);
        pane.getChildren().addAll(betLabel, betAmount);
    }

    public void initializeButtons(){
        /*----------------Buttons and Text fields labels----------------------------*/
        Button btStartRace = new Button("Start");
        btStartRace.setLayoutX(15);
        btStartRace.setLayoutY(550);

        Button btResetRace = new Button("Reset");
        btResetRace.setLayoutX(85);
        btResetRace.setLayoutY(550);
/*------------Add buttons to the Pane in the bottom of BorderPane-----*/
        pane.getChildren().add(btStartRace);
        pane.getChildren().add(btResetRace);
        btStartRace.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
        /* The program is not allowed to start unless the text fields contain expected values.
           (1-1000 for bet amount, 1-4 for car chosen)
           Warnings are displayed in announcer TextField in case . */
            try {if (betAmount.getText() == null || !userPicked )
                announcer.setText("Please enter your bet amount and choose a car.");
            else if (betAmount.getText() != null && !userPicked)
                announcer.setText("Please select a car.");
            else if (betAmount.getText() == null && userPicked)
                announcer.setText("You can't watch the race if you don't bet.");
            else if (Double.parseDouble(betAmount.getText()) > 1000)
                announcer.setText("Gambling laws don't allow bets greater than $1,000!");
            else if (Double.parseDouble(betAmount.getText()) < 1)
                announcer.setText("You can't enter the game with a bet that low.");
            else{

                if (isRacing == false){
                    announcer.clear();
                    ExecutorService executor = Executors.newFixedThreadPool(4);
            /* The constructor arguments are the following:
                The car(rectangle/imageview object),
                The Queue in which the winners will be stored,
                The button so we can manipulate it inside the subclass,
                The # of the car Object,
                The # the user chose,
            &   The $ amount the user bet(typecasted to double)*/

                    //These executors start the race.
                    executor.execute(new Race(blueSnowmobile,winnersQueue,btStartRace,
                            "1",carChosen,Double.parseDouble(betAmount.getText()),btResetRace));
                    executor.execute(new Race(greenSnowmobile,winnersQueue,btStartRace,
                            "2",carChosen,Double.parseDouble(betAmount.getText()),btResetRace));
                    executor.execute(new Race(purpleSnowmobile,winnersQueue,btStartRace,
                            "3",carChosen,Double.parseDouble(betAmount.getText()),btResetRace));
                    executor.execute(new Race(redSnowmobile, winnersQueue,btStartRace,
                            "4",carChosen,Double.parseDouble(betAmount.getText()),btResetRace));
                }
            }
            } catch (NumberFormatException Ex) {
                //announcer.setText("Please enter a valid bet amount.");
            }
        });
                                /*----This Handler is for the RESET button--*/
        btResetRace.addEventHandler(MouseEvent.MOUSE_CLICKED,
                (MouseEvent event) -> {
                    number = 0;
                    announcer.clear();
                    betAmount.clear();
                    isRacing = false;
                    userPicked = false;
                    cars.getSelectedToggle().setSelected(false);
                    winnersQueue.clear();
                    btStartRace.setDisable(false);
                    announcer.setText("Please enter the amount you wish to bet and "
                            + "the car you wish to bet on.");

                    PositionReset();
                });
    }

    public void initializeRadioButtons(){
        cars = new ToggleGroup();
        RadioButton car1 = new RadioButton("Blue");
        car1.setToggleGroup(cars);
        RadioButton car2 = new RadioButton("Green");
        car2.setToggleGroup(cars);
        RadioButton car3 = new RadioButton("Pink");
        car3.setToggleGroup(cars);
        RadioButton car4 = new RadioButton("Red");
        car4.setToggleGroup(cars);
        car1.setLayoutX(445);
        car1.setLayoutY(550);
        car2.setLayoutX(520);
        car2.setLayoutY(550);
        car3.setLayoutX(445);
        car3.setLayoutY(580);
        car4.setLayoutX(520);
        car4.setLayoutY(580);
        pane.getChildren().add(car1);
        pane.getChildren().add(car2);
        pane.getChildren().add(car3);
        pane.getChildren().add(car4);

        car1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    carChosen = "1";
                    userPicked = true;
                }
            }
        });
        car2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    carChosen = "2";
                    userPicked = true;
                }
            }
        });
        car3.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    carChosen = "3";
                    userPicked = true;
                }
            }
        });
        car4.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    carChosen = "4";
                    userPicked = true;
                }
            }
        });

    }
/*---------------------------Racer Class--------------------------*/
    class Race implements Runnable {

        ImageView snowMobile;
        Queue<String> finishedOrder;
        Button btStartRace, btResetRace;
        String carNumber, carChosen;
        double betAmount;
        boolean racing;
        
        Race (ImageView snowMobile, Queue winners, Button start, 
        String carNumber, String carChosen, double betAmount, Button reset){
           this.snowMobile = snowMobile;
           this.finishedOrder = winners;
           this.btStartRace = start;
           this.btResetRace = reset;
           this.carNumber = carNumber; 
           this.carChosen = carChosen;
           this.betAmount = betAmount;
        }
        
        
       @Override
        public void run() {	
            isRacing = true;
            //Disable the button while the race is running. This is to avoid errors.
            btStartRace.setDisable(true);
            btResetRace.setDisable(true);
            while(snowMobile.getX() < 550) {	
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           //This Math.random() generates a number between 1-3.
                           int random = (int)(Math.random() * ((6 - 1) + 1) + 1);
                           snowMobile.setX(snowMobile.getX() + random);
                        }
                    });
                try {
                    Thread.sleep(60);
                } catch (InterruptedException ex) {
                }
	   }
            //This system.out is only useful for testing purposes. It is to display which thread finished the race first.
            System.out.println("Car #" + this.carNumber + " has crossed the finish line!");
            if (snowMobile.getX() > 540) {
                finishedOrder.add(carNumber);
                Winners(finishedOrder,betAmount,carChosen,btResetRace);
            }
	}   
    }

    public void Winners(Queue WinnersQueueArray, double betAmount, String carChosen, Button resetbutton) {

            number++; /*This makes it so that the results are only displayed on
            the fourth time of this function call. 
            Reason to do this is because this function is called 4 times
            (because its called from within a thread that is run 4 times), 
            and Queue.poll() erases the object it returns*/
            if (number == 4){
                //Checks the first object in the queue(first one who won the race)  , compares it with the one the user chose
                if (WinnersQueueArray.poll().equals(carChosen)){
                   announcer.setText("Congratulations! "
                  + "The car you bet on won! Your prize is: " +
                           "$" + betAmount * 1000);
                   
                }
                //Checks the second object in the queue(second one who won the race)  & compares it with the one the user chose   
                else if (WinnersQueueArray.poll().equals(carChosen)){
                    announcer.setText("Congratulations! "
                  + "The car you bet came in 2nd, Your prize is: " +
                           "$" + betAmount * 500);
                }
                //Checks the third object in the queue(third one who won the race)  & compares it with the one the user chose   
                else if (WinnersQueueArray.poll().equals(carChosen)){
                    announcer.setText("Congratulations! "
                  + "The car you bet came in 3rd! Your prize is: " +
                    "$" + betAmount * 250);
                } else {
                    announcer.setText("Sorry, try again next time."
                  + "You lost: " +
                           "$" + betAmount);
                    
            }
                resetbutton.setDisable(false);
            }
    
    
    }
    public void PositionReset(){
        blueSnowmobile.setX(50);
        greenSnowmobile.setX(50);
        purpleSnowmobile.setX(50);
        redSnowmobile.setX(50);
    }
    public static void main(String[] args) {
    launch(args);
    }    
}
