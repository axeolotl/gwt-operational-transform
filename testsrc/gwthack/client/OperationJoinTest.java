package gwthack.client;

import junit.framework.TestCase;

import java.util.Collections;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 05.07.2009
 * Time: 14:34:25
 * To change this template use File | Settings | File Templates.
 */
public class OperationJoinTest extends TestCase {
  private Batch opQ;

  protected void setUp() throws Exception {
    opQ = new Batch();
  }

  public void testJoin(Operation op1, Operation op2, Operation opJoined) {
    opQ.clear();
    opQ.add(op1);
    opQ.add(op2);
    assertEquals(Collections.singletonList(opJoined), opQ.getOperations());
  }

  public void testNoJoin(Operation op1, Operation op2) {
    opQ.clear();
    opQ.add(op1);
    opQ.add(op2);
    assertEquals(Arrays.asList(op1,op2), opQ.getOperations());
  }

  public void testJoinInsInsEnd() {
    testJoin(new Inserted(0,"123"), new Inserted(3, "456"), new Inserted(0, "123456"));
  }
  public void testJoinInsInsBegin() {
    testJoin(new Inserted(0,"123"), new Inserted(0, "456"), new Inserted(0, "456123"));
  }
  public void testJoinInsInsMiddle() {
    testJoin(new Inserted(0,"123"), new Inserted(1, "456"), new Inserted(0, "145623"));
    testJoin(new Inserted(0,"123"), new Inserted(2, "456"), new Inserted(0, "124563"));
  }

  public void testJoinInsDelMiddle() {
    testJoin(new Inserted(0,"1234"), new Deleted(1, 3), new Inserted(0, "14"));
    testJoin(new Inserted(77,"1234"), new Deleted(77+1, 77+3), new Inserted(77, "14"));

    testJoin(new Inserted(0,"1234"), new Deleted(0, 2), new Inserted(0, "34"));
    testJoin(new Inserted(77,"1234"), new Deleted(77+0, 77+2), new Inserted(77, "34"));

    testJoin(new Inserted(0,"1234"), new Deleted(2, 4), new Inserted(0, "12"));
    testJoin(new Inserted(77,"1234"), new Deleted(77+2, 77+4), new Inserted(77, "12"));
  }

  public void testJoinDelDelEnd() {
    testJoin(new Deleted(0,3), new Deleted(0,2), new Deleted(0, 5));
    testJoin(new Deleted(77,77+3), new Deleted(77,77+2), new Deleted(77, 77+5));
  }
  public void testJoinDelDelBegin() {
    testJoin(new Deleted(2,3), new Deleted(0,2), new Deleted(0, 5));
    testJoin(new Deleted(77+2,77+3), new Deleted(77,77+2), new Deleted(77, 77+5));
  }

  public void testNoJoinInsInsEnd() {
    testNoJoin(new Inserted(0,"123"), new Inserted(4,"456"));
  }
  public void testNoJoinInsInsBegin() {
    testNoJoin(new Inserted(1,"123"), new Inserted(0,"456"));
  }

  public void testNoJoinInsDel() {
    // TODO: could actually join some cases of ins/del, but currently we don't
    testNoJoin(new Inserted(1,"123"), new Deleted(4,6));
  }
  public void testNoJoinDelIns() {
    testNoJoin(new Deleted(1,3), new Inserted(1,"123"));
    testNoJoin(new Deleted(0,2), new Inserted(1,"123"));
  }

  public void disabledTestJoinInsDel() {
    testJoin(new Inserted(1,"23"), new Deleted(0,4), new Deleted(0,2));
    testJoin(new Inserted(77+1,"23"), new Deleted(77+0,77+4), new Deleted(77+0,77+2));
  }

  public void testInsDelCancel() {
    opQ.clear();
    opQ.add(new Inserted(0,"xy"));
    opQ.add(new Deleted(0,2));
    assertEquals(Collections.<Operation>emptyList(), opQ.getOperations());
  }


}
