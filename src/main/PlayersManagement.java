package main;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import sprites.Player;

import java.io.FileWriter;
import java.util.*;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;

/**
 * Class responsible for managing player data.
 */
public class PlayersManagement {
	/**
	 * Path to the directory where the data is stored.
	 */
	private final String DIRECTORY_PATH = "data/";
	/**
	 * Name of the file where the data is stored.
	 */
	private final String FILE_NAME = "players.json";

	/**
	 * Constructor for the PlayersManagement class.
	 */
	public PlayersManagement() { }

	/**
	 * Reads the data from the JSON file and returns it as a Map(String, Player).
	 * @return Map(String, Player) with the data read from the JSON file.
	 */
	public Map<String, Player> readJsonToMap() {
		Map<String, Player> playersMap = new HashMap<>();
		Gson gson = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.create();
		Type type = new TypeToken<Map<String, Player>>() {}.getType();

		try {
			JsonReader reader = new JsonReader(new FileReader(Paths.get(DIRECTORY_PATH, FILE_NAME).toString()));
			playersMap = gson.fromJson(reader, type);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (playersMap == null) {
			playersMap = new HashMap<>();
		}

		return playersMap;
	}

	/**
	 * Initializes the players and returns them in an array.
	 * @param p1Name Name of the first player.
	 * @param p2Name Name of the second player.
	 * @param playersMap Map(String, Player) with the data read from the JSON file.
	 * @return Array of players.
	 */
	public Player[] initializePlayers(String p1Name, String p2Name, Map<String, Player> playersMap) {
		Player[] players = new Player[2];

		// Initialize players
		players[0] = new Player(p1Name, true);
		players[1] = new Player(p2Name, false);

		// Set enemies
		players[0].enemy = players[1];
		players[1].enemy = players[0];

		// Set player 1 data
		if (playersMap.containsKey(p2Name)) {
			players[0].matchesWon = playersMap.get(p1Name).matchesWon;
			players[0].matchesLost = playersMap.get(p1Name).matchesLost;
			players[0].record = playersMap.get(p1Name).record;
		} else {
			playersMap.put(p1Name, players[0]);
		}

		// Set player 2 data
		if (playersMap.containsKey(p2Name)) {
			players[1].matchesWon = playersMap.get(p2Name).matchesWon;
			players[1].matchesLost = playersMap.get(p2Name).matchesLost;
			players[1].record = playersMap.get(p2Name).record;
		} else {
			playersMap.put(p2Name, players[1]);
		}

		return players;
	}

	/**
	 * Saves the players data to the JSON file.
	 * @param playersMap Map(String, Player) with the data read from the JSON file.
	 */
	public void savePlayersToJson(Map<String, Player> playersMap) {
		// Save players data to JSON file
		Gson gson = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.setPrettyPrinting()
				.create();
		try {
			FileWriter writer = new FileWriter(Paths.get(DIRECTORY_PATH, FILE_NAME).toString());
			gson.toJson(playersMap, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the players data in a String[][], representing a table with columns.
	 * @param playersMap Map(String, Player) with the data read from the JSON file.
	 * @return String[][] with the players data.
	 */
	public String[][] getPlayersDataColumns(Map<String, Player> playersMap) {
		// Sort players by record
		List<Player> result = new ArrayList<>(playersMap.values());

		result.sort((p1, p2) -> Integer.compare(p2.record, p1.record));

		// Get players data
		int numPlayers = Math.min(result.size(), 10);
		String[][] playersData = new String[4][numPlayers];

		// Set columns
		for (int i = 0; i < numPlayers; i++) {
			playersData[0][i] = String.valueOf(i + 1);
			playersData[1][i] = result.get(i).name;
			playersData[2][i] = String.valueOf(result.get(i).record);
			playersData[3][i] = String.valueOf(result.get(i).matchesWon);
		}

		return playersData;
	}

	/**
	 * Method that updates the players scores
	 * @param p1 First player.
	 * @param p2 Second player.
	 */
	public void updatePlayersData(Player p1, Player p2) {
		// Update players data
		if (p1.points > p2.points) {
			Game1.playersMap.get(p1.name).matchesWon++;
			Game1.playersMap.get(p2.name).matchesLost++;
		} else if (p1.points < p2.points) {
			Game1.playersMap.get(p2.name).matchesWon++;
			Game1.playersMap.get(p1.name).matchesLost++;
		}

		// Update players record
		if (p1.points > p1.record) {
			Game1.playersMap.get(p1.name).record = p1.points;
		}
		if (p2.points > p2.record) {
			Game1.playersMap.get(p2.name).record = p2.points;
		}
	}
}
