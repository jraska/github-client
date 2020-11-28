package com.jraska.devanalytics.github.model

internal object EventNames {
  val REVIEW_REQUEST = "PR Review Request"
  val REVIEW_CREATED = "PR Review Create"
  val PR_OPEN = "PR Open"
  val PR_CLOSE = "PR Close"
  val PR_MERGE = "PR Merge"
  val PR_COMMENT = "PR Comment"
  val PR_EDIT = "PR Edit"
  val PR_COMMENT_EDIT = "PR Comment Edit"
  val PR_COMMENT_DELETED = "PR Comment Delete"
  val PR_ASSIGNED = "PR Assigned"
  val PR_UNASSIGNED = "PR Unassigned"
  val PR_SYNC = "PR Synchronize"
  val PR_LABELED = "PR Labeled"
  val PR_UNLABELED = "PR Unlabeled"
}
