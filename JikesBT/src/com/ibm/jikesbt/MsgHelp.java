
/*
 * Created on Jan 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.jikesbt;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 2003, 2004  All Rights Reserved
 */

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class contains helper methods for loading resource
 * bundles and formatting external message strings.
 *
 * @author		OTI
 * @version		initial
 */

public final class MsgHelp {

/**
 * Generates a formatted text string given a source string
 * containing "argument markers" of the form "{argNum}"
 * where each argNum must be in the range 0..9. The result
 * is generated by inserting the toString of each argument
 * into the position indicated in the string.
 * <p>
 * To insert the "{" character into the output, use a single
 * backslash character to escape it (i.e. "\{"). The "}"
 * character does not need to be escaped.
 *
 * @author		OTI
 * @version		initial
 *
 * @param		format String
 *					the format to use when printing.
 * @param		args Object[]
 *					the arguments to use.
 * @return		String
 *					the formatted message.
 */
public static String format (String format, Object[] args) {
	StringBuffer answer = new StringBuffer();
	String[] argStrings = new String[args.length];
	for (int i = 0; i < args.length; ++i) {
		if (args[i] == null)
			argStrings[i] = "<null>";
		else
			argStrings[i] = args[i].toString();
	}
	int lastI = 0;
	for (int i = format.indexOf('{', 0); i >= 0; i = format.indexOf('{', lastI)) {
		if (i != 0 && format.charAt(i-1) == '\\') {
			// It's escaped, just print and loop.
			if (i != 1)
				answer.append(format.substring(lastI,i-1));
			answer.append('{');
			lastI = i+1;
		} else {
			// It's a format character.
			if (i > format.length()-3) {
				// Bad format, just print and loop.
				answer.append(format.substring(lastI, format.length()));
				lastI = format.length();
			} else {
				int argnum = (byte) Character.digit(format.charAt(i+1), 10);
				if (argnum < 0 || format.charAt(i+2) != '}') {
					// Bad format, just print and loop.
					answer.append(format.substring(lastI, i+1));
					lastI = i+1;
				} else {
					// Got a good one!
					answer.append(format.substring(lastI, i));
					if (argnum >= argStrings.length)
						answer.append("<missing argument>");
					else
						answer.append(argStrings[argnum]);
					lastI = i + 3;
				}
			}
		}
	}
	if (lastI < format.length())
		answer.append(format.substring(lastI, format.length()));
	return answer.toString();
}
/**
 * Changes the locale of the messages.
 *
 * @author		OTI
 * @version		initial
 *
 * @param		locale Locale
 *					the locale to change to.
 */
static public ResourceBundle setLocale (final Locale locale, final String resource) {
	try {
		return (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				return ResourceBundle.getBundle(resource, locale);
			}});
	} catch (MissingResourceException e) {}
	return null;
}

}
