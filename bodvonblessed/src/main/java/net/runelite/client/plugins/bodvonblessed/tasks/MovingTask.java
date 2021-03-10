package net.runelite.client.plugins.bodvonblessed.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodvonblessed.Task;
import net.runelite.client.plugins.bodvonblessed.BodVonBlessedPlugin;

@Slf4j
public class MovingTask extends Task
{

    @Override
    public boolean validate()
    {
        return playerUtils.isMoving(BodVonBlessedPlugin.beforeLoc);
    }

    @Override
    public String getTaskDescription()
    {
        return BodVonBlessedPlugin.status;
    }

    @Override
    public void onGameTick(GameTick event)
    {
        Player player = client.getLocalPlayer();
        if (player != null)
        {
            playerUtils.handleRun(20, 30);
            BodVonBlessedPlugin.timeout = tickDelay();
        }
    }
}