package gwthack.client;

import junit.framework.TestCase;

/**
 */
public class OperationTransformTest extends TestCase {
  public void testTransform(String s0, Operation o1, Operation o2, String sm) {
    StringModel model1 = new StringModel(s0);
    StringModel model2 = new StringModel(s0);
    o1.apply(model1);
    o2.apply(model2);
    Operation.commute(o1,o2);
    o2.apply(model1);
    o1.apply(model2);
    assertEquals("model1", sm, model1.getValue());
    assertEquals("model2", sm, model2.getValue());
  }

  public void testTransformInsInsSamePos() {
    testTransform("", new Inserted(0,"hal"), new Inserted(0, "lo"), "hallo");
  }
  public void testTransformInsInsBefore() {
    testTransform("123", new Inserted(2,"xy"), new Inserted(1, "ab"), "1ab2xy3");
  }
  public void testTransformInsInsAfter() {
    testTransform("123", new Inserted(1, "ab"), new Inserted(2,"xy"), "1ab2xy3");
  }

  public void testTransformDelInsAfter() {
    testTransform("1234", new Deleted(0, 2), new Inserted(3,"xy"), "3xy4");
    testTransform("1234", new Inserted(3,"xy"), new Deleted(0, 2), "3xy4");
  }
  public void testTransformDelInsBefore() {
    testTransform("1234", new Deleted(1, 3), new Inserted(0,"xy"), "xy14");
    testTransform("1234", new Inserted(0,"xy"), new Deleted(1, 3), "xy14");
    
    testTransform("1234", new Deleted(2, 4), new Inserted(1,"xy"), "1xy2");
    testTransform("1234", new Inserted(1,"xy"), new Deleted(2, 4), "1xy2");
  }
  public void testTransformDelInsInside() {
    testTransform("1234", new Deleted(1, 3), new Inserted(2,"xy"), "14");
    testTransform("1234", new Inserted(2,"xy"), new Deleted(1, 3), "14");
  }
  public void testTransformDelInsRegionBegin() {
    // note: this case is not symmetric. client indices are "later".
    testTransform("1234", new Deleted(1, 3), new Inserted(1,"xy"), "14");
    testTransform("1234", new Inserted(1,"xy"), new Deleted(1, 3), "1xy4");
  }
  public void testTransformDelInsRegionEnd() {
    // note: this case is not symmetric. client indices are "later".
    testTransform("1234", new Deleted(1, 3), new Inserted(3,"xy"), "1xy4");
    testTransform("1234", new Inserted(3,"xy"), new Deleted(1, 3), "14");
  }
}
