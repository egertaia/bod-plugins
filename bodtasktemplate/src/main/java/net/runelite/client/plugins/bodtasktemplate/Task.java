package net.runelite.client.plugins.bodtasktemplate;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
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
	public BodTaskTemplateConfig config;

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

	public abstract boolean validate();

	public long sleepDelay()
	{
		BodTaskTemplatePlugin.sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
		return BodTaskTemplatePlugin.sleepLength;
	}

	public int tickDelay()
	{
		BodTaskTemplatePlugin.tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
		return BodTaskTemplatePlugin.tickLength;
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
