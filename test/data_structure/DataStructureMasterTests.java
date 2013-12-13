package data_structure;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.MatlabFileWriter;
import util.RandomGraphGenerator;

public class DataStructureMasterTests {
	@Test
	public void testGraphCreationNoError() {
		SkillGraph graph = new SkillGraph("10/SkillGraph.csv",
				"10/ItemToSkillMapping.csv", "Example/CPT_Ranges1.csv",
				"10/GuessAndSlipRanges.csv");
		System.out.println(graph.toString());
		MatlabFileWriter.outPutSkillGraphMatlabFile(graph, 2, "sampleDag.m");
		assertTrue(true);
	}

	// @Test
	// public void testSkillMatrixAlternatePath() {
	// int[][] skillArray = { { 0, 1, 0, 1 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 },
	// { 0, 0, 0, 0 } };
	// assertTrue(!(new SkillMatrix(skillArray)).isFreeOfAlternatePath(0, 3));
	// assertTrue((new SkillMatrix(skillArray)).isFreeOfAlternatePath(1, 2));
	// }

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
		int[] a = { 2, 4, 1, 2, 1, 3 };
		RandomGraphGenerator.generateRandomGraph(a, "10");
		assertTrue(true);
	}

	//
	// @Test
	// public void testGraphCreationFromGeneratedDataNoError() {
	// SkillGraph graph = new SkillGraph("2/SkillGraph.csv",
	// "2/ItemToSkillMapping.csv", "Example/CPT_Ranges1.csv",
	// "2/GuessAndSlipRanges.csv");
	// System.out.println(graph.toString());
	//
	// }

	@Test
	public void testCopying() {
		// copy the skill graph
		// test they are equal

		// change name
		// test that they are not equal
		// test each name is correct
		// change name back
		// test that they are equal

		// change a skill link
		// test that they are not equal
		// test each link is correct
		// change skill link back
		// test that they are equal

		// change an item link
		// test that they are not equal
		// test each item link is correct
		// change item link back
		// test that they are equal

		// change a cpt table
		// test that they are not equal
		// test that they are correct
		// change cpt table back
		// test that they ae equal

	}
}
