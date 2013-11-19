package data_structure;

/**
 * Item class for representing the items associated to skills
 * 
 * @author Bohao Li <bli@wpi.edu>
 * @author Douglas Selent
 * 
 */
public class Item extends Node implements Comparable<Item>
{
	private Skill parent;
	private Guess guess;
	private Slip slip;

	public Item(int ix, String n)
	{
		super(ix, n);
	}

	public Item(Item otherItem)
	{
		super(otherItem.getIndex(), otherItem.getName());

		parent = otherItem.getParent();
		guess = otherItem.getGuess();
		slip = otherItem.getSlip();

		cptTable = new CPT(otherItem.getCPTTable());
	}

	public Skill getParent()
	{
		return parent;
	}

	public void setParent(Skill p)
	{
		parent = p;
	}

	public Guess getGuess()
	{
		return guess;
	}

	public void setGuess(Guess g)
	{
		guess = g;
	}

	public Slip getSlip()
	{
		return slip;
	}

	public void setSlip(Slip sp)
	{
		slip = sp;
	}


	//output the cpt table in an easily readable form
	//assumes to commas or other annoying characters in the name
	public String getStringCPTTable()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(parent.getIndex() + ": " + parent.getName());
		sb.append(",");
		sb.append(guess.getIndex() + ": " + guess.getName());
		sb.append(",");
		sb.append(slip.getIndex() + ": " + slip.getName());
		sb.append(",");

		sb.append(getIndex() + ": " + getName() + " = incorrect");
		sb.append(",");
		sb.append(getIndex() + ": " + getName() + " = correct");
		
		sb.append("\n");

		int cptTableSize = cptTable.getSize();

		for(int i=0; i<cptTableSize; i++)
		{
			String binaryString = Integer.toBinaryString(i);

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
	public boolean equals(Object otherItem)
	{
		boolean returnValue = false;

		if (otherItem == null)
		{
			returnValue = false;
		}
		else if (this == otherItem)
		{
			returnValue = true;
		}
		else if(!(otherItem instanceof Item))
		{
			// probably an error

			returnValue = false;
		}
		else
		{
			Item otherItem1 = (Item) otherItem;

			if(getName().equals(otherItem1.getName()) && getIndex() == otherItem1.getIndex())
			{
				if(cptTable.equals(otherItem1.getCPTTable()))
				{
					returnValue = true;
				}
				else
				{
					System.err.println("Two items have the same name and index but different cpt's");
					System.err.println(getName() + " " + getIndex());
					System.err.println(otherItem1.getName() + " " + otherItem1.getIndex());
					System.exit(-1);
				}
			}
			else if(getName().equals(otherItem1.getName()) && getIndex() != otherItem1.getIndex())
			{
				System.err.println("Two items have the same name but different indices");
				System.err.println(getName() + " " + getIndex());
				System.err.println(otherItem1.getName() + " " + otherItem1.getIndex());
				System.exit(-1);
			}
			else if(!getName().equals(otherItem1.getName()) && getIndex() == otherItem1.getIndex())
			{
				System.err.println("Two items have different names but the same indices");
				System.err.println(getName() + " " + getIndex());
				System.err.println(otherItem1.getName() + " " + otherItem1.getIndex());
				System.exit(-1);
			}
		}

		return returnValue;
	}

	@Override
	public int compareTo(Item otherItem)
	{
		// die if null
		if (otherItem == null) {
			System.err.println("Trying to compare to a null item");
			System.exit(-1);
		}

		int otherItemIndex = otherItem.getIndex();

		// subtraction is okay, no overflow will happen
		return (getIndex() - otherItemIndex);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Item index = " + getIndex());
		sb.append("\n");
		sb.append("Item name = " + getName());
		sb.append("\n");
		sb.append("Skill name = " + parent.getName());
		sb.append("\n");

		return sb.toString();
	}
}