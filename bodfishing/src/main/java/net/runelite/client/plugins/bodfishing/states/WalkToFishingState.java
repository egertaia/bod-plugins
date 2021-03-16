package net.runelite.client.plugins.bodfishing.states;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.plugins.bodfishing.BodFishingPlugin;
import net.runelite.client.plugins.paistisuite.api.PPlayer;
import net.runelite.client.plugins.paistisuite.api.PUtils;
import net.runelite.client.plugins.paistisuite.api.PWalking;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.DaxWalker;
import net.runelite.client.plugins.paistisuite.api.WebWalker.wrappers.RSTile;

@Slf4j
public class WalkToFishingState extends State<BodFishingPlugin>
{
	BodFishingPlugin plugin;
	WorldPoint barbarianVillageLocation = new WorldPoint(3105, 3433, 0);
	WorldPoint barbarianOutpostLocation = new WorldPoint(2498, 3510, 0);

	public WalkToFishingState(BodFishingPlugin plugin)
	{
		super(plugin);
	}

	@Override
	public boolean condition()
	{
		Client client = PUtils.getClient();
		return barbarianVillageLocation.isInScene(client) || barbarianOutpostLocation.isInScene(client);
	}

	@Override
	public String getName()
	{
		return "Returning";
	}

	@Override
	public void loop()
	{
		WorldPoint targetLocation = null;
		switch (plugin.fishingChoice) {
			case BARBARIAN_OUTPOST:
				targetLocation = barbarianOutpostLocation;
				break;
			case BARBARIAN_VILLAGE:
				targetLocation = barbarianVillageLocation;
				break;
		}

		if (!PPlayer.isMoving()) {
			if (targetLocation.isInScene(PUtils.getClient()) && PWalking.sceneWalk(targetLocation)) {
				PUtils.sleepNormal(650, 1500);
			} else if (!DaxWalker.walkTo(new RSTile(targetLocation))) {
				log.info("Unable to move to " + plugin.fishingChoice.name);
				PUtils.sendGameMessage("Unable to move to " + plugin.fishingChoice.name);
				PUtils.sleepNormal(650, 1500);
				plugin.requestStop();
			}
		}
	}

	@Override
	public void onAnimationChanged(AnimationChanged event)
	{
	}
}
