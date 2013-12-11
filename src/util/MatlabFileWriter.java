package util;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import data_structure.Item;
import data_structure.Skill;
import data_structure.SkillGraph;

/**
 * Function to build the matlab file representation of the skill graph
 * 
 * @author Bohao Li
 * 
 */
/**
 * @author Li Bohao
 * 
 */
public class MatlabFileWriter {
	private final static String OUTPUT_DIR = "matlab_graphs";

	public static void outPutSkillGraphMatlabFile(SkillGraph graph,
			String fileName) {
		// set up the directory and the filePath
		File dir = new File(OUTPUT_DIR);

		if (!dir.exists()) {
			dir.mkdir();
		}

		String filePath = OUTPUT_DIR + File.separator + fileName;
		StringBuilder output = new StringBuilder();

		// get the values of variables used in matlab file generation
		int N = graph.getNumberOfSkills() + graph.getNumberOfItems();

		// write the first common part;

		output.append("%The fucntion name should correspond to the name of the file. Please remember to make it match the model and iteration numbers\n");
		output.append("function bnet = sampleDag()\n");
		output.append("% the total number of nodes in the model. This should be the sum of the skill and item nodes\n");

		// output the total number of nodes
		output.append("N = " + Integer.toString(N) + ";\n");

		// output the names and indices of the skills
		output.append("% variable names for the skills in the graph. In this model we have only five skills. We could have several.\n");
		int index = 1;
		for (Skill s : graph.getSkillList()) {
			output.append(s.getName() + " = " + Integer.toString(index) + ";\n");
			index++;
		}

		// output the names and indices of the items
		output.append("% variable names for the item nodes(observable) and their node numbers\n");
		for (Item i : graph.getItemList()) {
			output.append(i.getName() + " = " + Integer.toString(index) + ";\n");
			index++;
		}

		// output the matrix;
		output.append("% topology is defined in a directed acyclic graph\n");
		output.append("dag = zeros(N,N);\n");

		// output the skill matrix
		int[][] skillMatrix = graph.generateSkillMatrix();
		List<Skill> skillList = graph.getSkillList();

		output.append("% the following represent the links between the skill nodes\n");

		for (Skill parentSkill : skillList) {
			for (Skill childSkill : parentSkill.getChildren()) {
				output.append("dag(" + parentSkill.getName() + ","
						+ childSkill.getName() + ") = 1;\n");
			}
		}

		// output the item to skill mapping
		output.append("% the following represent the links between the skill and item nodes\n");

		for (Skill s : skillList) {
			for (Item i : s.getItems()) {
				output.append("dag(" + s.getName() + "," + i.getName()
						+ ") = 1;\n");
			}
		}

		// output the equivalence class info
		output.append("% equivalence classes specify which nodes share a single CPT.\n");
		output.append("eclass = zeros(1, N);\n");

		// output the equivalence class info for nodes with no parents

		for (Skill s : skillList) {
			if (s.getParents().isEmpty()) {
				output.append("eclass(" + s.getName() + ") = 1;\n");
			}
		}

		// output the info for observed variables
		output.append("% observed variables. This should correspond to the item nodes.\n");
		output.append("obs = "
				+ Integer.toString(N - graph.getNumberOfItems() + 1) + ":"
				+ Integer.toString(N) + ";\n");

		// all nodes modeled as binary variables, all nodes are discrete
		// variables
		output.append("node_sizes = 2*ones(1,N);\n");
		output.append("discrete_nodes = 1:N;\n");

		// make the bayes net
		output.append("bnet = mk_bnet(dag,node_sizes,'discrete',discrete_nodes,'observed',obs,'equiv_class',eclass);\n");

		writeFile(filePath, output.toString());
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