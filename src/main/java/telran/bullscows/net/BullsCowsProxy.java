package telran.bullscows.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import telran.bulls.net.BullsCowsService;
import telran.bulls.net.Move;
import telran.bulls.net.MoveResult;
import telran.net.Request;
import telran.net.TcpClient;

public class BullsCowsProxy implements BullsCowsService {
	 TcpClient tcpClient;
     
		public BullsCowsProxy(TcpClient tcpClient) {
			this.tcpClient = tcpClient;
		}

		@Override
	    public Long createNewGame() {
	        Request request = new Request("create", "");
	        String responseStr = tcpClient.sendAndReceive(request);
	        return Long.parseLong(responseStr);
	    }

	    @Override
	    public List<MoveResult> getResults(long gameId, Move move) {
	        String requestData = gameId + ";" + move.clientSequence();
	        Request request = new Request("move", requestData);
	        String responseStr = tcpClient.sendAndReceive(request);
	        return parseMoveResults(responseStr);
	    }

	    @Override
	    public Boolean isGameOver(long gameId) {
	        Request request = new Request("isGameOver", Long.toString(gameId));
	        String responseStr = tcpClient.sendAndReceive(request);
	        return Boolean.parseBoolean(responseStr);
	    }

	    private List<MoveResult> parseMoveResults(String responseStr) {
	        List<MoveResult> results = new ArrayList<>();
	        String[] entries = responseStr.split("\n");
	        for (String entry : entries) {
	            if (!entry.isEmpty()) {
	                String[] parts = entry.split(", ");
	                if (parts.length == 3) {
	                    String guessStr = parts[0].split(": ")[1];
	                    int bulls = Integer.parseInt(parts[1].split(": ")[1]);
	                    int cows = Integer.parseInt(parts[2].split(": ")[1]);
	                    results.add(new MoveResult(new Integer[]{bulls, cows}, guessStr));
	                }
	            }
	        }
	        return results;
	    }
	}