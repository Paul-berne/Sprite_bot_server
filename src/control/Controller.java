package control;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
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

	// Control
	private Configuration myConfiguration;

	// DAO
	private DataFileUser leDAOUser;
	private DAOsqlQuestion leStubQuestion;
	private DAOsqlAnswer leStubAnswer;
	private DAOsqlGame leStubGame;

	// model
	private Player lePlayer;
	private Game laGame;
	private ArrayList<Game> lesGames;
	private ArrayList<Player> lesPlayers;
	private ArrayList<Score> lesScores;
	private ArrayList<Player> lesPlayersconnecte;

	public boolean Start_timer = true;
	private Timer swingTimer;
	
	private int id_game;

	private final int DEFAULT_TIME = 60;
	private int theTime = DEFAULT_TIME;

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
			kryo.register(Score.class);

			System.out.println("Connecté");

			server.addListener(new Listener() {
				public void received(Connection connection, Object object) {
					if (!(object instanceof KeepAlive))
						System.out.println("Objet reçu de type : " + object.getClass().getName());
					System.out.println("Objet reçu de type : " + object.getClass().getName());
					if (object instanceof Player) {
						lePlayer = (Player) object;
						switch (lePlayer.getAction()) {
						case "login":
							System.out.println(lePlayer.getPseudo() + connection.getID());
							try {
								leDAOUser.VerifyUserExist(lePlayer.getPseudo(), lePlayer.getPassword());
								if (lePlayer.getNomclassement() != null) {
									for (Player player : lesPlayersconnecte) {
										if (player.getPseudo().equals(lePlayer.getPseudo().toString())) {
											System.out.println("on change le nom du classement du player");
											lePlayer.setNomclassement("already connect");
										}
									}
									lePlayer.setConnectionID(connection.getID());
									lesPlayersconnecte.add(lePlayer);
								}
								server.sendToTCP(connection.getID(), lePlayer);
								
							} catch (ParseException e) {
								e.printStackTrace();
							}
							break;
						case "changepassword":
							leDAOUser.ChangePasswordUser(lePlayer.getPseudo(), lePlayer.getPassword());
							System.out.println("on change le mdp");
							server.sendToTCP(connection.getID(), lePlayer);
							break;
						case "disconnect":
							int index = 0;
							Boolean delete = false;
							for (Player player : lesPlayersconnecte) {
								if (player.getConnectionID() == connection.getID()) {
									System.out.println("supprime le player " + player.getPseudo());
									delete = true;
									break;
								}
								index++;
							}
							if (delete) {
								lesPlayersconnecte.remove(index);
							}
							break;
						case "delete from game":
							delete = false;
							index = 0;
							int index_game = 0;
							for (Game game : lesGames) {
								System.out.println(game.getStatut());
								
								if (game.getStatut().equals("en attente de joueurs")) {
									for (Player player : game.getLesPlayers()) {
										if (player.getConnectionID() == connection.getID()) {
											lesGames.get(index_game).getLesPlayers().remove(index);
											delete = true;
											break;
										}
										index++;
									}
								}
								if (delete) {
									if (game.getLesPlayers().size() == 0) {
										System.out.println("remove de la game");
										swingTimer.cancel();
										lesGames.remove(index_game);
										theTime = DEFAULT_TIME;
										CreateNewGame();
									}else {
										for (int i = 0; i < game.getLesPlayers().size(); i++) {
											server.sendToTCP(game.getLesPlayers().get(i).getConnectionID(), game.getLesPlayers());
										}
									}
									break;
								}
								
								index_game++;
							}
						default:
							break;
						}

						
					} else if (object instanceof String) {
						String type_game = object.toString();
						switch (type_game) {
						case "monoplayer":
							laGame.setType_game(type_game);
							int index = 0;
							for (Game game : lesGames) {
								if (game.getStatut().equals("en attente")) {
									System.out.println(game.getId_game()); 
									for (Player player : lesPlayersconnecte) {
										if (player.getConnectionID() == connection.getID()) {
											game.getLesPlayers().add(player);
											game.setStatut("en cours");
											server.sendToTCP(player.getConnectionID(), game);
											break;
										}
									}
									System.out.println("monoplayer");
									break;
								}
								index++;
							}
							id_game++;
							CreateNewGame();
							break;
						case "multiplayer":
							index = 0;
							for (Game game : lesGames) {
								System.out.println(game.getStatut() + game.getType_game());
								if (game.getStatut().equals("en attente de joueurs")) {
									for (Player player : lesPlayersconnecte) {
										if (player.getConnectionID() == connection.getID()) {
											game.getLesPlayers().add(player);
											server.sendToTCP(player.getConnectionID(), game);
											break;
										}
									}
									for (Player player : game.getLesPlayers()) {
										server.sendToTCP(player.getConnectionID(), game.getLesPlayers());
									}
								} else if (game.getStatut().equals("en attente")) {
									System.out.println("game est en attente");
									for (Player player : lesPlayersconnecte) {
										if (player.getConnectionID() == connection.getID()) {
											game.getLesPlayers().add(player);
											break;
										}
									}
									game.setStatut("en attente de joueurs");
									game.setType_game("multiplayer");
									server.sendToTCP(connection.getID(), game);

									break;
								}
								index++;
							}

							break;
						case "ready":
							index = 0;
							for (Game game : lesGames) {
								if (game.getStatut().equals("en attente de joueurs")) {

									if (lesPlayers.size() == 1) {
										swingTimer = new Timer();
										swingTimer.schedule(new TimerTask() {

											@Override
											public void run() {
												theTime--;
												for (Player player : game.getLesPlayers()) {
													System.out.println(player.getConnectionID() + player.getPseudo());
													server.sendToTCP(player.getConnectionID(), theTime);
												}

												if (theTime == 0) {
													for (Player player : game.getLesPlayers()) {
														server.sendToTCP(player.getConnectionID(), "start");
													}
													game.setStatut("En cours");
													theTime = DEFAULT_TIME;
													id_game++;
													CreateNewGame();
													cancel();
												}

											}
										}, 0, 1000);
										
									}
									break;
								}
								index++;
							}
							break;
						default:
							break;
						}
					} else if (object instanceof Score) {
						int index = 0;
						Boolean delete = false;
						Score leScore = (Score) object;
						for (Game game : lesGames) {
							System.out.println(leScore.getId_game() == game.getId_game());
							if (leScore.getId_game() == game.getId_game()) {
								game.getLesScores().add(leScore);
								game.setSent_score(game.getSent_score() + 1);
								
								Collections.sort(game.getLesScores());		
								
								if (game.getLesPlayers().size() == game.getSent_score()) {
									leStubGame.InsertGameBDD(game);

									try {
										leStubGame.InsertScore(game.getLesScores());
									} catch (SQLException e) {
										e.printStackTrace();
									}
									
									for (Player player : game.getLesPlayers()) {
										System.out.println(player.getConnectionID() + player.getPseudo());
										server.sendToTCP(player.getConnectionID(), game.getLesScores());
										if (game.getType_game().equals("multiplayer")) {
											System.out.println("envoie du finish");
											server.sendToTCP(player.getConnectionID(), "finish");
										}
									}
									delete = true;
									break;
								}else {
									for (Player player : game.getLesPlayers()) {
										System.out.println(player.getConnectionID() + player.getPseudo());
										server.sendToTCP(player.getConnectionID(), game.getLesScores());
									}
								}
							} 
							
							index++;
							if (delete) {
								lesGames.remove(index);
							}
						}
						
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		lesPlayersconnecte = new ArrayList<Player>();
		id_game = leStubGame.IdOfTheGame();
		CreateNewGame();
	}

	private void CreateNewGame() {
		System.out.println("création partie");
		lesScores = new ArrayList<Score>();
		lesPlayers = new ArrayList<Player>();
		leStubQuestion.initializeQuestions();
		laGame = new Game(id_game, null, leStubQuestion.getLesQuestions(), lesPlayers, lesScores,
				"en attente");
		
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

	public static class SampleRequest {
		public String text;
	}

	public static class SampleResponse {
		public String text;
	}
}
