package me.nobonex.easywarps.commands;


import me.nobonex.easywarps.EasyWarps;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;


public class Warp implements CommandExecutor {
    private EasyWarps plugin = EasyWarps.getPlugin();
    private CommentedConfigurationNode config = this.plugin.getConfig();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(TextColors.RED, "This command may only be used in-game"));
            return CommandResult.success();
        }
        Player player = (Player) src;
        if (args.getOne("name").isPresent()) {
            String name = args.<String>getOne("name").get();
            if (!this.config.getNode(name.toUpperCase()).isVirtual()) {
                Location<World> warpPoint = new Location<World>(
                        Sponge.getServer().getWorld(
                                this.config.getNode(name.toUpperCase(), "world").getString()
                        ).get(),
                        this.config.getNode(name.toUpperCase(), "x").getDouble(),
                        this.config.getNode(name.toUpperCase(), "y").getDouble(),
                        this.config.getNode(name.toUpperCase(), "z").getDouble()
                );
                player.setLocationSafely(warpPoint);
                player.sendMessage(Text.of("[Easy Warps]: ", TextColors.GREEN, "Warped to ", TextColors.AQUA, name));
                return CommandResult.success();
            }
            player.sendMessage(Text.of("[Easy Warps]: ", TextColors.YELLOW, "The warp with the name ",TextColors.AQUA,name,TextColors.YELLOW," does not exist"));
            return CommandResult.success();
        }
        return CommandResult.success();
    }
}
