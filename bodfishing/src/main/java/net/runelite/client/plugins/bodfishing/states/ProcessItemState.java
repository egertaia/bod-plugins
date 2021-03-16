package net.runelite.client.plugins.bodfishing.states;

import java.util.List;
import java.util.function.Predicate;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.plugins.bodfishing.BodFishingPlugin;
import net.runelite.client.plugins.paistisuite.api.PInteraction;
import net.runelite.client.plugins.paistisuite.api.PInventory;
import net.runelite.client.plugins.paistisuite.api.PUtils;
import net.runelite.client.plugins.paistisuite.api.types.Filters;
import net.runelite.client.plugins.paistisuite.api.types.PItem;

public class ProcessItemState extends State<BodFishingPlugin>
{
	BodFishingPlugin plugin;
	boolean isDropping = false;
	boolean isCooking = false;

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
		switch (plugin.fishingChoice)
		{
			case BARBARIAN_VILLAGE:
				//todo:
				break;
			case BARBARIAN_OUTPOST:
				if (isDropping)
				{
					return;
				}

				Predicate<PItem> namePredicate = Filters.Items.nameContains("Leaping");

				if (plugin.dropClueScrolls)
				{
					namePredicate.and(Filters.Items.nameContains("Clue bottle"));
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
