package net.runelite.client.plugins.bodfishing;

import net.runelite.client.config.Button;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.bodfishing.enums.FishingChoice;
import net.runelite.client.plugins.bodfishing.enums.TickManipulation;

@ConfigGroup("BodFishing")
public interface BodFishingConfig extends Config
{
	@ConfigItem(
		keyName = "enableOverlay",
		name = "Enable overlay",
		description = "Enable drawing of the overlay",
		position = 0
	)
	default boolean enableOverlay()
	{
		return false;
	}

	@ConfigItem(
		keyName = "enableTickManipulation",
		name = "Enable 3T",
		description = "Enable 3t fishing",
		position = 1
	)
	default boolean enableTickManipulation()
	{
		return true;
	}

	@ConfigItem(
		keyName = "tickManipulationChoice",
		name = "3T Method",
		description = "Select the tick manipulation method you would like to perform",
		hidden = true,
		unhide = "enableTickManipulation",
		unhideValue = "true",
		position = 2
	)
	default TickManipulation tickManipulationChoice()
	{
		return TickManipulation.TEAK_KNIFE;
	}

	@ConfigItem(
		keyName = "fishingChoice",
		name = "Fishing Place",
		description = "Select the fishing place you would like to fish at",
		position = 3
	)
	default FishingChoice fishingChoice()
	{
		return FishingChoice.BARBARIAN_OUTPOST;
	}

	@ConfigItem(
		keyName = "bankFishChoice",
		name = "Bank raw fish?",
		description = "Select whether you would like to bank raw fish. This as a false, means it will process food right away.",
		hidden = true,
		unhide = "fishingChoice",
		unhideValue = "Barbarian Village",
		position = 4
	)
	default boolean bankFishChoice()
	{
		return false;
	}

	@ConfigItem(
		keyName = "dropClueScrolls",
		name = "Drop clues?",
		description = "Enable dropping of clues",
		position = 5
	)
	default boolean dropClueScrolls()
	{
		return true;
	}

	@ConfigItem(
		keyName = "startButton",
		name = "Start",
		description = "Start",
		position = 101
	)
	default Button startButton()
	{
		return new Button();
	}

	@ConfigItem(
		keyName = "stopButton",
		name = "Stop",
		description = "Stop",
		position = 102
	)
	default Button stopButton()
	{
		return new Button();
	}
}
