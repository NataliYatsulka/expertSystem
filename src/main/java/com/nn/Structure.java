package com.nn;

public class Structure {
  public String leftSide;
  public String rightSize;
  public boolean equivalentFlag;
  public boolean beingInThisRow;

  public Structure(String leftSide, String rightSize, boolean equivalentFlag, boolean beingInThisRow) {

    this.leftSide = leftSide;
    this.rightSize = rightSize;
    this.equivalentFlag = equivalentFlag;
    this.beingInThisRow = beingInThisRow;
  }

  @Override
  public String toString() {
    return leftSide + ", " + rightSize + ", " + equivalentFlag + ", beOrNotToBe = " + beingInThisRow;
  }
}
