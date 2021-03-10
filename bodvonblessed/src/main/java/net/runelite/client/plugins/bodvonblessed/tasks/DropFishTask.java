package net.runelite.client.plugins.bodvonblessed.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodvonblessed.BodVonBlessedPlugin;
import net.runelite.client.plugins.bodvonblessed.Task;
import net.runelite.client.plugins.bodutils.InventoryUtils;

@Slf4j
public class DropFishTask extends Task
{
	@Inject
	InventoryUtils inventory;

	@Override
	public boolean validate()
	{
		return inventory.isFull() && inventory.containsItem(rawFishIds);
	}

	@Override
	public void onGameTick(GameTick event)
	{
		inventory.dropItems(rawFishIds, true, config.sleepMin(), config.sleepMax());
		BodVonBlessedPlugin.timeout = tickDelay();
	}

	@Override
	public String getTaskDescription()
	{
		return "Dropping stuff";
	}
}
