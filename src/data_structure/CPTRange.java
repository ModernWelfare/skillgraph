/*******************************************************************************
 * This files was developed for CS4341: Artificial Intelligence. The course was
 * taken at Worcester Polytechnic Institute.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package data_structure;

import java.util.List;

/**
 * Class for holding the information read from the csv file that define the
 * characteristics of the conditional probability tables inside the bayes net
 * 
 * @author Bohao Li <bli@wpi.edu>
 * 
 */
public class CPTRange {

	private final Range[][] probabilityRanges;

	public CPTRange(List<String> CPTinfo) {

		// get the number of nodes in the graph
		int numberOfNodes = CPTinfo.size();
		// initialize the array to store the ranges
		probabilityRanges = new Range[numberOfNodes][numberOfNodes];

		for (int i = 0; i < numberOfNodes; i++) {
			String row = CPTinfo.get(i);
			String[] ranges = row.split(",");

			for (int j = 0; j < numberOfNodes; j++) {
				if (!ranges[j].equals("x")) {
					probabilityRanges[i][j] = new Range(ranges[j]);
				}
			}
		}
	}

	public Range getRange(int numberOfParents, int numberOfParentsWithTrueValue) {
		return probabilityRanges[numberOfParents][numberOfParentsWithTrueValue];
	}
}
