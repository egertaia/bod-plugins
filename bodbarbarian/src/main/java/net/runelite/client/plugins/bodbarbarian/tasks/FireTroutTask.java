package net.runelite.client.plugins.bodbarbarian.tasks;

public class FireTroutTask extends FindFireTask
{
	public FireTroutTask()
	{
		super(335);
	}

	@Override
	public boolean validate()
	{
		return inventory.isFull() && inventory.containsItem(335);
	}

	@Override
	public String getTaskDescription()
	{
		return "Trout -> Fire";
	}
}
