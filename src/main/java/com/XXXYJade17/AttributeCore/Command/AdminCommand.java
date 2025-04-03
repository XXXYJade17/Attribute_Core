package com.XXXYJade17.AttributeCore.Command;

import com.XXXYJade17.AttributeCore.Capability.CelestialEssence.CelestialEssence;
import com.XXXYJade17.AttributeCore.Data.Server.CelestialEssenceSavedData;
import com.XXXYJade17.AttributeCore.PlayerUUID.PlayerUUID;
import com.XXXYJade17.AttributeCore.AttributeCore;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.gui.screens.social.PlayerSocialManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.util.UUID;

public class AdminCommand {
    private static AdminCommand INSTANCE;
    private static final Logger LOGGER= AttributeCore.getLOGGER();

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
                                        .then(Commands.argument("old", StringArgumentType.word())
                                                .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setCultivationRealm)))))));

        Administrator.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("StageRank")
                                        .then(Commands.argument("old", StringArgumentType.word())
                                                .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setStageRank)))))));

        Administrator.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("EtherealEssence")
                                        .then(Commands.argument("old", StringArgumentType.word())
                                                .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setEtherealEssence)))))));

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
                                        .then(Commands.argument("old", StringArgumentType.word())
                                                .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setCultivationRealm)))))));

        Admin.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("StageRank")
                                        .then(Commands.argument("old", StringArgumentType.word())
                                                .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setStageRank)))))));

        Admin.then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("EtherealEssence")
                                        .then(Commands.argument("old", StringArgumentType.word())
                                                .then(Commands.argument("new", StringArgumentType.word())
                                                        .executes(this::setEtherealEssence)))))));

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
        UUID playerUUID = PlayerUUID.getUUID(playerName);
        if(playerUUID!=null){
            CelestialEssence playerCE = CelestialEssenceSavedData.getPlayerData(playerUUID);
            context.getSource().sendSuccess(() -> Component.literal("管理test1"), false);
            return 1;
        }
        return 0;
    }

    private int setCultivationRealm(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = PlayerUUID.getUUID(playerName);
        String oldLevel=StringArgumentType.getString(context, "old");
        String newLevel=StringArgumentType.getString(context, "new");
        //修改逻辑暂时省略
        context.getSource().sendSuccess(() -> Component.literal("管理test2"), false);
        return 1;
    }

    private int setStageRank(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = PlayerUUID.getUUID(playerName);
        String oldLevel=StringArgumentType.getString(context, "old");
        String newLevel=StringArgumentType.getString(context, "new");
        //修改逻辑暂时省略
        context.getSource().sendSuccess(() -> Component.literal("管理test3"), false);
        return 1;
    }

    private int setEtherealEssence(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = PlayerUUID.getUUID(playerName);
        String oldLevel=StringArgumentType.getString(context, "old");
        String newLevel=StringArgumentType.getString(context, "new");
        //修改逻辑暂时省略
        context.getSource().sendSuccess(() -> Component.literal("管理test4"), false);
        return 1;
    }

    private int addEtherealEssence(CommandContext<CommandSourceStack> context){
        String playerName = StringArgumentType.getString(context, "player");
        UUID playerUUID = PlayerUUID.getUUID(playerName);
        int num = Integer.parseInt(StringArgumentType.getString(context, "num"));
        //修改逻辑暂时省略
        context.getSource().sendSuccess(() -> Component.literal("管理test5"), false);
        return 1;
    }
}
