package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import data_structure.Skill;
import data_structure.SkillGraph;

/**
 * class for holding various functions on a skill graph
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class GraphFunctions {
	private GraphFunctions() {
	}

	/**
	 * function to get all possible merges given the skill graph
	 * 
	 * @return a list of Points of the two skill indices that can be merged
	 */
	public static List<Point> getAllPossibleMerges(final SkillGraph skillGraph) {
		int numberOfSkills = skillGraph.getNumberOfSkills();
		int[][] skillMatrix = skillGraph.generateSkillMatrix();
		List<Point> possibleMerges = new ArrayList<Point>();

		// i = parent, j = child
		for (int i = 0; i < numberOfSkills; i++) {
			for (int j = 0; j < numberOfSkills; j++) {
				if (skillMatrix[i][j] == 1) {
					if (i == j) {
						System.err.println("Self cycle detected");
						System.out.println("Skill index = " + i);
						System.exit(-1);
					} else {
						SkillGraph skillGraph2 = new SkillGraph(skillGraph);
						skillGraph2.mergeSkills(i, j);

						boolean cycles = cycleTest(skillGraph2);

						if (!cycles) {
							Point mergePair = new Point(i, j);
							possibleMerges.add(mergePair);
						}
					}
				}
			}
		}

		return possibleMerges;
	}

	// copies matrix
	// assumes square matrix
	public static int[][] matrixCopy(final int otherMatrix[][]) {
		int size = otherMatrix.length;
		int[][] newMatrix = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				newMatrix[i][j] = otherMatrix[i][j];
			}
		}

		return newMatrix;
	}

	public static boolean cycleTest(final SkillGraph skillGraph) {
		boolean returnValue = true;
		int numberOfSkills = skillGraph.getNumberOfSkills();
		List<Skill> skillList = topologicalSort(skillGraph);

		if (skillList.size() == numberOfSkills) {
			returnValue = false;
		}

		return returnValue;
	}

	// determine if the (undirected) skill graph is connected
	public static boolean isMatrixConnected(final int[][] skillMatrix) {
		boolean returnValue = false;

		int[][] workingMatrix = GraphFunctions.matrixCopy(skillMatrix);
		int size = workingMatrix.length;

		Stack<Integer> connectedVertices = new Stack<Integer>();
		List<Integer> fringeVertices = new ArrayList<Integer>();
		List<Integer> newFringeVertices = new ArrayList<Integer>();

		// convert to undirected graph

		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				workingMatrix[j][i] = workingMatrix[i][j];
			}
		}

		fringeVertices.add(0);
		connectedVertices.push(0);

		while (!fringeVertices.isEmpty()) {
			newFringeVertices.clear();

			// for all vertices on the fringe
			for (int i = 0; i < fringeVertices.size(); i++) {
				int vertex = fringeVertices.get(i);

				// add all vertices that are connected to this one
				for (int j = 0; j < size; j++) {
					if (workingMatrix[vertex][j] == 1) {
						if (!newFringeVertices.contains(j)
								&& connectedVertices.search(j) == -1) {
							newFringeVertices.add(j);
							connectedVertices.push(j);
						}
					}
				}
			}

			fringeVertices.clear();

			for (int i = 0; i < newFringeVertices.size(); i++) {
				fringeVertices.add(newFringeVertices.get(i));
			}
		}

		if (connectedVertices.size() == size) {
			returnValue = true;
		}

		/*
		 * else { System.out.println("Failed connectivity check");
		 * System.out.println(connectedVertices.size());
		 * printSkillMatrix(workingMatrix); }
		 */

		return returnValue;
	}

	// returns a topological sort of the graph
	// returns an incomplete list if the graph cannot be sorted (has cycles)
	public static List<Skill> topologicalSort(final SkillGraph skillGraph) {
		List<Skill> skillList = new ArrayList<Skill>();

		int numberOfSkills = skillGraph.getNumberOfSkills();
		int[][] skillMatrix = skillGraph.generateSkillMatrix();
		int[][] currentSkillMatrix = matrixCopy(skillMatrix);

		boolean stuck = false;

		while (skillList.size() < numberOfSkills && !stuck) {
			int skillToAdd = -1;

			// look for a skill with no incoming edges

			for (int i = 0; i < numberOfSkills && skillToAdd == -1; i++) {
				// skip if already added
				if (!skillList.contains(skillGraph.getSkill(i))) {
					boolean noParents = true;

					for (int j = 0; j < numberOfSkills && noParents; j++) {
						if (currentSkillMatrix[j][i] == 1) {
							noParents = false;
						}
					}

					if (noParents) {
						skillToAdd = i;
					}
				}
			}

			// add the skill to the topologically sorted list and update the
			// current matrix
			if (skillToAdd != -1) {
				skillList.add(skillGraph.getSkill(skillToAdd));

				for (int i = 0; i < numberOfSkills; i++) {
					currentSkillMatrix[skillToAdd][i] = 0;
				}
			}
		}

		return skillList;
	}

	public static boolean isLevelInRange(int[][] skillMatrix, int minLevel,
			int maxLevel) {
		boolean returnValue = false;
		int numberOfLevels = getNumberOfLevels(skillMatrix);

		if (minLevel <= numberOfLevels && maxLevel >= numberOfLevels) {
			returnValue = true;
		}

		return returnValue;
	}

	// slightly different topological sort
	public static int getNumberOfLevels(int[][] skillMatrix) {
		if (skillMatrix.length < 1) {
			System.err
					.println("Matrix of zero size when getting the number of levels in the skill graph");
			System.exit(-1);
		}

		int numberOfLevels = 0;
		int numberOfSkills = skillMatrix.length;
		int[][] currentSkillMatrix = matrixCopy(skillMatrix);
		List<Integer> skillList = new ArrayList<Integer>();
		boolean stuck = false;

		while (skillList.size() < numberOfSkills && !stuck) {
			List<Integer> addList = new ArrayList<Integer>();

			// look for *all* skills with no incoming edges

			for (int i = 0; i < numberOfSkills; i++) {
				if (!skillList.contains(i)) {
					boolean noParents = true;

					for (int j = 0; j < numberOfSkills && noParents; j++) {
						if (currentSkillMatrix[j][i] == 1) {
							noParents = false;
						}
					}

					if (noParents) {
						addList.add(i);
					}
				}
			}

			// add the skill to the topologically sorted list and update the
			// current matrix
			for (int i = 0; i < addList.size(); i++) {
				int skillToAdd = addList.get(i);
				skillList.add(skillToAdd);

				for (int j = 0; j < numberOfSkills; j++) {
					currentSkillMatrix[skillToAdd][j] = 0;
				}
			}

			if (!addList.isEmpty()) {
				numberOfLevels++;
			}
		}

		if (skillList.size() != numberOfSkills) {
			System.err
					.println("possible cycle detected when getting the number of levels in the skill graph");
			System.exit(-1);
		}

		return numberOfLevels;
	}

	public static void printSkillMatrix(int[][] skillMatrix) {
		for (int i = 0; i < skillMatrix.length; i++) {
			for (int j = 0; j < skillMatrix[i].length; j++) {
				System.out.print(skillMatrix[i][j]);
				System.out.print(" ");
			}

			System.out.println("\n");
		}
	}
}