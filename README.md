# CloverRPG

CloverRPG is a Minecraft plugin that adds RPG elements to your game. It is built with the Spigot API and provides various commands and features to enhance the RPG experience in Minecraft.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Permissions](#permissions)
- [Contributing](#contributing)
- [License](#license)

## Features

- RPG Classes: Use the `/classes` command to choose your RPG class.
- Party System: Use the `/party` command to manage your party.
- CRPG Command: Use the `/crpg` command for CRPG related operations.

## Installation

1. Build the project using Maven: `mvn clean install`
2. The built JAR file `CloverRPG-${version}.jar` can be found in the `target/` directory.
3. Create a server by downloading [Spigot](https://getbukkit.org/download/spigot).
4. Run the downloaded JAR file as follows: `java -jar [filename].jar --nogui` to create your test server.
5. Accept the EULA by changing the `eula` tag to true in the `eula.txt`.
6. Place the built JAR file in the `plugins/` directory of your Minecraft server.
7. Restart your Minecraft server using the command `java -jar [filename].jar --nogui`.

## Usage

After installing the plugin, you can use the following commands in your Minecraft server:

- `/classes`: Opens the classes menu.
- `/crpg`: Executes CRPG related operations.
- `/party`: Manages your party.

## Permissions

- `cloverrpg.*`: Access to all CloverRPG permissions.
- `cloverrpg.commands.*`: Access to all CloverRPG commands.
- `cloverrpg.commands.classes`: Access to the classes command.
- `cloverrpg.commands.crpg`: Access to the crpg command.
- `cloverrpg.commands.party.*`: Access to all the party commands.

## Contributing

Contributions are welcome. Please open an issue or submit a pull request.

## License

CloverRPG is licensed under the [GNU License](LICENSE).