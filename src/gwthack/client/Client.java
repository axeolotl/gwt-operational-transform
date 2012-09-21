package gwthack.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * represents one client of the server.
 * including text editing widget, local state and operations queue.
 */
public class Client {
  VerticalPanel panel = new VerticalPanel();
  TextArea textArea = new TextArea();
  StringModel stringModel = new StringModel("");
  private int serverVersion;
  CheckBox holdBack = new CheckBox("hold back");
  TextBox latencyBox = new TextBox();
  Label debug = new Label();
  Batch outQueue = new Batch();
  private boolean messageInProgress=false;
  private GwthackServiceAsync server;
  private ComputeOperations computeOperations = new ComputeOperations();
  private int DEFAULT_LATENCY_MILLIS = 1000;
  private int POLL_INTERVAL_MILLIS = 100;
  private boolean focused;
  private ClientID clientID;


  public Client(GwthackServiceAsync server) {
    this.server = server;
    initWidgets();
    server.createClientID(new AsyncCallback<ClientID>() {
      public void onFailure(Throwable caught) {

      }

      public void onSuccess(ClientID result) {
        clientID = result;
        init();
      }
    });   // TODO: also obtain serverVersion and model state
  }

  private void initWidgets() {
    textArea.setCharacterWidth(50);
    textArea.setHeight("100");
    panel.add(textArea);
    panel.add(holdBack);
    latencyBox.setText(String.valueOf(DEFAULT_LATENCY_MILLIS));
    panel.add(latencyBox);
    debug.setText("initializing...");
    panel.add(debug);
  }

  public void init() {
    debug.setText("initialized client ID "+clientID);
    textArea.addKeyboardListener(new KeyboardListenerAdapter(){
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        // debug.setText("key: "+keyCode+"("+(int)keyCode+") at "+textArea.getCursorPos());
        // textArea.cancelKey();
        new Timer() {
          public void run() {
            textAreaUpdated();
          }
        }.schedule(1);
      }
      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        // debug.setText("key: "+keyCode+"("+(int)keyCode+") at "+textArea.getCursorPos());
        // textArea.cancelKey();
        new Timer() {
          public void run() {
            textAreaUpdated();
          }
        }.schedule(1);
      }
    });
    textArea.addFocusListener(new FocusListenerAdapter() {
      public void onFocus(Widget sender) {
        focused = true;
      }

      public void onLostFocus(Widget sender) {
        focused = false;
      }
    });
    holdBack.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        new Timer() {
          public void run() {
            checkQueue();
          }
        }.schedule(1);
      }
    });
    startPolling();
//    textArea.addChangeListener(new ChangeListener(){
//      public void onChange(Widget sender) {
//        label.setText("cursor: "+textArea.getCursorPos()
//            +" text: "+textArea.getText()
//        );
//      }
//    });


  }

  private void startPolling() {
    new Timer() {
      public void run() {
        server.getNewOpsFromServer(serverVersion, new AsyncCallback<ArrayList<HistoryEntry>>() {
          public void onFailure(Throwable caught) {
            startPolling();
          }

          public void onSuccess(ArrayList<HistoryEntry> entries) {
            for(HistoryEntry entry : entries) {
              if(!entry.getClientID().equals(clientID)) {
                opsFromServer(entry.getVersionNumber(), entry.getBatch());
              }
            }
            startPolling();
          }
        });
      }
    }.schedule(POLL_INTERVAL_MILLIS);
  }

  private void textAreaUpdated() {
    String newValue = textArea.getText();
    String oldValue = stringModel.getValue();
    computeOperations.computeOperations(oldValue, newValue, new OperationVisitor() {
      public void insert(int i, String s) {
        outQueueAdd(new Inserted(i, s));
      }

      public void delete(int from, int to) {
        outQueueAdd(new Deleted(from, to));
      }
    });
    stringModel.setValue(newValue);
    //textArea.setText(newValue);
  }

  private boolean isHoldBack() {
    return holdBack.isChecked();
  }

  private void outQueueAdd(Operation operation) {
    outQueue.add(operation);
    checkQueue();
  }

  private void checkQueue() {
    if(!isHoldBack() && !isMessageInProgress() && !outQueue.isEmpty()) {
      serverSend(serverVersion, outQueue);
      outQueue = new Batch();
    }
    debug.setText(outQueue.toString());
  }

  private void serverSend(final int serverVersion, final Batch ops) {
    messageInProgress = true;
    new Timer() {
      public void run() {
        server.opsFromClient(serverVersion, ops, clientID, new AsyncCallback<Integer>() {
          public void onFailure(Throwable caught) {
            debug.setText(caught.toString());
            messageInProgress = false;
          }

          public void onSuccess(Integer result) {
            Client.this.serverVersion = result;
            messageInProgress = false;
            checkQueue();
          }
        });
      }
    }.schedule(getLatencyMillis());
  }

  private int getLatencyMillis() {
    String latencyStr = latencyBox.getText();
    int latency;
    try {
      latency = Integer.parseInt(latencyStr);
    } catch (NumberFormatException e) {
      latency = -1;
    }
    if(latency < 0 || latency > 10000)
      latency = DEFAULT_LATENCY_MILLIS;
    String newLatencyStr = String.valueOf(latency);
    if(!newLatencyStr.equals(latencyStr))
      latencyBox.setText(newLatencyStr);
    return latency;
  }

  public void opsFromServer(int serverVersion, Batch ops) {
    int cursorPos = textArea.getCursorPos();
    for(Operation serverOp : ops.getOperations()) {
      outQueue.commute(serverOp);
      // TODO: maintain selection
      serverOp.apply(stringModel);
      cursorPos = serverOp.shift(cursorPos, false);
    }
    this.serverVersion = serverVersion;
    debug.setText(outQueue.toString());
    textArea.setText(stringModel.getValue());
    if(focused)
      textArea.setCursorPos(cursorPos);
  }

  public Widget getWidget() {
    return panel;
  }

  public boolean isMessageInProgress() {
    return messageInProgress;
  }
}
