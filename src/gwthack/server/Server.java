package gwthack.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.List;
import java.util.ArrayList;

import gwthack.client.*;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 02.07.2009
 * Time: 21:58:24
 * To change this template use File | Settings | File Templates.
 */
public class Server extends RemoteServiceServlet implements GwthackService {
  StringModel model = new StringModel("");
  List<HistoryEntry> history = new ArrayList<HistoryEntry>();
  private int nextClientID=1;

  public Server() {
  }

  public ClientID createClientID() {
    return new ClientID(nextClientID++);
  }

  public int opsFromClient(int serverVersion, Batch batch, ClientID clientID) {
    while(serverVersion < history.size()) {
      HistoryEntry historyEntry = history.get(serverVersion);
      // TODO: assert: no pending batches from the same client
      Batch serverBatch = historyEntry.getBatch();
      batch.transform(serverBatch);
      ++serverVersion;
    }
    batch.apply(model);
    history.add(new HistoryEntry(history.size()+1, batch, clientID));
    int newVersion = history.size();
    // debug.setText(model.getValue());
    // widget.add(new Label(batch.toString()));
    // TODO: notify clients
    return newVersion;
  }

  public ArrayList<HistoryEntry> getNewOpsFromServer(int knownVersion) {
    ArrayList<HistoryEntry> result = new ArrayList<HistoryEntry>();
    // simulate copying on the wire, to avoid aliassing
    for(int i=knownVersion; i<history.size(); ++i) {
      result.add(history.get(i).copy());
    }
    return result;
  }
}
