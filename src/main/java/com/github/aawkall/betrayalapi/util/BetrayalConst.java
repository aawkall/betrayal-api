package com.github.aawkall.betrayalapi.util;

public class BetrayalConst {
	public static final String MONGO_HOST_KEY = "mongoHost";
	public static final String DOCKER_MONGO_HOST = "localhost";
	public static final String DB_NAME = "betrayal";

	// Each character's stat has an array with 9 items, max index = 8
	public static final int MAX_STAT_INDEX = 8;

	public static final int MAX_HAUNT_NUMBER = 100;

	public enum Stat {
		SPEED,
		MIGHT,
		SANITY,
		KNOWLEDGE
	}
}