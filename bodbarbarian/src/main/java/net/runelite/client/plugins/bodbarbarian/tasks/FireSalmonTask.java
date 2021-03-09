package net.runelite.client.plugins.bodbarbarian.tasks;

import net.runelite.api.Player;

public class FireSalmonTask extends FindFireTask
{
	public FireSalmonTask()
	{
		super(331);
	}

	@Override
	public boolean validate()
	{
		Player player = client.getLocalPlayer();
		return inventory.isFull() && inventory.containsItem(331) && player.getAnimation() == -1;
	}

	@Override
	public String getTaskDescription()
	{
		return "Salmon -> Fire";
	}
}
