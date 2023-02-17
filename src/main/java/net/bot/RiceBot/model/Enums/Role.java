package net.bot.RiceBot.model.Enums;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(Permission.ADD, Permission.GET, Permission.DELETE)),
    USER(Set.of(Permission.GET)),
    BANNED(Set.of()),
    UNREGISTERED(Set.of()),
    IN_PROCESS_OF_REG(Set.of());

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions(){
        return permissions;
    }
}
