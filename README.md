# DragonInvSync
![environment-server](https://img.shields.io/badge/environment-server-blue) ![modloader-fabric](https://img.shields.io/badge/modloader-fabric-blue) ![Modrinth Downloads](https://img.shields.io/modrinth/dt/VCaphsLy?logo=modrinth&label=Modrinth%20Downloads&color=green&link=https%3A%2F%2Fmodrinth.com%2Fmod%2Fdragoninvsync) ![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/dragon99z/DragonInvSync/total?logo=github&label=GitHub%20Downloads&color=blue&link=https%3A%2F%2Fgithub.com%2Fdragon99z%2FDragonInvSync%2Freleases%2Flatest)




## Why you want to use this mod
It can synchronize the playerdata across a server network
so every player has the same inventory and so on across all your server.

## Installation
You need to put this mod on all your backend server
and setup all config files to use the same database/server
```config/DragonInvSync/config.toml```

## Usage
The mod will auto sync the player every time the player join's/leave's and all online players on an set interval wich can be configured in the config file

## Features
Syncing players
- inventory/enderchest
- gamemode
- level/exp
- hunger/saturation
- health
- advancements

## Features Planned
- sqlite support 
- postgresql support

## Compatibility
It's currently only compatible server-side with a mysql server
