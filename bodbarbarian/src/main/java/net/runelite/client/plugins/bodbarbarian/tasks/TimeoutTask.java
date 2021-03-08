package net.runelite.client.plugins.bodbarbarian.tasks;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodbarbarian.BodBarbarianPlugin;

public class TimeoutTask extends Task
{
	@Override
	public boolean validate()
	{
		return BodBarbarianPlugin.timeout > 0;
	}

	@Override
	public String getTaskDescription()
	{
		return "Timeout: " + BodBarbarianPlugin.timeout;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		BodBarbarianPlugin.timeout--;
	}
}