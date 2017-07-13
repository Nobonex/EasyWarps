package me.nobonex.easywarps.listeners;

import me.nobonex.easywarps.EasyWarps;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;


public class Signs  {
    private EasyWarps plugin=EasyWarps.getPlugin();
    private CommentedConfigurationNode config = this.plugin.getConfig();
    @Listener
    public void onSignPlaceEvent(ChangeSignEvent event){
        Player player;
        SignData signData = event.getText();
        if(signData.getValue(Keys.SIGN_LINES).isPresent()){
            if(signData.getValue(Keys.SIGN_LINES).get().get(0).toPlain().equalsIgnoreCase("[warp]")){
                player = (Player)event.getCause().root();
                String warpName = signData.getValue(Keys.SIGN_LINES).get().get(1).toPlain();
                //If the sign starts with "[warp]" and has a valid warp name, set it up correctly.
                if(!this.config.getNode(warpName.toUpperCase()).isVirtual()){
                    signData.set(
                            signData.getValue(Keys.SIGN_LINES).get().set(0,Text.of(TextColors.YELLOW,"[Warp]"))
                    );
                    player.sendMessage(Text.of("[Easy Warps]: ",TextColors.GREEN,"Created a sign warp to ",TextColors.AQUA,warpName));
                    return;
                }
                player.sendMessage(Text.of("[Easy Warps]: ",TextColors.RED,"No warp with the name ",TextColors.AQUA,warpName,TextColors.RED," exists!"));
            }
        }
    }

    @Listener
    public void onSignInteractEvent(InteractBlockEvent.Secondary event, @Root Player player){
        Optional<SignData> signData;
        String warpName;
        if(event.getTargetBlock().getLocation().isPresent()){
            Location<World> signLocation = event.getTargetBlock().getLocation().get();
            if(signLocation.getTileEntity().isPresent()){
                signData = signLocation.getTileEntity().flatMap(t -> t.getOrCreate(SignData.class));
                if(signData.isPresent()){
                    if(signData.get().getValue(Keys.SIGN_LINES).get().get(0).toPlain().equalsIgnoreCase("[Warp]")){
                        warpName = signData.get().getValue(Keys.SIGN_LINES).get().get(1).toPlain();
                        if(!this.config.getNode(warpName.toUpperCase()).isVirtual()){
                            Location<World> warpLocation = new Location<World>(
                                    Sponge.getServer().getWorld(this.config.getNode(warpName.toUpperCase(),"world").getString()).get(),
                                    this.config.getNode(warpName.toUpperCase(), "x").getDouble(),
                                    this.config.getNode(warpName.toUpperCase(), "y").getDouble(),
                                    this.config.getNode(warpName.toUpperCase(), "z").getDouble()
                            );
                            player.setLocationSafely(warpLocation);
                            player.sendMessage(Text.of("[Easy Warps]: ",TextColors.GREEN,"Warped to ",TextColors.AQUA,warpName));
                            return;
                        }
                        player.sendMessage(Text.of("[Easy Warps]: ",TextColors.YELLOW,"The warp ",TextColors.AQUA,warpName,TextColors.YELLOW," doesn't exist"));
                    }
                }
            }
        }
    }

}
