package net.runelite.client.plugins.bodtasktemplate.tasks;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodtasktemplate.Task;
import net.runelite.client.plugins.bodtasktemplate.BodTaskTemplatePlugin;

public class TimeoutTask extends Task
{
	@Override
	public boolean validate()
	{
		return BodTaskTemplatePlugin.timeout > 0;
	}

	@Override
	public String getTaskDescription()
	{
		return "Timeout: " + BodTaskTemplatePlugin.timeout;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		BodTaskTemplatePlugin.timeout--;
	}
}