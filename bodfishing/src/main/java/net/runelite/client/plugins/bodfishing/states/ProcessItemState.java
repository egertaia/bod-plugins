package net.runelite.client.plugins.bodfishing.states;

import java.util.List;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.plugins.bodfishing.BodFishingPlugin;
import net.runelite.client.plugins.paistisuite.api.PBanking;
import net.runelite.client.plugins.paistisuite.api.PInteraction;
import net.runelite.client.plugins.paistisuite.api.PInventory;
import net.runelite.client.plugins.paistisuite.api.PUtils;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.DaxWalker;
import net.runelite.client.plugins.paistisuite.api.WebWalker.api_lib.models.RunescapeBank;
import net.runelite.client.plugins.paistisuite.api.WebWalker.wrappers.RSTile;
import net.runelite.client.plugins.paistisuite.api.types.Filters;
import net.runelite.client.plugins.paistisuite.api.types.PItem;

@Slf4j
public class ProcessItemState extends State<BodFishingPlugin>
{
	BodFishingPlugin plugin;
	boolean isDropping = false;
	boolean isCooking = false;
	boolean isBanking = false;

	public ProcessItemState(BodFishingPlugin plugin)
	{
		super(plugin);
	}

	@Override
	public boolean condition()
	{
		return PInventory.isFull();
	}

	@Override
	public String getName()
	{
		return "Processing";
	}

	@Override
	public void loop()
	{
		PUtils.sleepNormal(70, 130);

		switch (plugin.fishingChoice)
		{
			case BARBARIAN_VILLAGE:
				if (plugin.bankFishChoice) {

					if (plugin.cookedFishChoice) {
						//TODO: COOK;
						//TODO: DROP BURNT
					}

					if (isBanking) return;
					if (!PBanking.isBankOpen()) {
						isBanking = true;
						if (!DaxWalker.walkToBank(RunescapeBank.EDGEVILLE)) {
							log.info("Unable to find the bank");
						}
						return;
					}

					if (!PUtils.waitCondition(6000, () -> PBanking.isBankOpen())) {
						log.info("Bank never got opened!");
					}

					PItem trout = PInventory.findItem(Filters.Items.nameContains("trout"));
					PItem salmon = PInventory.findItem(Filters.Items.nameContains("salmon"));
					PInteraction.item(trout, "Deposit-all");
					PUtils.sleepNormal(2000, 4000);
					PInteraction.item(salmon, "Deposit-all");
					PUtils.sleepNormal(600, 1200);
					PBanking.closeBank();

					DaxWalker.walkTo(new RSTile(new WorldPoint(3105, 3433, 0)));


				}
				break;
			case BARBARIAN_OUTPOST:
				if (isDropping)
				{
					return;
				}

				Predicate<PItem> namePredicate = Filters.Items.nameContains("Leaping");

				if (plugin.dropClueScrolls)
				{
					namePredicate.or(Filters.Items.nameContains("Clue bottle"));
				}

				List<PItem> itemsToDrop = PInventory.findAllItems(namePredicate);

				if (itemsToDrop.size() > 0) {
					isDropping = true;
					itemsToDrop.forEach(item -> {
						PInteraction.item(item, "Drop");
						PUtils.sleepNormal(100, 200);
					});

					isDropping = false;
				}
				break;
		}
	}

	@Override
	public void onAnimationChanged(AnimationChanged event)
	{
	}

}
