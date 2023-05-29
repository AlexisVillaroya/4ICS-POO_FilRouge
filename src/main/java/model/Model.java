package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controller.OutputModelData;
import nutsAndBolts.GameStatus;
import nutsAndBolts.PieceSquareColor;

/**
 * @author francoise.perrin
 *
 * Cette classe g�re les aspects m�tiers du jeu de dame
 * ind�pendamment de toute vue
 * 
 * Elle d�l�gue � son objet ModelImplementor 
 * le stockage des PieceModel dans une collection
 * 
 * Les pi�ces sont capables de se d�placer d'une case en diagonale 
 * si la case de destination est vide
 * ou de 2 cases en diagonale s'il existe une pi�ce
 * du jeu oppos� � prendre sur le trajet
 * Ces tests sont d�l�gu�s aux PieceModel
 * 
 * Les pi�ces sont des pions ou des dames
 * les rafles ne sont pas g�r�es
 * 
 * N'est pas g�r� le fait que lorsqu'une prise est possible
 * une autre pi�ce ne doit pas �tre jou�e
 * 
 */
public class Model implements BoardGame<Coord> {

	private PieceSquareColor currentGamerColor;	// couleur du joueur courant
	private int whitePlayerScore; // score du joueur blanc
	private int blackPlayerScore; // score du joueur noir

	private ModelImplementor implementor;		// Cet objet sait communiquer avec les PieceModel

	public Model() {
		super();
		this.implementor = new ModelImplementor();
		this.currentGamerColor = ModelConfig.BEGIN_COLOR;
		this.whitePlayerScore = 0; // initialise le score du joueur blanc à 0
		this.blackPlayerScore = 0; // initialise le score du joueur noir à 0

		System.out.println(this);
	}


	/**
	 * Actions potentielles sur le model : move, capture, promotion pion, rafles
	 */
	@Override
	public OutputModelData<Coord> moveCapturePromote(Coord toMovePieceCoord, Coord targetSquareCoord) {

		OutputModelData<Coord> outputModelData = null;
		boolean isMoveDone = false;
		Coord toCapturePieceCoord = null;
		Coord toPromotePieceCoord = null;
		PieceSquareColor toPromotePieceColor = null;

		// Si la pi�ce est d�pla�able (couleur du joueur courant et case arriv�e disponible)
		if (this.isPieceMoveable(toMovePieceCoord, targetSquareCoord)) {

			// S'il n'existe pas plusieurs pi�ces sur le chemin
			if (this.isThereMaxOnePieceOnItinerary(toMovePieceCoord, targetSquareCoord)) {

				//Recherche coord de l'�ventuelle pi�ce � prendre
				toCapturePieceCoord = this.getToCapturePieceCoord(toMovePieceCoord, targetSquareCoord);

				// si le d�placement est l�gal (en diagonale selon algo pion ou dame)
				boolean isPieceToCapture = toCapturePieceCoord != null;
				if (this.isMovePiecePossible(toMovePieceCoord, targetSquareCoord, isPieceToCapture)) {

					// d�placement effectif de la pi�ce
					this.movePiece(toMovePieceCoord, targetSquareCoord);
					isMoveDone = true;

					// suppression effective de la pi�ce prise 
					this.remove(toCapturePieceCoord);

					// promotion �ventuelle de la pi�ce apr�s d�placement 
					if (this.isPiecePromotable(targetSquareCoord)) {
						this.promotePiece(targetSquareCoord);
						toPromotePieceCoord = targetSquareCoord;
						toPromotePieceColor = this.currentGamerColor;
					}
					
					// S'il n'y a pas eu de prise
					// ou si une rafle n'est pas possible alors changement de joueur 
					System.out.println("RAFLE POSSIBLE : " + isRaflePossible(targetSquareCoord));
			        System.out.println("CURRENT PLAYER : " + this.currentGamerColor);
			        System.out.println("BEST RAFLE ROUTE : " + this.bestRafleRoute(targetSquareCoord));
		            if (!isRaflePossible(targetSquareCoord) || toCapturePieceCoord == null) {
		                this.switchGamer();
		            }
				}
			}
		}
		System.out.println(this);

		// Constitution objet de donn�es avec toutes les infos n�cessaires � la view
		outputModelData = new OutputModelData<Coord>(
				isMoveDone, 
				toCapturePieceCoord, 
				toPromotePieceCoord, 
				toPromotePieceColor,
				getCurrentPlayerColor(),
				getWhitePlayerScore(),
				getBlackPlayerScore(),
				checkGameStatus());

		return outputModelData;

	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return true si la PieceModel � d�placer est de la couleur du joueur courant 
	 * et que les coordonn�es d'arriv�es soient dans les limites du tableau
	 * et qu'il n'y ait pas de pi�ce sur la case d'arriv�e
	 */
	private boolean isPieceMoveable(Coord toMovePieceCoord, Coord targetSquareCoord) {
		boolean bool = false;
		

		bool = 	this.implementor.isPiecehere(toMovePieceCoord) 
				&& this.implementor.getPieceColor(toMovePieceCoord) == this.currentGamerColor 
				&& Coord.coordonnees_valides(targetSquareCoord) 
				&& !this.implementor.isPiecehere(targetSquareCoord) ;

		return bool ;
	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return true s'il n'existe qu'1 seule pi�ce � prendre d'une autre couleur sur la trajectoire
	 * ou pas de pi�ce � prendre
	 */
	private boolean isThereMaxOnePieceOnItinerary(Coord toMovePieceCoord, Coord targetSquareCoord) {
		boolean isThereMaxOnePieceOnItinerary = false;

		List<Coord> coordsOnItinerary = this.implementor.getCoordsOnItinerary(toMovePieceCoord, targetSquareCoord);

		if (coordsOnItinerary != null) { 

			int count = 0;
			Coord potentialToCapturePieceCoord = null;
			for (Coord coordOnItinerary : coordsOnItinerary) {
				if (this.implementor.isPiecehere(coordOnItinerary)) {
					count++;
					potentialToCapturePieceCoord = coordOnItinerary;
				}
			}
			// Il n'existe qu'1 seule pi�ce � prendre d'une autre couleur sur la trajectoire
			if (count == 0 
					|| (count == 1 && this.currentGamerColor != 
					this.implementor.getPieceColor(potentialToCapturePieceCoord))) {
				isThereMaxOnePieceOnItinerary = true;
			}
		}

		return isThereMaxOnePieceOnItinerary;


	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * @return les coord de la pi�ce � prendre, null sinon
	 */
	private Coord getToCapturePieceCoord(Coord toMovePieceCoord, Coord targetSquareCoord) {
		Coord toCapturePieceCoord = null;
		List<Coord> coordsOnItinerary = this.implementor.getCoordsOnItinerary(toMovePieceCoord, targetSquareCoord);

		if (coordsOnItinerary != null) { 

			int count = 0;
			Coord potentialToCapturePieceCoord = null;
			for (Coord coordOnItinerary : coordsOnItinerary) {
				if (this.implementor.isPiecehere(coordOnItinerary)) {
					count++;
					potentialToCapturePieceCoord = coordOnItinerary;
				}
			}
			// Il n'existe qu'1 seule pi�ce � prendre d'une autre couleur sur la trajectoire
			if (count == 0 
					|| (count == 1 && this.currentGamerColor != 
					this.implementor.getPieceColor(potentialToCapturePieceCoord))) {
				toCapturePieceCoord = potentialToCapturePieceCoord;
			}
		}

		return toCapturePieceCoord;
	}

	/**
	 * @param initCoord
	 * @param targetCoord
	 * @param isPieceToCapture
	 * @return true si le d�placement est l�gal
	 * (s'effectue en diagonale, avec ou sans prise)
	 * La PieceModel qui se trouve aux coordonn�es pass�es en param�tre 
	 * est capable de r�pondre �cette question (par l'interm�diare du ModelImplementor)
	 */
	private boolean isMovePiecePossible(Coord toMovePieceCoord, Coord targetSquareCoord, boolean isPieceToCapture) {
		return this.implementor.isMovePieceOk(toMovePieceCoord, targetSquareCoord, isPieceToCapture ) ;
	}

	/**
	 * @param toMovePieceCoord
	 * @param targetSquareCoord
	 * D�placement effectif de la PieceModel
	 */
	private void movePiece(Coord toMovePieceCoord, Coord targetSquareCoord) {
		this.implementor.movePiece(toMovePieceCoord, targetSquareCoord);
	}

	/**
	 * @param toCapturePieceCoord
	 * Suppression effective de la pi�ce captur�e
	 */
	private void remove(Coord toCapturePieceCoord) {
		if (toCapturePieceCoord != null) {
			// si la couleur de la pièce capturée est blanche, alors le joueur noir marque un point
			if (this.implementor.getPieceColor(toCapturePieceCoord) == PieceSquareColor.WHITE) {
				this.blackPlayerScore += 1;
			}
			// sinon, le joueur blanc marque un point
			else {
				this.whitePlayerScore += 1;
			}
		}
		this.implementor.removePiece(toCapturePieceCoord);
	}

	/**
	 * @param targetSquareCoord
	 * @return true si le pion apr�s d�placement peut �tre promue en dame
	 */
	private boolean isPiecePromotable(Coord targetSquareCoord) {
		return this.implementor.isPiecePromotable(targetSquareCoord);
	}

	/**
	 * @param targetSquareCoord
	 * promotion effective du pion en dame 
	 */
	private void promotePiece(Coord targetSquareCoord) {
		this.implementor.promotePiece(targetSquareCoord);
	}
	
	private PieceSquareColor getCurrentPlayerColor() {
		return this.currentGamerColor;
	}
	
	public int getWhitePlayerScore() {
		return this.whitePlayerScore;
	}

	public int getBlackPlayerScore() {
		return this.blackPlayerScore;
	}

	private void switchGamer() {
            this.currentGamerColor = (PieceSquareColor.WHITE).equals(this.currentGamerColor) ?
                    PieceSquareColor.BLACK : PieceSquareColor.WHITE;
			System.out.println("SWITCH GAMER TO : " + this.currentGamerColor);

    }
	
	/**
	 * Vérifie si une rafle (saut multiple) est possible pour une pièce à une coordonnée donnée.
	 *
	 * @param pieceCoord La coordonnée de la pièce à vérifier.
	 * @return true si une rafle est possible, false sinon.
	 */
	private boolean isRaflePossible(Coord pieceCoord) {
	    // Si la pièce aux coordonnées spécifiées n'est pas de la couleur du joueur actuel, retourne false.
	    if (this.implementor.getPieceColor(pieceCoord) != this.currentGamerColor) {
	        return false;
	    }

	    // Obtient les coordonnées cibles pour une rafle (un saut de deux cases dans n'importe quelle direction).
	    List<Coord> targetCoords = this.implementor.getTargetCoordsInMultiJumpCase(pieceCoord);
	    System.out.println(targetCoords);

	    // Pour chaque coordonnée cible, vérifie s'il est possible d'effectuer une rafle.
	    for (Coord targetCoord : targetCoords) {
	        Coord captureCoord = this.getToCapturePieceCoord(pieceCoord, targetCoord);

	        // Vérifie si le déplacement vers la coordonnée cible est possible et s'il y a une pièce de couleur opposée sur la case à sauter.
	        if (this.isPieceMoveable(pieceCoord, targetCoord) &&
	            this.isThereMaxOnePieceOnItinerary(pieceCoord, targetCoord) &&
	            this.isMovePiecePossible(pieceCoord, targetCoord, true) &&
	            captureCoord != null &&
	            this.implementor.getPieceColor(captureCoord) != this.currentGamerColor) {
	            return true;
	        }
	    }

	    // Si aucune des mouvements possibles ne résulte en une rafle, retourne false.
	    return false;
	}
	
	private List<Coord> bestRafleRoute(Coord startCoord) {
	    List<Coord> bestRoute = new ArrayList<>();
	    findBestRoute(startCoord, new ArrayList<>(), bestRoute);
	    return bestRoute;
	}

	private void findBestRoute(Coord current, List<Coord> currentRoute, List<Coord> bestRoute) {
	    currentRoute.add(current);

	    // Si la route actuelle est meilleure, la mettre à jour.
	    if (currentRoute.size() > bestRoute.size()) {
	        bestRoute.clear();
	        bestRoute.addAll(currentRoute);
	    }

	    // Parcourir tous les mouvements possibles à partir de la position actuelle.
	    List<Coord> nextMoves = getPotentialCaptureCoord(current); // Une méthode qui renvoie tous les mouvements valides qui capturent une pièce ennemie.
	    for (Coord next : nextMoves) {
	        if (!currentRoute.contains(next)) {
	            findBestRoute(next, new ArrayList<>(currentRoute), bestRoute);
	        }
	    }
	}

	private List<Coord> getPotentialCaptureCoord(Coord start) {
	    List<Coord> potentialCaptures = new ArrayList<>();
	    for (int x = -2; x <= 2; x += 2) {
	        for (int y = -2; y <= 2; y += 2) {
	            Coord next = new Coord((char) (start.getColonne() + x), start.getLigne() + y);
	            if (isRaflePossible(next)) {
	                potentialCaptures.add(next);
	            }
	        }
	    }
	    return potentialCaptures;
	}
	
	/**
	 * Vérifie l'état du jeu
	 *
	 * @return retourne l'état du jeu
	 */
	public GameStatus checkGameStatus() {
	    if (this.implementor.hasNoPieces(PieceSquareColor.WHITE)) {
	        return GameStatus.BLACK_WIN;
	    } else if (this.implementor.hasNoPieces(PieceSquareColor.BLACK)) {
	        return GameStatus.WHITE_WIN;
	    } 
	    return GameStatus.ONGOING;
	}

}