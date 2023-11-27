package uk.co.developmentanddinosaurs.apps.poker.application.statistics

import poker.models.Vote

class Statistics(votes: List<Vote>) {
    val voteDistribution = votes.groupingBy { it }.eachCount()
    val mean = votes.filter { it != Vote.HIDDEN }.map { it.intValue() }.average()
    val mode = voteDistribution.maxByOrNull { it.value }?.key ?: Vote.HIDDEN
}
