package data_structure;

import util.ConstantRNG;

/**
 * Class for the CPT tables assigned to each node inside the Bayesian network.
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class CPT {
	// currently using this for all types of nodes
	// maybe need to redesign a little?

	// the number of entries inside the table
	// should be consistent with the array, with the exception of manually
	// setting the size
	private int size;

	// the array holding the entries inside the table
	// [cpt entry][trueEvent/falseEvent]
	private final double probabilityValues[][];

	/**
	 * Constructor for the CPT table
	 * 
	 * @param numberOfParents
	 *            the number of parents the skill to be assigned the CPT table
	 *            has
	 * @param cptTemplate
	 *            the table holding the CPT range information
	 */
	public CPT(int numberOfParents, CPTSkillTemplate cptTemplate) {
		// the binary representation of the table index, representing whether
		// the parents have true values
		String binaryRepresentation;
		// the number of ones in the binary string, representing the number of
		// parents with true values = known parents
		int numberOfOnes;

		size = (int) Math.pow(2, numberOfParents);

		probabilityValues = new double[size][2];

		for (int i = 0; i < size; i++) {
			binaryRepresentation = Integer.toBinaryString(i);
			numberOfOnes = countNumberOfOnes(binaryRepresentation);

			// generate a random value within the range and store it into the
			// array

			double lowerBound = cptTemplate.getLowerBound(numberOfParents,
					numberOfOnes);
			double upperBound = cptTemplate.getUpperBound(numberOfParents,
					numberOfOnes);

			// 0 = false, 1 = true
			probabilityValues[i][1] = ConstantRNG.getNextNumberUniform(
					lowerBound, upperBound);

			// double sillyness
			probabilityValues[i][0] = (100 - probabilityValues[i][1] * 100) / 100;

		}
	}

	// For guess and slip nodes
	public CPT(int index, int guessOrSlip, CPTGuessSlipTemplate cptTemplate) {
		size = 1;

		probabilityValues = new double[size][2];

		double lowerBound = cptTemplate.getLowerBound(index, guessOrSlip);
		double upperBound = cptTemplate.getUpperBound(index, guessOrSlip);

		probabilityValues[0][1] = ConstantRNG.getNextNumberUniform(lowerBound,
				upperBound);
		probabilityValues[0][0] = (100 - probabilityValues[0][1] * 100) / 100;
		;
	}

	// for item nodes
	// skill,guess,slip = the binary string
	// determnistic
	public CPT() {
		size = 8;

		probabilityValues = new double[size][2];

		// skill = 0, guess = 0, slip = 0
		probabilityValues[0][0] = 1;
		probabilityValues[0][1] = 0;

		// skill = 0, guess = 0, slip = 1
		probabilityValues[1][0] = 1;
		probabilityValues[1][1] = 0;

		// skill = 0, guess = 1, slip = 0
		probabilityValues[2][0] = 0;
		probabilityValues[2][1] = 1;

		// skill = 0, guess = 1, slip = 1
		probabilityValues[3][0] = 1;
		probabilityValues[3][1] = 0;

		// skill = 1, guess = 0, slip = 0
		probabilityValues[4][0] = 0;
		probabilityValues[4][1] = 1;

		// skill = 1, guess = 0, slip = 1
		probabilityValues[5][0] = 1;
		probabilityValues[5][1] = 0;

		// skill = 1, guess = 1, slip = 0
		probabilityValues[6][0] = 0;
		probabilityValues[6][1] = 1;

		// skill = 1, guess = 1, slip = 1
		probabilityValues[7][0] = 0;
		probabilityValues[7][1] = 1;
	}

	/**
	 * Construct a new CPT table using the old CPT table
	 * 
	 * @param originalCPT
	 */
	public CPT(CPT originalCPT) {
		size = originalCPT.getSize();
		probabilityValues = new double[size][2];

		for (int i = 0; i < size; i++) {
			probabilityValues[i][0] = originalCPT.getProbability(i, 0);
			probabilityValues[i][1] = originalCPT.getProbability(i, 1);
		}
	}

	public double[][] getProbabilityValues() {
		return probabilityValues;
	}

	// potentially dangeous, but might be useful later if we want to manually do
	// things
	// probably won't be used
	// stop program if error detected
	public void setProbabilityValues(double[][] newProbabilityValues) {
		if (sizeCheck(probabilityValues, newProbabilityValues)) {
			// set values
			for (int i = 0; i < newProbabilityValues.length; i++) {
				for (int j = 0; j < newProbabilityValues[i].length; j++) {
					probabilityValues[i][j] = newProbabilityValues[i][j];
				}
			}
		} else {
			System.err.println("CPT tables are different sizes");
			System.exit(-1);
		}
	}

	public int getSize() {
		return size;
	}

	// potentially dangeous, but might be useful later if we want to manually do
	// things
	// probably won't be used
	public void setSize(int newSize) {
		size = newSize;
	}

	// return the probability that the [(skill is known) || (student guessed) ||
	// (student slipped) || (item is known)]
	public double getProbability(int arrayIndex) {
		return probabilityValues[arrayIndex][1];
	}

	// 0 = false, 1 = true
	public double getProbability(int arrayIndex, int trueEvent) {
		return probabilityValues[arrayIndex][trueEvent];
	}

	public double getProbability(String binaryString) {
		int arrayIndex = Integer.parseInt(binaryString, 2);

		return probabilityValues[arrayIndex][1];
	}

	public double getProbability(String binaryString, int trueEvent) {
		int arrayIndex = Integer.parseInt(binaryString, 2);

		return probabilityValues[arrayIndex][trueEvent];
	}

	public void setProbability(int arrayIndex, double probability) {
		if (probability < 0 || probability > 1) {
			System.err.println("Probability range must be between 0 and 1");
			System.exit(-1);
		} else {
			probabilityValues[arrayIndex][1] = probability;
		}
	}

	public void setProbability(int arrayIndex, int trueEvent, double probability) {
		if (probability < 0 || probability > 1) {
			System.err.println("Probability range must be between 0 and 1");
			System.exit(-1);
		} else {
			probabilityValues[arrayIndex][trueEvent] = probability;
		}
	}

	public void setProbability(String binaryString, double probability) {
		if (probability < 0 || probability > 1) {
			System.err.println("Probability range must be between 0 and 1");
			System.exit(-1);
		} else {
			int arrayIndex = Integer.parseInt(binaryString, 2);
			probabilityValues[arrayIndex][1] = probability;
		}
	}

	public void setProbability(String binaryString, int trueEvent,
			double probability) {
		if (probability < 0 || probability > 1) {
			System.err.println("Probability range must be between 0 and 1");
			System.exit(-1);
		} else {
			int arrayIndex = Integer.parseInt(binaryString, 2);
			probabilityValues[arrayIndex][trueEvent] = probability;
		}
	}

	private int countNumberOfOnes(String binaryNumber) {
		int countOfOnes = 0;

		for (int i = 0; i < binaryNumber.length(); i++) {
			if (binaryNumber.charAt(i) == '1') {
				countOfOnes++;
			}
		}

		return countOfOnes;
	}

	public static boolean sizeCheck(double[][] cpt1, double[][] cpt2) {
		boolean returnValue = true;

		if (cpt1.length != cpt2.length) {
			returnValue = false;
		} else {
			// check table sizes
			for (int i = 0; i < cpt1.length && returnValue; i++) {
				for (int j = 0; j < cpt1[i].length && returnValue; j++) {
					if (cpt1[i].length != cpt2[i].length) {
						returnValue = false;
					}
				}
			}
		}

		return returnValue;
	}

	@Override
	public boolean equals(Object otherCPT) {
		boolean returnValue = false;

		if (otherCPT == null) {
			returnValue = false;
		} else if (this == otherCPT) {
			returnValue = true;
		} else if (!(otherCPT instanceof CPT)) {
			// probably an error

			returnValue = false;
		} else {
			returnValue = true;

			CPT otherCPT1 = (CPT) otherCPT;
			double[][] otherProbabilities = otherCPT1.getProbabilityValues();

			if (sizeCheck(probabilityValues, otherProbabilities)) {
				for (int i = 0; i < probabilityValues.length && returnValue; i++) {
					for (int j = 0; j < probabilityValues[i].length
							&& returnValue; j++) {
						if (probabilityValues[i][j] != otherProbabilities[i][j]) {
							returnValue = false;
						}
					}
				}
			} else {
				returnValue = false;
			}
		}

		return returnValue;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < probabilityValues.length; i++) {
			sb.append(i + ":  ");

			for (int j = 0; j < probabilityValues[i].length; j++) {
				sb.append(probabilityValues[i][j]);
				sb.append("  ");
			}

			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
		}

		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}
}
