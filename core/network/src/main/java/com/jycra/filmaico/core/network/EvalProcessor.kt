package com.jycra.filmaico.core.network

import javax.inject.Inject

class EvalProcessor @Inject constructor() {

    fun extractM3U8(html: String): String? {

        val evalRegex = Regex(
            "eval\\((function\\(p,a,c,k,e,d\\).*?split\\(['\"]\\|['\"]\\)\\))\\)",
            RegexOption.DOT_MATCHES_ALL
        )

        val match = evalRegex.find(html) ?: return null
        val raw = match.groupValues[1]

        val paramsRegex = Regex(
            "\\((['\"].*?['\"])\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(['\"].*?['\"])\\.split",
            RegexOption.DOT_MATCHES_ALL
        )

        val params = paramsRegex.find(raw) ?: return null

        val p = params.groupValues[1].trim('"', '\'')
        val a = params.groupValues[2].toInt()
        val c = params.groupValues[3].toInt()
        val k = params.groupValues[4].trim('"', '\'').split("|").toTypedArray()

        val unpacked = unpack(p, a, c, k)

        return Regex("https?://[^\"']+\\.m3u8[^\"']*")
            .find(unpacked)
            ?.value
            ?.replace("\\/", "/")

    }

    private fun unpack(p: String, a: Int, c: Int, k: Array<String>): String {

        var payload = p
        var count = c

        while (count-- > 0) {
            if (k[count].isNotEmpty()) {
                val regex = Regex("\\b" + count.toString(a) + "\\b")
                payload = payload.replace(regex, k[count])
            }
        }

        return payload

    }

}