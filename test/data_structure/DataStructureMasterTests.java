package data_structure;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.RandomGraphGenerator;

public class DataStructureMasterTests {
	// @Test
	// public void testGraphCreationNoError() {
	// SkillGraph graph = new SkillGraph("Example/SkillStructure1.csv",
	// "Example/ItemToSkillMapping1.txt", "Example/CPT_Ranges1.csv",
	// "Example/GuessAndSlipRanges1.txt");
	// System.out.println(graph.toString());
	// assertTrue(true);
	// }

	@Test
	public void rangeCreationWithString() {
		Range r = new Range(".5-.7");
		System.out.println(r.getLowerBound());
		System.out.println(r.getUpperBound());
		assertTrue(r.getLowerBound() == 0.5d);
		assertTrue(r.getUpperBound() == 0.7d);
	}

	@Test
	public void testSkillMatrixAlternatePath() {
		int[][] skillArray = { { 0, 1, 0, 1 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 },
				{ 0, 0, 0, 0 } };
		assertTrue(!(new SkillMatrix(skillArray)).isFreeOfAlternatePath(0, 3));
		assertTrue((new SkillMatrix(skillArray)).isFreeOfAlternatePath(1, 2));
	}

	// @Test
	// public void getAllPossibleMerges() {
	// SkillGraph graph = new SkillGraph("Example/SkillStructure1.csv",
	// "Example/ItemToSkillMapping1.txt", "Example/CPT_Ranges1.csv",
	// "Example/GuessAndSlipRanges1.txt");
	// graph.mergeSkills(2, 3);
	// System.out.println(graph);
	// assertTrue(true);
	// }

	@Test
	public void testRandomGraphGeneration() {
		RandomGraphGenerator.generateRandomGraph(20, 30, 3, 5, 5, 10, "2");
		assertTrue(true);
	}

	@Test
	public void testGraphCreationFromGeneratedDataNoError() {
		SkillGraph graph = new SkillGraph("2/SkillGraph.csv",
				"2/ItemToSkillMapping.csv", "Example/CPT_Ranges1.csv",
				"2/GuessAndSlipRanges.csv");
		System.out.println(graph.toString());
		assertTrue(true);
	}

}
