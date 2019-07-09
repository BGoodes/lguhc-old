package fr.aiidor.game;

public enum UHCState {
	
	WAIT(true), STARTING(true), PREGAME(false) ,GAME(false), GAMEPVP(false), FINISH(false);
	
	private boolean canJoin;
	private static UHCState currentState;
	
	UHCState(boolean canJoin) {
		
		this.canJoin = canJoin;
	}
	
	
	
	//Les joueurs peuvent rejoindre ?
	public boolean canJoin() {
		return canJoin;
	}
	
	//Set STATE
	public static void setState(UHCState state) {
		
		UHCState.currentState = state;
	}
	
	//On vérifie si le jeu est lancé
	public static boolean isState(UHCState state) {
		
		return UHCState.currentState == state;
	}
	
	//On récupe le statut actuel du jeu
	public static UHCState getState() {
		return currentState;
	}
}
