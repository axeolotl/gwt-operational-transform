package gwthack.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
* User: axel
* Date: 01.07.2009
* Time: 10:08:28
* To change this template use File | Settings | File Templates.
*/
class Deleted extends Operation implements IsSerializable {
  /** to is exclusive. **/
  private int from, to;

  Deleted(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public Deleted() {
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }

  public String toString() {
    return "del("+from+","+to+")";
  }

  protected String apply(String value) {
    return value.substring(0, from)+value.substring(to);
  }

  boolean deletes(int index, boolean iAmClient) {
    int off = iAmClient ? 1 : 0;  // client indices are later than equal server indices.
    return index >= from+off && index < to+off;
  }

  protected int shift(int index, boolean fromServer) {
    if(index <= from)
      return index;
    else if(index >= to)
      return index - (to-from);
    else
      return from;
  }

  public boolean join(Operation operation) {
    if(operation instanceof Deleted) {
      Deleted deleted = (Deleted) operation;
      if(deleted.getFrom() <= from && from <= deleted.getTo()) {
        from = deleted.getFrom();
        to += deleted.getTo()-deleted.getFrom();
        return true;
      }
    }
    return false;
  }

  public void transform(Operation op, boolean fromServer) {
    from = op.shift(from, fromServer);
    to = op.shift(to, fromServer);
  }

  public Operation copy() {
    return new Deleted(from,to);
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Deleted deleted = (Deleted) o;

    if (from != deleted.from) return false;
    if (to != deleted.to) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = from;
    result = 31 * result + to;
    return result;
  }
}
