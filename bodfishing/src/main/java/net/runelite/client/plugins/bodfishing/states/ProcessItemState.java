package net.runelite.client.plugins.bodfishing.states;

import net.runelite.client.plugins.bodfishing.BodFishingPlugin;
import net.runelite.client.plugins.paistisuite.api.PInventory;

public class ProcessItemState extends State<BodFishingPlugin>
{
	BodFishingPlugin plugin;
	boolean isDropping = false;
	boolean isCooking = false;

	public ProcessItemState(BodFishingPlugin plugin) {
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

	}

}
