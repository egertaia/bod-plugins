package net.runelite.client.plugins.bodbarbarian.tasks;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.plugins.bodbarbarian.BodBarbarianPlugin;
import net.runelite.client.plugins.bodbarbarian.Task;
import net.runelite.client.plugins.bodutils.BodUtils;
import net.runelite.client.plugins.bodutils.MenuUtils;

@Slf4j
public class CookingMenuTask extends Task
{
	@Inject
	BodBarbarianPlugin plugin;

	@Inject
	BodUtils bodUtils;

	@Inject
	MenuUtils menu;

	@Override
	public boolean validate()
	{
		return client.getWidget(WidgetID.MULTISKILL_MENU_GROUP_ID, 5) != null && !client.getWidget( WidgetID.MULTISKILL_MENU_GROUP_ID, 5).isHidden();
	}

	@Override
	public void onGameTick(GameTick event)
	{
		entry = new MenuEntry("", "", 1, 57, -1, 17694734, false);
		menu.setEntry(entry);
		bodUtils.doActionMsTime(entry, client.getWidget(WidgetID.MULTISKILL_MENU_GROUP_ID, 14).getBounds(), sleepDelay());
	}

	@Override
	public String getTaskDescription()
	{
		return "Cooking Menu";
	}
}
