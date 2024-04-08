package model;

import java.time.LocalTime;
import java.util.Date;

public class Score {
	
	private int id_game;
	private String pseaudo;
	private Date date_game;
	private int player_score;
	private LocalTime time_begin;
	private LocalTime time_end;
	
	public Score(int id_game, String pseaudo, Date date_game, int player_score, LocalTime time_begin,
			LocalTime time_end) {
		super();
		this.id_game = id_game;
		this.pseaudo = pseaudo;
		this.date_game = date_game;
		this.player_score = player_score;
		this.time_begin = time_begin;
		this.time_end = time_end;
	}

	public int getId_game() {
		return id_game;
	}

	public String getPseaudo() {
		return pseaudo;
	}

	public Date getDate_game() {
		return date_game;
	}

	public int getPlayer_score() {
		return player_score;
	}

	public LocalTime getTime_begin() {
		return time_begin;
	}

	public LocalTime getTime_end() {
		return time_end;
	}
	
	
}
