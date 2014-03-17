package samcarr.freedomtoyell

object Config {
    def apply(inputFileName: String, oldHost: String, newHost: String, outputDirName: String) = {
        new Config(inputFileName, oldHost, newHost, outputDirName)
    }
}

class Config(val inputFileName: String, _oldHost: String, _newHost: String, val outputDirName: String) {
    val oldHost = _oldHost.toLowerCase()
    val newHost = _newHost.toLowerCase()
}