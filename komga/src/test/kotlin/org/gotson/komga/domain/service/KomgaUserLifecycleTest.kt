package org.gotson.komga.domain.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.gotson.komga.domain.model.KomgaUser
import org.gotson.komga.domain.persistence.KomgaUserRepository
import org.gotson.komga.infrastructure.security.apikey.ApiKeyGenerator
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KomgaUserLifecycleTest(
  @Autowired private val userRepository: KomgaUserRepository,
  @Autowired private val userLifecycle: KomgaUserLifecycle,
) {
  @MockkBean
  private lateinit var apiKeyGenerator: ApiKeyGenerator

  private val user1 = KomgaUser("user1@example.org", "", false)
  private val user2 = KomgaUser("user2@example.org", "", false)

  @BeforeAll
  fun setup() {
    userRepository.insert(user1)
    userRepository.insert(user2)
  }

  @AfterAll
  fun teardown() {
    userRepository.deleteAll()
  }

  @Test
  fun `given existing api key when api key cannot be uniquely generated then it returns null`() {
    // given
    val uuid = ApiKeyGenerator().generate()
    every { apiKeyGenerator.generate() } returns uuid
    userLifecycle.createApiKey(user1, "test key")

    // when
    val apiKey = userLifecycle.createApiKey(user1, "test key")
    val apiKey2 = userLifecycle.createApiKey(user2, "test key")

    // then
    assertThat(apiKey).isNull()
    assertThat(apiKey2).isNull()
  }
}
