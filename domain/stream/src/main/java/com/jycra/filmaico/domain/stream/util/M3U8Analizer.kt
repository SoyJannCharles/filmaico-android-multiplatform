package com.jycra.filmaico.domain.stream.util

import com.jycra.filmaico.domain.stream.model.metadata.ProviderMetadata

object M3U8Analyzer {

    private val resolutionRegex = Regex("""RESOLUTION=(\d+)x(\d+)""")

    fun analyze(content: String): ProviderMetadata {

        val variantLines = content.lines().filter { it.startsWith("#EXT-X-STREAM-INF") }

        if (variantLines.isEmpty()) {
            return ProviderMetadata(
                mainResolution = "Direct",
                qualityCount = 1,
                isAdaptive = false
            )
        }

        val resolutions = variantLines.mapNotNull { line ->
            resolutionRegex.find(line)?.let {
                it.groupValues[1].toInt() to it.groupValues[2].toInt()
            }
        }

        val maxRes = resolutions.maxByOrNull { it.first * it.second }
        val height = maxRes?.second ?: 0

        return ProviderMetadata(
            mainResolution = if (height > 0) "${height}p" else "SD",
            qualityCount = resolutions.size,
            isAdaptive = resolutions.size > 1
        )

    }

}