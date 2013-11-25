package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
			int numberOfLevelsUpperBound, String outputFolderName) {
		int skillNum = (int) ConstantRNG.getNextNumberUniform(
				skillNumLowerBound, skillNumUpperBound);
		int levelNum = (int) ConstantRNG.getNextNumberUniform(
				numberOfLevelsLowerBound, numberOfLevelsUpperBound);
		int totalItemNum = 0;

		// initialize the new matrix to represent the skill graph
		int[][] skillMatrix = new int[skillNum][skillNum];

		// initialize the array to hold the number of items associated to each
		// skill
		int[] itemNum = new int[skillNum];

		// start with a disconnected graph
		for (int i = 0; i < skillNum; i++) {
			for (int j = 0; j < skillNum; j++) {
				skillMatrix[i][j] = 0;
			}
		}

		SkillMatrix sMatrix = new SkillMatrix(skillMatrix);
		sMatrix.makeRandomMatrixWithNoCycle(levelNum);
		sMatrix.outPutToFile(outputFolderName);

		// generate the number of items for each skill and store them to an
		// array
		for (int i = 0; i < skillNum; i++) {
			itemNum[i] = (int) ConstantRNG.getNextNumberUniform(
					itemNumLowerBound, itemNumUpperBound);
			totalItemNum += itemNum[i];
		}

		outputItemToSkillMapping(outputFolderName, itemNum);

		outputGuessAndSlipFile(outputFolderName, totalItemNum);

	}

	private static void outputItemToSkillMapping(String folderName,
			int[] itemNum) {
		String path = folderName + "/ItemToSkillMapping.csv";

		File dir = new File(folderName);

		if (!dir.exists()) {
			dir.mkdir();
		}

		StringBuilder sb = new StringBuilder();

		int itemIndex = 0;

		for (int i = 0; i < itemNum.length; i++) {
			for (int j = 0; j < itemNum[i]; j++) {
				sb.append(Integer.toString(itemIndex) + ","
						+ Integer.toString(i) + ",");
				sb.append("Item" + Integer.toString(itemIndex + 1) + ","
						+ "Skill" + Integer.toString(i + 1));
				sb.append("\n");
				itemIndex++;
			}
		}

		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Outputs the file containing the guess and slip ranges for each item
	 * 
	 * @param folderName
	 *            the folder in which the file would be stored
	 * @param numberOfItems
	 *            the number of guess and slip entries that should be generated
	 */
	private static void outputGuessAndSlipFile(String folderName,
			int numberOfItems) {

		String path = folderName + "/GuessAndSlipRanges.csv";

		File dir = new File(folderName);

		if (!dir.exists()) {
			dir.mkdir();
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numberOfItems; i++) {
			sb.append("0.1-0.3,0.05-0.2,");
			sb.append("Guess" + Integer.toString(i + 1) + ", ");
			sb.append("Slip" + Integer.toString(i + 1) + "\n");
		}

		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			writer.write(sb.toString());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
