package xyz.goldendupe.perks;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.InitAfterBootstrap;
import bet.astral.data.helper.PackedPreparedStatement;
import bet.astral.data.helper.PackedResultSet;
import bet.astral.messenger.v2.translation.TranslationKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.perks.action.Action;
import xyz.goldendupe.perks.action.ActionManager;

import java.io.File;
import java.sql.*;
import java.util.*;

public class PerkHolderManager implements InitAfterBootstrap {
    private Connection connection;
    private final ActionManager actionManager;
    private final Map<UUID, PerkHolder> perkHolders = new HashMap<>();

    public PerkHolderManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    private Connection connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(GoldenDupe.instance().getDataFolder(), "perks.db"));
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable() {
        try {
            PreparedStatement statement = connection.
                    prepareStatement("CREATE TABLE IF NOT EXISTS perks (" +
                            "perk_id VARCHAR(100) NOT NULL, " +
                            "owner_id VARCHAR(36) NOT NULL, " +
                            "tier INTEGER DEFAULT 0," +
                            "slot INTEGER," +
                            "PRIMARY KEY (owner_id, perk_id))");
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            GoldenDupe.instance().getSLF4JLogger().error("Error while trying to create player perks table!", e);
        }
    }

    public PerkHolder getPerkHolder(UUID uuid) {
        return perkHolders.get(uuid);
    }

    public PerkHolder getPerkHolder(@NotNull Player player) {
        return perkHolders.get(player.getUniqueId());
    }

    public void save(@NotNull Player player) {
        for (Action action : actionManager.getActions()) {
            try {
                PackedPreparedStatement select = new PackedPreparedStatement(
                        connection.prepareStatement("SELECT * FROM perks WHERE perk_id = ? AND owner_id = ?")
                );
                select.setString(1, action.getName().getKey());
                select.setString(2, player.getUniqueId().toString());
                ResultSet resultSet = select.executeQuery();
                if (resultSet.next()) {
                    resultSet.close();
                    select.close();

                    PackedPreparedStatement statement = new PackedPreparedStatement(
                            connection.prepareStatement("UPDATE perks SET tier = ?, slot = ? WHERE perk_id = ? AND owner_id = ?\n")
                    );

                    int tier = actionManager.getPlayerPerkTier(player, action);
                    int slot = actionManager.getPerkSlot(player, action);


                    statement.setInt(1, tier);
                    statement.setInt(2, slot);
                    statement.setString(3, action.getName().getKey());
                    statement.setUUID(4, player.getUniqueId());

                    statement.executeUpdate();
                    statement.close();
                } else {
                    resultSet.close();
                    select.close();

                    PackedPreparedStatement statement = new PackedPreparedStatement(
                            connection.prepareStatement("INSERT INTO perks (tier, slot, perk_id, owner_id) VALUES (?, ?, ?, ?)")
                    );

                    int tier = actionManager.getPlayerPerkTier(player, action);
                    int slot = actionManager.getPerkSlot(player, action);


                    statement.setInt(1, tier);
                    statement.setInt(2, slot);
                    statement.setString(3, action.getName().getKey());
                    statement.setUUID(4, player.getUniqueId());

                    statement.executeUpdate();
                    statement.close();
                }
            } catch (SQLException e) {
                GoldenDupe.instance().getSLF4JLogger().error("Error while trying to save " + player.getUniqueId(), e);
                if (Bukkit.getPlayer(player.getUniqueId()) != null) {
                    Bukkit.getPlayer(player.getUniqueId()).getScheduler().run(GoldenDupe.instance(), (t)->{
                        player.kick(Component.text("INTERNAL ERROR RETRY TO JOIN AND CREATE A TICKET! " + GoldenDupe.DISCORD));
                    }, null);
                }
                throw new RuntimeException(e);
            }
        }
    }

    public void load(@NotNull Player player) {
        PerkHolder perkHolder = new PerkHolder(player.getUniqueId());
        perkHolders.put(player.getUniqueId(), perkHolder);

        try {
            PackedPreparedStatement statement =
                    new PackedPreparedStatement(connection.prepareStatement("SELECT * FROM perks WHERE owner_id = ?"));
            statement.setUUID(1, player.getUniqueId());


            PackedResultSet resultSet = new PackedResultSet(
                    statement.executeQuery());

            while (!resultSet.isClosed() && resultSet.next()) {
                Action action = actionManager.getAction(TranslationKey.of(resultSet.getString("perk_id")));
                int tier = resultSet.getInt("tier");
                int slot = resultSet.getInt("slot");
                perkHolder.setPerkTier(action, tier);
                perkHolder.setPerkSlot(action, slot);
            }

            if (!resultSet.isClosed()) {
                resultSet.close();
            }
            statement.close();
        } catch (Exception e) {
            GoldenDupe.instance().getSLF4JLogger().error("Error while trying to load perk of " + player.getUniqueId(), e);
            player.kick(Component.text("INTERNAL ERROR RETRY TO JOIN AND CREATE A TICKET IF UNABLE TO! " + GoldenDupe.DISCORD));
        }
    }

    public void unload(Player player) {
        perkHolders.remove(player.getUniqueId());
    }

    @Override
    public void init() {
        connect();
        createTable();
    }
}
