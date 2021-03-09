package net.runelite.client.plugins.bodbarbarian.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodbarbarian.BodBarbarianPlugin;

@Slf4j
public class MovingTask extends Task
{

	@Override
	public boolean validate()
	{
		return playerUtils.isMoving(BodBarbarianPlugin.beforeLoc);
	}

	@Override
	public void onGameTick(GameTick event)
	{
		Player player = client.getLocalPlayer();
		if (player != null)
		{
			playerUtils.handleRun(20, 30);
			BodBarbarianPlugin.timeout = 2+ tickDelay();
		}
	}
}