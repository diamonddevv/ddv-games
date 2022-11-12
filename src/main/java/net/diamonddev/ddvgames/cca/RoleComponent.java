package net.diamonddev.ddvgames.cca;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.diamonddev.ddvgames.minigame.Role;
import net.minecraft.nbt.NbtCompound;

public class RoleComponent implements Component {

    private Role role;
    private final String roleComponent = "Role";

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.role = Role.fromName(tag.getString(roleComponent));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString(roleComponent, role != null ? this.role.getName() : "");
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
