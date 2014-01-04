package util;

import java.util.List;
import java.io.File;

import data_structure.Guess;
import data_structure.Item;
import data_structure.Skill;
import data_structure.SkillGraph;
import data_structure.Slip;

/**
 * Function to build the matlab file representation of the skill graph
 * 
 * @author Bohao Li
 * 
 */
public class MatlabFileWriter
{
	private final static String OUTPUT_DIR = "matlab_scripts";

	public static void outputSkillGraphMatlabFile(SkillGraph graph, int graphNumber, String fileName, int numberOfStudents)
	{
		// set up the directory and the filePath
		QuickFileWriter.createFolder(OUTPUT_DIR);

		String filePath = OUTPUT_DIR + File.separator + fileName;
		StringBuilder output = new StringBuilder();

		// get the values of variables used in matlab file generation
		int numberOfNodes = graph.getNumberOfNodes();

		// write the first common part;

		output.append("%The function name should correspond to the name of the file. Please remember to make it match the model and iteration numbers\n");
		output.append("function [bnet, UID, numberOfStudents] = currentDag()\n");
		output.append("% the total number of nodes in the model. This should be the sum of the skill and item nodes\n");

		// output the total number of nodes
		output.append("N = " + Integer.toString(numberOfNodes) + ";\n");
		output.append("numberOfStudents = " + Integer.toString(numberOfStudents) + ";\n");
		output.append("numberOfItems = " + Integer.toString(graph.getNumberOfItems()) + ";\n");
		output.append("numberOfSkills = " + Integer.toString(graph.getNumberOfSkills()) + ";\n");
		output.append("UID = " + Integer.toString(graphNumber) + ";\n");

		// output the names and indices of the skills
		output.append("% variable names for the skills in the graph. In this model we have only five skills. We could have several.\n");

		for(Skill s : graph.getSkillList())
		{
			output.append(s.getName() + " = " + Integer.toString(s.getNodeIndex()+1) + ";\n");
		}

		// output the names and indices of the guesses
		output.append("% variable names for the guesses in the graph.\n");

		for(Guess g : graph.getGuessList())
		{
			output.append(g.getName() + " = " + Integer.toString(g.getNodeIndex()+1) + ";\n");
		}

		// output the names and indices of the slips
		output.append("% variable names for the slips in the graph.\n");
		for(Slip s : graph.getSlipList())
		{
			output.append(s.getName() + " = " + Integer.toString(s.getNodeIndex()+1) + ";\n");
		}

		// output the names and indices of the items
		output.append("% variable names for the item nodes(observable) and their node numbers\n");

		for(Item i : graph.getItemList())
		{
			output.append(i.getName() + " = " + Integer.toString(i.getNodeIndex()+1) + ";\n");
		}

		// output the matrix;
		output.append("% topology is defined in a directed acyclic graph\n");
		output.append("dag = zeros(N,N);\n");

		// output the skill matrix
		List<Skill> skillList = graph.getSkillList();

		output.append("% the following represent the links between the skill nodes\n");

		for(Skill parentSkill : skillList)
		{
			for(Skill childSkill : parentSkill.getChildren())
			{
				output.append("dag(" + parentSkill.getName() + "," + childSkill.getName() + ") = 1;\n");
			}
		}

		// output the guess to item mapping
		output.append("% the following represent the links between the guess and item nodes\n");

		for(Guess g : graph.getGuessList())
		{
			output.append("dag(" + g.getName() + "," + g.getItem().getName() + ") = 1;\n");
		}

		// output the slip to item mapping
		output.append("% the following represent the links between the slip and item nodes\n");

		for(Slip s : graph.getSlipList())
		{
			output.append("dag(" + s.getName() + "," + s.getItem().getName() + ") = 1;\n");
		}

		// output the item to skill mapping
		output.append("% the following represent the links between the skill and item nodes\n");

		for(Skill s : skillList)
		{
			for(Item i : s.getItems())
			{
				output.append("dag(" + s.getName() + "," + i.getName() + ") = 1;\n");
			}
		}

		// output the equivalence class info
		output.append("% equivalence classes specify which nodes share a single CPT.\n");
		output.append("eclass = zeros(1, N);\n");

		// output the equivalence class info for nodes with no parents

		// for (Skill s : skillList) {
		// if (s.getParents().isEmpty()) {
		// output.append("eclass(" + s.getName() + ") = 1;\n");
		// }
		// }

		output.append("eclass = 1:N;\n");

		// output the info for observed variables
		output.append("% observed variables. This should correspond to the item nodes.\n");
		output.append("obs = " + Integer.toString(numberOfNodes - graph.getNumberOfItems() + 1) + ":" + Integer.toString(numberOfNodes) + ";\n");

		// all nodes modeled as binary variables, all nodes are discrete
		// variables
		output.append("node_sizes = 2*ones(1,N);\n");
		output.append("discrete_nodes = 1:N;\n");

		// make the bayes net
		output.append("bnet = mk_bnet(dag,node_sizes,'discrete',discrete_nodes,'observed',obs,'equiv_class',eclass);\n");

		// output the cpt table
		for(Skill s : graph.getSkillList())
		{
			output.append(s.convertCPTToMatlab(false));
		}

		for(Guess g : graph.getGuessList())
		{
			output.append(g.convertCPTToMatlab(false));
		}

		for(Slip s : graph.getSlipList())
		{
			output.append(s.convertCPTToMatlab(false));
		}

		for(Item i : graph.getItemList())
		{
			output.append(i.convertCPTToMatlab(false));
		}

		QuickFileWriter.writeFile(filePath, output.toString());
	}
}
