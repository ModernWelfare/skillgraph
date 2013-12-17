package util;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.List;

/**
 * Class to hold the functions to generate a random graph given the input
 * parameters
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
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
	 */
	public static void generateRandomGraph(int[] parameters,
			String outputFolderPath) {
		if (!checkParameterBounds(parameters)) {
			System.exit(-1);
		}

		int skillNumLowerBound = parameters[0];
		int skillNumUpperBound = parameters[1];
		int itemNumLowerBound = parameters[2];
		int itemNumUpperBound = parameters[3];
		int numberOfLevelsLowerBound = parameters[4];
		int numberOfLevelsUpperBound = parameters[5];

		// bounds check

		int totalItemNum = 0;

		// generate the semi-random skill-skill matrix
		int[][] skillMatrix = makeRandomMatrix(skillNumLowerBound,
				skillNumUpperBound, numberOfLevelsLowerBound,
				numberOfLevelsUpperBound);
		int numberOfSkills = skillMatrix.length;

		// initialize the array to hold the number of items associated to each
		// skill
		int[] itemNum = new int[numberOfSkills];

		// generate the number of items for each skill and store them to an
		// array
		for (int i = 0; i < numberOfSkills; i++) {
			itemNum[i] = ConstantRNG.getNextInt(itemNumLowerBound,
					itemNumUpperBound);
			totalItemNum += itemNum[i];
		}

		File dir = new File(outputFolderPath);

		if (!dir.exists()) {
			dir.mkdir();
		}

		String skillPath = outputFolderPath + File.separator + "SkillGraph.csv";
		String itemToSkillMappingPath = outputFolderPath + File.separator
				+ "ItemToSkillMapping.csv";
		String guessSlipPath = outputFolderPath + File.separator
				+ "GuessAndSlipRanges.csv";

		outputSkillMatrix(skillMatrix, skillPath);
		outputItemToSkillMapping(itemNum, itemToSkillMappingPath);
		outputGuessAndSlipFile(totalItemNum, guessSlipPath);
	}

	private static boolean checkParameterBounds(final int[] parameters) {
		boolean returnValue = true;

		// check lower bounds

		if (parameters[0] < 1) {
			System.err
					.println("Lower bound for number of skills is less than 1 "
							+ parameters[0]);
			returnValue = false;
		}

		if (parameters[2] < 1) {
			System.err
					.println("Lower bound for number of items per skill is less than 1 "
							+ parameters[2]);
			returnValue = false;
		}

		if (parameters[4] < 1) {
			System.err
					.println("Lower bound for number of levels is less than 1 "
							+ parameters[4]);
			returnValue = false;
		}

		if (parameters[0] > parameters[1]) {
			System.err.println("Number of skills lower bound > upper bound "
					+ parameters[0] + " " + parameters[1]);
			returnValue = false;
		}

		if (parameters[2] > parameters[3]) {
			System.err
					.println("Number of items per skill lower bound > upper bound "
							+ parameters[2] + " " + parameters[3]);
			returnValue = false;
		}

		if (parameters[4] > parameters[5]) {
			System.err.println("Number of levels lower bound > upper bound "
					+ parameters[4] + " " + parameters[5]);
			returnValue = false;
		}

		return returnValue;
	}

	public static int[][] makeRandomMatrix(int skillNumLowerBound,
			int skillNumUpperBound, int numberOfLevelsLowerBound,
			int numberOfLevelsUpperBound) {
		// store the number of possible graphs for each skill
		BigInteger[] possibleGraphs = new BigInteger[(skillNumUpperBound - skillNumLowerBound) + 1];
		BigInteger[] skillIndices = new BigInteger[(skillNumUpperBound - skillNumLowerBound) + 1];
		BigInteger maxNumber = new BigInteger("-1");

		int randomMatrix[][] = { { -1, -1 }, { -1, -1 } };

		for (int i = 0; i < possibleGraphs.length; i++) {
			int numberOfSkills = skillNumLowerBound + i;
			int variableSpots = ((numberOfSkills - 1) * numberOfSkills) / 2;
			BigInteger numberOfGraphs = new BigInteger("2");

			possibleGraphs[i] = numberOfGraphs.pow(variableSpots);

			if (i == 0) {
				skillIndices[i] = new BigInteger("0");
			} else {
				skillIndices[i] = skillIndices[i - 1]
						.add(possibleGraphs[i - 1]);
			}
		}

		maxNumber = skillIndices[skillIndices.length - 1]
				.add(possibleGraphs[possibleGraphs.length - 1]);

		boolean foundValidMatrix = false;

		while (!foundValidMatrix) {
			// choose a random number
			// construct the graph
			// check the graph

			int numberOfBits = ((skillNumUpperBound - 1) * skillNumUpperBound) / 2;
			BigInteger randomNumber = new BigInteger(numberOfBits,
					ConstantRNG.getRNG());

			// get random value in range
			while (randomNumber.compareTo(maxNumber) == 1) {
				randomNumber = new BigInteger(numberOfBits,
						ConstantRNG.getRNG());
			}

			// construct the graph given the bigint

			// figure out the number of skills

			int size = skillNumUpperBound;

			for (int i = 0; i < skillIndices.length - 1; i++) {
				if (randomNumber.compareTo(skillIndices[i]) >= 0
						&& randomNumber.compareTo(skillIndices[i + 1]) < 0) {
					size = skillNumLowerBound + i;
				}
			}

			// convert to string and add correct leading number of zeros

			String bitString = randomNumber.toString(2);

			while (bitString.length() < numberOfBits) {
				bitString = "0".concat(bitString);
			}

			randomMatrix = new int[size][size];
			int stringSpot = 0;

			// System.out.println("Random number = " + randomNumber);
			// System.out.println("Bit string = " + bitString);
			// System.out.println("Size = " + size);

			// exlude last row
			for (int i = 0; i < size - 1; i++) {
				for (int j = i + 1; j < size; j++) {
					// exclude first column and diagonal
					if (i != j && j != 0) {
						randomMatrix[i][j] = Integer.parseInt(Character
								.toString(bitString.charAt(stringSpot)));
						stringSpot++;
					}
				}
			}

			if (GraphFunctions.isMatrixConnected(randomMatrix)
					&& GraphFunctions.isLevelInRange(randomMatrix,
							numberOfLevelsLowerBound, numberOfLevelsUpperBound)) {
				foundValidMatrix = true;
			}
		}

		return randomMatrix;
	}

	private static boolean containsSkillMatrix(
			final List<int[][]> iterationSkillMatrices, int[][] newSkillMatrix) {
		boolean returnValue = false;

		for (int i = 0; i < iterationSkillMatrices.size() && !returnValue; i++) {
			if (compareMatrices(iterationSkillMatrices.get(i), newSkillMatrix) == 0) {
				returnValue = true;
			}
		}

		return returnValue;
	}

	private static void sortMatrices(List<int[][]> iterationSkillMatrices) {
		int size = iterationSkillMatrices.size();

		// basic selection sort for now
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				int[][] matrix1 = iterationSkillMatrices.get(i);
				int[][] matrix2 = iterationSkillMatrices.get(j);

				// swap if out of order
				if (compareMatrices(matrix1, matrix2) > 0) {
					iterationSkillMatrices.set(i, matrix2);
					iterationSkillMatrices.set(j, matrix1);
				}
			}
		}
	}

	// -1 if matrix 1 is smaller, 0 if equal, 1 if matrix 2 is smaller
	// bigger = first to have a 1
	// assumes equal size
	private static int compareMatrices(int[][] matrix1, int[][] matrix2) {
		int returnValue = 0;

		for (int i = 0; i < matrix1.length && returnValue == 0; i++) {
			for (int j = 0; j < matrix1.length && returnValue == 0; j++) {
				if (matrix1[i][j] != matrix2[i][j]) {
					if (matrix1[i][j] == 0) {
						returnValue = -1;
					} else {
						returnValue = 1;
					}
				}
			}
		}

		return returnValue;
	}

	/**
	 * Outputs the skill to skill mapping matrix to a file
	 * 
	 * @param skillMatrix
	 *            the skill matrix
	 * @param folderName
	 *            the name of the folder in which the file would be stored
	 * 
	 */
	private static void outputSkillMatrix(int[][] skillMatrix, String filePath) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < skillMatrix.length; i++) {
			for (int j = 0; j < skillMatrix[i].length; j++) {
				sb.append(Integer.toString(skillMatrix[i][j]) + ",");
			}

			sb.append("\n");
		}

		// remove the extra \n
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}

		// write string to file

		String outputString = sb.toString();
		writeFile(filePath, outputString);
	}

	private static void outputItemToSkillMapping(int[] itemNum, String filePath) {
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

		// remove the extra \n
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}

		// write string to file

		String outputString = sb.toString();
		writeFile(filePath, outputString);
	}

	/**
	 * Outputs the file containing the guess and slip ranges for each item
	 * 
	 * @param folderName
	 *            the folder in which the file would be stored
	 * @param numberOfItems
	 *            the number of guess and slip entries that should be generated
	 */
	private static void outputGuessAndSlipFile(int numberOfItems,
			String filePath) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < numberOfItems; i++) {
			String minGuess = Double.toString(Constants.MIN_GUESS);
			String maxGuess = Double.toString(Constants.MAX_GUESS);
			String minSlip = Double.toString(Constants.MIN_SLIP);
			String maxSlip = Double.toString(Constants.MAX_SLIP);

			sb.append(minGuess);
			sb.append("-");
			sb.append(maxGuess);

			sb.append(",");

			sb.append(minSlip);
			sb.append("-");
			sb.append(maxSlip);

			sb.append(",");

			sb.append("Guess" + Integer.toString(i + 1) + ", ");
			sb.append("Slip" + Integer.toString(i + 1) + "\n");
		}

		// remove the extra \n
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}

		// write string to file

		String outputString = sb.toString();
		writeFile(filePath, outputString);
	}

	private static void writeFile(String filePath, String outputString) {
		try {
			FileWriter file = new FileWriter(filePath, false);

			file.write(outputString);
			file.flush();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
