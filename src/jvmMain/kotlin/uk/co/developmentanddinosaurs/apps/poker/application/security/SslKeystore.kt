package uk.co.developmentanddinosaurs.apps.poker.application.security

import io.ktor.network.tls.certificates.generateCertificate
import java.io.File

class SslKeystore {
    val alias = "alias"
    val password = "password"
    val file = File("build/keystore.jks")
    val keyStore =
        generateCertificate(
            file = file,
            keyAlias = alias,
            keyPassword = password,
            jksPassword = password,
        )
}
