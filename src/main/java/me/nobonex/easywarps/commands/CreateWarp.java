package me.nobonex.easywarps.commands;

import me.nobonex.easywarps.EasyWarps;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;


public class CreateWarp implements CommandExecutor {
    private EasyWarps plugin = EasyWarps.getPlugin();
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(!(src instanceof Player)){
            src.sendMessage(Text.of(TextColors.RED,"This command can only be used in-game"));
            return CommandResult.success();
        }
        Player player = (Player)src;
        if(args.getOne("name").isPresent()){
            String name = args.<String>getOne("name").get();
            CommentedConfigurationNode config = plugin.getConfig();
            if(config.getNode(name.toUpperCase()).isVirtual()){
                config.getNode(name.toUpperCase(), "world").setValue(player.getWorld().getName());
                config.getNode(name.toUpperCase(), "x").setValue(player.getLocation().getX());
                config.getNode(name.toUpperCase(), "y").setValue(player.getLocation().getY());
                config.getNode(name.toUpperCase(), "z").setValue(player.getLocation().getZ());
                try{
                    plugin.loader.save(config);
                }catch (IOException e){
                    plugin.getLogger().warn("Error creating warp");
                    e.printStackTrace();
                }
                player.sendMessage(Text.of("[Easy Warps]: ",TextColors.GREEN,"Warp ",TextColors.AQUA, name,TextColors.GREEN," created"));
                return CommandResult.success();
            }
            player.sendMessage(Text.of("[Easy Warps]: ",TextColors.YELLOW,"There already is a warp with the name ",TextColors.AQUA,name));
            return CommandResult.success();
        }
        return CommandResult.success();
    }
}