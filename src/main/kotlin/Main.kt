import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.util.*


fun main() {
    val scan = Scanner(System.`in`)
    scan.use { scan ->
        println("Set path to keystore:")
        val path = scan.nextLine()


        println("Set password:")
        val password = scan.nextLine()

        println("Set alias:")
        val alias = scan.nextLine()

        println("Set new password:")
        val newPassword = scan.nextLine()

        println("Set name of certificate:")
        val certificateName = scan.nextLine()

        val storeData = StoreData(alias, newPassword, certificateName)

        createCertificate(loadCertificate(path, password.toCharArray(), storeData.alias), storeData)
    }

}

fun loadCertificate(path: String, password: CharArray?, alias: String): Certificate {
    val certIs: InputStream = FileInputStream(path)
    val ks = KeyStore.getInstance("jks")
    ks.load(certIs, password)
    return ks.getCertificate(alias)
}


private fun createCertificate(certificate: Certificate, storeData: StoreData) {

    // Creating an empty JKS keystore
    val keystore = KeyStore.getInstance("jks")
    keystore.load(null, null)

    // Adding the cert to the keystore
    keystore.setCertificateEntry(storeData.alias, certificate)

    // Saving the keystore with a zero length password
    val fout = FileOutputStream(storeData.name)
    keystore.store(fout, storeData.newPassword?.toCharArray())  //CharArray(0)

}

data class StoreData(val alias: String, val newPassword: String?, val name: String)
