package uk.co.developmentanddinosaurs.apps.poker.application.statistics

import poker.models.Vote

class Statistics(votes: List<Vote>) {
    val voteDistribution = votes.groupingBy { it }.eachCount()
    val mean = cleanNan(votes.filter { it != Vote.HIDDEN }.map { it.intValue() }.average())
    val mode = voteDistribution.maxByOrNull { it.value }?.key ?: Vote.HIDDEN

    private fun cleanNan(double: Double): Double {
        return if (double.isNaN()) 0.0 else double
    }
}
