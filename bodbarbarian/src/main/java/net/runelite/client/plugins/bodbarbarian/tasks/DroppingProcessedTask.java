package net.runelite.client.plugins.bodbarbarian.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodutils.BodUtils;
import net.runelite.client.plugins.bodutils.InventoryUtils;

@Slf4j
public class DroppingProcessedTask extends Task
{
	@Inject
	BodUtils bodUtils;

	@Inject
	InventoryUtils inventory;

	@Override
	public boolean validate()
	{
		return !inventory.containsItem(rawFishIds) && inventory.containsItem(processedFIshIds);
	}

	@Override
	public void onGameTick(GameTick event)
	{
		inventory.dropItems(processedFIshIds, true, config.sleepMin(), config.sleepMax());
	}

	@Override
	public String getTaskDescription()
	{
		return "Dropping stuff";
	}
}
