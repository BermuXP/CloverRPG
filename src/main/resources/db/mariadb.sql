CREATE TABLE IF NOT EXISTS party (
    id INT AUTO_INCREMENT PRIMARY KEY
    `name` VARCHAR(255) NOT NULL,
);

CREATE TABLE IF NOT EXISTS party_members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    party_id INT,
    player_uuid VARCHAR(255) NOT NULL,
    is_leader TINYINT DEFAULT 0,
    FOREIGN KEY (party_id) REFERENCES party(id)
);

CREATE TABLE IF NOT EXISTS `character` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_uuid VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    level INT DEFAULT 0,
    exp INT DEFAULT 0,
    class_id INT DEFAULT 0,

CREATE TABLE IF NOT EXISTS guilds (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL
    level INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS guild_members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guild_id INT,
    player_uuid VARCHAR(255) NOT NULL,
    rank VARCHAR(255) DEFAULT "",
    level INT DEFAULT 0,
    FOREIGN KEY (guild_id) REFERENCES guild(id)
);

CREATE TABLE IF NOT EXISTS guild_ranks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guild_id INT,
    rank_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (guild_id) REFERENCES guild(id)
);

CREATE TABLE IF NOT EXISTS guild_rank_permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guild_id INT,
    rank_id INT,
    permission VARCHAR(255) NOT NULL,
    FOREIGN KEY (guild_id) REFERENCES guild(id),
    FOREIGN KEY (rank_id) REFERENCES guild_ranks(id)
);