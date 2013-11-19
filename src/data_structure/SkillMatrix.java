package data_structure;

import java.util.ArrayList;
import java.util.List;

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
}
