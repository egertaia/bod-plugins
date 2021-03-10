package net.runelite.client.plugins.bodvonblessed.tasks;

import java.util.Collections;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.NpcID;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodvonblessed.BodVonBlessedPlugin;
import net.runelite.client.plugins.bodvonblessed.Task;
import net.runelite.client.plugins.bodutils.BodUtils;
import net.runelite.client.plugins.bodutils.InventoryUtils;
import net.runelite.client.plugins.bodutils.NPCUtils;

@Slf4j
public class FindFishingSpotTask extends Task
{
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
		var fishingSpot = npcUtils.findNearestNpcWithin(client.getLocalPlayer().getWorldLocation(), 20, Collections.singleton(NpcID.FISHING_SPOT_1542));
		if (fishingSpot != null) {
			entry = new MenuEntry("", "", fishingSpot.getIndex(), 9, 0, 0, false);
			bodUtils.doInvokeClientTick(entry, sleepDelay());
			BodVonBlessedPlugin.timeout = tickDelay();

		} else {
			log.info("BodVonBlessed - Fishing spot not found");
		}
	}

	@Override
	public String getTaskDescription()
	{
		return "Fishing Spot?";
	}
}
