# JetLytra

Adds powered Elytra options to 1.7.10! Requires Et Futurum Requiem ([GH](https://github.com/Roadhog360/Et-Futurum-Requiem/releases) [MR](https://modrinth.com/mod/etfuturum/versions)) and Thermal Expansion ([CF](https://www.curseforge.com/minecraft/mc-mods/thermal-expansion)).

When flying, hold down the forward key (i.e `W`) and enjoy the miracle of flight! No fireworks needed!

Flux Elytras will consume RF during flight and more while you hold the forward key. Additionally, the Flux elytra will absorb collision damage in exchange for RF.

Simply recharge and take off!

As you can see in the below tables, boosting takes a lot of energy and on the lower tiers, even takes a little while to reach a high enough speed to maintain flight. It is recommended to use the boost to gain altitude and then coast for a while to save energy. The higher tiers can boost to maximum speed quicker, so they are more flexible in their flight patterns.

Warning! The Flux Elytra cannot sustain flight without energy! If you run out, be ready to bucket clutch.

## Heads-Up Display

![5RF/t - 8m](/doc/crosshair.png)

When flying with a Flux Elytra, a heads-up display will appear right below your crosshair. It will show the current energy consumption rate and the estimated remaining flight time based on the current energy level.

This allows you to manage your energy usage and plan your flights accordingly.

## Tiers

### Leadstone Flux Elytra

![](/doc/leadstone_flux_elytra.png)

|                        |                   |
| ---------------------- | ----------------- |
| Flight consumption     | 5RF/t             |
| Max boost acceleration | 10% of a firework |
| 0-100% boost           | 2.5 seconds       |
| Flight time (coasting) | 13 minutes        |
| Flight time (boosting) | 36 seconds        |

### Hardened Flux Elytra

![](/doc/hardened_flux_elytra.png)

|                        |                   |
| ---------------------- | ----------------- |
| Flight consumption     | 5RF/t             |
| Max boost acceleration | 30% of a firework |
| 0-100% boost           | 1.5 seconds       |
| Flight time (coasting) | 1 hour            |
| Flight time (boosting) | 1 minute          |

### Redstone Flux Elytra

![](/doc/redstone_flux_elytra.png)

|                        |                   |
| ---------------------- | ----------------- |
| Flight consumption     | 5RF/t             |
| Max boost acceleration | 70% of a firework |
| 0-100% boost           | 0.75 seconds      |
| Flight time (coasting) | 11 hours          |
| Flight time (boosting) | 4 minutes         |

### Resonant Flux Elytra

![](/doc/resonant_flux_elytra.png)

|                        |                    |
| ---------------------- | ------------------ |
| Flight consumption     | 5RF/t              |
| Max boost acceleration | Same as a firework |
| 0-100% boost           | _Instant_          |
| Flight time (coasting) | 55 hours           |
| Flight time (boosting) | 16 minutes         |

## Building and Development

1. Make sure `JAVA_HOME` currently points to a JDK 8 installation.
2. Run `gradlew setupDecompWorkspace`.
3. Run `gradlew build` to build the mod.
4. Drag the generated jar (located in `build/libs/`) into a modded Minecraft installation with the aforementioned dependencies.
