package gwthack.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
* User: axel
* Date: 01.07.2009
* Time: 10:08:33
* To change this template use File | Settings | File Templates.
*/
class Inserted extends Operation implements IsSerializable {
  private int at;
  private String chars;

  Inserted(int at, String chars) {
    this.at = at;
    this.chars = chars;
  }

  public Inserted() {
  }

  public int getAt() {
    return at;
  }

  public String getChars() {
    return chars;
  }

  public String toString() {
    return "ins("+at+","+chars+")";
  }

  protected String apply(String value) {
    return value.substring(0, at)+chars+value.substring(at);
  }

  boolean deletes(int index, boolean fromServer) {
    return false;
  }

  protected int shift(int index, boolean iAmClient) {
    if(index < at || iAmClient && index == at)
      return index;
    else
      return index + chars.length();
  }

  public boolean join(Operation operation) {
    if(operation instanceof Inserted) {
      Inserted inserted = (Inserted) operation;
      if(inserted.getAt() >= getAt() && inserted.getAt() <= getAt() + getChars().length()) {
        int insertAt = inserted.getAt() - getAt();
        chars = chars.substring(0, insertAt) + inserted.getChars() + chars.substring(insertAt);
        return true;
      }
    } else if (operation instanceof Deleted) {
      Deleted deleted = (Deleted) operation;
      if(deleted.getFrom() >= getAt() && deleted.getTo() <= getAt() + getChars().length()) {
        chars = chars.substring(0, deleted.getFrom() - getAt()) + chars.substring(deleted.getTo() - getAt());
        return true;
      }
      // the case where the delete only partially overlaps with this insert is too complicated for now.
    }
    return false;
  }

  public boolean isNop() {
    return chars.length() == 0;
  }

  public void transform(Operation op, boolean fromServer) {
    if(op.deletes(at, fromServer))
      chars = "";
    at = op.shift(at, fromServer);
  }

  public Operation copy() {
    return new Inserted(at, chars);
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Inserted inserted = (Inserted) o;

    if (at != inserted.at) return false;
    if (chars != null ? !chars.equals(inserted.chars) : inserted.chars != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = at;
    result = 31 * result + (chars != null ? chars.hashCode() : 0);
    return result;
  }
}
