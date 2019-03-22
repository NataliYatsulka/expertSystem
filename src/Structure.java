import java.util.HashMap;

public class Structure {
  public static String leftSide;
  public static String rightSize;
  public static boolean equivalentFlag;

  public Structure(String leftSide, String rightSize, boolean equivalentFlag) {
    this.leftSide = leftSide;
    this.rightSize = rightSize;
    this.equivalentFlag = equivalentFlag;
  }

  public static String getLeftSide() {
    return leftSide;
  }

  public static void setLeftSide(String leftSide) {
    Structure.leftSide = leftSide;
  }

  public static String getRightSize() {
    return rightSize;
  }

  public static void setRightSize(String rightSize) {
    Structure.rightSize = rightSize;
  }

  public static boolean isEquivalentFlag() {
    return equivalentFlag;
  }

  public static void setEquivalentFlag(boolean equivalentFlag) {
    Structure.equivalentFlag = equivalentFlag;
  }

  @Override
  public String toString() {
    return leftSide + ", " + rightSize + ", " + equivalentFlag;
  }
}
