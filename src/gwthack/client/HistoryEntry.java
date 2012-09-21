package gwthack.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 18.07.2009
 * Time: 16:45:09
 * To change this template use File | Settings | File Templates.
 */
public class HistoryEntry implements Serializable, IsSerializable {
  int versionNumber;
  Batch batch;
  ClientID clientID;

  public HistoryEntry(int versionNumber, Batch batch, ClientID clientID) {
    this.versionNumber = versionNumber;
    this.batch = batch;
    this.clientID = clientID;
  }

  public HistoryEntry() {
  }

  public int getVersionNumber() {
    return versionNumber;
  }

  public Batch getBatch() {
    return batch;
  }

  public ClientID getClientID() {
    return clientID;
  }

  public HistoryEntry copy() {
    return new HistoryEntry(versionNumber, batch.copy(), clientID);
  }
}
