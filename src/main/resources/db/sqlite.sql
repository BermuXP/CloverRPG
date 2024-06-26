CREATE TABLE IF NOT EXISTS party (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS party_members (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    party_id INTEGER,
    player_uuid TEXT NOT NULL,
    is_leader INTEGER DEFAULT 0,
    FOREIGN KEY (party_id) REFERENCES party(id)
);

CREATE TABLE IF NOT EXISTS character (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_uuid TEXT NOT NULL,
    name TEXT NOT NULL,
    level INTEGER DEFAULT 0,
    exp INTEGER DEFAULT 0,
    class_id INTEGER DEFAULT 0,
    race_id INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS guilds (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    level INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS guild_members (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    guild_id INTEGER,
    player_uuid TEXT NOT NULL,
    rank TEXT DEFAULT "",
    level INTEGER DEFAULT 0,
    FOREIGN KEY (guild_id) REFERENCES guild(id)
);

CREATE TABLE IF NOT EXISTS guild_ranks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    guild_id INTEGER,
    rank_name TEXT NOT NULL,
    FOREIGN KEY (guild_id) REFERENCES guild(id)
);

CREATE TABLE IF NOT EXISTS guild_rank_permissions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    guild_id INTEGER,
    rank_id INTEGER,
    permission TEXT NOT NULL,
    FOREIGN KEY (guild_id) REFERENCES guild(id),
    FOREIGN KEY (rank_id) REFERENCES guild_ranks(id)
);

CREATE TABLE IF NOT EXISTS guild_coordinates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    guild_id INTEGER,
    x INTEGER DEFAULT 0,
    y INTEGER DEFAULT 0,
    z INTEGER DEFAULT 0,
    FOREIGN KEY (guild_id) REFERENCES guild(id)
);