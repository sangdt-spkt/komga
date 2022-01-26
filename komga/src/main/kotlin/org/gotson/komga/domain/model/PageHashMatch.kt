package org.gotson.komga.domain.model

import java.net.URL

data class PageHashMatch(
  val bookId: String,
  val url: URL,
  val pageNumber: Int,
)
