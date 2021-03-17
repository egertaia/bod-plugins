package net.runelite.client.plugins.bodfishing.states;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.plugins.bodfishing.BodFishingPlugin;
import net.runelite.client.plugins.bodfishing.enums.FishingChoice;
import net.runelite.client.plugins.paistisuite.api.PInteraction;
import net.runelite.client.plugins.paistisuite.api.PInventory;
import net.runelite.client.plugins.paistisuite.api.PObjects;
import net.runelite.client.plugins.paistisuite.api.PPlayer;
import net.runelite.client.plugins.paistisuite.api.PUtils;
import net.runelite.client.plugins.paistisuite.api.types.Filters;
import net.runelite.client.plugins.paistisuite.api.types.PItem;

@Slf4j
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

	@Override
	public void onAnimationChanged(AnimationChanged event)
	{
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
					switch (plugin.tickManipulation)
					{
						case GUAM_TAR:
							PItem tar = PInventory.findItem(Filters.Items.nameEquals("Swamp tar"));
							PItem herb = PInventory.findItem(Filters.Items.nameContains("Guam leaf"));
							if (!PInteraction.useItemOnItem(tar, herb))
							{
								PUtils.sendGameMessage("Unable to make guam tar");
							}
							break;
						case TEAK_KNIFE:
							PItem log = PInventory.findItem(Filters.Items.nameEquals("Teak logs"));
							PItem knife = PInventory.findItem(Filters.Items.nameEquals("Knife"));
							if (!PInteraction.useItemOnItem(log, knife))
							{
								PUtils.sendGameMessage("Unable to cut teak log");
							}
					}

					PUtils.sleepNormal(150, 400);
					if (plugin.fishingChoice == FishingChoice.BARBARIAN_OUTPOST)
					{
						PItem dropFish = PInventory.findItem(Filters.Items.nameContains("Leaping "));
						PInteraction.item(dropFish, "Drop");
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

		if (plugin.enableTickManipulation || isFishActionFinished()) {
			NPC fishingSpot = PObjects.findNPC(Filters.NPCs.actionsContains(actionName)
				.and(n -> n.getWorldLocation().distanceTo2D(PPlayer.getWorldLocation()) < 20));

			if (!PInteraction.npc(fishingSpot, actionName))
			{
				PUtils.sendGameMessage("Unable to find fishing spot");
			}
		}

		setFishActionFinished(false);
		PUtils.waitCondition((int) PUtils.randomNormal(1200, 2400), this::isFishActionFinished);
		if (PPlayer.get().getAnimation() == AnimationID.IDLE || plugin.enableTickManipulation) {
			setFishActionFinished(true);
		}

		//should 3t soon
		if (plugin.enableTickManipulation)
		{
			switch (plugin.tickManipulation)
			{
				case GUAM_TAR:
					PUtils.waitCondition((int) PUtils.randomNormal(1000, 1500), () -> PPlayer.get().getAnimation() == AnimationID.HERBLORE_MAKE_TAR);
					setFishActionFinished(false);
					break;
				case TEAK_KNIFE:
					PUtils.waitCondition((int) PUtils.randomNormal(1000, 1500), () -> PPlayer.get().getAnimation() == AnimationID.FLETCHING_BOW_CUTTING);
					setFishActionFinished(false);
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
