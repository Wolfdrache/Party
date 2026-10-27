# Party Plugin

A Bukkit/Paper plugin for party management and friend requests. It exposes both player-facing commands and Java service endpoints for other plugins to use.

## What this plugin does

- Create and manage a party with a leader and members
- Send, accept, decline, and cancel party invitations
- Kick players from the party or leave/disband the party
- Send friend requests and manage friendships
- Expose party and friend state through Bukkit's Services API

## Build and install

This project is a Maven multi-module build:

```bash
mvn -B verify
```

The plugin jar is produced in the `party-plugin` module.

## Player commands

### Party commands

```text
/party invite <player>
/party kick <player>
/party leave
/party disband
/party accept <player>
/party decline <player>
/party list
```

Command behavior:

- The party leader can invite players.
- Only the leader can kick members.
- If the leader leaves, the first member becomes the new leader if one exists.
- `accept` joins the player into the inviting party.
- `decline` removes the pending invite.
- `list` prints the current party members.

### Party chat

```text
/partychat <message>
```

Alias:

```text
/pc <message>
```

### Friend commands

```text
/friends add <player>
/friends remove <player>
/friends accept <player>
/friends deny <player>
/friends cancel <player>
/friends list [page]
```

These commands manage requests and stored friendships through the `FriendsManager` and the `FriendsService` endpoint.

## How the party events are fired

The plugin fires a Bukkit event called `PartyUpdateEvent` whenever party state changes.

### Event class

`PartyUpdateEvent` lives in the API module and has:

- `getPlayer()` — the player affected by the update
- `getTarget()` — the related player for the action, such as the invited player or the kicked player
- `getUpdateType()` — the type of update that occurred

### Event types

```java
public enum PartyUpdateType {
    PARTY_LEFT,
    PARTY_DISBANDED,
    PARTY_INVITE_RECEIVED,
    PARTY_INVITE_SENT,
    INVITE_ACCEPTED,
    INVITE_DECLINED
}
```

### Event firing pattern

All event dispatches go through the private helper in `PartyManager`:

```java
private void firePartyUpdate(Player player, Player target, PartyUpdateEvent.PartyUpdateType updateType) {
    Bukkit.getPluginManager().callEvent(new PartyUpdateEvent(player, target, updateType));
}
```

That method is invoked from the state-changing methods in `PartyManager`:

- `invitePlayer(...)`
  - fires `PARTY_INVITE_SENT` for party members
  - fires `PARTY_INVITE_RECEIVED` for the invited player
- `kickPlayer(...)` / `leaveParty(...)`
  - fires `PARTY_LEFT` for the leaving or kicked player and remaining members
- `disbandParty(...)`
  - fires `PARTY_DISBANDED` for all members and invited players
- `acceptInvite(...)`
  - fires `INVITE_ACCEPTED` for members already in the party
- `declineInvite(...)`
  - fires `INVITE_DECLINED` for party members

### Example listener

```java
import com.wolfdrache.party.api.PartyUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MyPartyListener implements Listener {

    @EventHandler
    public void onPartyUpdate(PartyUpdateEvent event) {
        switch (event.getUpdateType()) {
            case PARTY_INVITE_RECEIVED -> {
                System.out.println(event.getPlayer().getName() + " received an invite from " + event.getTarget().getName());
            }
            case INVITE_ACCEPTED -> {
                System.out.println(event.getPlayer().getName() + " joined a party");
            }
            case PARTY_LEFT -> {
                System.out.println(event.getPlayer().getName() + " left a party");
            }
            default -> {
                // handle other event types
            }
        }
    }
}
```

The plugin registers this listener in `Party.onEnable()` using `getServer().getPluginManager().registerEvents(...)`.

## How the service endpoints work

The plugin exposes state through Bukkit service registration. In `Party.onEnable()` it registers:

```java
Bukkit.getServicesManager().register(PartyService.class, this.partyService, this, ServicePriority.Normal);
Bukkit.getServicesManager().register(FriendsService.class, this.friendsService, this, ServicePriority.Normal);
```

### PartyService

The interface in `party-api` contains the public API:

```java
public interface PartyService {
    boolean isInParty(Player player);
    boolean isPartyLeader(Player player);
    List<Player> getPartyMembers(Player player);
    Player getPartyLeader(Player player);
    void invitePlayer(Player player, Player invitedPlayer);
    void kickPlayer(Player player, Player playerToKick);
    void leaveParty(Player player);
    void disbandParty(Player player);
    void acceptInvite(Player player, Player invitingPlayer);
}
```

The implementation is `PartyServiceImpl`, which is just a thin wrapper around `PartyManager`.

### FriendsService

The friend API is similar, and it exposes data and actions for friend requests and friendships:

```java
public interface FriendsService {
    List<OfflinePlayer> getFriends(OfflinePlayer player);
    List<OfflinePlayer> getFriendRequests(OfflinePlayer player);
    List<OfflinePlayer> getSentFriendRequests(OfflinePlayer player);
    void cancelFriendRequest(Player player, OfflinePlayer target);
    void replyFriendRequest(Player player, OfflinePlayer requester, boolean accept);
    void sendFriendRequest(Player player, OfflinePlayer target);
    void removeFriend(Player player, OfflinePlayer friend);
}
```

### Accessing the endpoints from another plugin

```java
import com.wolfdrache.party.api.PartyService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

PartyService partyService = Bukkit.getServicesManager().load(PartyService.class);

if (partyService != null) {
    boolean inParty = partyService.isInParty(player);
    Player leader = partyService.getPartyLeader(player);
    List<Player> members = partyService.getPartyMembers(player);
}
```

This is the standard Bukkit pattern used by the plugin: a service is registered once at startup and then loaded by any plugin that needs it.

## Notes on behavior

- The party leader is the root of the party state.
- Invite state is tracked in `pendingInvites`, so accept/decline checks are not just a UI action but a real state transition.
- `PartyManager` keeps the actual state in memory, so it is runtime state only and does not persist party data across restarts.
- `FriendsManager` is backed by a file-based configuration system, meaning friend data is stored in the plugin config files.

## Files to look at

- `party-api/.../PartyService.java`
- `party-api/.../PartyUpdateEvent.java`
- `party-plugin/.../Party.java`
- `party-plugin/.../party/PartyManager.java`
- `party-plugin/.../party/PartyCommand.java`
- `party-plugin/.../service/PartyServiceImpl.java`

These are the main entry points for commands, events, and service registration.
