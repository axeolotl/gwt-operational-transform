package gwthack.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 28.06.2009
 * Time: 22:20:20
 * To change this template use File | Settings | File Templates.
 */
public interface GwthackService extends RemoteService {
  public int opsFromClient(int serverVersion, Batch batch, ClientID clientID);
  public ArrayList<HistoryEntry> getNewOpsFromServer(int knownVersion);

  ClientID createClientID();

  /**
   * Utility/Convenience class.
   * Use GwthackService.App.getInstance () to access static instance of GwthackServiceAsync
   */
  public static class App {
    private static GwthackServiceAsync app = null;

    public static synchronized GwthackServiceAsync getInstance() {
      if (app == null) {
        app = (GwthackServiceAsync) GWT.create(GwthackService.class);
        ((ServiceDefTarget) app).setServiceEntryPoint(GWT.getModuleBaseURL() + "gwthack.Gwthack/GwthackService");
      }
      return app;
    }
  }
}
