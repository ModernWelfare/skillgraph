package util;

import data_structure.SkillMatrix;

/**
 * Class to hold the functions to generate a random graph given the input
 * parameters
 * 
 * @author Bohao Li <bli@wpi.edu>
 * 
 */
public class RandomGraphGenerator {

	/**
	 * Function to generate a random skill graph
	 * 
	 * @param skillNumLowerBound
	 *            the lower bound of the number of skills
	 * @param skillNumUpperBound
	 *            the upper bound of the number of skills
	 * @param itemNumLowerBound
	 *            the lower bound of the number of items tied to each skill
	 * @param itemNumUpperBound
	 *            the upper bound of the number of items tied to each skill
	 * @return a skillGraph generated that satisfies the constraints
	 */
	public static void generateRandomGraph(int skillNumLowerBound,
			int skillNumUpperBound, int itemNumLowerBound,
			int itemNumUpperBound, int numberOfLevelsLowerBound,
			int numberOfLevelsUpperBound) {
		int skillNum = (int) ConstantRNG.getNextNumberUniform(
				skillNumLowerBound, skillNumUpperBound);
		int itemNum = (int) ConstantRNG.getNextNumberUniform(itemNumLowerBound,
				itemNumUpperBound);
		int levelNum = (int) ConstantRNG.getNextNumberUniform(
				numberOfLevelsLowerBound, numberOfLevelsUpperBound);

		// initialize the new matrix to represent the skill graph
		int[][] skillMatrix = new int[skillNum][skillNum];

		// start with a disconnected graph
		for (int i = 0; i < skillNum; i++) {
			for (int j = 0; j < skillNum; j++) {
				skillMatrix[i][j] = 0;
			}
		}

		SkillMatrix sMatrix = new SkillMatrix(skillMatrix);

		sMatrix.makeRandomMatrixWithNoCycle(levelNum);

		sMatrix.outPutToFile("1");

	}

}
