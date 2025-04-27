import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

public class UltimateCreativeBot1 extends Application {
    private Label timeLabel = new Label();
    private TextArea outputArea = new TextArea();
    private TextField inputField = new TextField();
    private Button poemButton = new Button("Get Poem");
    private Button musicButton = new Button("Get Music");
    private Button reminderButton = new Button("Set Reminder");
    private final String[] RANDOM_POEMS = {
        "The moonlight shines so bright,\nA silver glow in the night.\nDreams take flight, hearts ignite,\nUnder the stars, a peaceful sight.",
        "The wind whispers through the trees,\nA gentle song upon the breeze.\nNature speaks in hushed tones,\nSoftly humming through the stones."
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the main layout
        VBox root = new VBox(20);
        root.setPadding(new javafx.geometry.Insets(15));
        
        // Create UI components
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefHeight(300);
        
        ScrollPane scrollPane = new ScrollPane(outputArea);
        scrollPane.setFitToWidth(true);
        
        HBox buttonBox = new HBox(10, poemButton, musicButton, reminderButton);
        
        // Set up event handlers
        poemButton.setOnAction(e -> handlePoemRequest());
        musicButton.setOnAction(e -> handleMusicRequest());
        reminderButton.setOnAction(e -> handleReminderRequest());
        inputField.setOnAction(e -> handleCustomCommand());
        
        // Add components to root
        root.getChildren().addAll(timeLabel, scrollPane, inputField, buttonBox);
        
        // Start time updater
        updateTime();
        
        // Initial greeting
        outputArea.appendText("Welcome to the Ultimate Creative Bot!\n");
        outputArea.appendText("I can generate poems, compose music, and remind you of things.\n");
        outputArea.appendText("Type a command or use the buttons above.\n\n");
        
        // Set up the scene
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Ultimate Creative Bot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateTime() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    timeLabel.setText("Current Time: " + formattedTime);
                });
            }
        }, 0, 1000);
    }

    private void handlePoemRequest() {
        String theme = inputField.getText().trim();
        if (theme.isEmpty()) {
            theme = "random";
        }
        
        String poem;
        if (theme.equalsIgnoreCase("random")) {
            Random rand = new Random();
            poem = RANDOM_POEMS[rand.nextInt(RANDOM_POEMS.length)];
        } else {
            poem = generatePoem(theme);
        }
        
        outputArea.appendText("\n=== Poem ===\n");
        outputArea.appendText(poem + "\n\n");
        inputField.clear();
    }

    private void handleMusicRequest() {
        String mood = inputField.getText().trim();
        if (mood.isEmpty()) {
            mood = "random";
        }
        
        String melody = generateMelody(mood);
        outputArea.appendText("\n=== Music ===\n");
        outputArea.appendText("Playing " + mood + " melody: " + melody + "\n");
        
        try {
            Player player = new Player();
            Pattern pattern = new Pattern(melody);
            player.play(pattern);
        } catch (Exception e) {
            outputArea.appendText("Couldn't play the melody. Pattern: " + melody + "\n");
        }
        
        inputField.clear();
    }

    private void handleReminderRequest() {
        String reminderText = inputField.getText().trim();
        if (reminderText.isEmpty()) {
            outputArea.appendText("Please enter a reminder message first.\n");
            return;
        }
        
        outputArea.appendText("\nReminder set: \"" + reminderText + "\" in 10 seconds\n");
        scheduleReminder(reminderText, 10);
        inputField.clear();
    }

    private void handleCustomCommand() {
        String input = inputField.getText().trim().toLowerCase();
        
        switch (input) {
            case "poem":
                handlePoemRequest();
                break;
            case "music":
                handleMusicRequest();
                break;
            case "reminder":
                handleReminderRequest();
                break;
            case "clear":
                outputArea.clear();
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                if (input.startsWith("poem ")) {
                    inputField.setText(input.substring(5));
                    handlePoemRequest();
                } else if (input.startsWith("music ")) {
                    inputField.setText(input.substring(6));
                    handleMusicRequest();
                } else if (input.startsWith("reminder ")) {
                    inputField.setText(input.substring(9));
                    handleReminderRequest();
                } else {
                    outputArea.appendText("Unknown command. Try 'poem', 'music', or 'reminder'.\n");
                }
                break;
        }
    }

    private void scheduleReminder(String message, int seconds) {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    outputArea.appendText("\n=== REMINDER ===\n" + message + "\n\n");
                });
            }
        }, seconds * 1000);
    }

    public String generatePoem(String theme) {
        switch (theme.toLowerCase()) {
            case "love":
                return "Love is a flame, eternal and bright,\nIt warms the soul, it feels so right.\nIn every heart, it finds its place,\nA gentle touch, a warm embrace.";
            case "nature":
                return "The trees whisper secrets in the breeze,\nThe rivers flow with graceful ease.\nMountains stand tall, clouds drift by,\nNature's beauty will never die.";
            case "happiness":
                return "A smile, a laugh, a joyful cheer,\nHappiness is always near.\nIt lights the path, it fills the air,\nA precious gift, beyond compare.";
            default:
                Random rand = new Random();
                return RANDOM_POEMS[rand.nextInt(RANDOM_POEMS.length)];
        }
    }
    
    public String generateMelody(String mood) {
        switch (mood.toLowerCase()) {
            case "happy":
                return "C D E G G F E D C";
            case "sad":
                return "A G F E D C B A";
            case "energetic":
                return "C E G C5 G E C5 G E";
            default:
                Random rand = new Random();
                String[] defaultMelodies = {
                    "C D E F G A B C",
                    "G A B C5 D E F# G",
                    "A B C# D E F# G# A"
                };
                return defaultMelodies[rand.nextInt(defaultMelodies.length)];
        }
    }
}
