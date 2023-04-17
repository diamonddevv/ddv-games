package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.libgenetics.core.command.abstraction.StringArrayListArgType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class RoleArgType extends StringArrayListArgType {

    private static final DynamicCommandExceptionType INVALID_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.argument.role", id));

    private RoleArgType() {}
    public static RoleArgType role() {return new RoleArgType();}

    public static Role getRole(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Collection<Role> roles = DDVGamesMod.gameManager.getRoles();
        String name = context.getArgument(argumentName, String.class);
        Role role = null;
        for (Role r : roles) {
            if (r.getName().matches(name)) {
                role = r;
            }
        }

        if (role == null) {
            throw INVALID_EXCEPTION.create(name);
        } else {
            return role;
        }
    }

    @Override
    public ArrayList<String> getArray() {
        ArrayList<String> roles = new ArrayList<>();
        if (DDVGamesMod.gameManager.hasGame()) {
            roles = DDVGamesMod.gameManager.getRolesAsSimpleNames();
        }
        return roles;
    }
}
