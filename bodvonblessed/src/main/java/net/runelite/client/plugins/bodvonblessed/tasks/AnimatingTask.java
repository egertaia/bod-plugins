package net.runelite.client.plugins.bodvonblessed.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.client.plugins.bodvonblessed.Task;

@Slf4j
public class AnimatingTask extends Task
{
	@Override
	public boolean validate()
	{
		return client.getLocalPlayer().getAnimation() != -1;
	}

	@Override
	public String getTaskDescription()
	{
		switch(client.getLocalPlayer().getAnimation()) {
			case AnimationID.FISHING_POLE_CAST:
				return "Fishing";
			default:
				return "?";
		}
	}

}
