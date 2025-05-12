package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.ProgressBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Random;

public class QuizView extends View {

    private int currentQuestion = 0;
    private int score = 0;
    private final String[][] questions = {
            // Question, Correct Answer, Wrong Answers...
            {"What is the capital of Lesotho?", "Maseru", "Johannesburg", "Gaborone", "Maputo"},
            {"Thaba-Bosiu was the stronghold of which king?", "Moshoeshoe I", "Letsie I", "Moshoeshoe II", "Letsie II"},
            {"Maletsunyane Falls is one of the highest waterfalls in what region?", "Southern Africa", "East Africa", "West Africa", "North Africa"},
            {"What is the traditional Basotho hat called?", "Mokorotlo", "Basotho hat", "Seshoeshoe", "Kobo"},
            {"Which mountain range covers much of Lesotho?", "Maloti Mountains", "Drakensberg", "Atlas Mountains", "Kilimanjaro"},
            {"What is the official language of Lesotho?", "Sesotho", "English", "Zulu", "Afrikaans"},
            {"Lesotho is completely surrounded by which country?", "South Africa", "Zimbabwe", "Botswana", "Eswatini"},
            {"What is the traditional Basotho blanket called?", "Seanamarena", "Kobo", "Kaross", "Blankie"},
            {"Which famous dam is located in Lesotho?", "Katse Dam", "Gariep Dam", "Vaal Dam", "Sterkfontein Dam"},
            {"What is the currency of Lesotho?", "Loti", "Rand", "Pula", "Emalangeni"},
            {"Lesotho gained independence from Britain in which year?", "1966", "1956", "1976", "1986"},
            {"What is the main economic activity in Lesotho?", "Textile manufacturing", "Oil production", "Tourism", "Mining"},
            {"Which national park in Lesotho is known for dinosaur footprints?", "Ts'ehlanyane", "Sehlabathebe", "Bokong", "Teyateyaneng"},
            {"What is the traditional Basotho dwelling called?", "Rondavel", "Hut", "Kraal", "Lapa"},
            {"Which famous pass connects Lesotho to South Africa?", "Sani Pass", "Chapman's Peak", "Bainskloof", "Van Reenen's"}
    };

    public QuizView() {
        // Create UI elements
        Label questionLabel = new Label();
        questionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        questionLabel.setTextFill(Color.web("#2c3e50"));
        questionLabel.setWrapText(true);
        questionLabel.setAlignment(Pos.CENTER);
        questionLabel.setMaxWidth(Double.MAX_VALUE);

        Button[] answerButtons = new Button[4];
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new Button();
            answerButtons[i].setMaxWidth(Double.MAX_VALUE);
            answerButtons[i].setFont(Font.font("Arial", 14));
            answerButtons[i].setStyle("-fx-background-radius: 10;");
            final int index = i;
            answerButtons[i].setOnAction(e -> checkAnswer(index));
        }

        ProgressBar progress = new ProgressBar();
        progress.setStyle("-fx-accent: #3498db;");

        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        scoreLabel.setTextFill(Color.web("#3498db"));

        // Create layout
        VBox layout = new VBox(15, questionLabel, answerButtons[0], answerButtons[1],
                answerButtons[2], answerButtons[3], progress, scoreLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");

        // Make buttons expand to full width
        for (Button btn : answerButtons) {
            HBox.setHgrow(btn, Priority.ALWAYS);
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        // Set up the view
        setCenter(layout);
        showQuestion(0);
    }

    private void showQuestion(int index) {
        if (index < questions.length) {
            currentQuestion = index;
            ((Label) ((VBox) getCenter()).getChildren().get(0)).setText(questions[index][0]);

            // Shuffle answers for current question
            String[] answers = new String[4];
            System.arraycopy(questions[index], 1, answers, 0, 4);
            shuffleArray(answers);

            for (int i = 0; i < 4; i++) {
                Button btn = (Button) ((VBox) getCenter()).getChildren().get(i + 1);
                btn.setText(answers[i]);
                btn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50;");
            }

            ((ProgressBar) ((VBox) getCenter()).getChildren().get(5)).setProgress((double) (index + 1) / questions.length);
        } else {
            // Quiz completed
            VBox resultsBox = new VBox(20);
            resultsBox.setAlignment(Pos.CENTER);
            resultsBox.setPadding(new Insets(20));

            Label resultLabel = new Label("Quiz Completed!");
            resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            resultLabel.setTextFill(Color.web("#2c3e50"));

            Label scoreLabel = new Label("Final Score: " + score + "/" + questions.length);
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            scoreLabel.setTextFill(Color.web("#3498db"));

            String message;
            if (score == questions.length) {
                message = "Perfect! You know Lesotho very well!";
            } else if (score >= questions.length * 0.7) {
                message = "Great job! You have good knowledge of Lesotho!";
            } else if (score >= questions.length * 0.4) {
                message = "Not bad! Keep learning about Lesotho!";
            } else {
                message = "Keep exploring Lesotho to learn more!";
            }

            Label messageLabel = new Label(message);
            messageLabel.setFont(Font.font("Arial", 16));
            messageLabel.setTextFill(Color.web("#7f8c8d"));
            messageLabel.setWrapText(true);
            messageLabel.setAlignment(Pos.CENTER);
            messageLabel.setMaxWidth(300);

            Button restartButton = new Button("Try Again");
            restartButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
            restartButton.setOnAction(e -> {
                currentQuestion = 0;
                score = 0;
                showQuestion(0);
            });

            resultsBox.getChildren().addAll(resultLabel, scoreLabel, messageLabel, restartButton);
            ((VBox) getCenter()).getChildren().setAll(resultsBox);
        }
    }

    private void checkAnswer(int selectedIndex) {
        Button selectedButton = (Button) ((VBox) getCenter()).getChildren().get(selectedIndex + 1);
        String selectedAnswer = selectedButton.getText();

        // Check if selected answer matches the first option (correct answer)
        if (selectedAnswer.equals(questions[currentQuestion][1])) {
            score++;
            selectedButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
            ((Label) ((VBox) getCenter()).getChildren().get(6)).setText("Score: " + score);
        } else {
            selectedButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

            // Highlight correct answer
            for (int i = 1; i < 5; i++) {
                Button btn = (Button) ((VBox) getCenter()).getChildren().get(i);
                if (btn.getText().equals(questions[currentQuestion][1])) {
                    btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                    break;
                }
            }
        }

        // Delay next question to show feedback
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> showQuestion(currentQuestion + 1));
        pause.play();
    }

    // Helper method to shuffle array
    private void shuffleArray(String[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Lesotho Quiz");
        appBar.getActionItems().add(MaterialDesignIcon.HOME.button(e ->
                getAppManager().goHome()
        ));
    }
}