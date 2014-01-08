package data_structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.ConstantRNG;
import util.Globals;
import util.FileReader;

/**
 * Class that represents the graph generated from the given inputs Note 1: All
 * items belong to exactly one skill Note 2: All skills have at least one item
 * Note 3: Once CPT's are instansiated the graph + list ordering must not change
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * @author Seth Adjei
 */
public class SkillGraph
{

	// assume these lists stay sorted by indices
	// skill + item index corresponds to their index in the arrayList
	private final List<Skill> skillList;
	private final List<Item> itemList;
	private final List<Guess> guessList;
	private final List<Slip> slipList;

	private int numberOfNodes;

	private CPTSkillTemplate cptSkillTemplate;
	private CPTGuessSlipTemplate cptGuessSlipTemplate;

	/**
	 * Constructor for the skillgraph
	 * 
	 * @param skillStructureFilePath
	 *            file path to the skill structure file
	 * @param itemToSkillMappingFilePath
	 *            file path to the item to skill mapping file
	 * @param cptInfoFilePath
	 *            file path to the cpt information file
	 * @param guessSlipFilePath
	 *            file path to the guess slip csv file
	 */
	public SkillGraph(String skillStructureFilePath, String itemToSkillMappingFilePath, String skillFilePath, String guessSlipFilePath)
	{
		skillList = new ArrayList<Skill>();
		itemList = new ArrayList<Item>();
		guessList = new ArrayList<Guess>();
		slipList = new ArrayList<Slip>();

		numberOfNodes = 0;

		createSkillsAndItems(itemToSkillMappingFilePath);
		createGuessAndSlip(guessSlipFilePath);

		reIndexNodes();

		addItemsToSkills(itemToSkillMappingFilePath);
		addSkillsToSkills(skillStructureFilePath);
		addGuessSlipToItems();

		storeSkillTemplate(skillFilePath);
		storeGuessSlipTemplate(guessSlipFilePath);

		instansiateSkillCPTS();
		instansiateGuessSlipCPTS();
		instansiateItemCPTS();
	}

	public SkillGraph(SkillGraph otherSkillGraph)
	{
		skillList = new ArrayList<Skill>();
		itemList = new ArrayList<Item>();
		guessList = new ArrayList<Guess>();
		slipList = new ArrayList<Slip>();

		List<Skill> otherSkillList = new ArrayList<Skill>(otherSkillGraph.getSkillList());
		List<Item> otherItemList = new ArrayList<Item>(otherSkillGraph.getItemList());
		List<Guess> otherGuessList = new ArrayList<Guess>(otherSkillGraph.getGuessList());
		List<Slip> otherSlipList = new ArrayList<Slip>(otherSkillGraph.getSlipList());

		numberOfNodes = otherSkillGraph.getNumberOfNodes();

		CPTSkillTemplate otherCPTSkillTemplate = new CPTSkillTemplate(otherSkillGraph.getCPTSkillTemplate());
		CPTGuessSlipTemplate otherCPTGuessSlipTemplate = new CPTGuessSlipTemplate(otherSkillGraph.getCPTGuessSlipTemplate());

		// this group of loops will create the new nodes, but not link them

		for(int i=0; i<otherSkillList.size(); i++)
		{
			Skill skill = new Skill(otherSkillList.get(i));
			skillList.add(skill);
		}

		for(int i=0; i<otherItemList.size(); i++)
		{
			Item item = new Item(otherItemList.get(i));
			itemList.add(item);
		}

		for (int i=0; i<otherGuessList.size(); i++)
		{
			Guess guess = new Guess(otherGuessList.get(i));
			guessList.add(guess);
		}

		for(int i= 0; i<otherSlipList.size(); i++)
		{
			Slip slip = new Slip(otherSlipList.get(i));
			slipList.add(slip);
		}

		// must link all NEW nodes now

		// links up all skills and items
		for(int i=0; i<skillList.size(); i++)
		{
			Skill newSkill = skillList.get(i);
			Skill oldSkill = otherSkillList.get(i);

			for(int j=0; j<oldSkill.getNumberOfParents(); j++)
			{
				Skill oldParent = oldSkill.getParent(j);
				Skill newParent = skillList.get(oldParent.getIndex());

				if(!newSkill.hasParent(newParent))
				{
					newSkill.addParentAndParentChild(newParent);
				}
			}

			for(int j=0; j<oldSkill.getNumberOfChildren(); j++)
			{
				Skill oldChild = oldSkill.getChild(j);
				Skill newChild = skillList.get(oldChild.getIndex());

				if(!newSkill.hasChild(newChild))
				{
					newSkill.addChildAndChildParent(newChild);
				}
			}

			for(int j=0; j<oldSkill.getNumberOfItems(); j++)
			{
				Item oldItem = oldSkill.getItem(j);
				Item newItem = itemList.get(oldItem.getIndex());

				if(!newSkill.hasItem(newItem))
				{
					newSkill.addItem(newItem);
				}
			}
		}

		// links up guesses and slips
		for(int i=0; i<itemList.size(); i++)
		{
			Item newItem = itemList.get(i);
			Item oldItem = otherItemList.get(i);

			int guessIndex = oldItem.getGuess().getIndex();
			int slipIndex = oldItem.getSlip().getIndex();

			newItem.setGuessAndGuessItem(guessList.get(guessIndex));
			newItem.setSlipAndSlipItem(slipList.get(slipIndex));
		}

		cptSkillTemplate = new CPTSkillTemplate(otherCPTSkillTemplate);
		cptGuessSlipTemplate = new CPTGuessSlipTemplate(otherCPTGuessSlipTemplate);
	}

	// Set and get functions

	public List<Skill> getSkillList()
	{
		return skillList;
	}

	public List<Item> getItemList()
	{
		return itemList;
	}

	public List<Guess> getGuessList()
	{
		return guessList;
	}

	public List<Slip> getSlipList()
	{
		return slipList;
	}

	public int getNumberOfSkills()
	{
		return skillList.size();
	}

	public int getNumberOfItems()
	{
		return itemList.size();
	}

	public int getNumberOfGuesses()
	{
		return guessList.size();
	}

	public int getNumberOfSlips()
	{
		return slipList.size();
	}

	public int getNumberOfNodes()
	{
		return numberOfNodes;
	}

	private void setNumberOfNodes(int non)
	{
		numberOfNodes = non;
	}

	// skillIndex = array spot and not necessarily the skill index number
	// typically they would be the same since we would want the skill indices to
	// be in order
	public Skill getSkill(int skillIndex)
	{
		return skillList.get(skillIndex);
	}

	public Item getItem(int itemIndex)
	{
		return itemList.get(itemIndex);
	}

	public Guess getGuess(int guessIndex)
	{
		return guessList.get(guessIndex);
	}

	public Slip getSlip(int slipIndex)
	{
		return slipList.get(slipIndex);
	}

	public CPTSkillTemplate getCPTSkillTemplate()
	{
		return cptSkillTemplate;
	}

	public CPTGuessSlipTemplate getCPTGuessSlipTemplate()
	{
		return cptGuessSlipTemplate;
	}

	// add and remove functions?

	// Create Functions

	/**
	 * Function to create the skills and items
	 * 
	 * @param itemToSkillMappingFilePath
	 */
	private void createSkillsAndItems(String itemToSkillMappingFilePath)
	{
		List<String> itemInfo = FileReader.readFile(itemToSkillMappingFilePath);

		// loop to add all items and skills to lists
		for(int i = 0; i < itemInfo.size(); i++)
		{
			int itemIndex = -1;
			int skillIndex = -1;

			String itemName = "";
			String skillName = "";

			String row = itemInfo.get(i);

			// break the string into separate item parameters
			// item index, skill index, item name, skill name
			String[] itemParams = row.split(",");

			try
			{
				itemIndex = Integer.parseInt(itemParams[0]);
				skillIndex = Integer.parseInt(itemParams[1]);

				itemName = itemParams[2];
				skillName = itemParams[3];
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(-1);
			}

			// create item and skill and add to lists
			// will indices work correctly if items in the file are not in
			// order? think so?
			// should always sort the lists to keep indices synced

			Item newItem = new Item(itemIndex, -1, itemName);
			Skill newSkill = new Skill(skillIndex, -1, skillName);

			// compare to existing lists and add if not there

			if(!itemList.contains(newItem))
			{
				itemList.add(newItem);
			}

			if(!skillList.contains(newSkill))
			{
				skillList.add(newSkill);
			}

			// sort lists by index

			Collections.sort(itemList);
			Collections.sort(skillList);
		}
	}

	/**
	 * sets the guess and slip node attached to each skill
	 * 
	 * @param guessSlipFilePath
	 */
	private void createGuessAndSlip(String guessSlipFilePath)
	{
		List<String> guessSlipInfo = FileReader.readFile(guessSlipFilePath);

		// loop to add all guess and slip nodes to the lists
		for(int i=0; i<guessSlipInfo.size(); i++)
		{
			String row = guessSlipInfo.get(i);

			// guess index, slip index, guess node name, slip node name
			String[] guessSlipParams = row.split(",");

			String guessName = guessSlipParams[2];
			String slipName = guessSlipParams[3];

			Guess guess = new Guess(i, -1, guessName);
			Slip slip = new Slip(i, -1, slipName);

			// compare to existing lists and add if not there
			// not needed, but doesn't hurt for a safety check
			if(!guessList.contains(guess))
			{
				guessList.add(guess);
			}
			else
			{
				System.err.println("Duplicate guess node names");
				System.err.println(guessName);
				System.exit(-1);
			}

			// compare to existing lists and add if not there
			// not needed, but doesn't hurt for a safety check
			if(!slipList.contains(slip))
			{
				slipList.add(slip);
			}
			else
			{
				System.err.println("Duplicate slip node names");
				System.err.println(slipName);
				System.exit(-1);
			}
		}
	}

	// Store functions

	/**
	 * Function to store the skill cpt info in the bayes net into an appropriate
	 * data structure
	 * 
	 * @param cptInfoFilePath
	 */
	private void storeSkillTemplate(String skillFilePath)
	{
		List<String> cptInfo = FileReader.readFile(skillFilePath);
		cptSkillTemplate = new CPTSkillTemplate(cptInfo);
	}

	/**
	 * Function to store the guess cpt info in the bayes net into an appropriate
	 * data structure
	 * 
	 * @param guessSlipFilePath
	 */
	private void storeGuessSlipTemplate(String guessSlipFilePath)
	{
		List<String> cptInfo = FileReader.readFile(guessSlipFilePath);
		cptGuessSlipTemplate = new CPTGuessSlipTemplate(cptInfo);
	}

	// Mapping functions

	private void addItemsToSkills(String itemToSkillMappingFilePath)
	{
		List<String> itemInfo = FileReader.readFile(itemToSkillMappingFilePath);

		// loop to link up the items to the skills
		for(int i=0; i<itemInfo.size(); i++)
		{
			int itemIndex = -1;
			int skillIndex = -1;

			String row = itemInfo.get(i);
			String[] itemParams = row.split(",");

			try
			{
				itemIndex = Integer.parseInt(itemParams[0]);
				skillIndex = Integer.parseInt(itemParams[1]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(-1);
			}

			Item newItem = itemList.get(itemIndex);
			Skill newSkill = skillList.get(skillIndex);

			newItem.setParent(newSkill);
			newSkill.addItem(newItem);
		}
	}

	/**
	 * Builds the skill structure with the information given in the file
	 * 
	 * @param skillStructureFilePath
	 *            the file path to the csv file holding the skill structure
	 *            information
	 */
	private void addSkillsToSkills(String skillStructureFilePath)
	{

		// read use the file reader to read the csv file

		List<String> structureInfo = FileReader.readFile(skillStructureFilePath);
		int numberOfSkills = skillList.size();

		int[][] skillMatrix = new int[numberOfSkills][numberOfSkills];

		// link the skills together
		for(int i=0; i<numberOfSkills; i++)
		{
			String row = structureInfo.get(i);
			Skill currentSkill = skillList.get(i);

			String[] arcArray = row.split(",");

			for(int j=0; j<numberOfSkills; j++)
			{
				skillMatrix[i][j] = Integer.parseInt(arcArray[j]);

				if (arcArray[j].equals("1"))
				{
					currentSkill.addChildAndChildParent(skillList.get(j));
				}
			}
		}

		// sort the parents and children inside the skill list to maintain
		// their order according to their indices
		for(Skill s : skillList)
		{
			Collections.sort(s.getChildren());
			Collections.sort(s.getParents());
		}
	}

	private void addGuessSlipToItems()
	{
		for(int i=0; i<itemList.size(); i++)
		{
			Item item = itemList.get(i);
			Guess guess = guessList.get(i);
			Slip slip = slipList.get(i);

			item.setGuess(guess);
			item.setSlip(slip);

			guess.setItem(item);
			slip.setItem(item);
		}
	}

	// Instansiation functions

	private void instansiateSkillCPTS()
	{
		// Create CPT table with random values for each field with the range
		// specified by cptSkillTemplate
		for(Skill skill : skillList)
		{
			// get the number of parents the node has
			int numberOfParents = skill.getNumberOfParents();
			skill.setCPTTable(new CPT(numberOfParents, cptSkillTemplate));
		}
	}

	private void instansiateGuessSlipCPTS()
	{
		// Create CPT table with random values for each field with the range
		// specified by cptGuessSlipTemplate
		for(int i=0; i<guessList.size(); i++)
		{
			Guess guess = guessList.get(i);
			guess.setCPTTable(new CPT(i, 0, cptGuessSlipTemplate));
		}

		for(int i=0; i<slipList.size(); i++)
		{
			Slip slip = slipList.get(i);
			slip.setCPTTable(new CPT(i, 1, cptGuessSlipTemplate));
		}
	}

	// assume item has 1 skill, 1 guess, and 1 slip
	private void instansiateItemCPTS()
	{
		for(Item item : itemList)
		{
			item.setCPTTable(new CPT());
		}
	}

	// Miscellaneous functions

	/**
	 * Merges the skills inside the skill graph specified by the parent and the
	 * child indices
	 * 
	 * @param parentSkillIndex
	 *            the index of the parent skill in this merge
	 * @param childSkillIndex
	 *            the index of the child skill in this merge
	 */
	public void mergeSkills(int parentSkillIndex, int childSkillIndex)
	{

		Skill parent = skillList.get(parentSkillIndex);
		Skill child = skillList.get(childSkillIndex);

		if(!parent.hasChild(child))
		{
			System.err.println("Cannot merge these skills, the first skill is not a parent of the second skill");
			System.err.println(parent.getName() + "  " + child.getName());
			System.exit(-1);
		}

		parent.setName(parent.getName() + "x" + child.getName());

		// add child children to parent child list
		List<Skill> grandChildren = new ArrayList<Skill>(child.getChildren());

		for(int i=0; i<grandChildren.size(); i++)
		{
			Skill grandChild = grandChildren.get(i);
			child.removeChildAndChildParent(grandChild);

			if(!parent.hasChild(grandChild))
			{
				parent.addChildAndChildParent(grandChild);
			}
		}

		// add child parents to parent parents list
		List<Skill> childParents = new ArrayList<Skill>(child.getParents());

		for(int i=0; i<childParents.size(); i++)
		{
			Skill childParent = childParents.get(i);
			child.removeParentAndParentChild(childParent);

			if(!parent.hasParent(childParent) && parent.getIndex() != childParent.getIndex())
			{
				parent.addParentAndParentChild(childParent);
			}
		}

		List<Item> childItemList = new ArrayList<Item>(child.getItems());

		// add the items the child has to the parent
		for(Item item : childItemList)
		{
			child.removeItem(item);
			parent.addItem(item);
		}

		// remove the child from the skillList
		skillList.remove(child);

		// update the parent's CPT table as the parent may now have more parents
		parent.setCPTTable(new CPT(parent.getParents().size(), cptSkillTemplate));
		reIndexSkills();

		//System.out.println(skillList);
	}

	public void generateFakeSkill()
	{
		// If no skill is selected, the skill is selected at random.
		int oldSkillID = ConstantRNG.getNextInt(0, getSkillList().size()-1);
		Skill oldSkill = getSkill(oldSkillID);
		generateFakeSkill(oldSkill);
	}

	// know skill but not cut point
	public void generateFakeSkill(Skill oldSkill)
	{
		int totalItems = oldSkill.getNumberOfItems();

		if(totalItems < 2)
		{
			System.err.println("Not enough items to split this skill");
			System.err.println(oldSkill.getIndex() + ": " + oldSkill.getName());
			System.exit(-1);
		}

		// choose a cutPoint where both skills will have at least one item
		int cutPoint = ConstantRNG.getNextInt(1, totalItems - 1);

		generateFakeSkill(oldSkill, cutPoint);
	}

	public void generateFakeSkill(Skill oldSkill, int cutPoint)
	{
		// some initial checks

		int totalItems = oldSkill.getNumberOfItems();

		if(totalItems < 2)
		{
			System.err.println("Not enough items to split this skill");
			System.err.println(oldSkill.getIndex() + ": " + oldSkill.getName());
			System.exit(-1);
		}

		if(cutPoint < 1 || cutPoint > totalItems - 1)
		{
			System.err.println("Invalid cut point");
			System.err.println("Cutpoint = " + cutPoint + ", total items = " + totalItems);
			System.exit(-1);
		}

		Skill newSkill = new Skill(oldSkill);

		newSkill.setIndex(getSkillList().size());
		newSkill.setName(oldSkill.getName() + "zsplitz" + newSkill.getIndex());

		// Split the items up between the two skills
		for(int i=0; i<cutPoint; i++)
		{
			//keep removing the first item
			newSkill.addItem(oldSkill.removeItem(0));
		}
		
		//Determine attachment point.
		int attachPoint = ConstantRNG.getNextInt(1, 2);
		
		if(attachPoint == 1)
		{
			//Attach as child of Old Skill
			// Move the children of the oldSkill and attach them to the new skill
			List<Skill> children = new ArrayList<Skill>(oldSkill.getChildren());

			for(int i=0; i<children.size(); i++)
			{
				Skill child = children.get(i);
				newSkill.addChildAndChildParent(oldSkill.removeChildAndChildParent(child));
			}
			
			// Make the new skill a child of the old skill
			oldSkill.addChildAndChildParent(newSkill);	
		}
		else{
			//Attach as parent of Old Skill
			//Get all the parents of this current skill
			List<Skill> parents = new ArrayList<Skill>(oldSkill.getParents());
			
			for(int i=0; i<parents.size(); i++)
			{
				Skill parent = parents.get(i);
				//Remove the link between the parent skill and the old skill

				parent.removeChildAndChildParent(oldSkill);

				//Make the new skill the child of the parent skills of the old skill
				parent.addChildAndChildParent(newSkill);
			}

			//Make the new skill the parent of the old skill
			newSkill.addChildAndChildParent(oldSkill);				
			
		}
		
		// attach the new skill and re-index all the skills
		skillList.add(newSkill);
		reIndexSkills();
	}

	// super important to call after any additions or subtractions of skills in
	// the graph
	private void reIndexSkills()
	{
		Collections.sort(skillList);

		for(int i=0; i<skillList.size(); i++)
		{
			Skill skill = skillList.get(i);
			skill.setIndex(i);
		}

		reIndexNodes();
	}

	/*
	private void reIndexItems()
	{
		Collections.sort(itemList);
	  
	 	for(int i=0; i<itemList.size(); i++)
		{
			Item item = itemList.get(i);
			item.setIndex(i);
		}
	 
		reIndexNodes();
	}
	*/

	// skills, guess, slip, items
	private void reIndexNodes()
	{
		int nodeIndex = 0;

		for(int i=0; i<skillList.size(); i++)
		{
			Skill skill = skillList.get(i);
			skill.setNodeIndex(nodeIndex);
			nodeIndex++;
		}

		for(int i=0; i<guessList.size(); i++)
		{
			Guess guess = guessList.get(i);
			guess.setNodeIndex(nodeIndex);
			nodeIndex++;
		}

		for(int i=0; i<slipList.size(); i++)
		{
			Slip slip = slipList.get(i);
			slip.setNodeIndex(nodeIndex);
			nodeIndex++;
		}

		for(int i=0; i<itemList.size(); i++)
		{
			Item item = itemList.get(i);
			item.setNodeIndex(nodeIndex);
			nodeIndex++;
		}

		setNumberOfNodes(nodeIndex);
	}

	/**
	 * we should not hold the skill matrix as an array in this class since that
	 * would allow the possibility for it to be out of sync with the skill list.
	 * Everytime we need a skill matrix we can call this function to generate it
	 * off of the current skill list
	 * 
	 * 
	 */
	public int[][] generateSkillMatrix()
	{
		int skillListSize = skillList.size();
		int skillMatrix[][] = new int[skillListSize][skillListSize];

		for(Skill skill : skillList)
		{
			int index = skill.getIndex();
			List<Skill> childList = skill.getChildren();

			for(Skill child : childList)
			{
				int childIndex = child.getIndex();
				skillMatrix[index][childIndex] = 1;
			}
		}

		// check that skill graph has no self-cycles

		for(int i=0; i<skillListSize; i++)
		{
			if(skillMatrix[i][i] == 1)
			{
				System.err.println("Self cycle in skill matrix detected");
				System.exit(-1);
			}
		}

		return skillMatrix;
	}

	public String stringifySkillMatrix()
	{
		int skillMatrix[][] = generateSkillMatrix();
		return stringifySkillMatrix(skillMatrix);
	}

	public String stringifySkillMatrix(int skillMatrix[][])
	{
		StringBuilder sb = new StringBuilder();

		for (int i=0; i<skillMatrix.length; i++)
		{
			for (int j=0; j<skillMatrix.length; j++)
			{
				sb.append(skillMatrix[i][j]);
				sb.append(" ");
			}

			sb.deleteCharAt(sb.length()-1);
			sb.append("\n");
		}

		sb.deleteCharAt(sb.length()-1);

		return sb.toString();
	}

	public boolean compareGraph(SkillGraph otherGraph)
	{
		boolean returnValue = true;

		if(getNumberOfSkills() == otherGraph.getNumberOfSkills())
		{
			int skillMatrix1[][] = generateSkillMatrix();
			int skillMatrix2[][] = otherGraph.generateSkillMatrix();

			for(int i=0; i<skillMatrix1.length; i++)
			{
				for(int j=0; j<skillMatrix1.length; j++)
				{
					if(skillMatrix1[i][j] != skillMatrix2[i][j])
					{
						returnValue = false;
					}
				}
			}
		}
		else
		{
			returnValue = false;
		}
		
		//graph is the same but nodes may be different
		//a->c, b->c = x->c, x could be a or b
		//check that best graph did not merge two real skills
		//check that best graph merged the fake skill into the correct skill
		if(returnValue)
		{
			for(int i=0; i<getNumberOfSkills(); i++)
			{
				String name = otherGraph.getSkill(i).getName();
				String skillNames[] = name.split("x");

				//multiple skills merged
				if(skillNames.length > 1)
				{
					String realSkill = "";

					//search for the real skill
					for(int j=0; j<skillNames.length; j++)
					{
						//found real skill
						if(!skillNames[j].contains("split"))
						{
							//if this is not the only real skill
							if(!realSkill.equals(""))
							{
								returnValue = false;
							}

							realSkill = skillNames[j];
						}
					}

					//real skill was found
					if(!realSkill.equals(""))
					{
						for(int j=0; j<skillNames.length; j++)
						{
							//make sure fake skill was split off of the real skill
							if(skillNames[j].contains("split"))
							{
								int endIndex = skillNames[j].indexOf("z");
								String realSkill1 = skillNames[j].substring(0, endIndex);

								if(!realSkill.equals(realSkill1))
								{

									returnValue = false;
								}
							}
						}
					}
					else
					{
						returnValue = false;
					}
				}
			}
		}

		return returnValue;
	}
	
	@Override
	public boolean equals(Object otherSkillGraph)
	{
		boolean returnValue = false;

		if(otherSkillGraph == null)
		{
			returnValue = false;
		}
		else if(this == otherSkillGraph)
		{
			returnValue = true;
		}
		else if(!(otherSkillGraph instanceof SkillGraph))
		{
			// probably an error
			returnValue = false;
		}
		else
		{
			returnValue = true;

			SkillGraph otherSkillGraph1 = (SkillGraph) otherSkillGraph;

			if(!cptSkillTemplate.equals(otherSkillGraph1.getCPTSkillTemplate()))
			{
				returnValue = false;
			}

			if(returnValue && !cptGuessSlipTemplate.equals(otherSkillGraph1.getCPTGuessSlipTemplate()))
			{
				returnValue = false;
			}

			for(int i=0; i<skillList.size() && returnValue; i++)
			{
				Skill skill = skillList.get(i);
				Skill otherSkill = otherSkillGraph1.getSkill(i);

				if(!skill.equals(otherSkill))
				{
					returnValue = false;
				}

			}

			for(int i=0; i<itemList.size() && returnValue; i++)
			{
				Item item = itemList.get(i);
				Item otherItem = otherSkillGraph1.getItem(i);

				if(!item.equals(otherItem))
				{
					returnValue = false;
				}

			}

			for(int i=0; i<guessList.size() && returnValue; i++)
			{
				Guess guess = guessList.get(i);
				Guess otherGuess = otherSkillGraph1.getGuess(i);

				if(!guess.equals(otherGuess))
				{
					returnValue = false;
				}

			}

			for(int i=0; i<slipList.size() && returnValue; i++)
			{
				Slip slip = slipList.get(i);
				Slip otherSlip = otherSkillGraph1.getSlip(i);

				if (!slip.equals(otherSlip))
				{
					returnValue = false;
				}
			}
		}

		return returnValue;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append("CPT Skill Template");
		sb.append("\n");
		sb.append("\n");

		sb.append(cptSkillTemplate);

		sb.append("\n");
		sb.append("CPT GUESS/SLIP Template");
		sb.append("\n");
		sb.append("\n");

		sb.append(cptGuessSlipTemplate);

		sb.append("\n");
		sb.append("SKILLS");
		sb.append("\n");
		sb.append("\n");

		for(int i=0; i<skillList.size(); i++)
		{
			sb.append(skillList.get(i));
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("ITEMS");
		sb.append("\n");
		sb.append("\n");

		for(int i=0; i<itemList.size(); i++)
		{
			sb.append(itemList.get(i));
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("GUESS");
		sb.append("\n");
		sb.append("\n");

		for(int i=0; i<guessList.size(); i++)
		{
			sb.append(guessList.get(i));
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("SLIP");
		sb.append("\n");
		sb.append("\n");

		for(int i=0; i<slipList.size(); i++)
		{
			sb.append(slipList.get(i));
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("SKILL MATRIX");
		sb.append("\n");
		sb.append("\n");

		sb.append(stringifySkillMatrix());

		return sb.toString();
	}
}
