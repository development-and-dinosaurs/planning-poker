package uk.co.developmentanddinosaurs.apps.poker.application.statistics

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import poker.models.Vote

class StatisticsTest : StringSpec({

    "calculates vote distribution" {
        val votes = listOf(Vote.ONE, Vote.ONE, Vote.TWO, Vote.THREE)

        val statistics = Statistics(votes)

        statistics.voteDistribution shouldBe mapOf(Vote.ONE to 2, Vote.TWO to 1, Vote.THREE to 1)
    }

    "calculates vote distribution with no votes" {
        val votes = listOf<Vote>()

        val statistics = Statistics(votes)

        statistics.voteDistribution shouldBe mapOf()
    }

    "calculates mean of votes" {
        val votes = listOf(Vote.ONE, Vote.ONE, Vote.TWO, Vote.THREE)

        val statistics = Statistics(votes)

        statistics.mean shouldBe 1.75
    }

    "ignores hidden votes when calculating mean" {
        val votes = listOf(Vote.ONE, Vote.ONE, Vote.TWO, Vote.THREE, Vote.HIDDEN)

        val statistics = Statistics(votes)

        statistics.mean shouldBe 1.75
    }

    "calculates mean of votes with no votes" {
        val votes = listOf<Vote>()

        val statistics = Statistics(votes)

        statistics.mean shouldBe 0.0
    }

    "calculates mean of votes with all hidden votes" {
        val votes = listOf(Vote.HIDDEN, Vote.HIDDEN)

        val statistics = Statistics(votes)

        statistics.mean shouldBe 0.0
    }

    "calculates mode of votes" {
        val votes = listOf(Vote.ONE, Vote.ONE, Vote.TWO, Vote.THREE)

        val statistics = Statistics(votes)

        statistics.mode shouldBe Vote.ONE
    }

    "calculates mode of votes with no votes" {
        val votes = listOf<Vote>()

        val statistics = Statistics(votes)

        statistics.mode shouldBe Vote.HIDDEN
    }

})
