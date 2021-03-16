package net.runelite.client.plugins.bodfishing;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.XpDropEvent;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.bodfishing.enums.FishingChoice;
import net.runelite.client.plugins.bodfishing.enums.TickManipulation;
import net.runelite.client.plugins.bodfishing.states.FishingState;
import net.runelite.client.plugins.bodfishing.states.State;
import net.runelite.client.plugins.bodfishing.states.StateSet;
import net.runelite.client.plugins.paistisuite.PScript;
import net.runelite.client.plugins.paistisuite.PaistiSuite;
import net.runelite.client.plugins.paistisuite.api.PPlayer;
import net.runelite.client.plugins.paistisuite.api.PUtils;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.DaxWalker;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Extension
@PluginDependency(PaistiSuite.class)
@PluginDescriptor(
	name = "BodFishing",
	enabledByDefault = false,
	description = "Dadbod - AIO Fisher",
	tags = {"banking", "items", "paisti", "dadbod", "fishing", "tick"}
)

@Slf4j
@Singleton
public class BodFishingPlugin extends PScript
{
	public Instant startedTimestamp;
	public boolean enableTickManipulation = false;
	public TickManipulation tickManipulation = TickManipulation.TEAK_KNIFE;
	public FishingChoice fishingChoice = FishingChoice.BARBARIAN_OUTPOST;
	public boolean bankFishChoice = false;
	public int caughtFish = 0;
	public long caughtFishPerHour = 0;

	private StateSet<BodFishingPlugin> states = new StateSet<>();
	State<BodFishingPlugin> currentState;

	public FishingState fishingState = new FishingState(this);

	@Getter
	@Setter
	private String status;

	@Inject
	private BodFishingConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private BodFishingOverlay overlay;
	@Inject
	private ConfigManager configManager;

	@Provides
	BodFishingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BodFishingConfig.class);
	}

	@Override
	protected void startUp()
	{
		if (PUtils.getClient() != null && PUtils.getClient().getGameState() == GameState.LOGGED_IN)
		{
			readConfig();
		}
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		requestStop();
		overlayManager.remove(overlay);
	}

	@Subscribe
	protected void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			readConfig();
		}
	}

	private synchronized void readConfig()
	{
		enableTickManipulation = config.enableTickManipulation();
		tickManipulation = config.tickManipulationChoice();
		fishingChoice = config.fishingChoice();
		bankFishChoice = config.bankFishChoice();
	}

	private synchronized void loadStates()
	{
		states.clear();
		states.addAll(
			this.fishingState
		);
	}

	@Override
	protected synchronized void onStart()
	{
		PUtils.sendGameMessage("BodFishing started!");
		startedTimestamp = Instant.now();
		readConfig();
		DaxWalker.setCredentials(PaistiSuite.getDaxCredentialsProvider());
		DaxWalker.getInstance().allowTeleports = false;
		loadStates();
	}

	@Override
	protected synchronized void onStop()
	{
		PUtils.sendGameMessage("BodFishing stopped!");
		startedTimestamp = null;
	}

	@Subscribe
	private synchronized void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equalsIgnoreCase("BodFishing"))
		{
			return;
		}
		readConfig();
	}

	@Subscribe
	private synchronized void onConfigButtonPressed(ConfigButtonClicked configButtonClicked)
	{
		if (!configButtonClicked.getGroup().equalsIgnoreCase("BodFishing"))
		{
			return;
		}
		if (PPlayer.get() == null && PUtils.getClient().getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (configButtonClicked.getKey().equals("startButton"))
		{
			try
			{
				super.start();
			}
			catch (Exception e)
			{
				log.error(e.toString());
				e.printStackTrace();
			}
		}
		else if (configButtonClicked.getKey().equals("stopButton"))
		{
			requestStop();
		}

	}

	@Subscribe
	private synchronized void onXpDropEvent(XpDropEvent event)
	{
		if (event.getSkill() == Skill.FISHING)
		{
			//TODO: Fix this, it gives triple the fish rn.
			caughtFish++;
			caughtFishPerHour = getPerHour(caughtFish);
		}
	}

	@Override
	protected void loop()
	{
		PUtils.sleepFlat(50, 150);
		if (PUtils.getClient().getGameState() != GameState.LOGGED_IN)
		{
			return;
		}
		if (isStopRequested())
		{
			return;
		}
		updateState();
		currentState.loop();
	}

	public long getPerHour(int quantity)
	{
		if (startedTimestamp != null) {
			Duration timeSinceStart = Duration.between(startedTimestamp, Instant.now());
			if (!timeSinceStart.isZero())
			{
				return (int) ((double) quantity * (double) Duration.ofHours(1).toMillis() / (double) timeSinceStart.toMillis());
			}
		}
		return 0;
	}

	@Subscribe
	protected void onAnimationChanged(AnimationChanged event)
	{
		if (!event.getActor().equals(PPlayer.get()))
		{
			return;
		}
		updateState();
		if (currentState != null) {
			currentState.onAnimationChanged(event);
			log.info("on anim change @ main");
		}
	}

	private void updateState() {
		currentState = states.getValidState();
		if (currentState != null)
		{
			setStatus(currentState.getName());
		}
		else
		{
			setStatus("?");
		}
	}

}
