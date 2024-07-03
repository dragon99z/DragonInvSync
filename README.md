# DragonInvSync
![environment-server](https://img.shields.io/badge/environment-server-blue) ![modloader-fabric](https://img.shields.io/badge/modloader-fabric-blue)

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
