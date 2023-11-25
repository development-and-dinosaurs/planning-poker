package uk.co.developmentanddinosaurs.apps.poker

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import poker.models.Stats

class StatsSection(private val document: Document) {

    private val statsSection = document.getElementById("stats") as HTMLElement

    fun write(stats: Stats) {
        stats.votes.entries.sortedBy { it.key }.map { vote ->
            statsSection.appendChild(row(vote.key.value, vote.value.toString()))
        }
        statsSection.appendChild(row("Mode", stats.average.mode.value))
        statsSection.appendChild(row("Mean", stats.average.mean.toString()))
    }

    fun reset() {
        statsSection.innerHTML = ""
    }

    private fun row(column1: String, column2: String): Element {
        return document.createElement("tr").apply {
            appendChild(column(column1))
            appendChild(column(column2))
        }
    }

    private fun column(content: String): Element {
        return document.createElement("td").apply {
            textContent = content
        }
    }
}