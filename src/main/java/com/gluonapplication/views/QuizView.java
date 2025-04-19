package com.gluonapplication.views;

import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.ProgressBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class QuizView extends View {

    private int currentQuestion = 0;
    private int score = 0;
    private final String[][] questions = {
            {"What is the capital of Lesotho?", "Maseru", "Johannesburg", "Gaborone", "Maputo"},
            {"Thaba-Bosiu was the stronghold of which king?", "Moshoeshoe I", "Letsie I", "Moshoeshoe II", "Letsie II"},
            {"Maletsunyane Falls is one of the highest waterfalls in what region?", "Southern Africa", "East Africa", "West Africa", "North Africa"}
    };

    public QuizView() {
        Label questionLabel = new Label();
        Button[] answerButtons = new Button[4];
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new Button();
            final int index = i;
            answerButtons[i].setOnAction(e -> checkAnswer(index));
        }

        ProgressBar progress = new ProgressBar();//0 was inside
        Label scoreLabel = new Label("Score: 0");

        VBox layout = new VBox(10, questionLabel, answerButtons[0], answerButtons[1],
                answerButtons[2], answerButtons[3], progress, scoreLabel);
        layout.setAlignment(Pos.CENTER);

        setCenter(layout);
        showQuestion(0);
    }

    private void showQuestion(int index) {
        if (index < questions.length) {
            currentQuestion = index;
            ((Label) ((VBox) getCenter()).getChildren().get(0)).setText(questions[index][0]);
            for (int i = 0; i < 4; i++) {
                ((Button) ((VBox) getCenter()).getChildren().get(i + 1)).setText(questions[index][i + 1]);
            }
            ((ProgressBar) ((VBox) getCenter()).getChildren().get(5)).setProgress((double) index / questions.length);
        } else {
            // Quiz completed
            ((VBox) getCenter()).getChildren().clear();
            ((VBox) getCenter()).getChildren().add(new Label("Quiz completed! Final score: " + score + "/" + questions.length));
        }
    }

    private void checkAnswer(int selectedIndex) {
        if (selectedIndex == 0) { // First answer is always correct in this simple example
            score++;
            ((Label) ((VBox) getCenter()).getChildren().get(6)).setText("Score: " + score);
        }
        showQuestion(currentQuestion + 1);
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> getAppManager().getDrawer().open()));
        appBar.setTitleText("Lesotho Quiz");
    }
}