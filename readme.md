### List of available commands

### Modifier
```ozu modify [modifier] [beatmap-folder-location]```  
- ```ozu modify bpm [new-bpm] [beatmap-folder-location]``` changes bpm of a beatmap to the specified ```[new-bpm]``` one  

### Fixer
```ozu fix [fixer] [beatmap-folder-location]```  
- ```ozu fix bpm [beatmap-folder-location]``` finds the best matching bpm and offset of a song (it turns out that finding bpm and offset isn't solved yet, this tool uses an heuristic that may not work really well depending on the song)  
- ```ozu fix green-line-snap [beatmap-folder-location]``` snaps green lines to specified time divisions  
- ```ozu fix note-snap [beatmap-folder-location]``` snaps circles, sliders and spinners to specified time divisions  

### Analyser
```ozu analyse [analyser] [beatmap-folder-location]```  
_nothing yet_

### Hitsound
```ozu hitsound [hitsound-tool] [beatmap-folder-location]```  
_nothing yet_

### Beatmap generator
```ozu beatmap-generator [difficulty] [optional: seed] [beatmap-folder-location]```  
_nothing yet_

### Help
```ozu help [optional: command-name]```
- ```ozu help``` displays a bunch of information on this app
