package com.ibm.ive.tools.japt.reduction.ita;

import java.util.Iterator;

/**
 * Represents a propagator to which objects can be thrown.  For an object to be thrown, 
 * this means that the object is not propagated to all targets of this propagator, 
 * but only those targets which initiated this propagator as a target first.
 * <p>  
 * In particular, if method x calls method y, and an object is thrown to method y, 
 * then that object is not propagated to methods called by y
 * or fields accessed by y (targets of y initiated by y), the object is only propagated to 
 * x and other methods calling y (targets not initiated by y).
 * <p>
 * Another way to think of it: the propagated object never really exists inside y, it is only
 * "thrown" through y.  It is thrown to methods that previously invoked y.
 * @author sfoley
 *
 */
abstract class ThrowingPropagator extends ObjectPropagator {

	private ObjectSet propagatedThrownObjects;
	private ObjectSet allPropagatedThrownObjects;
	
	private static final short requiresThrowingRepropagation = 0x8;
	
	void scheduleThrowingRepropagation() {
		flags |= requiresThrowingRepropagation;
	}
	
	void addThrownPropagatedObject(FieldObject obj, ThrowingPropagator from) {
		logPropagation(obj, THROWN, from);
		if(propagatedThrownObjects == null) {
			propagatedThrownObjects = new ObjectSet();
		}
		propagatedThrownObjects.add(obj);
	}
	
	boolean isThrownPropagated(FieldObject obj) {
		return allPropagatedThrownObjects != null && allPropagatedThrownObjects.contains(obj);
	}
	
	boolean hasThrownPropagated() {
		return allPropagatedThrownObjects != null && allPropagatedThrownObjects.size() > 0;
	}
			
	/*
	 * Overriding methods:
	 */
	boolean propagateObjects() throws PropagationException {
		boolean result = super.propagateObjects();
		if(propagatedThrownObjects != null && propagatedThrownObjects.size() > 0) {
			if(allPropagatedThrownObjects == null) {
				allPropagatedThrownObjects = new ObjectSet();
			}
			
			ObjectSet nowPropagating = propagatedThrownObjects;
			propagatedThrownObjects = null;
			do {
				Iterator iterator = nowPropagating.iterator();
				while(iterator.hasNext()) {
					FieldObject obj = (FieldObject) iterator.next();
					propagateNewThrownObject(obj);
					allPropagatedThrownObjects.add(obj);
				}
				nowPropagating.clear();
				if(propagatedThrownObjects == null || propagatedThrownObjects.size() == 0) {
					propagatedThrownObjects = nowPropagating;
					return true;
				}
				nowPropagating.addAll(propagatedThrownObjects);
				propagatedThrownObjects.clear();
			} while(true);
		}
		return result;
	}
	
	protected boolean repropagateObjects() {
		boolean result = false;
		if(super.requiresRepropagation()) {
			result = super.repropagateObjects();
		}
		if((flags & requiresThrowingRepropagation) != 0) {
			if(allPropagatedThrownObjects.size() > 0) {
				result = true;
				Iterator it = allPropagatedThrownObjects.iterator();
				while(it.hasNext()) {
					FieldObject object = (FieldObject) it.next();
					propagateOldThrownObject(object);
				}
			}
			flags &= ~requiresThrowingRepropagation;
		}
		return result;
	}
	
	boolean requiresRepropagation() {
		return ((flags & requiresThrowingRepropagation) != 0) || super.requiresRepropagation();
	}
	
	abstract void propagateNewThrownObject(FieldObject obj);
	
	abstract void propagateOldThrownObject(FieldObject obj);
}
