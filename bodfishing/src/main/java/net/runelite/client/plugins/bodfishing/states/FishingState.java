package net.runelite.client.plugins.bodfishing.states;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import net.runelite.api.AnimationID;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.bodfishing.BodFishingPlugin;
import net.runelite.client.plugins.bodfishing.enums.FishingChoice;
import net.runelite.client.plugins.paistisuite.api.PInteraction;
import net.runelite.client.plugins.paistisuite.api.PInventory;
import net.runelite.client.plugins.paistisuite.api.PObjects;
import net.runelite.client.plugins.paistisuite.api.PPlayer;
import net.runelite.client.plugins.paistisuite.api.PUtils;
import net.runelite.client.plugins.paistisuite.api.types.Filters;
import net.runelite.client.plugins.paistisuite.api.types.PItem;

public class FishingState extends State<BodFishingPlugin>
{
	BodFishingPlugin plugin;
	boolean fishActionFinished = false;
	final ReentrantLock fishingLock = new ReentrantLock();
	ExecutorService tickManipulationThread = Executors.newSingleThreadExecutor();

	public FishingState(BodFishingPlugin plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@Subscribe
	protected void onAnimationChanged(AnimationChanged event)
	{
		if (!event.getActor().equals(PPlayer.get()))
		{
			return;
		}
		if (isFishActionFinished())
		{
			return;
		}

		if (event.getActor().getAnimation() == AnimationID.FISHING_POLE_CAST)
		{
			if (plugin.enableTickManipulation)
			{
				tickManipulationThread.submit(() ->
				{
					PUtils.sleepNormal(650, 800);
					if (plugin.fishingChoice == FishingChoice.BARBARIAN_OUTPOST)
					{
						PItem dropFish = PInventory.findItem(Filters.Items.nameContains("Leaping "));
						PInteraction.item(dropFish, "Drop");
						PUtils.sleepNormal(100, 200);
					}

					String firstItem = "";
					String secondItem = "";
					switch (plugin.tickManipulation)
					{
						case GUAM_TAR:
							firstItem = "Swamp tar";
							secondItem = "Guam leaf";
							break;
						case TEAK_KNIFE:
							firstItem = "Knife";
							secondItem = "Teak logs";
							break;
					}

					PItem item1 = PInventory.findItem(Filters.Items.nameEquals(firstItem));
					PItem item2 = PInventory.findItem(Filters.Items.nameContains(secondItem));
					if (!PInteraction.useItemOnItem(item1, item2))
					{
						PUtils.sendGameMessage(String.format("Unable to use %s on %s", item1, item2));
					}

					setFishActionFinished(true);
				});
			}
		}
	}

	@Override
	public boolean condition()
	{
		return !PInventory.isFull();
	}

	@Override
	public String getName()
	{
		return "Fishing";
	}

	@Override
	public void loop()
	{
		PUtils.sleepNormal(70, 130);
		if (PInventory.isFull())
		{
			return;
		}

		String actionName = "";
		switch (plugin.fishingChoice)
		{
			case BARBARIAN_OUTPOST:
				actionName = "Use-rod";
				break;
			case BARBARIAN_VILLAGE:
				actionName = "Lure";
				break;
		}

		NPC fishingSpot = PObjects.findNPC(Filters.NPCs.actionsContains(actionName)
			.and(n -> n.getWorldLocation().distanceTo2D(PPlayer.getWorldLocation()) < 20));

		if (!PInteraction.npc(fishingSpot, actionName))
		{
			PUtils.sendGameMessage("Unable to find fishing spot");
		}

		setFishActionFinished(false);
		PUtils.waitCondition((int) PUtils.randomNormal(7000, 10000), this::isFishActionFinished);
		setFishActionFinished(true);

		//should 3t soon
		if (plugin.enableTickManipulation)
		{
			switch (plugin.tickManipulation)
			{
				case GUAM_TAR:
					PUtils.waitCondition((int) PUtils.randomNormal(1000, 1500), () -> PPlayer.get().getAnimation() == AnimationID.HERBLORE_MAKE_TAR);
					break;
				case TEAK_KNIFE:
					PUtils.waitCondition((int) PUtils.randomNormal(1000, 1500), () -> PPlayer.get().getAnimation() == AnimationID.FLETCHING_BOW_CUTTING);
					break;
			}
		}
	}

	public boolean isFishActionFinished()
	{
		synchronized (fishingLock)
		{
			return this.fishActionFinished;
		}
	}

	public void setFishActionFinished(boolean val)
	{
		synchronized (fishingLock)
		{
			this.fishActionFinished = val;
		}
	}
}
