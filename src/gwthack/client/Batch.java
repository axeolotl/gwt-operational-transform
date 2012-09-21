package gwthack.client;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

/**
 * A compound operation.
 */
public class Batch implements Serializable {
  private List<Operation> operations;

  public Batch() {
    operations = new ArrayList<Operation>();
  }

  private Batch(List<Operation> operations) {
    this.operations = operations;
  }

  public void add(Operation operation) {
    if(!operations.isEmpty()) {
      Operation last = operations.get(operations.size()-1);
      if(last.join(operation)) {
        operations.remove(operations.size()-1);
        if(!last.isNop())
          add(last);
        return;
      }
    }
    operations.add(operation);
  }

  public void commute(Operation serverOp) {
    for(Iterator<Operation> it = operations.iterator(); it.hasNext(); ) {
      Operation localOp = it.next();
      Operation.commute(serverOp, localOp);
      if(localOp.isNop())
        it.remove();
    }
  }

  public void transform(Batch serverBatch) {
    for(Operation op: serverBatch.getOperations())
      transform(op);
  }
  public void transform(Operation serverOp) {
    for(Iterator<Operation> it = operations.iterator(); it.hasNext(); ) {
      Operation localOp = it.next();
      localOp.transform(serverOp, false);
      if(localOp.isNop())
        it.remove();
    }
  }

  public void apply(StringModel model) {
    for(Operation op : operations)
      op.apply(model);
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public boolean isEmpty() {
    return operations.isEmpty();
  }

  public void clear() {
    operations.clear();
  }

  public String toString() {
    return operations.toString();
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Batch that = (Batch) o;

    if (operations != null ? !operations.equals(that.operations) : that.operations != null) return false;

    return true;
  }

  public int hashCode() {
    return (operations != null ? operations.hashCode() : 0);
  }

  public Batch copy() {
    List<Operation> cop = new ArrayList<Operation>();
    for(Operation op: operations) {
      cop.add(op.copy());
    }
    return new Batch(cop);
  }
}
