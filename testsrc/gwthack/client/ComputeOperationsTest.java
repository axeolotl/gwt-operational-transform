package gwthack.client;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * test for {@link ComputeOperations }
 */
public class ComputeOperationsTest extends TestCase {
  private ComputeOperations computeOperations;
  private List<Operation> ops = new ArrayList<Operation>();

  protected void setUp() throws Exception {
    super.setUp();
    computeOperations = new ComputeOperations();
  }

  public void testOps(String from, String to) {
    final StringModel model = new StringModel(from);
    ops.clear();
    computeOperations.computeOperations(from, to, new OperationVisitor() {
      public void insert(int i, String s) {
        addOp(new Inserted(i, s));
      }

      public void delete(int from, int to) {
        addOp(new Deleted(from, to));
      }

      private void addOp(Operation op) {
        ops.add(op);
        op.apply(model);
      }

    });
    assertEquals(to, model.getValue());
  }

  public void testOp(String from, String to) {
    testOps(from, to);
    assertEquals(1, ops.size());
  }

  public void testDeleteAll() {
    testOp("hallo","");
  }

  public void testInsertAll() {
    testOp("","hallo");
  }

  public void testInsertMiddle() {
    testOp("1234","12xxx34");
  }
  public void testInsertBegin() {
    testOp("1234","xxx1234");
  }
  public void testInsertEnd() {
    testOp("1234","1234xxx");
  }
  public void testDeleteBegin() {
    testOp("1234","234");
    testOp("1234","34");
    testOp("1234","4");
  }
  public void testDeleteEnd() {
    testOp("1234","123");
    testOp("1234","12");
    testOp("1234","1");
  }

  public void testNop() {
    testOps("","");
    assertEquals(Collections.<Operation>emptyList(), ops);
    testOps("hallo","hallo");
    assertEquals(Collections.<Operation>emptyList(), ops);
  }

  public void testMixed() {
    testOps("12345", "1x2y45zz");
  }
}
