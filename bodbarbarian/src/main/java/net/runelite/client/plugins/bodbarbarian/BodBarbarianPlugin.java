package net.runelite.client.plugins.bodbarbarian;

import com.google.inject.Injector;
import com.google.inject.Provides;
import java.time.Instant;
import java.util.Set;
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
import net.runelite.client.plugins.bodbarbarian.tasks.AnimatingTask;
import net.runelite.client.plugins.bodbarbarian.tasks.CookingMenuTask;
import net.runelite.client.plugins.bodbarbarian.tasks.DroppingProcessedTask;
import net.runelite.client.plugins.bodbarbarian.tasks.FindFishingSpotTask;
import net.runelite.client.plugins.bodbarbarian.tasks.CookSalmonTask;
import net.runelite.client.plugins.bodbarbarian.tasks.CookTroutTask;
import net.runelite.client.plugins.bodbarbarian.tasks.ReturnToVillageTask;
import net.runelite.client.plugins.bodbreakhandler.BodBreakHandler;
import net.runelite.client.plugins.bodbarbarian.tasks.MovingTask;
import net.runelite.client.plugins.bodbarbarian.tasks.TimeoutTask;
import net.runelite.client.plugins.bodutils.BodUtils;
import net.runelite.client.plugins.bodutils.InventoryUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;


@Extension
@PluginDependency(BodUtils.class)
@PluginDescriptor(
	name = "Bod Barbarian",
	enabledByDefault = false,
	description = "Dadbod - Barbarian Village Plugin",
	tags = {"dadbod", "fishing", "cooking", "bot"}
)
@Slf4j
public class BodBarbarianPlugin extends Plugin
{
	@Inject
	private Injector injector;

	@Inject
	private Client client;

	@Inject
	private BodBarbarianConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BodBarbarianOverlay overlay;

	@Inject
	public BodBreakHandler bodBreakHandler;

	@Inject
	private ConfigManager configManager;

	@Inject
	InventoryUtils inventory;

	private TaskSet tasks = new TaskSet();
	public static LocalPoint beforeLoc = new LocalPoint(0, 0);

	MenuEntry targetMenu;
	Instant botTimer;
	Player player;
	Set<Integer> requiredItemsIds = Set.of(314, 309);


	public static boolean startBot;
	public static long sleepLength;
	public static int tickLength;
	public static int timeout;
	public static String status = "starting...";

	@Provides
	BodBarbarianConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BodBarbarianConfig.class);
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
			injector.getInstance(ReturnToVillageTask.class),
			injector.getInstance(MovingTask.class),
			injector.getInstance(AnimatingTask.class),
			injector.getInstance(CookingMenuTask.class),
			injector.getInstance(CookTroutTask.class),
			injector.getInstance(CookSalmonTask.class),
			injector.getInstance(DroppingProcessedTask.class),
			injector.getInstance(FindFishingSpotTask.class)
		);
	}

	public void resetVals()
	{
		log.debug("stopping Bod Barbarian");
		overlayManager.remove(overlay);
		bodBreakHandler.stopPlugin(this);
		startBot = false;
		botTimer = null;
		tasks.clear();
	}

	@Subscribe
	private void onConfigButtonPressed(ConfigButtonClicked configButtonClicked)
	{
		if (!configButtonClicked.getGroup().equalsIgnoreCase("BodBarbarian"))
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
					if (inventory.containsItem(requiredItemsIds)) {
						log.info("starting Bod Barbarian plugin");
						loadTasks();
						startBot = true;
						bodBreakHandler.startPlugin(this);
						timeout = 0;
						targetMenu = null;
						botTimer = Instant.now();
						overlayManager.add(overlay);
						beforeLoc = client.getLocalPlayer().getLocalLocation();
					} else {
						log.info ("You don't have required items.");
					}

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