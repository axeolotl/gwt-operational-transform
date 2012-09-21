package gwthack.client;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 01.07.2009
 * Time: 10:08:52
 * To change this template use File | Settings | File Templates.
 */
public interface OperationVisitor {
  public void insert(int i, String s);
  public void delete(int from, int to);
}
