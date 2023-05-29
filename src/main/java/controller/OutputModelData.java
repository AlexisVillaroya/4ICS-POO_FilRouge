package controller;

import java.io.Serializable;

import nutsAndBolts.GameStatus;
import nutsAndBolts.PieceSquareColor;

public class OutputModelData<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public boolean isMoveDone = false;
	public T capturedPieceCoord = null;
	public T promotedPieceCoord = null;
	public PieceSquareColor promotedPieceColor = null;
	public PieceSquareColor currentPlayerColor = null;  
	public int whiteScore = 0;
	public int blackScore = 0;
	public GameStatus gameStatus = null;
	
	public OutputModelData(
			boolean isMoveDone, 
			T capturedPieceCoord,
			T promotedPieceCoord,
			PieceSquareColor promotedPieceColor,
			PieceSquareColor currentPlayerColor,
			int whiteScore,
			int blackScore,
			GameStatus gameStatus) {  
		super();
		this.isMoveDone = isMoveDone;
		this.capturedPieceCoord = capturedPieceCoord;
		this.promotedPieceCoord = promotedPieceCoord;
		this.promotedPieceColor = promotedPieceColor;
		this.currentPlayerColor = currentPlayerColor;
		this.whiteScore = whiteScore;
		this.blackScore = blackScore;
		this.gameStatus = gameStatus;
	}

	@Override
	public String toString() {
		return "DataAfterMove [isMoveDone=" + isMoveDone + ", capturedPieceIndex=" + capturedPieceCoord
				+ ", promotedPieceIndex=" + promotedPieceCoord + ", promotedPieceColor=" + promotedPieceColor
				+ ", currentPlayerColor=" + currentPlayerColor + "]";  
	}
}
