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