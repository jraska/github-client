package com.jraska.devanalytics.github

import com.jraska.devanalytics.github.model.EventReader
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.File

class EventReaderTest {
  @Test
  fun canReadPrOpenEvent() {
    val jsonReader = json("response/pr_open.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Open")
    assertThat(event.action).isEqualTo("opened")
    assertThat(event.author).isEqualTo("Codertocat")
    assertThat(event.prUrl).isEqualTo("https://github.com/Codertocat/Hello-World/pull/2")
    assertThat(event.prNumber).isEqualTo(2)
    assertThat(event.comment).isEqualTo("This is a pretty simple change that we need to pull into master.")
    assertThat(event.state).isEqualTo("open")
  }

  @Test
  fun canReadPrCommentEvent() {
    val jsonReader = json("response/pr_comment.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Comment")
    assertThat(event.action).isEqualTo("created")
    assertThat(event.author).isEqualTo("jraska")
    assertThat(event.prUrl).isEqualTo("https://github.com/jraska/github-client/pull/353")
    assertThat(event.prNumber).isEqualTo(353)
    assertThat(event.comment).isEqualTo("Another test comment")
  }

  @Test
  fun canReadPrReviewRequestedEvent() {
    val jsonReader = json("response/review_requested.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Review Request")
    assertThat(event.action).isEqualTo("review_requested")
    assertThat(event.author).isEqualTo("jraska")
    assertThat(event.prUrl).isEqualTo("https://github.com/jraska/github-client/pull/353")
    assertThat(event.prNumber).isEqualTo(353)
    assertThat(event.comment).isNull()
  }

  @Test
  fun canReadPrCommentDeletedEvent() {
    val jsonReader = json("response/comment_deleted.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Comment Delete")
    assertThat(event.action).isEqualTo("deleted")
    assertThat(event.author).isEqualTo("jraska")
    assertThat(event.prUrl).isEqualTo("https://github.com/jraska/github-client/pull/353")
    assertThat(event.prNumber).isEqualTo(353)
    assertThat(event.comment).isEqualTo("Test comment for webhook")
  }

  @Test
  fun canReadReviewCreatedDeletedEvent() {
    val jsonReader = json("response/review_created.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Review Create")
    assertThat(event.action).isEqualTo("created")
    assertThat(event.author).isEqualTo("mikehardy")
    assertThat(event.prUrl).isEqualTo("https://github.com/ankidroid/Anki-Android/pull/7765")
    assertThat(event.prNumber).isEqualTo(7765)
    assertThat(event.comment).isEqualTo("Yeah this all seems fine, including the default setting\r\n\r\n> I'm bad at days off\r\n\r\nhahaha you and me both, I suppose as long as it's fun, is it a day on?")
    assertThat(event.state).isEqualTo("approved")
  }

  @Test
  fun canReadMergedEvent() {
    val jsonReader = json("response/pr_merged.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Merge")
    assertThat(event.action).isEqualTo("closed")
    assertThat(event.author).isEqualTo("jraska")
    assertThat(event.prUrl).isEqualTo("https://github.com/jraska/github-client/pull/356")
    assertThat(event.prNumber).isEqualTo(356)
    assertThat(event.comment).isEqualTo("Kotlin in plugins to 1.4.20")
    assertThat(event.state).isEqualTo("closed")
  }

  @Test
  fun canReadCommentEditEvent() {
    val jsonReader = json("response/pr_comment_edit.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Comment Edit")
  }

  @Test
  fun canReadPrDescriptionEditEvent() {
    val jsonReader = json("response/pr_description_edit.json")

    val event = EventReader.create().parse(jsonReader)

    assertThat(event.name).isEqualTo("PR Edit")
  }

  companion object {
    fun json(path: String): BufferedReader {
      val uri = this.javaClass.classLoader.getResource(path)
      val file = File(uri!!.path)
      return file.bufferedReader()
    }
  }
}
