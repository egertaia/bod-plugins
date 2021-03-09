package net.runelite.client.plugins.bodbarbarian.tasks;

import java.util.Collections;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.BodBarbarianPlugin;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodutils.BodUtils;
import net.runelite.client.plugins.bodutils.InventoryUtils;
import net.runelite.client.plugins.bodutils.NPCUtils;

@Slf4j
public class FindFishingSpotTask extends Task
{
	@Inject
	BodBarbarianPlugin plugin;

	@Inject
	InventoryUtils inventory;

	@Inject
	NPCUtils npcUtils;

	@Inject
	BodUtils bodUtils;

	@Override
	public boolean validate()
	{
		return !inventory.isFull();
	}

	@Override
	public void onGameTick(GameTick event)
	{
		var fishingSpot = npcUtils.findNearestNpcWithin(client.getLocalPlayer().getWorldLocation(), 10, Collections.singleton(1526));
		if (fishingSpot != null) {
			entry = new MenuEntry("", "", fishingSpot.getIndex(), 9, 0, 0, false);
			bodUtils.doActionMsTime(entry, fishingSpot.getConvexHull().getBounds(), sleepDelay());
			plugin.timeout = tickDelay();

		} else {
			log.info("BodBarbarian - Fishing spot not found");
		}
	}

	@Override
	public String getTaskDescription()
	{
		return "Fishing Spot?";
	}
}
