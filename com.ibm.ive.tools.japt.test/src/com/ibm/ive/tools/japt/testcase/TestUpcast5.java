/*
 * Created on Nov 6, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.ibm.ive.tools.japt.testcase;

/**
 * 
 * @author sfoley
 *
 * This class is primarily designed to test the casting optimizations for startup performance.
 * 
 * It contains various methods which normally cause the verifier to load the class SomeException but
 * with all the upcast optimizations this class will not be needed.
 * 
 * It also contains various other methods to test the dataflow analyzer used for the casting optimizations and 
 * for stackmap creation.
 */
public class TestUpcast5 {
	
	/**
	 * a method upcast that requires upcasting a type from a null array.
	 * @param e
	 */
	void aMethodThatInvokes() {
		SomeException array[] = null;
		
		//this array load occurs on the null type
		SomeException testUpcast = array[1];
		
		testUpcast.aMethodArgFarBelowStackTop(testUpcast, 0, 0, null);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestUpcast6.main(args);
	}
}
