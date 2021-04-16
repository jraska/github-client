package com.jraska.github.client.users.ui

import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.espresso.util.TreeIterables
import androidx.test.platform.app.InstrumentationRegistry
import com.jraska.github.client.users.model.RepoHeader
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ReposSectionModelTest {
  @Test
  fun checkPopulatesViewsCorrectly() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val frameLayout = FrameLayout(context)

    val repos = listOf(
      RepoHeader("owner", "some name", "repo description", 6, 10),
      RepoHeader("owner2", "other name", "repo description2", 0, 3)
    )

    val reposSectionModel = ReposSectionModel("Test title", repos) {/* do nothing */ }
    val view = LayoutInflater.from(context).inflate(reposSectionModel.layout, frameLayout)
    reposSectionModel.bind(view)

    val views = TreeIterables.breadthFirstViewTraversal(view)

    assertThat(views).anyMatch { it is TextView && it.text == "some name" }
    assertThat(views).anyMatch { it is TextView && it.text == "other name" }
    assertThat(views).anyMatch { it is TextView && it.text == "repo description" }
    assertThat(views).anyMatch { it is TextView && it.text == "repo description2" }
    assertThat(views).anyMatch { it is TextView && it.text == "6" }
    assertThat(views).anyMatch { it is TextView && it.text == "10" }
    assertThat(views).anyMatch { it is TextView && it.text == "0" }
    assertThat(views).anyMatch { it is TextView && it.text == "3" }
  }
}
