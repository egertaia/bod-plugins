package net.runelite.client.plugins.bodvonblessed;

import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodutils.CalculationUtils;
import net.runelite.client.plugins.bodutils.MenuUtils;
import net.runelite.client.plugins.bodutils.MouseUtils;
import net.runelite.client.plugins.bodutils.ObjectUtils;
import net.runelite.client.plugins.bodutils.PlayerUtils;
import net.runelite.client.plugins.bodutils.BodUtils;

@Slf4j
public abstract class Task
{
    public Task()
    {
    }

    @Inject
    public Client client;

    @Inject
    public BodVonBlessedConfig config;

    @Inject
    public BodUtils utils;

    @Inject
    public MenuUtils menu;

    @Inject
    public MouseUtils mouse;

    @Inject
    public CalculationUtils calc;

    @Inject
    public PlayerUtils playerUtils;

    @Inject
    public ObjectUtils object;

    public MenuEntry entry;
    public final Set<Integer> rawFishIds = Set.of(ItemID.LEAPING_SALMON, ItemID.LEAPING_TROUT, ItemID.LEAPING_STURGEON);


    public abstract boolean validate();

    public long sleepDelay()
    {
        BodVonBlessedPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
        return BodVonBlessedPlugin.sleepLength;
    }

    public int tickDelay()
    {
        BodVonBlessedPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
        return BodVonBlessedPlugin.tickLength;
    }

    public String getTaskDescription()
    {
        return this.getClass().getSimpleName();
    }

    public void onGameTick(GameTick event)
    {
        return;
    }

}
