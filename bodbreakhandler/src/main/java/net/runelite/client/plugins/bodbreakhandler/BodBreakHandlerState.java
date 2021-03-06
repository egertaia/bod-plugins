package net.runelite.client.plugins.bodbreakhandler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor
public enum BodBreakHandlerState
{
	NULL,

	LOGIN_SCREEN,
	INVENTORY,
	RESUME,

	LOGOUT,
	LOGOUT_TAB,
	LOGOUT_BUTTON,
	LOGOUT_WAIT,

	;
}
