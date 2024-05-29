package hunzz.study.springauthjwt.global.jwt

import hunzz.study.springauthjwt.user.model.UserRole
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Description
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*

@Component
@PropertySource("classpath:application.yml")
class JwtUtil(
    @Value("\${jwt.secret.key}") private val secretKey: String
) {
    companion object {
        const val AUTHORIZATION_HEADER = "Authorization" // Header KEY
        const val AUTHORIZATION_KEY = "auth" // 사용자 권한 값의 KEY
        const val BEARER_PREFIX = "Bearer " // Token 식별자
        const val ROLE_PREFIX = "ROLE_" // role 식별자
        const val WHO_AM_I = "hunzz" // Token 발급자
        const val EXPIRATION_TIME = 1000L * 60 * 60 // 토큰 만료 시간 (1시간)
    }

    @Description("JWT 토큰 생성")
    fun createToken(email: String, role: UserRole): String {
        return "$BEARER_PREFIX${
            Jwts.builder().let {
                it.subject(email) // 사용자 식별자 값
                it.issuer(WHO_AM_I) // 토큰 발급자
                it.issuedAt(Date.from(Instant.now())) // 토큰 발급 시각
                it.claim(AUTHORIZATION_KEY, "$ROLE_PREFIX${role.name}") // 사용자 권한
                it.expiration(Date.from(Instant.now().plusMillis(EXPIRATION_TIME))) // 토큰 만료 시각
                it.signWith(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))) // 암호화 알고리즘
                    .compact()
            }
        }"
    }

    @Description("JWT 토큰을 Cookie에 저장")
    fun addJwtToCookie(jwt: String, response: HttpServletResponse) =
        Cookie(
            AUTHORIZATION_HEADER,
            URLEncoder.encode(jwt, "UTF-8").replace("\\+", "%20")
        ).let {
            it.path = "/"
            it.maxAge = 30 * 60
            response.addCookie(it)
        }

    @Description("JWT 토큰 SubString (BEARER PREFIX 제거)")
    fun substringJwt(jwt: String): String {
        if (jwt.isNotBlank() && jwt.startsWith("Bearer")) return jwt.substring(7)
        else throw IllegalArgumentException("") // TODO
    }

    @Description("JWT 토큰 검증")
    fun validateToken(jwt: String) {
        kotlin.runCatching {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(jwt)
        }.onFailure {
            when (it) {
                SecurityException::class, MalformedJwtException::class, SignatureException::class -> "유효하지 않은 JWT 서명입니다."
                ExpiredJwtException::class -> "만료된 JWT 토큰입니다."
                UnsupportedJwtException::class -> "지원하지 않는 JWT 토큰입니다."
                IllegalArgumentException::class -> "잘못된 JWT 토큰입니다."
                else -> ""
            }.let { message -> throw IllegalArgumentException(message) }
        }
    }

    @Description("JWT 토큰에서 사용자 정보 추출")
    fun getUserInfoFromJwt(jwt: String): Jws<Claims> =
        Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(jwt)
}