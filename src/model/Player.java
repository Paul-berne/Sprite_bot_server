package model;

public class Player {

	private String pseudo;
	private String password;
	private String nomclassement;
	private String action;
	private int connectionID;

	public Player() {
		
	}
	/**
	public Player(String pseudo, String password, String nomclassement) {
		super();
		this.pseudo = pseudo;
		this.password = password;
		this.nomclassement = nomclassement;
	}
	**/
	
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNomclassement() {
		return nomclassement;
	}
	public void setNomclassement(String nomclassement) {
		this.nomclassement = nomclassement;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getConnectionID() {
		return connectionID;
	}
	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}
	
	
}
