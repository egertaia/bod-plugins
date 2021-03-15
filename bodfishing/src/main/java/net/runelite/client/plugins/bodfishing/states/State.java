package net.runelite.client.plugins.bodfishing.states;

public abstract class State<T>
{
	T plugin;
	String name;

	public State(T plugin){
		this.plugin = plugin;
		this.name = getName();
	}

	public abstract boolean condition();
	public abstract String getName();
	public abstract void loop();
}