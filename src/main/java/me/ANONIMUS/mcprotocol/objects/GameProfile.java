package me.ANONIMUS.mcprotocol.objects;

import lombok.Data;

import java.util.UUID;

@Data
public class GameProfile {
    private UUID uuid;
    private final String name;

    public GameProfile(final String name, final UUID id) {
        this.uuid = id;
        this.name = name;
    }

    public GameProfile(final String id, final String name) {
        this(UUID.fromString(id), name);
    }

    public GameProfile(final UUID uuid, final String name) {
        this(name, uuid);
    }

    public String getIdAsString() {
        return (this.uuid != null) ? this.uuid.toString() : "";
    }
}