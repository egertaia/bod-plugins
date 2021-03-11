
version = "0.1.2"

project.extra["PluginName"] = "Bod Von Godblessed"
project.extra["PluginDescription"] = "Otto Godblessed knows the secrets of barbarian fishing - now you will too."

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