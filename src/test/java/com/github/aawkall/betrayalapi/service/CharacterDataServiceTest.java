package com.github.aawkall.betrayalapi.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.github.aawkall.betrayalapi.config.TestContextNoDb;
import com.github.aawkall.betrayalapi.entity.Player.Character;
import com.github.aawkall.betrayalapi.exception.BetrayalException;
import com.github.aawkall.betrayalapi.util.BetrayalConst;
import com.github.aawkall.betrayalapi.util.BetrayalConst.Stat;

import junit.framework.Assert;

@ContextConfiguration(classes = TestContextNoDb.class)
@TestExecutionListeners(inheritListeners = false, value = { DependencyInjectionTestExecutionListener.class })
public class CharacterDataServiceTest extends AbstractTestNGSpringContextTests {

	private static final int baseSpeedIndexJaspers = 3;
	private static final int baseMightIndexZostra = 4;
	private static final int baseSanityIndexIngstrom = 3;
	private static final int baseKnowledgeIndexLongfellow = 5;

	private static final int index1SpeedJaspers = 3;
	private static final int index7MightZostra = 5;
	private static final int index3SanityIngstrom = 5;
	private static final int index1KnowledgeLongfellow = 4;

	private static final String colorJaspers = "green";
	private static final String colorZostra = "blue";
	private static final String colorIngstrom = "yellow";
	private static final String colorLongfellow = "white";

	@Inject
	private CharacterDataService characterDataService;

	@Test
	public void testGetStatBaseIndex() throws BetrayalException {
		// Get 1 example of the base stat index for 4 different characters and verify against known values
		int getBaseSpeedIndex = characterDataService.getStatBaseIndex(Stat.SPEED, Character.BRANDON_JASPERS);
		int getBaseMightIndex = characterDataService.getStatBaseIndex(Stat.MIGHT, Character.MADAME_ZOSTRA);
		int getBaseSanityIndex = characterDataService.getStatBaseIndex(Stat.SANITY, Character.ZOE_INGSTROM);
		int getBaseKnowledgeIndex = characterDataService.getStatBaseIndex(Stat.KNOWLEDGE, Character.PROFESSOR_LONGFELLOW);
		Assert.assertEquals(getBaseSpeedIndex, baseSpeedIndexJaspers);
		Assert.assertEquals(getBaseMightIndex, baseMightIndexZostra);
		Assert.assertEquals(getBaseSanityIndex, baseSanityIndexIngstrom);
		Assert.assertEquals(getBaseKnowledgeIndex, baseKnowledgeIndexLongfellow);
	}

	@Test
	public void testGetStatValue_invalidIndex() throws BetrayalException {
		// Test both limits of the stat index parameter in getStatValue
		try {
			characterDataService.getStatValue(Stat.SPEED, -1, Character.BRANDON_JASPERS);
			Assert.fail("GetStatValue should throw an exception when requested index is below zero");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.INTERNAL_SERVER_ERROR);
			Assert.assertTrue(e.getMessage().contains("Index must be between"));
		}
		try {
			characterDataService.getStatValue(Stat.SPEED, BetrayalConst.MAX_STAT_INDEX + 1, Character.BRANDON_JASPERS);
			Assert.fail("GetStatValue should throw an exception when requested index is above the max stat index");
		} catch (BetrayalException e) {
			Assert.assertEquals(e.getHttpCode(), Response.Status.INTERNAL_SERVER_ERROR);
			Assert.assertTrue(e.getMessage().contains("Index must be between"));
		}
	}

	@Test
	public void testGetStatValue_success() throws BetrayalException {
		// Get 1 example of the stat value for 4 different characters and verify against known values
		// Each example has a value that is different numerically than its index to ensure values are correct
		int getSpeedValue = characterDataService.getStatValue(Stat.SPEED, 1, Character.BRANDON_JASPERS);
		int getMightValue = characterDataService.getStatValue(Stat.MIGHT, 7, Character.MADAME_ZOSTRA);
		int getSanityValue = characterDataService.getStatValue(Stat.SANITY, 3, Character.ZOE_INGSTROM);
		int getKnowledgeValue = characterDataService.getStatValue(Stat.KNOWLEDGE, 1, Character.PROFESSOR_LONGFELLOW);
		Assert.assertEquals(getSpeedValue, index1SpeedJaspers);
		Assert.assertEquals(getMightValue, index7MightZostra);
		Assert.assertEquals(getSanityValue, index3SanityIngstrom);
		Assert.assertEquals(getKnowledgeValue, index1KnowledgeLongfellow);
	}

	@Test
	public void testGetColor() throws BetrayalException {
		// Get color for 4 different characters, verify against known values
		String getColorJaspers = characterDataService.getColor(Character.BRANDON_JASPERS);
		String getColorZostra = characterDataService.getColor(Character.MADAME_ZOSTRA);
		String getColorIngstrom = characterDataService.getColor(Character.ZOE_INGSTROM);
		String getColorLongfellow = characterDataService.getColor(Character.PROFESSOR_LONGFELLOW);
		Assert.assertEquals(getColorJaspers, colorJaspers);
		Assert.assertEquals(getColorZostra, colorZostra);
		Assert.assertEquals(getColorIngstrom, colorIngstrom);
		Assert.assertEquals(getColorLongfellow, colorLongfellow);
	}

	@Test
	public void testGetFlipSide() throws BetrayalException {
		// Get flipside for a pair of characters, ensure the flipsides are correct and that they have the same color
		Character getFlipSideJaspers = characterDataService.getFlipSide(Character.BRANDON_JASPERS);
		Character getFlipSideAkimoto = characterDataService.getFlipSide(Character.PETER_AKIMOTO);
		Assert.assertEquals(getFlipSideJaspers, Character.PETER_AKIMOTO);
		Assert.assertEquals(getFlipSideAkimoto, Character.BRANDON_JASPERS);
		Assert.assertEquals(characterDataService.getColor(getFlipSideJaspers), characterDataService.getColor(getFlipSideAkimoto));
	}
}