package zonecraftmod.commands;

import com.mojang.brigadier.context.CommandContext;
import de.maxhenkel.admiral.annotations.Command;
import de.maxhenkel.admiral.annotations.RequiresPermission;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import zonecraftmod.Emission;

@Command("emission")
@RequiresPermission("zonecraft.admin.emission")
public class EmissionCommand {
    @Command()
    public void noArguments(CommandContext<CommandSourceStack> cxt) {
        ServerPlayer player = cxt.getSource().getPlayer();
        player.sendSystemMessage(Component.literal(ChatFormatting.RED + "Invalid Command."));
    }

    @Command("start")
    public void startEmission(CommandContext<CommandSourceStack> cxt) {
        ServerPlayer player = cxt.getSource().getPlayer();
        if (!Emission.isEmissionActive()) {
            player.sendSystemMessage(Component.literal(ChatFormatting.GREEN + "Emission Started."));
            Emission.startEmission();
        } else {
            player.sendSystemMessage(Component.literal(ChatFormatting.RED + "Emission already started."));
        }
    }
}
