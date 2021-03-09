package net.runelite.client.plugins.bodbarbarian.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.BodBarbarianPlugin;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodutils.InventoryUtils;

@Slf4j
public class DroppingProcessedTask extends Task
{
	@Inject
	InventoryUtils inventory;

	@Override
	public boolean validate()
	{
		return !inventory.containsItem(rawFishIds) && inventory.containsItem(processedFishIds);
	}

	@Override
	public void onGameTick(GameTick event)
	{
		inventory.dropItems(processedFishIds, true, config.sleepMin(), config.sleepMax());
		BodBarbarianPlugin.timeout = tickDelay();
	}

	@Override
	public String getTaskDescription()
	{
		return "Dropping stuff";
	}
}
