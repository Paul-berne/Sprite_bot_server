package model;

public class Score implements Comparable<Score>{
	
	private int id_game;
	private String pseaudo;
	private String date_game;
	private int player_score;
	private String time_begin;
	private String time_end;
	
	
	public Score(int id_game, String pseaudo, String date_game, int player_score, String time_begin, String time_end) {
		super();
		this.id_game = id_game;
		this.pseaudo = pseaudo;
		this.date_game = date_game;
		this.player_score = player_score;
		this.time_begin = time_begin;
		this.time_end = time_end;
	}

	public Score() {
		
	}

	public int getId_game() {
		return id_game;
	}

	public String getPseaudo() {
		return pseaudo;
	}

	public String getDate_game() {
		return date_game;
	}

	public int getPlayer_score() {
		return player_score;
	}

	public String getTime_begin() {
		return time_begin;
	}

	public String getTime_end() {
		return time_end;
	}
	
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public void addPlayerScore(int Score) {
		player_score = player_score + Score;
	}
	
	@Override
    public int compareTo(Score otherScore) {
        // Compare les player_score
        return Integer.compare(this.player_score, otherScore.getPlayer_score());
    }
	
}



