package me.nobonex.easywarps.commands;

import me.nobonex.easywarps.EasyWarps;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;

/**
 * Created by Nobonex on 12-7-2017.
 */
public class DeleteWarp implements CommandExecutor {
    private EasyWarps plugin = EasyWarps.getPlugin();
    private CommentedConfigurationNode config = this.plugin.getConfig();
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(args.getOne("name").isPresent()){
            String name = args.<String>getOne("name").get();

            if(!this.config.getNode(name.toUpperCase()).isVirtual()){
                this.config.removeChild(name.toUpperCase());
                try{
                    this.plugin.loader.save(this.config);
                }catch (IOException e){
                    this.plugin.getLogger().warn("Error deleting warp" + name);
                    e.printStackTrace();
                }
                src.sendMessage(Text.of("[Easy Warps]: ",TextColors.GREEN,"Warp ",TextColors.AQUA,name,TextColors.GREEN," has been deleted"));
                return CommandResult.success();
            }
            src.sendMessage(Text.of("[Easy Warps]: ",TextColors.YELLOW,"No warp with the name ",TextColors.AQUA,name, TextColors.YELLOW," exists"));
            return CommandResult.success();
        }
        return CommandResult.success();
    }
}
