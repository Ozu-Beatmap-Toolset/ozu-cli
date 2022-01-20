# Ozu Cli

A cli app to help with beatmap creation.

# List of available commands

### help
```ozu help [optional: command-name]```
- ```ozu help``` displays a bunch of information on this app

### fix
```ozu fix [fixer] [beatmap-folder-location]```
- ```ozu fix bpm [beatmap-folder-location]``` fixes the bpm and offset of a beatmap
- ```ozu fix green-line-snap [beatmap-folder-location]``` snaps green lines to specified time divisions
- ```ozu fix note-snap [beatmap-folder-location]``` snaps circles, sliders and spinners to specified time divisions

### modify
```ozu modify [modifier] [beatmap-folder-location]```
- ```ozu modify bpm [new-bpm] [beatmap-folder-location]``` changes bpm of a beatmap to the specified ```[new-bpm]``` one

### analyse
```ozu analyse [analyser] [beatmap-folder-location]```
_nothing yet_

### hitsound
```ozu hitsound [hitsound-tool] [beatmap-folder-location]```
_nothing yet_

### beatmap-generator
```ozu beatmap-generator [difficulty] [optional: seed] [beatmap-folder-location]```
_nothing yet_
