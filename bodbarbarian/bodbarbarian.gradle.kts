
version = "1.1.3"

project.extra["PluginName"] = "Bod Barbarian"
project.extra["PluginDescription"] = "Bod - Village Barbarian Lifestyle | Cooks and Fishes"

dependencies {
    compileOnly(project(":bodutils"))
    compileOnly(project(":bodbreakhandler"))
}

tasks {
    jar {
        manifest {
            attributes(mapOf(
                    "Plugin-Version" to project.version,
                    "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                    "Plugin-Provider" to project.extra["PluginProvider"],
                    "Plugin-Dependencies" to
                            arrayOf(
                                nameToId("BodUtils"),
                                nameToId("BodBreakHandler")
                            ).joinToString(),
                    "Plugin-Description" to project.extra["PluginDescription"],
                    "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}