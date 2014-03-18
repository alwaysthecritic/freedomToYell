package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config

object UrlMapper {
    def mapUrl(url: String)(implicit config: Config) = {
        val hostSwapped = url.replace(config.oldHost, config.newHost)
        hostSwapped.replaceAll("-(pi|popup)$", "")
    }
}