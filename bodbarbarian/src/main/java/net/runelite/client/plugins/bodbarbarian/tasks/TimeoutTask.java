package net.runelite.client.plugins.bodbarbarian.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodbarbarian.BodBarbarianPlugin;

@Slf4j
public class TimeoutTask extends Task
{
	@Inject
	BodBarbarianPlugin plugin;

	@Override
	public boolean validate()
	{
		return plugin.timeout > 0;
	}

	@Override
	public String getTaskDescription()
	{
		return "Timeout: " + plugin.timeout;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		plugin.timeout--;
	}
}