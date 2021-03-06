/*
 * Copyright (c) 2018, SomeoneWithAnInternetConnection
 * Copyright (c) 2018, oplosthee <https://github.com/oplosthee>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.bodtasktemplate;

import com.google.inject.Injector;
import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.bodbreakhandler.BodBreakHandler;
import net.runelite.client.plugins.bodtasktemplate.tasks.MovingTask;
import net.runelite.client.plugins.bodtasktemplate.tasks.TimeoutTask;
import net.runelite.client.plugins.bodutils.BodUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;


@Extension
@PluginDependency(BodUtils.class)
@PluginDescriptor(
	name = "iTaskTemplate",
	enabledByDefault = false,
	description = "Illumine - Task Template plugin",
	tags = {"illumine", "task", "template", "bot"}
)
@Slf4j
public class BodTaskTemplatePlugin extends Plugin
{
	@Inject
	private Injector injector;

	@Inject
	private Client client;

	@Inject
	private BodTaskTemplateConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BodTaskTemplateOverlay overlay;

	@Inject
	private BodUtils utils;

	@Inject
	public BodBreakHandler bodBreakHandler;

	@Inject
	private ConfigManager configManager;

	private TaskSet tasks = new TaskSet();
	public static LocalPoint beforeLoc = new LocalPoint(0, 0);

	MenuEntry targetMenu;
	Instant botTimer;
	Player player;

	public static boolean startBot;
	public static long sleepLength;
	public static int tickLength;
	public static int timeout;
	public static String status = "starting...";

	@Provides
	BodTaskTemplateConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BodTaskTemplateConfig.class);
	}

	@Override
	protected void startUp()
	{
		bodBreakHandler.registerPlugin(this);
	}

	@Override
	protected void shutDown()
	{
		resetVals();
		bodBreakHandler.unregisterPlugin(this);
	}


	private void loadTasks()
	{
		tasks.clear();
		tasks.addAll(
			injector.getInstance(TimeoutTask.class),
			injector.getInstance(MovingTask.class)
		);
	}

	public void resetVals()
	{
		log.debug("stopping Task Template plugin");
		overlayManager.remove(overlay);
		bodBreakHandler.stopPlugin(this);
		startBot = false;
		botTimer = null;
		tasks.clear();
	}

	@Subscribe
	private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked)
	{
		if (!configButtonClicked.getGroup().equalsIgnoreCase("iTaskTemplate"))
		{
			return;
		}
		log.debug("button {} pressed!", configButtonClicked.getKey());
		if (configButtonClicked.getKey().equals("startButton"))
		{
			if (!startBot)
			{
				Player player = client.getLocalPlayer();
				if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN)
				{
					log.info("starting Task Template plugin");
					loadTasks();
					startBot = true;
					bodBreakHandler.startPlugin(this);
					timeout = 0;
					targetMenu = null;
					botTimer = Instant.now();
					overlayManager.add(overlay);
					beforeLoc = client.getLocalPlayer().getLocalLocation();
				}
				else
				{
					log.info("Start logged in");
				}
			}
			else
			{
				resetVals();
			}
		}
	}

	public void updateStats()
	{
		//templatePH = (int) getPerHour(totalBraceletCount);
		//coinsPH = (int) getPerHour(totalCoins - ((totalCoins / BRACELET_HA_VAL) * (unchargedBraceletCost + revEtherCost + natureRuneCost)));
	}

	public long getPerHour(int quantity)
	{
		Duration timeSinceStart = Duration.between(botTimer, Instant.now());
		if (!timeSinceStart.isZero())
		{
			return (int) ((double) quantity * (double) Duration.ofHours(1).toMillis() / (double) timeSinceStart.toMillis());
		}
		return 0;
	}

	@Subscribe
	private void onGameTick(GameTick event)
	{
		if (!startBot || bodBreakHandler.isBreakActive(this))
		{
			return;
		}
		player = client.getLocalPlayer();
		if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN)
		{
			if (bodBreakHandler.shouldBreak(this))
			{
				status = "Taking a break";
				bodBreakHandler.startBreak(this);
				timeout = 5;
			}

			Task task = tasks.getValidTask();
			if (task != null)
			{
				status = task.getTaskDescription();
				task.onGameTick(event);
			}
			else
			{
				status = "Task not found";
				log.debug(status);
			}
			beforeLoc = player.getLocalLocation();
		}
	}
}