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
}

class PullRequestDto {
  @SerializedName("number")
  var number: Int = 0

  @SerializedName("title")
  var title: String = ""

  @SerializedName("milestone")
  var milestone: MilestoneDto? = null
}

class ReleaseBodyDto(
  @SerializedName("body")
  val body: String
)

class ReleaseDto {
  @SerializedName("id")
  var id: Int = 0
}

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
