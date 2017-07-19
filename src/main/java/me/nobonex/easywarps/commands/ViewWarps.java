package me.nobonex.easywarps.commands;

import me.nobonex.easywarps.EasyWarps;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nobonex on 14-7-2017.
 */
public class ViewWarps implements CommandExecutor {
    private static List<Text> warpList = new ArrayList<>();
    private static ViewWarps warps;
    private PaginationList.Builder builder = PaginationList.builder();
    private EasyWarps plugin = EasyWarps.getPlugin();
    private CommentedConfigurationNode config = this.plugin.getConfig();
    public ViewWarps(){
        warps=this;
        setupWarpList();
        builder
                .title(Text.of(TextColors.GREEN,"Warps"))
                .padding(Text.of(TextColors.DARK_BLUE,"*"))
                .contents(warpList)
                .linesPerPage(18)
                .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        builder.sendTo(src);
        return CommandResult.success();
    }

    private void setupWarpList(){
        Map warps = this.config.getChildrenMap();
        for (Object key : warps.keySet()) {
            warpList.add(Text.of(Text.builder().append(Text.of(TextColors.AQUA,key)).onClick(TextActions.runCommand("/warp "+key)).build()));
        }
    }

    public static List<Text> getWarpList(){
        return warpList;
    }

    public static ViewWarps getInstance(){
        return warps;
    }

}
