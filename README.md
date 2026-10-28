# MavenMCP 1.8.9

### About
Juk client is a client based on maven mcp 1.8.9
I made this client for playing on no anticheat servers and botting agerapvp :)

### Building
(same as mcp)

To build a working .jar file, which later can be put to `/versions` in MC folder, you just need to run `mvn clean package` command.
<br>You can also use the Maven menu *on the right side*, or add a new run configuration, and run it from there (my favourite way).
<br>Once the process is complete, artifacts will be in `/target` directory.
<br>There's no requirement to delete MANIFEST from the jar before putting to MC folder.

### Running
To launch the client in the IDE, you need to execute Start.java, **and specify working directory to `./test_run/`**.<br>
An example run configuration.<br>
<img src="https://developers.marcloud.net/i/launchConfig.png"/>
Minecraft's directory will be `./test_run/`. All saves, resource packs etc. will be there.