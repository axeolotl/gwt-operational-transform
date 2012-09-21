package gwthack.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 28.06.2009
 * Time: 22:20:21
 * To change this template use File | Settings | File Templates.
 */
public interface GwthackServiceAsync {

  void opsFromClient(int serverVersion, Batch batch, ClientID clientID, AsyncCallback<Integer> async);

  void getNewOpsFromServer(int knownVersion, AsyncCallback<ArrayList<HistoryEntry>> async);

  void createClientID(AsyncCallback<ClientID> async);
}
