package nguyenhoa.app;

import java.io.IOException;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Pane root;
    public static final int WIDTH = 600, HEIGHT = 600, RADIUS = 10, STEP = 2, DELAY = 50;
    private Random random;
    private Snake snake;
    private Circle food;
    private Text scoreText;

    public static void main(String[] args) {
        launch();
    }

    // restart game
    private void restartGame() {
        Platform.runLater(() -> {
            scoreText.setText("Score: ");
            root.getChildren().clear();
            this.createSnake();
            this.createFood();
        });
    }

    // generate random value in range
    private int randomInRange(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    // generate the food
    private void createFood() {
        food = new Circle(randomInRange(RADIUS * 3, WIDTH - RADIUS * 3),
                randomInRange(RADIUS * 3, HEIGHT - RADIUS * 3), RADIUS);
        while (snake.isCollided(food)) {
            food = new Circle(randomInRange(RADIUS * 3, WIDTH - RADIUS * 3),
                    randomInRange(RADIUS * 3, HEIGHT - RADIUS * 3), RADIUS);
        }
        Platform.runLater(() -> {
            root.getChildren().add(food);
        });
    }

    // create snake object
    private void createSnake() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2, RADIUS);
        snake.setFill(Color.RED);
        root.getChildren().add(snake);
    }

    // handle keyboard input to change the direction of the snake
    private void handleKeyPresses(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case UP:
                if (snake.getDirection() != Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
                break;
            case DOWN:
                if (snake.getDirection() != Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
                break;
            case LEFT:
                if (snake.getDirection() != Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                }
                break;
            case RIGHT:
                if (snake.getDirection() != Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
                break;
            default:
                break; 
        }
    }

    // detect the hitting event of the snake and the food
    private boolean hit() {
        return snake.intersects(food.getBoundsInLocal());
    }

    @Override
    public void start(Stage stage) throws IOException {
        // scene = new Scene(loadFXML("primary"), 640, 480);
        random = new Random();
        VBox gameBox = new VBox();
        root = new Pane();
        scoreText = new Text("Score: ");
        scoreText.setLayoutX(20);
        scoreText.setLayoutY(25);
        scoreText.setStyle("-fx-font: 25 Arial;");
        gameBox.getChildren().addAll(scoreText, root);
        this.createSnake();
        this.createFood();

        Runnable moveSnake = () -> {
            try {
                while (true) {
                    if (hit()) {
                        snake.eat(food);
                        createFood();
                        Platform.runLater(() -> {
                            scoreText.setText("Score: " + snake.getLength());
                        });
                    }

                    snake.move();

                    if (snake.eatItSelf()) {
                        restartGame();
                    }
                    Thread.sleep(DELAY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        scene = new Scene(gameBox, WIDTH, HEIGHT);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> handleKeyPresses(event));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    stop();
                    System.exit(0);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.show();
        Thread t = new Thread(moveSnake);
        t.start();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}