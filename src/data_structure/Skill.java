package data_structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Skill class that represents the skills in the bayes net
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 *
 */
public class Skill extends Node implements Comparable<Skill>
{

	private List<Skill> children;
	private List<Skill> parents;
	private List<Item> items;

	/**
	 * Constructor for Skill
	 * 
	 * @param ix
	 *            the index of the skill
	 * @param nx
	 *            the index of the node
	 * @param n
	 *            the name of the skill
	 */
	public Skill(int ix, int nx, String n)
	{
		super(ix, nx, n);

		children = new ArrayList<Skill>();
		parents = new ArrayList<Skill>();
		items = new ArrayList<Item>();
	}

	/**
	 * Constructor to deep copy a skill
	 * 
	 * @param oldSkill
	 *            the old skill to be copied from
	 */
	public Skill(Skill oldSkill)
	{
		super(oldSkill.getIndex(), oldSkill.getNodeIndex(), oldSkill.getName());

		children = new ArrayList<Skill>();
		parents = new ArrayList<Skill>();
		items = new ArrayList<Item>();

		cptTable = new CPT(oldSkill.getCPTTable());
	}


	// child related functions

	// do not use the set function yet
	public void setChild(int childIndex, Skill childSkill)
	{
		children.set(childIndex, childSkill);
	}

	public Skill getChild(int childIndex)
	{
		return children.get(childIndex);
	}

	public boolean hasChild(Skill childSkill)
	{
		return children.contains(childSkill);
	}

	public void addChild(Skill childSkill)
	{
		// considering this (add when aldready there) an error for not, may
		// change to a no-op in the future
		if(children.contains(childSkill))
		{
			System.err.println("Trying to add a child that is already there");
			System.err.println(name + " " + childSkill.getName());
			System.exit(-1);
		}

		children.add(childSkill);
	}

	/**
	 * Function to add a child skill to the given skill
	 * 
	 * @param childSkill
	 */
	public void addChildAndChildParent(Skill childSkill)
	{
		if(children.contains(childSkill))
		{
			System.err.println("Trying to add a child that is already there");
			System.err.println(name + " " + childSkill.getName());
			System.exit(-1);
		}

		children.add(childSkill);
		childSkill.addParent(this);
	}

	// remove child skill from children list
	// remove parent skill of the child from the child's parent list
	public Skill removeChildAndChildParent(int childIndex)
	{
		Skill childSkill = children.remove(childIndex);
		childSkill.removeParent(this);

		return childSkill;
	}

	// remove child skill from children list
	// remove parent skill of the child from the child's parent list
	public Skill removeChildAndChildParent(Skill childSkill)
	{
		if(!children.contains(childSkill))
		{
			System.err.println("Trying to remove a child that is not there");
			System.err.println(name + " " + childSkill.getName());
			System.exit(-1);
		}

		children.remove(childSkill);
		childSkill.removeParent(this);

		return childSkill;
	}

	public Skill removeChild(int childIndex)
	{
		Skill childSkill = children.remove(childIndex);
		return childSkill;
	}

	public Skill removeChild(Skill childSkill)
	{
		if(!children.contains(childSkill))
		{
			System.err.println("Trying to remove a child that is not there");
			System.err.println(name + " " + childSkill.getName());
			System.exit(-1);
		}

		children.remove(childSkill);
		return childSkill;
	}

	public List<Skill> getChildren()
	{
		return children;
	}

	// parent related functions

	// do not use the set function yet
	public void setParent(int parentIndex, Skill parentSkill)
	{
		parents.set(parentIndex, parentSkill);
	}

	public Skill getParent(int parentIndex)
	{
		return parents.get(parentIndex);
	}

	public boolean hasParent(Skill parentSkill)
	{
		return parents.contains(parentSkill);
	}

	public void addParent(Skill parentSkill)
	{
		if(parents.contains(parentSkill))
		{
			System.err.println("Trying to add a parent that is already there");
			System.err.println(name + " " + parentSkill.getName());
			System.exit(-1);
		}

		parents.add(parentSkill);
	}

	public void addParentAndParentChild(Skill parentSkill)
	{
		if(parents.contains(parentSkill))
		{
			System.err.println("Trying to add a parent that is already there");
			System.err.println(name + " " + parentSkill.getName());
			System.exit(-1);
		}

		parents.add(parentSkill);
		parentSkill.addChild(this);
	}

	public Skill removeParentAndParentChild(int parentIndex)
	{
		Skill parentSkill = parents.remove(parentIndex);
		parentSkill.removeChild(this);

		return parentSkill;
	}

	public Skill removeParentAndParentChild(Skill parentSkill)
	{
		if(!parents.contains(parentSkill))
		{
			System.err.println("Trying to remove a parent that is not there");
			System.err.println(name + " " + parentSkill.getName());
			System.exit(-1);
		}

		parents.remove(parentSkill);
		parentSkill.removeChild(this);

		return parentSkill;
	}

	public Skill removeParent(int parentIndex)
	{
		Skill parentSkill = parents.remove(parentIndex);
		return parentSkill;
	}

	public Skill removeParent(Skill parentSkill)
	{
		if(!parents.contains(parentSkill))
		{
			System.err.println("Trying to remove a parent that is not there");
			System.err.println(name + " " + parentSkill.getName());
			System.exit(-1);
		}

		parents.remove(parentSkill);
		return parentSkill;
	}

	public List<Skill> getParents()
	{
		return parents;
	}

	public int getNumberOfParents()
	{
		return parents.size();
	}

	public int getNumberOfChildren()
	{
		return children.size();
	}

	// item related functions

	public void setItem(int itemIndex, Item item)
	{
		items.set(itemIndex, item);
	}

	public Item getItem(int itemIndex)
	{
		return items.get(itemIndex);
	}

	public boolean hasItem(Item item)
	{
		return items.contains(item);
	}

	// assumed an item can have only 1 parent
	// break assumption at own risk
	public void addItem(Item item)
	{
		if(!items.contains(item))
		{
			items.add(item);
			item.setParent(this);
		}
	}

	public Item removeItem(int itemIndex)
	{
		Item item = items.remove(itemIndex);
		item.setParent(null);
		return item;
	}

	public Item removeItem(Item item)
	{
		if(!items.contains(item))
		{
			System.err.println("Tring to remove an item that is not there");
			System.err.println(name + " " + item.getName());
			System.exit(-1);
		}

		items.remove(item);
		item.setParent(null);

		return item;
	}

	public List<Item> getItems()
	{
		return items;
	}


	public int getNumberOfItems()
	{
		return items.size();
	}


	//cpt functions


	//output the cpt table in an easily readable form
	//assumes no commas or other annoying characters in the name
	public String getStringCPTTable()
	{
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<parents.size(); i++)
		{
			sb.append(parents.get(i).getIndex() + ": " + parents.get(i).getName());
			sb.append(",");
		}

		sb.append(getIndex() + ": " + getName() + " = not known");
		sb.append(",");
		sb.append(getIndex() + ": " + getName() + " = known");

		sb.append("\n");

		int cptTableSize = cptTable.getSize();

		for(int i=0; i<cptTableSize; i++)
		{
			String binaryString = Integer.toBinaryString(i);

			//add leading zeroes
			while(binaryString.length() < parents.size())
			{
				binaryString = "0".concat(binaryString);
			}

			for(int j=0; j<parents.size(); j++)
			{
				sb.append(binaryString.charAt(j));
				sb.append(",");
			}

			for(int j=0; j<2; j++)
			{
				sb.append(cptTable.getProbability(binaryString, j));
				sb.append(",");				
			}

			sb.deleteCharAt(sb.length()-1);
			sb.append("\n");
		}

		sb.deleteCharAt(sb.length()-1);

		return sb.toString();
	}

	@Override
	public boolean equals(Object otherSkill)
	{
		boolean returnValue = false;

		if(otherSkill == null)
		{
			returnValue = false;
		}
		else if(this == otherSkill)
		{
			returnValue = true;
		}
		else if(!(otherSkill instanceof Skill))
		{
			// probably an error

			returnValue = false;
		}
		else
		{
			Skill otherSkill1 = (Skill) otherSkill;

			if(getName().equals(otherSkill1.getName()) && getIndex() == otherSkill1.getIndex())
			{
				if(cptTable.equals(otherSkill1.getCPTTable()))
				{
					returnValue = true;
				}
			}
		}

		return returnValue;
	}

	@Override
	public int compareTo(Skill otherSkill)
	{
		// die if null
		if(otherSkill == null)
		{
			System.err.println("Trying to compare to a null skill");
			System.exit(-1);
		}

		// subtraction is okay, no overflow will happen
		return (index - otherSkill.getIndex());
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Node index = " + getNodeIndex());
		sb.append("\n");
		sb.append("Skill index = " + getIndex());
		sb.append("\n");
		sb.append("Skill name = " + getName());
		sb.append("\n");

		sb.append("PARENTS");
		sb.append("\n");

		for (int i = 0; i < parents.size(); i++)
		{
			sb.append(parents.get(i).getName());
			sb.append("\n");
		}

		sb.append("CHILDREN");
		sb.append("\n");

		for (int i = 0; i < children.size(); i++)
		{
			sb.append(children.get(i).getName());
			sb.append("\n");
		}

		sb.append("ITEMS");
		sb.append("\n");

		for (int i = 0; i < items.size(); i++)
		{
			sb.append(items.get(i).getName());
			sb.append("\n");
		}

		sb.append("CPT TABLE");
		sb.append("\n");

		sb.append(getStringCPTTable());

		return sb.toString();
	}
}
