/*
 * Copyright 2017, Yahoo, Inc.
 * Licensed under the terms of the Apache License 2.0. See LICENSE file at the project root for terms.
 */

package com.yahoo.sketches;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the various families of sketch and set operation classes.  A family defines a set of
 * classes that share fundamental algorithms and behaviors.  The classes within a family may
 * still differ by how they are stored and accessed. For example, internally there may be separate
 * classes for algorithms that operate on the Java heap and off-heap.
 * Not all of these families have parallel forms on and off-heap but are included for completeness.
 *
 * <p>Family IDs start at 128 to allow separation from sketches-core for as long as possible without
 * inducing a mutual dependency between packages.</p>
 *
 * @author Lee Rhodes
 * @author Jon Malkin
 */
public enum MatrixFamily {
  /**
   * The Frequent Directions sketch is used for approximate Singular Value Decomposition (SVD) of a
   * matrix.
   */
  MATRIX(128, "FrequentDirections", 2, 3),
  FREQUENTDIRECTIONS(129, "FrequentDirections", 2, 4);


  private static final Map<Integer, MatrixFamily> lookupID = new HashMap<>();
  private static final Map<String, MatrixFamily> lookupFamName = new HashMap<>();
  private int id_;
  private String famName_;
  private int minPreLongs_;
  private int maxPreLongs_;

  static {
    for (MatrixFamily f : values()) {
      lookupID.put(f.getID(), f);
      lookupFamName.put(f.getFamilyName().toUpperCase(), f);
    }
  }

  MatrixFamily(final int id, final String famName, final int minPreLongs, final int maxPreLongs) {
    id_ = id;
    famName_ = famName.toUpperCase();
    minPreLongs_ = minPreLongs;
    maxPreLongs_ = maxPreLongs;
  }

  /**
   * Returns the byte ID for this family
   * @return the byte ID for this family
   */
  public int getID() {
    return id_;
  }

  /**
   *
   * @param id the given id, a value &ge; 128.
   */
  public void checkFamilyID(final int id) {
    if (id != id_) {
      throw new IllegalArgumentException(
              "Possible Corruption: This Family " + this.toString()
                      + " does not match the ID of the given Family: " + idToFamily(id).toString());
    }
  }

  /**
   * Returns the name for this family
   * @return the name for this family
   */
  public String getFamilyName() {
    return famName_;
  }

  /**
   * Returns the minimum preamble size for this family in longs
   * @return the minimum preamble size for this family in longs
   */
  public int getMinPreLongs() {
    return minPreLongs_;
  }

  /**
   * Returns the maximum preamble size for this family in longs
   * @return the maximum preamble size for this family in longs
   */
  public int getMaxPreLongs() {
    return maxPreLongs_;
  }

  @Override
  public String toString() {
    return famName_;
  }

  /**
   * Returns the Family given the ID
   * @param id the given ID
   * @return the Family given the ID
   */
  public static MatrixFamily idToFamily(final int id) {
    final MatrixFamily f = lookupID.get(id);
    if (f == null) {
      throw new IllegalArgumentException("Possible Corruption: Illegal Family ID: " + id);
    }
    return f;
  }

  /**
   * Returns the Family given the family name
   * @param famName the family name
   * @return the Family given the family name
   */
  public static MatrixFamily stringToFamily(final String famName) {
    final MatrixFamily f = lookupFamName.get(famName.toUpperCase());
    if (f == null) {
      throw new IllegalArgumentException("Possible Corruption: Illegal Family Name: " + famName);
    }
    return f;
  }

  /**
   * Returns the Family given one of the recognized class objects on one of the Families
   * @param obj a recognized Family class object
   * @return the Family given one of the recognized class objects on one of the Families
   */
  public static MatrixFamily objectToFamily(final Object obj) {
    final String sname = obj.getClass().getSimpleName().toUpperCase();
    for (MatrixFamily f : values()) {
      if (sname.contains(f.toString())) {
        return f;
      }
    }
    throw new IllegalArgumentException("Possible Corruption: Unknown object");
  }
}
