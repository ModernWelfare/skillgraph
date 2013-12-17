package data_structure;

import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import util.GraphFunctions;
import util.RandomGraphGenerator;

public class DataStructureMasterTests {
	// @Test
	// public void testGraphCreationNoError() {
	// SkillGraph graph = new SkillGraph("10/SkillGraph.csv",
	// "10/ItemToSkillMapping.csv", "Example/CPT_Ranges1.csv",
	// "10/GuessAndSlipRanges.csv");
	// System.out.println(graph.toString());
	// MatlabFileWriter.outPutSkillGraphMatlabFile(graph, 2, "sampleDag.m");
	// assertTrue(true);
	// }

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
		int[] a = { 2, 4, 3, 4, 1, 3 };
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
		SkillGraph graph = new SkillGraph("10/SkillGraph.csv",
				"10/ItemToSkillMapping.csv", "Example/CPT_Ranges1.csv",
				"10/GuessAndSlipRanges.csv");
		List<Point> possibleMerges = GraphFunctions.getAllPossibleMerges(graph);
		List<SkillGraph> mergedGraphs = new ArrayList<SkillGraph>();

		for (Point p : possibleMerges) {
			// first create a fresh copy of the original graph
			SkillGraph mergedGraph = new SkillGraph(graph);
			// perform the merge
			mergedGraph.mergeSkills(p.x, p.y);
			mergedGraphs.add(mergedGraph);
		}
		assertTrue(true);
	}

	@Test
	public void testFakeGraph() {
		SkillGraph graph = new SkillGraph("10/SkillGraph.csv",
				"10/ItemToSkillMapping.csv", "Example/CPT_Ranges1.csv",
				"10/GuessAndSlipRanges.csv");
		graph.generateFakeSkill();
		graph.generateGraphFiles("11");
	}
}
