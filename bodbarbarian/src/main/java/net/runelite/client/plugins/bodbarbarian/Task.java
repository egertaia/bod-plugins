package net.runelite.client.plugins.bodbarbarian;

import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.bodutils.CalculationUtils;
import net.runelite.client.plugins.bodutils.PlayerUtils;

@Slf4j
public abstract class Task
{
	public Task()
	{
	}

	@Inject
	public Client client;

	@Inject
	public BodBarbarianConfig config;

	@Inject
	public CalculationUtils calc;

	@Inject
	public PlayerUtils playerUtils;

	public MenuEntry entry;
	public final Set<Integer> rawFishIds = Set.of(331,335);
	public final Set<Integer> processedFIshIds = Set.of(333, 329, 343);

	public abstract boolean validate();

	public long sleepDelay()
	{
		BodBarbarianPlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
		return BodBarbarianPlugin.sleepLength;
	}

	public int tickDelay()
	{
		BodBarbarianPlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
		return BodBarbarianPlugin.tickLength;
	}

	public String getTaskDescription()
	{
		return this.getClass().getSimpleName();
	}

	public void onGameTick(GameTick event)
	{
	}

}
