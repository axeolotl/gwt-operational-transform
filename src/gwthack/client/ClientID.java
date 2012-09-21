package gwthack.client;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 18.07.2009
 * Time: 16:42:49
 * To change this template use File | Settings | File Templates.
 */
public class ClientID implements Serializable, IsSerializable {
  int n;

  public ClientID(int n) {
    this.n = n;
  }

  public ClientID() {
  }

  public int getN() {
    return n;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClientID clientID = (ClientID) o;

    if (n != clientID.n) return false;

    return true;
  }

  public int hashCode() {
    return n;
  }

  public String toString() {
    return ""+n;
  }
}
