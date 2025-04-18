package com.XXXYJade17.AttributeCore.Command;

import com.XXXYJade17.AttributeCore.AttributeCore;
import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import com.XXXYJade17.AttributeCore.Capability.Handler.CapabilityHandler;
import com.XXXYJade17.AttributeCore.Config.Config;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.util.Optional;

public class PlayerCommand {
    private static PlayerCommand INSTANCE;
    private static final Logger LOGGER= AttributeCore.getLOGGER();
    private static final Config config= Config.getINSTANCE();

    public static PlayerCommand getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerCommand();
        }
        return INSTANCE;
    }

    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        this.CelestialEssenceCommand(dispatcher);
    }

    private void CelestialEssenceCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> CelestialEssence =
                Commands.literal("CelestialEssence")
                        .requires(source -> source.hasPermission(0))
                        .then(Commands.literal("get")
                                .executes(this::getCelestialEssence));

        LiteralArgumentBuilder<CommandSourceStack> CE =
                Commands.literal("CE")
                        .requires(source -> source.hasPermission(0))
                        .then(Commands.literal("get")
                                .executes(this::getCelestialEssence));

        dispatcher.register(CelestialEssence);
        dispatcher.register(CE);
    }

    private int getCelestialEssence(CommandContext<CommandSourceStack> context) {
        try {
            Player player = context.getSource().getPlayerOrException();
            Optional<CelestialEssence> optionalCE =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalCE.ifPresent(CE -> {
                String cultivationRealm=config.getCultivationRealm(CE.getCultivationRealm());
                String stageRank=config.getStageRank(CE.getStageRank());
                int etherealEssence = CE.getEtherealEssence();
                context.getSource().sendSuccess(() ->
                        config.getMessage("ce.get",cultivationRealm,stageRank,etherealEssence), false);
            });
            return 1;
        } catch (CommandSyntaxException e) {
            return 0;
        }
    }
}
