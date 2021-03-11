package net.runelite.client.plugins.bodbarbarian;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import net.runelite.client.util.ColorUtil;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;

@Slf4j
@Singleton
class BodBarbarianOverlay extends OverlayPanel
{
	private final Client client;
	private final BodBarbarianPlugin plugin;
	private final BodBarbarianConfig config;

	String timeFormat;

	@Inject
	private BodBarbarianOverlay(final Client client, final BodBarbarianPlugin plugin, final BodBarbarianConfig config)
	{
		super(plugin);
		setPosition(OverlayPosition.BOTTOM_RIGHT);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "BodBarbarian overlay"));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.botTimer == null || !config.enableUI())
		{
			return null;
		}
		TableComponent tableComponent = new TableComponent();
		tableComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);

		Duration duration = Duration.between(plugin.botTimer, Instant.now());
		timeFormat = (duration.toHours() < 1) ? "mm:ss" : "HH:mm:ss";
		tableComponent.addRow("Time:", formatDuration(duration.toMillis(), timeFormat));
		tableComponent.addRow("Status:", plugin.status);

		if (!tableComponent.isEmpty())
		{
			panelComponent.setBackgroundColor(new Color(70, 61, 50, 156));
			panelComponent.setPreferredSize(new Dimension(150, 100));
			panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
			panelComponent.getChildren().add(TitleComponent.builder()
				.text("Bod Barbarian")
				.color(ColorUtil.fromHex("#40C4FF"))
				.build());
			panelComponent.getChildren().add(tableComponent);

		}
		return super.render(graphics);
	}
}
