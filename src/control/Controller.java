package control;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import DAO.DAOsqlAnswer;
import DAO.DAOsqlGame;
import DAO.DAOsqlQuestion;
import DAO.DataFileUser;
import model.Answer;
import model.Game;
import model.Player;
import model.Question;
import model.Score;

public class Controller {

	private Server server;
	private Kryo kryo;
	
	//Control
	private Configuration myConfiguration;
	
	//DAO
	private DataFileUser leDAOUser;
	private DAOsqlQuestion leStubQuestion;
	private DAOsqlAnswer leStubAnswer;
	private DAOsqlGame leStubGame;
	
	//model
	private Player lePlayer;
	private Game laGame;
	private ArrayList<Game> lesGames;
	private ArrayList<Player> lesPlayers;
	private ArrayList<Score> lesScores;

	public Controller() {
		this.myConfiguration = new Configuration();
		this.leDAOUser = new DataFileUser(this);
		this.leStubAnswer = new DAOsqlAnswer(this);
		this.leStubQuestion = new DAOsqlQuestion(this);
		this.leStubGame = new DAOsqlGame(this);
		this.laGame = new Game(0, null, null, null, null, null);
		this.lesGames = new ArrayList<Game>();
		
		try {
			server = new Server();
			server.start();
			server.bind(54555, 54777);

			kryo = server.getKryo();
			kryo.register(SampleRequest.class);
			kryo.register(String.class);
			kryo.register(Player.class);
			kryo.register(Game.class);
			kryo.register(ArrayList.class);
			kryo.register(Question.class);
			kryo.register(Answer.class);
			kryo.register(Integer.class);
			
			System.out.println("Connecté");
			server.addListener(new Listener() {
				
				public void received(Connection connection, Object object) {
					if (object instanceof Player) {
						lePlayer = (Player) object;
						switch (lePlayer.getAction()) {
						case "login":
							System.out.println(lePlayer.getPseudo());
							System.out.println(connection.getID());
							try {
								leDAOUser.VerifyUserExist(lePlayer.getPseudo(), lePlayer.getPassword());
								server.sendToTCP(connection.getID(), lePlayer);							
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						case "changepassword" :
							leDAOUser.ChangePasswordUser(lePlayer.getPseudo(), lePlayer.getPassword());
							System.out.println("on change le mdp");
							server.sendToTCP(connection.getID(), true);
							break;
						default:
							break;
						}
						
						/**
						SampleResponse response = new SampleResponse();
						response.text = "Salut, je suis le serveur ! " + unPlayer.getPseudo();
						server.sendToTCP(connection.getID(), response);
						
						
						SampleResponse broadcast = new SampleResponse();
						broadcast.text = "[Broadcast] " + unPlayer.getPseudo();
						server.sendToAllTCP(broadcast);
						**/
					}else if (object instanceof String) {
						String type_game = object.toString();
						laGame.setType_game(type_game);
						switch (type_game) {
						case "monoplayer":
							System.out.println("monoplayer");
							lePlayer.setConnectionID(connection.getID());
							lesPlayers.add(lePlayer);
							laGame.setStatut("en cours");
							server.sendToTCP(connection.getID(), laGame);
							CreateNewGame();
							break;
						default:
							break;
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		CreateNewGame();
	}
    
	private void CreateNewGame() {
		if (laGame.getType_game() != null) {
			//leStubGame.InsertGameBDD();
		}
		System.out.println("création partie");
		lesScores = new ArrayList<Score>();
		lesPlayers = new ArrayList<Player>();
		leStubQuestion.initializeQuestions();
		laGame = new Game(leStubGame.IdOfTheGame(), null, leStubQuestion.getLesQuestions(), lesPlayers, "en attente",);
		System.out.println(laGame.getId_game());
		lesGames.add(laGame);
	}
	
	public Game findGameById(int id) {
	    for (Game game : lesGames) {
	        if (game.getId_game() == id) {
	            return game;
	        }
	    }
	    return null;  
	}

	public static class SampleRequest {
		public String text;
	}

	public static class SampleResponse {
		public String text;
	}
	public Configuration getMyConfiguration() {
        return myConfiguration;
    }
	public Player getLePlayer() {
		return lePlayer;
	}
	public DAOsqlQuestion getLeStubQuestion() {
		return leStubQuestion;
	}
	public DAOsqlAnswer getLeStubAnswer() {
		return leStubAnswer;
	}

	public Game getLaGame() {
		return laGame;
	}
	
	
}