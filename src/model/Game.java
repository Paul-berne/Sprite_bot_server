package model;

import java.util.ArrayList;

public class Game {

    private int id_game;
    private String type_game;
    private ArrayList<Question> lesQuestions;
    private ArrayList<Player> lesPlayers;
    private ArrayList<Score> lesScores;
    private String Statut;
    
	public Game(int id_game, String type_game, ArrayList<Question> lesQuestions, ArrayList<Player> lesPlayers,
			ArrayList<Score> lesScores, String statut) {
		super();
		this.id_game = id_game;
		this.type_game = type_game;
		this.lesQuestions = lesQuestions;
		this.lesPlayers = lesPlayers;
		this.lesScores = lesScores;
		Statut = statut;
	}

	public ArrayList<Player> getLesPlayers() {
		return lesPlayers;
	}

	public String getStatut() {
		return Statut;
	}

	public int getId_game() {
		return id_game;
	}

	public String getType_game() {
		return type_game;
	}

	public ArrayList<Question> getLesQuestions() {
		return lesQuestions;
	}

	public void setId_game(int id_game) {
		this.id_game = id_game;
	}

	public void setType_game(String type_game) {
		this.type_game = type_game;
	}

	public void setLesQuestions(ArrayList<Question> lesQuestions) {
		this.lesQuestions = lesQuestions;
	}

	public void setLesPlayers(ArrayList<Player> lesPlayers) {
		this.lesPlayers = lesPlayers;
	}

	public void setStatut(String statut) {
		Statut = statut;
	}

	public ArrayList<Score> getLesScores() {
		return lesScores;
	}

	public void setLesScores(ArrayList<Score> lesScores) {
		this.lesScores = lesScores;
	}
    
	
    
}
