package me.nobonex.easywarps;
import com.google.inject.Inject;
import me.nobonex.easywarps.commands.CreateWarp;
import me.nobonex.easywarps.commands.DeleteWarp;
import me.nobonex.easywarps.commands.Warp;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;

@Plugin(
        id = "easywarps",
        name = "Easy Warps",
        authors = "Nobonex",
        version = "1.0"
)
public class EasyWarps {
    private static EasyWarps plugin;

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File configurationFile=null;

    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> loader;

    private CommentedConfigurationNode node = null;


    @Listener
    public void onPreInit(GamePreInitializationEvent event){
        plugin=this;
        try {
            if(!this.configurationFile.exists()){
                this.configurationFile.createNewFile();
            }
            this.node = loader.load();
            this.loader.save(node);
        }catch (IOException e){
            this.logger.warn("Creating configuration file failed");
        }
    }

    @Listener
    public void onInit(GameInitializationEvent event){
        initialiseCommandSpecs();
    }

    @Listener
    public void onServerStop(GameStoppingEvent event){
        try{
            this.loader.save(node);
        }catch (IOException e){
            this.logger.warn("Error with final save");
        }
    }

    public static EasyWarps getPlugin(){
        return plugin;
    }

    public CommentedConfigurationNode getConfig(){
        return this.node;
    }

    public Logger getLogger(){
        return this.logger;
    }

    private void initialiseCommandSpecs(){
        CommandSpec deleteWarp = CommandSpec.builder()
                .executor(new DeleteWarp())
                .description(Text.of("Delete a warp with the given name"))
                .permission("ew.warpdel")
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .build();

        CommandSpec createWarp = CommandSpec.builder()
                .executor(new CreateWarp())
                .description(Text.of("Create a warp at your location with the given name"))
                .permission("ew.warpcreate")
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .build();
        /*-------------------------------------------------------------------*/
        /*No commands below this point unless they have a separate function! */
        /*-------------------------------------------------------------------*/
        CommandSpec warp = CommandSpec.builder()
                .executor(new Warp())
                .description(Text.of("Warps a player to the given warp point"))
                .permission("ew.warp")
                .arguments(
                        GenericArguments.string(Text.of("name"))
                )
                .child(deleteWarp,"delete")
                .child(createWarp,"create")
                .build();
        game.getCommandManager().register(this,warp,"warp");
    }
}
