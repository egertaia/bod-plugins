package net.runelite.client.plugins.bodbarbarian.tasks;

import net.runelite.api.Player;

public class FireTroutTask extends FindFireTask
{
	public FireTroutTask()
	{
		super(335);
	}

	@Override
	public boolean validate()
	{
		Player player = client.getLocalPlayer();
		return inventory.isFull() && inventory.containsItem(335)  && player.getAnimation() == -1;
	}

	@Override
	public String getTaskDescription()
	{
		return "Trout -> Fire";
	}
}
