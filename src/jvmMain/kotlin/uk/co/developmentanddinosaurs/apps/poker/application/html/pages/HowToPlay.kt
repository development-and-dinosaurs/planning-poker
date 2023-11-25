package uk.co.developmentanddinosaurs.apps.poker.application.html.pages

import kotlinx.html.*
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.head
import uk.co.developmentanddinosaurs.apps.poker.application.html.components.header

fun HTML.howToPlay() = run {
    head(title = "How to Play | Prehistoric Planning Poker")
    body {
        header(page = "how-to-play")
        div(classes = "frame") {
            div(classes = "container") {
                div(classes = "header") {
                    h1 { +"How to Play" }
                }
                div(classes = "row") {
                    p {
                        +"If you want to learn how to play Prehistoric Planning Poker, than you've come to the right place! This page will expand on the quickstart guide on the homepage so you know exactly what you need to do."
                    }
                    p {
                        +"Prehistoric Planning Poker can be summed up by the following steps:"
                    }
                    ul(classes = "standard-list") {
                        li { +"Create a room" }
                        li { +"Invite your team" }
                        li { +"Discuss a story" }
                        li { +"Vote" }
                        li { +"Reveal" }
                        li { +"Discuss" }
                        li { +"Repeat as necessary" }
                    }
                    div(classes = "row") {
                        h2 { +"Create a room" }
                        p { +"We start by creating a room in which we can play" }
                        p { +"To create a room, click the 'Create Room' button on the homepage, at the top in the navigation bar, or even this lovely button right here." }
                        a(classes = "waves-effect waves-light btn-large orange") {
                            href = "/how-to-play"
                            +"create-a-room"
                        }
                        p { +"Once you've created a room, you'll be taken directly to the room page, where you'll be able to invite your team." }
                    }
                    div(classes = "row") {
                        h2 { +"Invite your team" }
                        p { +"The next step is to invite your team to the room so they can play too. This is as simple as sending them a link to the room you are in." }
                        p { +"Once they join, they'll be assigned a name automatically." }
                        p { +"Once everyone has joined, you are ready to play." }
                    }
                    div(classes = "row") {
                        h2 { +"Discuss a story" }
                        p { +"Now that everyone has joined, the fun can begin. We start by picking a story to discuss. " }
                        p { +"To protect your data, no story information is included in the Prehistoric Planning Poker application." }
                        p { +"This means you'll want to open up whichever application the story details are stored in and share them with the team. This might be in Jira, Trello, a post-it note, or anything." }
                        p { +"To have an effective discussion, you'll want to make sure that everyone knows the purpose, acceptance, and constraints of any story. Any pre-estimation refinement such as three amigos output should be shared at this point. " }
                        p { +"Once everyone is happy that they understand the story, you can move on to voting." }
                    }
                    div(classes = "row") {
                        h2 { +"Vote" }
                        p { +"After everyone has agreed on the story, it's time to vote." }
                        p { +"To vote, simply click on the card that represents the story points you think this story deserves. Story points in Prehistoric Planning Poker are non-customisable and range in a standard Fibonacci sequence of 1 to 13. The best estimates take into account the relative effort and complexity of the story based on the stories the team usually works on. It can be helpful to have a set of 'standard measurement' stories to compare to." }
                        p { +"After a player votes, their player name will turn orange. Once everyone has voted, you can reveal the results." }
                    }
                    div(classes = "row") {
                        h2 { +"Reveal" }
                        p { +"Once everyone has voted, you can reveal the results." }
                        p { +"To reveal the results, simply click on the 'Reveal Votes' button in the control panel at the top of the room." }
                        p { +"Once revealed, the story points that each player picked will be displayed next to their name, and some statistics will be calculated such as the mean and mode averages for the votes that were made." }
                        p { +"If everyone has voted for the same number, congratulations! You have achieved consensus and you can move right on to the next story. Check out how in the 'Repeat as necessary' step." }
                        p { +"If not, it's time to discuss the reasons for the discrepancy in votes." }
                    }
                    div(classes = "row") {
                        h2 { +"Discuss" }
                        p { +"We haven't achieved consensus for this story, meaning that we aren't in agreement how much effort and complexity is present." }
                        p { +"To achieve consensus, players should discuss the reasons for their vote. Outliers are the most useful as these can highlight a misunderstanding of a key part of the story that can lead to wildly different estimates." }
                        p { +"Anything that was not clear and led to different estimates should be discussed at this point until people are more comfortable with the details of the story." }
                        p { +"You may achieve consensus at this point if there was a majority opinion and only a few outliers caused by misunderstanding, or you might need to vote on this story again after the discussion. For both cases, you're into the 'Repeat as necessary' step now." }
                    }
                    div(classes = "row") {
                        h2 { +"Repeat as necessary" }
                        p { +"In this stage, you've either achieved consensus on this story, or you need to go back and vote on it again." }
                        p { +"Regardless of the reason, to play the next round (with a new story or the existing one), just click on the 'Reset Votes' button to put things back as they were during the discussion stage. Now you can either vote again or move onto the next story" }
                        p { +"Repeat this process as necessary until you've estimated every story in scope for this estimation session." }
                    }
                }
            }
        }
    }
}