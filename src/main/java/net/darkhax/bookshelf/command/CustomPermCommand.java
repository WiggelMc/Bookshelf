package net.darkhax.bookshelf.command;

import net.darkhax.bookshelf.BookshelfConfig;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CustomPermCommand implements ICommand {

    private final ICommand original;
    private final int permissionLevel;

    public CustomPermCommand(ICommand original, int permissionLevel) {
        this.original = original;
        this.permissionLevel = permissionLevel;
    }

    public int getRequiredPermissionLevel() {
        return permissionLevel;
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender sender) {
        return this.getRequiredPermissionLevel() <= 0 || sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    @Override
    public int compareTo(ICommand p_compareTo_1_) {
        return this.getName().compareTo(p_compareTo_1_.getName());
    }


    @Override
    public String getName() {
        return original.getName();
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return original.getUsage(iCommandSender);
    }

    @Override
    public List<String> getAliases() {
        return original.getAliases();
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        original.execute(minecraftServer, iCommandSender, strings);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings, @Nullable BlockPos blockPos) {
        return original.getTabCompletions(minecraftServer, iCommandSender, strings, blockPos);
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return original.isUsernameIndex(strings, i);
    }

    public static ICommand transformCommand(ICommand command) {
        if (
                Arrays.stream(BookshelfConfig.publicCommands)
                        .anyMatch(
                                a -> Objects.equals(a, command.getName())
                        )
        ) {
            return new CustomPermCommand(command, 0);
        } else {
            return command;
        }
    }
}
