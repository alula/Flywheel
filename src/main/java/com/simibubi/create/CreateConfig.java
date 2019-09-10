package com.simibubi.create;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class CreateConfig {

	public static final ForgeConfigSpec specification;
	public static final CreateConfig parameters;

	static {
		final Pair<CreateConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CreateConfig::new);

		specification = specPair.getRight();
		parameters = specPair.getLeft();
	}

	// Schematics
	public IntValue maxSchematics, maxTotalSchematicSize, maxSchematicPacketSize, schematicIdleTimeout;
	public IntValue schematicannonDelay, schematicannonSkips;
	public DoubleValue schematicannonGunpowderWorth, schematicannonFuelUsage;
	public ConfigValue<String> schematicPath;
	
	// Curiosities
	public IntValue maxSymmetryWandRange;
	
	// Contraptions
	public IntValue maxBeltLength, crushingDamage, maxMotorSpeed, maxRotationSpeed;
	public IntValue fanMaxPushDistance, fanMaxPullDistance, fanBlockCheckRate, fanRotationArgmax;
	public IntValue maxChassisForTranslation, maxChassisForRotation, maxChassisRange, maxPistonPoles;
	
	// Logistics
	public IntValue extractorDelay, extractorAmount, linkRange;
	
	// Gardens
	public DoubleValue cocoaLogGrowthSpeed;

	CreateConfig(final ForgeConfigSpec.Builder builder) {

		// Schematics
		initSchematics(builder);
		initContraptions(builder);
		initCuriosities(builder);
		initLogistics(builder);
		initGardens(builder);
	}

	private void initGardens(Builder builder) {
		builder.comment("The Gardens Module").push("gardens");
		String basePath = "create.config.gardens";
		String name = "";
		
		name = "cocoaLogGrowthSpeed";
		cocoaLogGrowthSpeed = builder
				.comment("", "% of random Ticks causing a Cocoa log to grow.")
				.translation(basePath + name).defineInRange(name, 0D, 20D, 100D);
		
		builder.pop();
	}

	private void initLogistics(Builder builder) {
		builder.comment("The Logistics Module").push("logistics");
		String basePath = "create.config.logistics";
		String name = "";
		
		name = "extractorDelay";
		extractorDelay = builder
				.comment("", "The amount of game ticks an Extractor waits after pulling an item successfully.")
				.translation(basePath + name).defineInRange(name, 20, 1, Integer.MAX_VALUE);
		
		name = "extractorAmount";
		extractorAmount = builder
				.comment("", "The amount of items an extractor pulls at a time without an applied filter.")
				.translation(basePath + name).defineInRange(name, 16, 1, 64);
		
		name = "linkRange";
		linkRange = builder
				.comment("", "Maximum possible range in blocks of redstone link connections.")
				.translation(basePath + name).defineInRange(name, 128, 4, Integer.MAX_VALUE);
		
		builder.pop();
	}

	private void initContraptions(Builder builder) {
		builder.comment("The Contraptions Module").push("contraptions");
		String basePath = "create.config.contraptions";
		String name = "";
		
		name = "maxBeltLength";
		maxBeltLength = builder
				.comment("", "Maximum length in blocks of mechanical belts.")
				.translation(basePath + name).defineInRange(name, 20, 5, Integer.MAX_VALUE);
		
		name = "crushingDamage";
		crushingDamage = builder
				.comment("", "Damage dealt by active Crushing Wheels.")
				.translation(basePath + name).defineInRange(name, 4, 0, Integer.MAX_VALUE);
		
		{
			builder.comment("Encased Fan").push("encasedFan");
			basePath = "create.config.contraptions.encasedFan";
			
			name = "fanBlockCheckRate";
			fanBlockCheckRate = builder
					.comment("", "Game ticks between Fans checking for anything blocking their air flow.")
					.translation(basePath + name).defineInRange(name, 100, 20, Integer.MAX_VALUE);
			
			name = "fanMaxPushDistance";
			fanMaxPushDistance = builder
					.comment("", "Maximum distance in blocks Fans can push entities.")
					.translation(basePath + name).defineInRange(name, 20, 1, Integer.MAX_VALUE);
			
			name = "fanMaxPullDistance";
			fanMaxPullDistance = builder
					.comment("", "Maximum distance in blocks from where Fans can pull entities.")
					.translation(basePath + name).defineInRange(name, 5, 1, Integer.MAX_VALUE);
			
			name = "fanRotationArgmax";
			fanRotationArgmax = builder
					.comment("", "Rotation speed at which the maximum stats of fans are reached.")
					.translation(basePath + name).defineInRange(name, 8192, 64, Integer.MAX_VALUE);
			
			
			builder.pop();
		}
		
		{
			builder.comment("Mechanical Pistons and Bearings").push("constructs");
			basePath = "create.config.contraptions.constructs";
			
			name = "maxChassisRange";
			maxChassisRange = builder
					.comment("", "Maximum value of a chassis attachment range.")
					.translation(basePath + name).defineInRange(name, 16, 1, Integer.MAX_VALUE);
			
			name = "maxChassisForRotation";
			maxChassisForRotation = builder
					.comment("", "Maximum amount of chassis blocks movable by a Mechanical Bearing.")
					.translation(basePath + name).defineInRange(name, 16, 1, Integer.MAX_VALUE);
			
			name = "maxChassisForTranslation";
			maxChassisForTranslation = builder
					.comment("", "Maximum amount of chassis blocks movable by a Mechanical Piston.")
					.translation(basePath + name).defineInRange(name, 16, 1, Integer.MAX_VALUE);
			
			name = "maxPistonPoles";
			maxPistonPoles = builder
					.comment("", "Maximum amount of extension poles behind a Mechanical Piston.")
					.translation(basePath + name).defineInRange(name, 64, 1, Integer.MAX_VALUE);
			
			builder.pop();
		}
		
		name = "maxMotorSpeed";
		maxMotorSpeed = builder
				.comment("", "Maximum allowed speed of a configurable motor.")
				.translation(basePath + name).defineInRange(name, 4096, 64, Integer.MAX_VALUE);
		
		name = "maxRotationSpeed";
		maxRotationSpeed = builder
				.comment("", "Maximum allowed rotation speed for any Kinetic Tile.")
				.translation(basePath + name).defineInRange(name, 16384, 64, Integer.MAX_VALUE);
		
		builder.pop();
	}
	
	private void initCuriosities(Builder builder) {
		builder.comment("The Curiosities Module").push("curiosities");
		String basePath = "create.config.curiosities";
		String name = "";
		
		name = "maxSymmetryWandRange";
		maxSymmetryWandRange = builder
				.comment("", "The Maximum Distance to an active mirror for the symmetry wand to trigger.")
				.translation(basePath + name).defineInRange(name, 50, 10, Integer.MAX_VALUE);
		
		builder.pop();
	}

	public void initSchematics(final ForgeConfigSpec.Builder builder) {
		builder.comment("The Schematics Module").push("schematics");
		String basePath = "create.config.schematics";
		String name = "";

		name = "maxSchematics";
		maxSchematics = builder
				.comment("", "The amount of Schematics a player can upload until previous ones are overwritten.")
				.translation(basePath + name).defineInRange(name, 10, 1, Integer.MAX_VALUE);

		name = "schematicPath";
		schematicPath = builder.comment("", "The file location where uploaded Schematics are stored.").define(name,
				"schematics/uploaded", this::isValidPath);

		name = "maxTotalSchematicSize";
		maxTotalSchematicSize = builder
				.comment("", "[in KiloBytes]", "The maximum allowed file size of uploaded Schematics.")
				.translation(basePath + name).defineInRange(name, 256, 16, Integer.MAX_VALUE);

		name = "maxSchematicPacketSize";
		maxSchematicPacketSize = builder
				.comment("", "[in Bytes]", "The maximum packet size uploaded Schematics are split into.")
				.translation(basePath + name).defineInRange(name, 1024, 256, 32767);

		name = "schematicIdleTimeout";
		schematicIdleTimeout = builder.comment("",
				"Amount of game ticks without new packets arriving until an active schematic upload process is discarded.")
				.translation(basePath + name).defineInRange(name, 600, 100, Integer.MAX_VALUE);

		{
			builder.comment("Schematicannon").push("schematicannon");
			basePath = "create.config.schematics.schematicannon";

			name = "schematicannonDelay";
			schematicannonDelay = builder.comment("",
					"Amount of game ticks between shots of the cannon. Higher => Slower")
					.translation(basePath + name).defineInRange(name, 10, 1, Integer.MAX_VALUE);

			name = "schematicannonSkips";
			schematicannonSkips = builder.comment("",
					"Amount of block positions per tick scanned by a running cannon. Higher => Faster")
					.translation(basePath + name).defineInRange(name, 10, 1, Integer.MAX_VALUE);

			name = "schematicannonGunpowderWorth";
			schematicannonGunpowderWorth = builder.comment("",
					"% of Schematicannon's Fuel filled by 1 Gunpowder.")
					.translation(basePath + name).defineInRange(name, 20D, 0D, 100D);

			name = "schematicannonFuelUsage";
			schematicannonFuelUsage = builder.comment("",
					"% of Schematicannon's Fuel used for each fired block.")
					.translation(basePath + name).defineInRange(name, 0.05D, 0D, 100D);
			builder.pop();
		}

		builder.pop();
	}

	private boolean isValidPath(Object path) {
		if (!(path instanceof String))
			return false;
		try {
			Paths.get((String) path);
			return true;
		} catch (InvalidPathException e) {
			return false;
		}
	}

}
