package com.jraska.github.client.release.data

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitGitHubApi {
  @POST("milestones")
  fun createMilestone(@Body dto: MilestoneDto): Call<CreateMilestoneResponseDto>

  @PATCH("milestones/{milestoneNumber}")
  fun updateMilestone(@Path("milestoneNumber") milestoneNumber: Int, @Body dto: UpdateMilestoneDto): Call<ResponseBody>

  @POST("issues/{prNumber}/comments")
  fun sendComment(@Path("prNumber") prNumber: Int, @Body commentRequestDto: CommentRequestDto): Call<ResponseBody>

  @GET("releases/tags/{tag}")
  fun getRelease(@Path("tag") tag: String): Call<ReleaseDto>

  @PATCH("releases/{release_id}")
  fun setReleseBody(@Path("release_id") id: Int, @Body relaseBody: ReleaseBodyDto): Call<ResponseBody>

  @PATCH("issues/{issue_number}")
  fun assignMilestone(@Path("issue_number") prNumber: Int, @Body dto: AssignMilestoneDto): Call<ResponseBody>

  @GET("pulls?state=closed&base=master&per_page=100")
  fun getPulls(@Query("page") page: Int = 1): Call<List<PullRequestDto>>

  @POST("releases")
  fun createRelease(@Body dto: CreateReleaseDto): Call<ResponseBody>

  @GET("pulls/{pr_number}/commits")
  fun commits(@Path("pr_number") prNumber: Int): Call<List<CommitItemDto>>
}

class PullRequestDto {
  @SerializedName("number")
  var number: Int = 0

  @SerializedName("title")
  var title: String = ""

  @SerializedName("milestone")
  var milestone: MilestoneDto? = null

  @SerializedName("merged_at")
  var mergedAt: String? = null
}

class ReleaseBodyDto(
  @SerializedName("body")
  val body: String
)

class ReleaseDto {
  @SerializedName("id")
  var id: Int = 0
}

class CreateReleaseDto(
  @SerializedName("tag_name")
  val tagName: String,

  @SerializedName("target_commitish")
  val targetCommitish: String = "master",

  @SerializedName("name")
  val name: String = tagName
)

class AssignMilestoneDto(
  @SerializedName("milestone")
  val milestoneNumber: Int
)

class MilestoneDto(
  @SerializedName("title")
  val title: String,

  @SerializedName("state")
  val state: String = "closed"
)

class UpdateMilestoneDto(
  @SerializedName("description")
  val body: String,
)

class CreateMilestoneResponseDto {
  @SerializedName("number")
  var number: Int = 0
}

class CommentRequestDto(val body: String)

class CommitItemDto {
  @SerializedName("sha")
  lateinit var sha: String

  @SerializedName("commit")
  lateinit var commit: CommitDto

  @SerializedName("author")
  lateinit var author: UserDto
}

class CommitDto {
  @SerializedName("author")
  lateinit var author: AuthorDto

  @SerializedName("message")
  lateinit var message: String
}

class AuthorDto {
  @SerializedName("date")
  lateinit var dateString: String
}

class UserDto {
  @SerializedName("login")
  lateinit var login: String
}
