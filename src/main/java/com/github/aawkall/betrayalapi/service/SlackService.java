package com.github.aawkall.betrayalapi.service;

public interface SlackService {

	// Registers and listens to any channels that the bot is in
	// Processes incoming text, calls appropriate betrayal methods

	// Commands to support:
	// - Create game, with optional list of players included
	// - Add players to already existing game
	// - Remove players from existing game
	// - Delete existing game
	// - Increment or decrement a stat for a player
	// - Reset a stat for a player to base index
	// - Display full stats for a character
	// - Get list of players that are alive
	// - Get list of players that are traitors
	// - NOTE: When specifying player, can use name or character name
}