package samcarr.freedomtoyell

object Config {
    def apply(inputFilename: String, oldHost: String, newHost: String,
            articleDir: String, outputDirName: String) = {
        new Config(inputFilename, oldHost, newHost, articleDir, outputDirName)
    }
}

class Config(val inputFilename: String, _oldHost: String, _newHost: String,
             val articleDir: String, val outputDirName: String) {
    val oldHost = _oldHost.toLowerCase()
    val newHost = _newHost.toLowerCase()
}