package com.XXXYJade17.AttributeCore.Command;

import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import com.XXXYJade17.AttributeCore.Capability.Handler.CapabilityHandler;
import com.XXXYJade17.AttributeCore.Config.Config;
import com.XXXYJade17.AttributeCore.Data.Client.CelestialEssenceData;
import com.XXXYJade17.AttributeCore.Data.Server.CelestialEssenceSavedData;
import com.XXXYJade17.AttributeCore.AttributeCore;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import javax.swing.*;
import java.util.Optional;
import java.util.UUID;

public class AdminCommand {
    private static AdminCommand INSTANCE;
    private static final Logger LOGGER= AttributeCore.getLOGGER();
    private static final Config config= Config.getINSTANCE();

    public static AdminCommand getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE=new AdminCommand();
        }
        return INSTANCE;
    }

    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        this.AdminCelestialEssenceCommand(dispatcher);
    }

    private void AdminCelestialEssenceCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> Administrator =
                Commands.literal("CelestialEssence");

        Administrator.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("get")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .executes(this::getPlayerCelestialEssence))));

        Administrator.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("CultivationRealm")
                                        .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setCultivationRealm))))));

        Administrator.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("StageRank")
                                        .then(Commands.argument("new", StringArgumentType.word())
                                                .executes(this::setStageRank))))));

        Administrator.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("EtherealEssence")
                                        .then(Commands.argument("new", StringArgumentType.word())
                                                .executes(this::setEtherealEssence))))));

        Administrator.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("EtherealEssence")
                                        .then(Commands.argument("num" , StringArgumentType.word())
                                                        .executes(this::addEtherealEssence))))));

        dispatcher.register(Administrator);

        LiteralArgumentBuilder<CommandSourceStack> Admin =
                Commands.literal("CE");

        Admin.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("get")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .executes(this::getPlayerCelestialEssence))));

        Admin.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("CultivationRealm")
                                        .then(Commands.argument("new", StringArgumentType.word())
                                                .executes(this::setCultivationRealm))))));

        Admin.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("StageRank")
                                        .then(Commands.argument("new", StringArgumentType.word())
                                                .executes(this::setStageRank))))));

        Admin.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("EtherealEssence")
                                                .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setEtherealEssence))))));

        Admin.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("EtherealEssence")
                                        .then(Commands.argument("num" , StringArgumentType.word())
                                                        .executes(this::addEtherealEssence))))));

        dispatcher.register(Admin);
    }

    private int getPlayerCelestialEssence(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = CelestialEssenceSavedData.getUUID(playerName);
        if(playerUUID!=null){
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(playerUUID);
            Optional<CelestialEssence> optionalCE =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalCE.ifPresent(CE -> {
                String cultivationRealm= config.getCultivationRealm(CE.getCultivationRealm());
                String stageRank= config.getStageRank(CE.getStageRank());
                int etherealEssence = CE.getEtherealEssence();
                context.getSource().sendSuccess(() ->
                        config.getMessage("ce.admin.get",playerName,cultivationRealm,stageRank,etherealEssence), false);
            });
            return 1;
        }
        context.getSource().sendFailure(config.getMessage("ce.admin.empty",playerName));
        return 0;
    }

    private int setCultivationRealm(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = CelestialEssenceSavedData.getUUID(playerName);
        int newLevel=Integer.valueOf(StringArgumentType.getString(context, "new"));
        if(playerUUID!=null){
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(playerUUID);
            Optional<CelestialEssence> optionalCE =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalCE.ifPresent(CE -> {
                CE.setCultivationRealm(newLevel);
            });
            context.getSource().sendSuccess(() ->
                    config.getMessage("ce.admin.change",playerName,"CultivationRealm"), false);
            return 1;
        }
        return 0;
    }

    private int setStageRank(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = CelestialEssenceSavedData.getUUID(playerName);
        int newLevel=Integer.valueOf(StringArgumentType.getString(context, "new"));
        if(playerUUID!=null){
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(playerUUID);
            Optional<CelestialEssence> optionalCE =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalCE.ifPresent(CE -> {
                CE.setStageRank(newLevel);
            });
            context.getSource().sendSuccess(() ->
                    config.getMessage("ce.admin.change",playerName,"StageRank"), false);
            return 1;
        }
        return 0;
    }

    private int setEtherealEssence(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = CelestialEssenceSavedData.getUUID(playerName);
        int newLevel=Integer.valueOf(StringArgumentType.getString(context, "new"));
        if(playerUUID!=null){
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(playerUUID);
            Optional<CelestialEssence> optionalCE =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalCE.ifPresent(CE -> {
                CE.setEtherealEssence(newLevel);
            });
            context.getSource().sendSuccess(() ->
                    config.getMessage("ce.admin.change",playerName,"EtherealEssence"), false);
            return 1;
        }
        return 0;
    }

    private int addEtherealEssence(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = CelestialEssenceSavedData.getUUID(playerName);
        int newLevel=Integer.valueOf(StringArgumentType.getString(context, "num"));
        if(playerUUID!=null){
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(playerUUID);
            Optional<CelestialEssence> optionalCE =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.CELESTIAL_ESSENCE_HANDLER));
            optionalCE.ifPresent(CE -> {
                CE.addEtherealEssence(newLevel);
            });
            context.getSource().sendSuccess(() ->
                    config.getMessage("ce.admin.add",playerName), false);
            return 1;
        }
        return 0;
    }
}
