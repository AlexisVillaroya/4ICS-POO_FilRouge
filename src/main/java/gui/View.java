package gui;

import controller.InputViewData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nutsAndBolts.GameStatus;
import nutsAndBolts.PieceSquareColor;

public class View extends BorderPane {

    private Pane board;
    private Label player1ScoreLabel;
    private Label player2ScoreLabel;
    private Button resetButton;
    private Label currentPlayerLabel;
    private Label gameStatusLabel;

    private EventHandler<MouseEvent> clickListener;

    public View(EventHandler<MouseEvent> clickListener) {
        super();

        this.clickListener = clickListener;

        board = new Board(clickListener);

        board.prefWidthProperty().bind(this.widthProperty());
        board.prefHeightProperty().bind(this.heightProperty());

        currentPlayerLabel = new Label("Tour du Joueur: Blanc");
        gameStatusLabel = new Label("test");
        player1ScoreLabel = new Label("Score Joueur 1: 0");
        player2ScoreLabel = new Label("Score Joueur 2: 0");

        VBox scoresBox = new VBox(10, player1ScoreLabel, player2ScoreLabel);
        scoresBox.setAlignment(Pos.CENTER);

        resetButton = new Button("Recommencer");
        resetButton.setOnAction(resetHandler);
        resetButton.getStyleClass().add("reset-button");

        VBox controlBox = new VBox(10, scoresBox, currentPlayerLabel, gameStatusLabel, resetButton);
        controlBox.setPadding(new Insets(10));
        controlBox.setAlignment(Pos.CENTER);

        this.setCenter(board);
        this.setTop(controlBox);

        // Appliquer un style CSS à la vue
        this.getStyleClass().add("view");
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
