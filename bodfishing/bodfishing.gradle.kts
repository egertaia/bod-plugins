
version = "0.1.1"

project.extra["PluginName"] = "Bod Fishing AIO"
project.extra["PluginDescription"] = "Bod Fishing AIO"

dependencies {
    compileOnly(group = "com.openosrs.externals", name = "paistisuite", version = "+")
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
                            nameToId("PaistiSuite")
                        ).joinToString(),
                "Plugin-Description" to project.extra["PluginDescription"],
                "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}