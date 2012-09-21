package gwthack.client;

/**
 * Created by IntelliJ IDEA.
* User: axel
* Date: 01.07.2009
* Time: 10:08:22
* To change this template use File | Settings | File Templates.
*/
abstract class Operation {

  /**
   * if possible, update this operation so that it also reflects the given operation, and return true.
   * otherwise return false,
   */
  public abstract boolean join(Operation operation);

  public boolean isNop() {
    return false;
  }

  public void apply(StringModel model) {
    model.setValue(apply(model.getValue()));
  }

  protected abstract String apply(String value);

  abstract boolean deletes(int index, boolean fromServer);

  /** where is the given index moved to by this operation? **/
  protected abstract int shift(int index, boolean iAmClient);

  /**
   * Change this operation so that it is applied to the state after <var>op</var>.
   * @param op
   * @param fromServer
   */
  public abstract void transform(Operation op, boolean fromServer);

  public static void commute(Operation serverOp, Operation clientOp) {
    Operation serverOpBak = serverOp.copy();
    serverOp.transform(clientOp, true);
    clientOp.transform(serverOpBak, false);
  }

  public abstract Operation copy();
}
