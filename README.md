# Arcana
for Minecraft 1.16.

Arcana is a magic-themed Minecraft mod, and an unofficial successor to Thaumcraft.

## Playing with Arcana
Arcana is still in a very rough state. Although the mod is open-source, it is not ready for playing in survival mode, and has a number of bugs and unfinished content.

## Contributing to Arcana
Simply clone the git repo and open `build.gradle` with your IDE of your choice. Run `genIntellijRuns` or `genEclipseRuns` (or VS Code runs for the 1 person who uses that), and restart your IDE to get run configs. (Eclipse users may also need to edit their run configs; check the forge docs.)

In IntelliJ, you may also need to turn off IntelliJ's annotation processing (which Gradle handles), if you get "missing refmap" errors.
Go to Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> (in the list) Gradle Imported -> Arcana-1.15.main -> Turn off "Enable annotation processing".

## Found a bug?
Report it here. Before reporting, check it hasn't already been reported. State the commit you were running and steps to reproduce, if possible.
Help with fixing bugs would be appreciated.

## License?
All code in this project is under the LGPLv3 lisence, all artwork is under All rights reserved and sound is under cc0
