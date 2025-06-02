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
    private final VBox quizLayout;
    private final Label questionLabel;
    private final Button[] answerButtons;
    private final ProgressBar progress;
    private final Label scoreLabel;

    private final String[][] questions = {
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
        questionLabel = createQuestionLabel();
        answerButtons = createAnswerButtons();
        progress = createProgressBar();
        scoreLabel = createScoreLabel();

        // Create layout
        quizLayout = createQuizLayout();

        // Set up the view
        setCenter(quizLayout);
        showQuestion(0);
    }

    private Label createQuestionLabel() {
        Label label = new Label();
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        label.setTextFill(Color.web("#2c3e50"));
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private Button[] createAnswerButtons() {
        Button[] buttons = new Button[4];
        for (int i = 0; i < 4; i++) {
            buttons[i] = new Button();
            buttons[i].setMaxWidth(Double.MAX_VALUE);
            buttons[i].setFont(Font.font("Arial", 14));
            buttons[i].setStyle("-fx-background-radius: 10; -fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50;");
            final int index = i;
            buttons[i].setOnAction(e -> checkAnswer(index));

            // Make buttons expand to full width
            HBox.setHgrow(buttons[i], Priority.ALWAYS);
        }
        return buttons;
    }

    private ProgressBar createProgressBar() {
        ProgressBar pb = new ProgressBar();
        pb.setStyle("-fx-accent: #3498db;");
        return pb;
    }

    private Label createScoreLabel() {
        Label label = new Label("Score: 0");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        label.setTextFill(Color.web("#3498db"));
        return label;
    }

    private VBox createQuizLayout() {
        VBox layout = new VBox(15, questionLabel, answerButtons[0], answerButtons[1],
                answerButtons[2], answerButtons[3], progress, scoreLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");
        return layout;
    }

    private void showQuestion(int index) {
        if (index < questions.length) {
            currentQuestion = index;
            questionLabel.setText(questions[index][0]);

            // Reset all buttons to default style
            for (Button btn : answerButtons) {
                btn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50;");
            }

            // Shuffle and set answers
            String[] answers = new String[4];
            System.arraycopy(questions[index], 1, answers, 0, 4);
            shuffleArray(answers);
            for (int i = 0; i < 4; i++) {
                answerButtons[i].setText(answers[i]);
            }

            progress.setProgress((double) (index + 1) / questions.length);
            scoreLabel.setText("Score: " + score);
        } else {
            showResults();
        }
    }

    private void showResults() {
        VBox resultsBox = new VBox(20);
        resultsBox.setAlignment(Pos.CENTER);
        resultsBox.setPadding(new Insets(20));

        Label resultLabel = new Label("Quiz Completed!");
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        resultLabel.setTextFill(Color.web("#2c3e50"));

        Label finalScoreLabel = new Label("Final Score: " + score + "/" + questions.length);
        finalScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        finalScoreLabel.setTextFill(Color.web("#3498db"));

        String message = getResultMessage();
        Button restartButton = new Button();
        restartButton.setStyle("-fx-opacity:0;");
        Label messageLabel = createMessageLabel(message);
        if(score!=questions.length)
        {
            restartButton = createRestartButton();
            restartButton.setStyle("-fx-opacity:1;");
        }


        resultsBox.getChildren().addAll(resultLabel, finalScoreLabel, messageLabel, restartButton);
        setCenter(resultsBox);
    }

    private String getResultMessage() {
        if (score == questions.length) {
            return "Perfect! You know Lesotho very well!";
        } else if (score >= questions.length * 0.7) {
            return "Great job! You have good knowledge of Lesotho!";
        } else if (score >= questions.length * 0.4) {
            return "Not bad! Keep learning about Lesotho!";
        } else
        {
            return "Keep exploring Lesotho to learn more!";
        }
    }

    private Label createMessageLabel(String message) {
        Label label = new Label(message);
        label.setFont(Font.font("Arial", 16));
        label.setTextFill(Color.web("#7f8c8d"));
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(300);
        return label;
    }

    private Button createRestartButton() {
        Button button = new Button("Try Again");
        button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setOnAction(e -> restartQuiz());
        return button;
    }

    private void restartQuiz() {
        currentQuestion = 0;
        score = 0;
        setCenter(quizLayout); // Reset to the quiz layout
        showQuestion(0); // Start from the first question
    }

    private void checkAnswer(int selectedIndex) {
        Button selectedButton = answerButtons[selectedIndex];
        String selectedAnswer = selectedButton.getText();

        if (selectedAnswer.equals(questions[currentQuestion][1])) {
            score++;
            selectedButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
            scoreLabel.setText("Score: " + score);
        } else {
            selectedButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            highlightCorrectAnswer();
        }

        // Delay next question to show feedback
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> showQuestion(currentQuestion + 1));
        pause.play();
    }

    private void highlightCorrectAnswer() {
        for (Button btn : answerButtons) {
            if (btn.getText().equals(questions[currentQuestion][1])) {
                btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                break;
            }
        }
    }

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