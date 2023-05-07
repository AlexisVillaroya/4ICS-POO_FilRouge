package model;

import controller.OutputModelData;

/**
 * @author francoise.perrin
 *
 * Cette interface d�finit le comportement attendu des jeux de plateau
 * @param <T>
 * 
 * 
 */
public interface BoardGame<T>  {

	/**
	 * @param toMovePieceIndex
	 * @param targetSquareIndex
	 * @return 1 objet complexe
	 * 		- true si le d�placement a �t� effectu�, false sinon
	 * 		- �ventuellement les coordonn�es de la pi�ce captur�e, null sinon 
	 * 		- �ventuellement les coordonn�es et la couleur du pion promus en dame, null sinon
	 */
	public OutputModelData<T> moveCapturePromote(T toMovePieceIndex, T targetSquareIndex);
	
}