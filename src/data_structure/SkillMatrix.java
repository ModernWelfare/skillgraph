package data_structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import util.ConstantRNG;

/**
 * Class that holds all information for the skillMatrix used to perform skill
 * graph merging
 * 
 * @author Bohao Li <bli@wpi.edu>
 * 
 */
public class SkillMatrix {
	private final int width;
	private final int[][] skillMatrix;
	private final int MAX_PARENT_PER_CHILD = 4;

	public SkillMatrix(int[][] skillMatrix) {
		this.skillMatrix = skillMatrix;
		this.width = skillMatrix.length;
	}

	/**
	 * function to get all possible merges given the skill graph
	 * 
	 * @return a linked list containing one dimensional integer arrays that
	 *         holds coordinates to arcs that can be removed by merging
	 */
	public List<Integer[]> getAllPossibleMerges() {
		List<Integer[]> possibleArc = new ArrayList<Integer[]>();

		for (int parentIndex = 0; parentIndex < width; parentIndex++) {
			for (int childIndex = 0; childIndex < width; childIndex++) {
				if ((skillMatrix[parentIndex][childIndex] == 1)
						&& isFreeOfAlternatePath(parentIndex, childIndex)) {
					Integer[] newCoordinate = { parentIndex, childIndex };
					possibleArc.add(newCoordinate);
				}
			}
		}
		return possibleArc;
	}

	/**
	 * tests whether an arc can be used for merging by checking if there's an
	 * alternate path between the source and the destination
	 * 
	 * @param coordinate
	 *            the coordinate of the arc inside the array
	 * @return boolean value representing whether there is an alternate path
	 */
	public boolean isFreeOfAlternatePath(int parentSkillIndex,
			int childSkillIndex) {
		boolean returnValue = true;

		// if the coordinate is not valid, return false
		if (skillMatrix[parentSkillIndex][childSkillIndex] != 1) {
			return false;
		}

		for (int i = 0; i < width; i++) {
			if (skillMatrix[parentSkillIndex][i] == 1) {
				returnValue &= !hasChildSkill(i, childSkillIndex);
			}
		}
		return returnValue;
	}

	/**
	 * check if the parent has a child skill
	 * 
	 * @param parentSkillIndex
	 * @param childSkillIndex
	 * @return
	 */
	private boolean hasChildSkill(int parentSkillIndex, int childSkillIndex) {

		if (skillMatrix[parentSkillIndex][childSkillIndex] == 1) {
			return true;
		}

		for (int i = 0; i < width; i++) {
			if (skillMatrix[parentSkillIndex][i] == 1) {
				return hasChildSkill(i, childSkillIndex);
			}
		}

		return false;
	}

	public int[][] getSkillMatrix() {
		return skillMatrix;
	}

	/**
	 * makes a graph with no undirected cycles of three nodes with the levels
	 * specified by levelNum;
	 * 
	 * @param levelNum
	 */
	public void makeRandomMatrixWithNoCycle(int levelNum) {
		int[] levelStartIndex = new int[levelNum];

		// first level starts with index 0
		levelStartIndex[0] = 0;

		if (levelNum <= 1) {
			System.out.println("Level number must be greater than 1!");
			System.exit(-1);
		}

		List<Integer> availableIndexes = new ArrayList<Integer>();

		for (int i = 1; i < width; i++) {
			availableIndexes.add(i);
		}

		for (int i = 1; i < levelNum; i++) {
			Collections.shuffle(availableIndexes);
			levelStartIndex[i] = availableIndexes.remove(0);
		}

		Arrays.sort(levelStartIndex);

		List<ArrayList<Integer>> levelList = new ArrayList<ArrayList<Integer>>();

		// one list per level
		for (int i = 0; i < levelNum - 1; i++) {
			levelList.add(new ArrayList<Integer>());
			// populate each list
			for (int j = levelStartIndex[i]; j < levelStartIndex[i + 1]; j++) {
				levelList.get(i).add(j);
			}
		}

		// add the last level to the list
		levelList.add(new ArrayList<Integer>());
		for (int j = levelStartIndex[levelNum - 1]; j < width; j++) {
			levelList.get(levelNum - 1).add(j);
		}

		// make sure that every node in the first level has at least one child
		for (Integer n : levelList.get(0)) {
			int childIndex = (int) ConstantRNG.getNextNumberUniform(0,
					levelList.get(1).size());
			addArc(n, levelList.get(1).get(childIndex));
		}

		for (int i = 1; i < levelNum; i++) {
			for (int j = 0; j < levelList.get(i).size(); j++) {
				// randomly generate the number of parents a child should have,
				// the number must be below or equal to the number of
				// max_parent_per_child
				int numOfParents = (int) ConstantRNG.getNextNumberUniform(1,
						Math.min(levelList.get(i - 1).size(),
								MAX_PARENT_PER_CHILD + 1));
				int childIndex = levelList.get(i).get(j);
				List<Integer> parentListCopy = new ArrayList<Integer>();

				// make a copy of the original parent list
				for (Integer t : levelList.get(i - 1)) {
					parentListCopy.add(t);
				}

				// shuffle the list for randomness
				Collections.shuffle(parentListCopy);

				for (int k = 0; k < numOfParents; k++) {
					addArc(parentListCopy.remove(0), childIndex);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				sb.append(Integer.toString(skillMatrix[i][j]) + ", ");
			}
			sb.append("\n");
		}

		System.out.println(sb.toString());
		int abc = 0;

	}

	private void addArc(int parentIndex, int childIndex) {
		skillMatrix[parentIndex][childIndex] = 1;
	}
}
