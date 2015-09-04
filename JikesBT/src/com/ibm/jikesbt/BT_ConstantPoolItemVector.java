package com.ibm.jikesbt;

/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 1998, 2003
 * All rights reserved
 */

import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 BT_ConstantPoolItemVector is a variable size contiguous indexable array of {@link BT_ConstantPoolItem}s.
 The size of the BT_ConstantPoolItemVector is the number of BT_ConstantPoolItems it contains.
 The capacity of the BT_ConstantPoolItemVector is the number of BT_ConstantPoolItems it can hold.

 <p> BT_ConstantPoolItems may be inserted at any position up to the size of the
 BT_ConstantPoolItemVector, increasing the size of the BT_ConstantPoolItemVector. BT_ConstantPoolItems at any
 position in the BT_ConstantPoolItemVector may be removed, shrinking the size of
 the BT_ConstantPoolItemVector. BT_ConstantPoolItems at any position in the BT_ConstantPoolItemVector may be replaced,
 which does not affect the BT_ConstantPoolItemVector size.

 <p> The capacity of a BT_ConstantPoolItemVector may be specified when the BT_ConstantPoolItemVector is
 created. If the capacity of the BT_ConstantPoolItemVector is exceeded, the capacity
 is increased, doubling by default.
 * @author IBM
**/

public final class BT_ConstantPoolItemVector
	implements Cloneable, Serializable {
	/**
	 * The number of elements or the size of the vector.
	 */
	protected int elementCount;
	/**
	 * The elements of the vector.
	 */
	protected BT_ConstantPoolItem[] elementData;
	/**
	 * The amount by which the capacity of the vector is increased.
	 */
	protected int capacityIncrement;

	/**
	 * Initial empty value for elementData.
	 */
	private final static BT_ConstantPoolItem[] emptyData =
		new BT_ConstantPoolItem[0];
	private static final int DEFAULT_SIZE = 0;

	/**
	 * Constructs a new BT_ConstantPoolItemVector using the default capacity.
	 *
	 */
	public BT_ConstantPoolItemVector() {
		this(DEFAULT_SIZE, 0);
	}
	/**
	 * Constructs a new BT_ConstantPoolItemVector using the specified capacity.
	 *
	 *
	 * @param           capacity        the initial capacity of the new vector
	 */
	public BT_ConstantPoolItemVector(int capacity) {
		this(capacity, 0);
	}
	/**
	 * Constructs a new BT_ConstantPoolItemVector using the specified capacity and
	 * capacity increment.
	 *
	 *
	 * @param           capacity        the initial capacity of the new BT_ConstantPoolItemVector
	 * @param           capacityIncrement       the amount to increase the capacity
	                                        when this BT_ConstantPoolItemVector is full
	 */
	public BT_ConstantPoolItemVector(int capacity, int capacityIncrement) {
		elementCount = 0;
		elementData =
			(capacity == 0) ? emptyData : new BT_ConstantPoolItem[capacity];
		this.capacityIncrement = capacityIncrement;
	}
	/**
	 * Adds the specified object at the end of this BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           object  the object to add to the BT_ConstantPoolItemVector
	 */
	public void addElement(BT_ConstantPoolItem object) {
		insertElementAt(object, elementCount);
	}
	/**
	 * Answers the number of elements this BT_ConstantPoolItemVector can hold without
	 * growing.
	 *
	 *
	 * @return          the capacity of this BT_ConstantPoolItemVector
	 *
	 * @see                     #ensureCapacity
	 * @see                     #size
	 */
	public int capacity() {
		return elementData.length;
	}
	/**
	 * Answers a new BT_ConstantPoolItemVector with the same elements, size, capacity
	 * and capacityIncrement as this BT_ConstantPoolItemVector.
	 *
	 *
	 * @return          a shallow copy of this BT_ConstantPoolItemVector
	 *
	 * @see                     java.lang.Cloneable
	 */
	public Object clone() {
		try {
			BT_ConstantPoolItemVector vector =
				(BT_ConstantPoolItemVector) super.clone();
			int length = elementData.length;
			vector.elementData = new BT_ConstantPoolItem[length];
			System.arraycopy(elementData, 0, vector.elementData, 0, length);
			return vector;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	/**
	 * Searches this BT_ConstantPoolItemVector for the specified object.
	 *
	 *
	 * @param           object  the object to look for in this BT_ConstantPoolItemVector
	 * @return          true if object is an element of this BT_ConstantPoolItemVector, false otherwise
	 *
	 * @see                     #indexOf
	 * @see                     java.lang.Object#equals
	 */
	public boolean contains(BT_ConstantPoolItem object) {
		return indexOf(object, 0) != -1;
	}
	/**
	 * Copies the elements of this BT_ConstantPoolItemVector into the specified BT_ConstantPoolItem array.
	 *
	 *
	 * @param           elements        the BT_ConstantPoolItem array into which the elements
	 *                                                  of this BT_ConstantPoolItemVector are copied
	 *
	 * @see                     #clone
	 */
	public void copyInto(BT_ConstantPoolItem[] elements) {
		System.arraycopy(elementData, 0, elements, 0, elementCount);
	}
	/**
	 * Answers the element at the specified location in this BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           location        the index of the element to return in this BT_ConstantPoolItemVector
	 * @return          the element at the specified location
	 *
	 * @exception       ArrayIndexOutOfBoundsException when location < 0 || >= size()
	 *
	 * @see                     #size
	 */
	public BT_ConstantPoolItem elementAt(int location) {
		//BB fast range check, no need to check for < 0 as the array access will throw the exception
		if (location < elementCount) {
			return elementData[location];
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	/**
	 * Answers an Enumeration on the elements of this BT_ConstantPoolItemVector. The
	 * results of the Enumeration may be affected if the contents
	 * of this BT_ConstantPoolItemVector are modified.
	 *
	 *
	 * @return          an Enumeration of the elements of this BT_ConstantPoolItemVector
	 *
	 * @see                     #elementAt
	 * @see                     Enumeration
	 */
	public Enumeration elements() {
		return new BT_ArrayEnumerator(elementData, elementCount);
	}
	/**
	 * Ensures that this BT_ConstantPoolItemVector can hold the specified number of elements
	 * without growing.
	 *
	 *
	 * @param           minimumCapacity  the minimum number of elements that this
	 *                                  vector will hold before growing
	 *
	 * @see                     #capacity
	 */
	public void ensureCapacity(int minimumCapacity) {
		if (elementData.length < minimumCapacity)
			grow(minimumCapacity);
	}
	/**
	 * Answers the first element in this BT_ConstantPoolItemVector.
	 *
	 *
	 * @return          the element at the first position
	 *
	 * @exception       NoSuchElementException  when this vector is empty
	 *
	 * @see                     #elementAt
	 * @see                     #lastElement
	 * @see                     #size
	 */
	public BT_ConstantPoolItem firstElement() {
		if (elementCount == 0)
			throw new NoSuchElementException();
		return elementData[0];
	}
	private void grow(int newCapacity) {
		BT_ConstantPoolItem newData[] = new BT_ConstantPoolItem[newCapacity];
		System.arraycopy(elementData, 0, newData, 0, elementCount);
		elementData = newData;
	}
	/**
	 * Searches in this BT_ConstantPoolItemVector for the index of the specified object. The
	 * search for the object starts at the beginning and moves towards the
	 * end of this BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           object  the object to find in this BT_ConstantPoolItemVector
	 * @return          the index in this BT_ConstantPoolItemVector of the specified element, -1 if the
	 *                          element isn't found
	 *
	 * @see                     #contains
	 * @see                     #lastIndexOf
	 */
	public int indexOf(BT_ConstantPoolItem object) {
		return indexOf(object, 0);
	}
	/**
	 * Searches in this BT_ConstantPoolItemVector for the index of the specified object. The
	 * search for the object starts at the specified location and moves
	 * towards the end of this BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           object  the object to find in this BT_ConstantPoolItemVector
	 * @param           location        the index at which to start searching
	 * @return          the index in this BT_ConstantPoolItemVector of the specified element, -1 if the
	 *                          element isn't found
	 *
	 * @exception       ArrayIndexOutOfBoundsException when location < 0
	 *
	 * @see                     #contains
	 * @see                     #lastIndexOf
	 */
	public int indexOf(BT_ConstantPoolItem object, int location) {
		BT_ConstantPoolItem element;
		for (int i = location; i < elementCount; i++) {
			if ((element = elementData[i]) == object)
				return i;
			if ((element != null) && (element.equals(object)))
				return i;
		}
		return -1;
	}
	/**
	 * Inserts the specified object into this BT_ConstantPoolItemVector at the specified
	 * location. This object is inserted before any previous element at
	 * the specified location. If the location is equal to the size of
	 * this BT_ConstantPoolItemVector, the object is added at the end.
	 *
	 *
	 * @param           object  the object to insert in this BT_ConstantPoolItemVector
	 * @param           location        the index at which to insert the element
	 *
	 * @exception       ArrayIndexOutOfBoundsException when location < 0 || > size()
	 *
	 * @see                     #addElement
	 * @see                     #size
	 */
	public void insertElementAt(
		BT_ConstantPoolItem object,
		int location) {
		int count;

		if (location >= 0 && location <= elementCount) {
			if (elementCount == elementData.length) {
				int newCapacity =
					(capacityIncrement == 0 ? elementCount : capacityIncrement)
						+ elementCount;
				if (newCapacity == 0)
					newCapacity++;
				grow(newCapacity);
			}
			if ((count = elementCount - location) > 0) {
				System.arraycopy(
					elementData,
					location,
					elementData,
					location + 1,
					count);
			}
			elementData[location] = object;
			elementCount++;
			return;
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	/**
	 * Answers if this BT_ConstantPoolItemVector has no elements, a size of zero.
	 *
	 *
	 * @return          true if this BT_ConstantPoolItemVector has no elements, false otherwise
	 *
	 * @see                     #size
	 */
	public boolean isEmpty() {
		return elementCount == 0;
	}
	/**
	 * Answers the last element in this BT_ConstantPoolItemVector.
	 *
	 *
	 * @return          the element at the last position
	 *
	 * @exception       NoSuchElementException  when this vector is empty
	 *
	 * @see                     #elementAt
	 * @see                     #firstElement
	 * @see                     #size
	 */
	public BT_ConstantPoolItem lastElement() {
		try {
			return elementData[elementCount - 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchElementException();
		}
	}
	/**
	 * Searches in this BT_ConstantPoolItemVector for the index of the specified object. The
	 * search for the object starts at the end and moves towards the start
	 * of this BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           object  the object to find in this BT_ConstantPoolItemVector
	 * @return          the index in this BT_ConstantPoolItemVector of the specified element, -1 if the
	 *                          element isn't found
	 *
	 * @see                     #contains
	 * @see                     #indexOf
	 */
	public int lastIndexOf(BT_ConstantPoolItem object) {
		return lastIndexOf(object, elementCount - 1);
	}
	/**
	 * Searches in this BT_ConstantPoolItemVector for the index of the specified object. The
	 * search for the object starts at the specified location and moves
	 * towards the start of this BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           object  the object to find in this BT_ConstantPoolItemVector
	 * @param           location        the index at which to start searching
	 * @return          the index in this BT_ConstantPoolItemVector of the specified element, -1 if the
	 *                          element isn't found
	 *
	 * @exception       ArrayIndexOutOfBoundsException when location >= size()
	 *
	 * @see                     #contains
	 * @see                     #indexOf
	 */
	public int lastIndexOf(BT_ConstantPoolItem object, int location) {
		BT_ConstantPoolItem element;
		if (location < elementCount) {
			for (int i = location; i >= 0; i--) {
				if ((element = elementData[i]) == object)
					return i;
				if ((element != null) && (element.equals(object)))
					return i;
			}
			return -1;
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	/**
	 * Removes all elements from this BT_ConstantPoolItemVector, leaving the size zero and
	 * the capacity unchanged.
	 *
	 *
	 * @see                     #isEmpty
	 * @see                     #size
	 */
	public void removeAllElements() {
		for (int i = 0; i < elementCount; i++) {
			elementData[i] = null;
		}
		elementCount = 0;
	}
	/**
	 * Removes the first occurrence, starting at the beginning and
	 * moving towards the end, of the specified object from this
	 * BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           object  the object to remove from this BT_ConstantPoolItemVector
	 * @return          true if the specified object was found, false otherwise
	 *
	 * @see                     #removeAllElements
	 * @see                     #removeElementAt
	 * @see                     #size
	 */
	public boolean removeElement(BT_ConstantPoolItem object) {
		int index;
		if ((index = indexOf(object, 0)) == -1)
			return false;
		removeElementAt(index);
		return true;
	}
	/**
	 * Removes the element at the specified location from this BT_ConstantPoolItemVector.
	 *
	 *
	 * @param           location        the index of the element to remove
	 *
	 * @exception       ArrayIndexOutOfBoundsException when location < 0 || >= size()
	 *
	 * @see                     #removeElement
	 * @see                     #removeAllElements
	 * @see                     #size
	 */
	public void removeElementAt(int location) {
		if (location < elementCount) {
			int size;
			elementCount--;
			if ((size = elementCount - location) > 0) {
				System.arraycopy(
					elementData,
					location + 1,
					elementData,
					location,
					size);
			}
			elementData[elementCount] = null;
			return;
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	/**
	 * Replaces the element at the specified location in this BT_ConstantPoolItemVector with
	 * the specified object.
	 *
	 *
	 * @param           object  the object to add to this BT_ConstantPoolItemVector
	 * @param           location        the index at which to put the specified object
	 *
	 * @exception       ArrayIndexOutOfBoundsException when location < 0 || >= size()
	 *
	 * @see                     #size
	 */
	public void setElementAt(BT_ConstantPoolItem object, int location) {
		if (location < elementCount) {
			elementData[location] = object;
			return;
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	/**
	 * Sets the size of this BT_ConstantPoolItemVector to the specified size. If there
	 * are more than length elements in this BT_ConstantPoolItemVector, the elements
	 * at end are lost. If there are less than length elements in
	 * the BT_ConstantPoolItemVector, the additional elements contain null.
	 *
	 *
	 * @param           length  the new size of this BT_ConstantPoolItemVector
	 *
	 * @see                     #size
	 */
	public void setSize(int length) {
		ensureCapacity(length);
		if (elementCount > length) {
			for (int i = length; i < elementCount; i++)
				elementData[i] = null;
		}
		elementCount = length;
	}
	/**
	 * Answers the number of elements in this BT_ConstantPoolItemVector.
	 *
	 *
	 * @return          the number of elements in this BT_ConstantPoolItemVector
	 *
	 * @see                     #elementCount
	 * @see                     #lastElement
	 */
	public int size() {
		return elementCount;
	}
	/**
	 * Answers the string representation of this BT_ConstantPoolItemVector.
	 *
	 *
	 * @return          the string representation of this BT_ConstantPoolItemVector
	 *
	 * @see                     #elements
	 */
	public String toString() {
		if (elementCount == 0)
			return "[]";
		int length = elementCount - 1;
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		for (int i = 0; i < length; i++) {
			buffer.append(elementData[i]);
			buffer.append(',');
		}
		buffer.append(elementData[length]);
		buffer.append(']');
		return buffer.toString();
	}
	/**
	 * Sets the capacity of this BT_ConstantPoolItemVector to be the same as the size.
	 *
	 *
	 * @see                     #capacity
	 * @see                     #ensureCapacity
	 * @see                     #size
	 */
	public void trimToSize() {
		if (elementData.length != elementCount)
			grow(elementCount);
	}
	// ---------- End of code common to all JikesBT Vectors ----------
}