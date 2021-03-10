package net.runelite.client.plugins.bodvonblessed.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.bodutils.WalkUtils;
import net.runelite.client.plugins.bodvonblessed.BodVonBlessedPlugin;
import net.runelite.client.plugins.bodvonblessed.Task;

@Slf4j
public class CloseLevelUpDialogTask extends Task
{
	@Inject
	public WalkUtils walking;

	@Override
	public boolean validate()
	{
		Widget levelUpWidget = client.getWidget(WidgetInfo.LEVEL_UP);

		return levelUpWidget != null && !levelUpWidget.isHidden();
	}

	@Override
	public String getTaskDescription()
	{
		return "Close Dialog";
	}

	@Override
	public void onGameTick(GameTick event)
	{
		Widget levelUpWidget = client.getWidget(WidgetInfo.LEVEL_UP);

		if (levelUpWidget != null && !levelUpWidget.isHidden())
		{
			Player local = client.getLocalPlayer();
			if (local == null)
			{
				return;
			}

			log.info("Walking on spot to close level up dialog");
			walking.sceneWalk(local.getWorldLocation(), 0, sleepDelay());
			BodVonBlessedPlugin.timeout = tickDelay();
		}
	}
}
