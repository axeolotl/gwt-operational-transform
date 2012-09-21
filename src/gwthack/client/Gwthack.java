package gwthack.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.DOM;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gwthack implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    // final Button button = new Button("Click me");
    // final Label label = new Label();
    GwthackServiceAsync server = GwthackService.App.getInstance();
    Client client1 = new Client(server);
    Client client2 = new Client(server);

//    button.addClickListener(new ClickListener() {
//      public void onClick(Widget sender) {
//        if (label.getText().equals("")) {
//          GwthackService.App.getInstance().getMessage("Hello, World!", new MyAsyncCallback(label));
//        } else
//          label.setText("");
//      }
//    });
//
    // Assume that the host HTML has elements defined whose
    // IDs are "slot1", "slot2".  In a real app, you probably would not want
    // to hard-code IDs.  Instead, you could, for example, search for all
    // elements with a particular CSS class and replace them with widgets.
    //
    RootPanel.get("client1").add(client1.getWidget());
    RootPanel.get("client2").add(client2.getWidget());
    // RootPanel.get("server").add(server.getWidget());
  }


//  static class MyAsyncCallback implements AsyncCallback {
//    public void onSuccess(Object object) {
//      DOM.setInnerHTML(label.getElement(), (String) object);
//    }
//
//    public void onFailure(Throwable throwable) {
//      label.setText("Failed to receive answer from server!");
//    }
//
//    Label label;
//
//    public MyAsyncCallback(Label label) {
//      this.label = label;
//    }
//  }
}
