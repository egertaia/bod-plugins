package net.runelite.client.plugins.bodbarbarian.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodutils.WalkUtils;

@Slf4j
public class ReturnToVillageTask extends Task
{
	WorldArea village = new WorldArea(new WorldPoint(3110, 3421, 0), new WorldPoint(3098, 3436, 0));
	WorldPoint firePosition = new WorldPoint(3105, 3433, 0);

	@Inject
	WalkUtils walk;

	@Override
	public boolean validate()
	{
		log.info("dist: " + client.getLocalPlayer().getWorldLocation().distanceTo(village));
		return client.getLocalPlayer().getWorldLocation().distanceTo(village) > 10;
	}

	@Override
	public void onGameTick(GameTick event)
	{
		walk.webWalk(firePosition, 2, client.getLocalPlayer().isMoving(), sleepDelay());
	}

	@Override
	public String getTaskDescription()
	{
		return "Walking back";
	}
}