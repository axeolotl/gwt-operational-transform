package gwthack.client;

/**
 * Created by IntelliJ IDEA.
 * User: axel
 * Date: 05.07.2009
 * Time: 14:05:50
 * To change this template use File | Settings | File Templates.
 */
public class ComputeOperations {
  public void computeOperations(String oldValue, String newValue, OperationVisitor opv) {
    int newLength = newValue.length();
    int oldLength = oldValue.length();
    int i=0;
    while(i< newLength && i< oldLength && newValue.charAt(i)==oldValue.charAt(i))
      ++i;
    if(i == newLength) {
      if(i == oldLength) {
        // nop.
        return;
      } else {
        opv.delete(newLength, oldLength);
      }
    } else {
      if(i == oldLength) {
        opv.insert(oldLength, newValue.substring(i, newLength));
      } else {
        // find equal suffix
        int j=0;
        while(j < newLength-i && j < oldLength-i
            && newValue.charAt(newLength-1-j)==oldValue.charAt(oldLength-1-j))
          ++j;
        if(j < oldLength-i) {
          opv.delete(i, oldLength-j);
        }
        if(j < newLength-i) {
          opv.insert(i, newValue.substring(i, newLength-j));
        }
      }
    }
  }

}
