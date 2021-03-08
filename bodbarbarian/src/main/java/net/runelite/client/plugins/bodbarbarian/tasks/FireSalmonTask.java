package net.runelite.client.plugins.bodbarbarian.tasks;

public class FireSalmonTask extends FindFireTask
{
	public FireSalmonTask()
	{
		super(331);
	}

	@Override
	public boolean validate()
	{
		return inventory.isFull() && inventory.containsItem(331);
	}

	@Override
	public String getTaskDescription()
	{
		return "Salmon -> Fire";
	}
}
