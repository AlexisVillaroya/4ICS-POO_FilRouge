package gui;

import controller.InputViewData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import nutsAndBolts.GameStatus;
import nutsAndBolts.PieceSquareColor;

public class View extends BorderPane {

    private Pane board;
    private Label player1ScoreLabel;
    private Label player2ScoreLabel;
    private Button resetButton;
    private Label currentPlayerLabel;
    private Label gameStatusLabel;
    private VBox controlBox;

    private EventHandler<MouseEvent> clickListener;

    public View(EventHandler<MouseEvent> clickListener) {
        super();
        this.clickListener = clickListener;

        initializeBoard();
        initializeLabels();
        initializeResetButton();
        initializeControlBox();

        setCenter(board);
        setTop(controlBox);

        // Appliquer un style CSS à la vue
        getStyleClass().add("view");
    }

    private void initializeBoard() {
        board = new Board(clickListener);
        board.prefWidthProperty().bind(widthProperty());
        board.prefHeightProperty().bind(heightProperty());
        board.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 2px;");
    }

    private void initializeLabels() {
        currentPlayerLabel = createLabel("Tour du Joueur: Blanc");
        gameStatusLabel = createLabel("");
        player1ScoreLabel = createLabel("Score Joueur 1: 0");
        player2ScoreLabel = createLabel("Score Joueur 2: 0");
    }

    private void initializeResetButton() {
        resetButton = new Button("Recommencer");
        resetButton.setOnAction(resetHandler);
        resetButton.getStyleClass().add("reset-button");
        resetButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 24px; -fx-border-radius: 12px;");
        resetButton.setOnMouseEntered(e -> resetButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 24px; -fx-border-radius: 12px;"));
        resetButton.setOnMouseExited(e -> resetButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 24px; -fx-border-radius: 12px;"));
    }

    private void initializeControlBox() {
        VBox scoresBox = new VBox(10, player1ScoreLabel, player2ScoreLabel);
        scoresBox.setAlignment(Pos.CENTER);

        controlBox = new VBox(10, scoresBox, currentPlayerLabel, gameStatusLabel, resetButton);
        controlBox.setPadding(new Insets(10));
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #000000; -fx-border-width: 2px;");

        setTop(controlBox);
    }


    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.BLACK);
        return label;
    }

    // Méthode invoquée depuis le Controller pour propager les déplacements effectués sur le model sur la vue
    public void actionOnGui(InputViewData<Integer> dataToRefreshView) {
        ((Board) this.board).actionOnGui(dataToRefreshView);

        // Mise à jour de la couleur du joueur courant
        String color = dataToRefreshView.currentPlayerColor == PieceSquareColor.WHITE ? "Blanc" : "Noir";
        updateCurrentPlayer(color);

        // Mise à jour des scores des joueurs
        updateScores(dataToRefreshView.whiteScore, dataToRefreshView.blackScore);

        // Mise à jour de l'état de la partie
        updateGameStatus(dataToRefreshView.gameStatus);
    }

    public void updateScores(int player1Score, int player2Score) {
        player1ScoreLabel.setText("Score joueur Blanc : " + player1Score);
        player2ScoreLabel.setText("Score joueur Noir : " + player2Score);
    }

    public void updateCurrentPlayer(String color) {
        currentPlayerLabel.setText("Tour du Joueur: " + color);
    }

    public void updateGameStatus(GameStatus gameStatus) {
        switch (gameStatus) {
            case WHITE_WIN:
                gameStatusLabel.setText("Le joueur Blanc a gagné !");
                break;
            case BLACK_WIN:
                gameStatusLabel.setText("Le joueur Noir a gagné !");
                break;
            case DRAW:
                gameStatusLabel.setText("Match nul !");
                break;
            case ONGOING:
                gameStatusLabel.setText("Le jeu continue...");
                break;
        }
    }

    public void reset() {
        // Réinitialise les scores
        int player1Score = 0;
        int player2Score = 0;
        player1ScoreLabel.setText("Score joueur Blanc : " + player1Score);
        player2ScoreLabel.setText("Score joueur Noir : " + player2Score);
        ((Board) this.board).resetGame(clickListener);
    }

    EventHandler<ActionEvent> resetHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            // code pour réinitialiser le jeu
            reset();
        }
    };
}
