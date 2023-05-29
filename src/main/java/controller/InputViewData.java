package controller;

import java.io.Serializable;

import nutsAndBolts.GameStatus;
import nutsAndBolts.PieceSquareColor;

/**
 * Objet à destination de la View
 * créé par le Controller
 * à partir des données retournées par le Model
 */
public class InputViewData<T> implements Serializable{
    
    private static final long serialVersionUID = 1L;

    public T toMovePieceIndex = null;
    public T targetSquareIndex = null;
    public T capturedPieceIndex = null;
    public T promotedPieceIndex = null;
    public PieceSquareColor promotedPieceColor = null;
    public PieceSquareColor currentPlayerColor = null;
    public int whiteScore = 0;
    public int blackScore = 0;
    public GameStatus gameStatus = null;
    
    public InputViewData(
            T toMovePieceIndex, 
            T targetSquareIndex, 
            T capturedPieceIndex,
            T promotedPieceIndex,
            PieceSquareColor promotedPieceColor,
            PieceSquareColor currentPlayerColor,
            int whiteScore,
            int blackScore,
            GameStatus gameStatus) {
        super();
        this.toMovePieceIndex = toMovePieceIndex;
        this.targetSquareIndex = targetSquareIndex;
        this.capturedPieceIndex = capturedPieceIndex;
        this.promotedPieceIndex = promotedPieceIndex;
        this.promotedPieceColor = promotedPieceColor;
        this.currentPlayerColor = currentPlayerColor;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
        this.gameStatus = gameStatus;
    }

    @Override
    public String toString() {
        return "DataAfterMove [toMovePieceIndex=" + toMovePieceIndex
                + ", targetSquareIndex=" + targetSquareIndex + ", capturedPieceIndex=" + capturedPieceIndex
                + ", promotedPieceIndex=" + promotedPieceIndex + ", promotedPieceColor=" + promotedPieceColor 
                + ", currentPlayerColor=" + currentPlayerColor + ", whiteScore=" + whiteScore
                + ", blackScore=" + blackScore + "]";
    }
}
