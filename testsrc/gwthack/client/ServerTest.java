package gwthack.client;

import junit.framework.TestCase;
import gwthack.server.Server;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 18.07.2009
 * Time: 23:11:09
 * To change this template use File | Settings | File Templates.
 */
public class ServerTest extends TestCase {
  Server server;

  protected void setUp() throws Exception {
    server = new Server();
  }

  public void testClientID() {
    ClientID id = server.createClientID();
    assertEquals(id, id);
    ClientID id2 = server.createClientID();
    assertEquals(id2, id2);
    assertFalse(id.equals(id2));
  }

  public void testOpFromClient() {
    ClientID id = server.createClientID();
    Batch batch = new Batch();
    batch.add(new Inserted(0,"huhu"));
    int newVersion = server.opsFromClient(0, batch, id);
    assertEquals(1, newVersion);
    ArrayList<HistoryEntry> history = server.getNewOpsFromServer(0);
    assertEquals(1, history.size());
    HistoryEntry historyEntry = history.get(0);
    assertEquals(newVersion, historyEntry.getVersionNumber());
    assertEquals(id, historyEntry.getClientID());
    assertEquals(batch, historyEntry.getBatch());
  }

  // TODO: test transformation by batches from other clients
}
