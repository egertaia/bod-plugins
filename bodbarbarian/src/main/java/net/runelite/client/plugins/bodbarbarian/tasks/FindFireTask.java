package net.runelite.client.plugins.bodbarbarian.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.MenuEntry;
import net.runelite.api.ObjectID;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodbarbarian.BodBarbarianPlugin;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodutils.BodUtils;
import net.runelite.client.plugins.bodutils.InventoryUtils;
import net.runelite.client.plugins.bodutils.ObjectUtils;

@Slf4j
public abstract class FindFireTask extends Task
{
	public FindFireTask (int fishId) {
		this.fishId = fishId;
	}

	@Inject
	BodBarbarianPlugin plugin;

	@Inject
	BodUtils bodUtils;

	@Inject
	InventoryUtils inventory;

	@Inject
	ObjectUtils object;

	int fishId;

	@Override
	public void onGameTick(GameTick event)
	{
		GameObject fire = object.findNearestGameObjectWithin(client.getLocalPlayer().getWorldLocation(), 10, ObjectID.FIRE_26185);
		if (fire != null) {
			entry = new MenuEntry("", "", fire.getId(), 1, fire.getSceneMinLocation().getX(), fire.getSceneMaxLocation().getY(), false);
			bodUtils.doModifiedActionMsTime(
				entry,
				fishId,
				inventory.getWidgetItem(fishId).getIndex(),
				1,
				fire.getConvexHull().getBounds(),
				sleepDelay()
			);

			plugin.timeout = tickDelay();
		} else {
			log.info("BodBarbarian couldn't find a fire.");
		}
	}
}
