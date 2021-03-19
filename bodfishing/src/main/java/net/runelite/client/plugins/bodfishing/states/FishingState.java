package net.runelite.client.plugins.bodfishing.states;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

	public FishingState(BodFishingPlugin plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public void onAnimationChanged(AnimationChanged event)
	{

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

		if (!isFishingAnimation())
		{
			if (!plugin.enableTickManipulation)
			{
				PUtils.sleepNormal(1800, 2400);
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
		}

		if (plugin.enableTickManipulation)
		{
			PUtils.waitCondition(1500, () -> isFishingAnimation());
			PUtils.sleepNormal(650, 800);
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

			PUtils.sleepNormal(200, 400);
			if (plugin.fishingChoice == FishingChoice.BARBARIAN_OUTPOST)
			{
				PItem dropFish = PInventory.findItem(Filters.Items.nameContains("Leaping "));
				PInteraction.item(dropFish, "Drop");
			}

			PUtils.sleepNormal(700, 800);

		}

	}

	private boolean isFishingAnimation() {
		return PPlayer.get().getAnimation() == AnimationID.FISHING_POLE_CAST || PPlayer.get().getAnimation() == 622;
	}
}
