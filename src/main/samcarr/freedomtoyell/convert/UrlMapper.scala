package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config

class UrlMapper(implicit config: Config) {
    def mapUrl(url: String) = {
        val hostSwapped = url.replace(config.oldHost, config.newHost)
        hostSwapped.replaceAll("-(pi|popup)$", "")
    }
}