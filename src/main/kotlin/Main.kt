import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.FileInputStream

fun main() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("fcm.json")))
        .build()

    FirebaseApp.initializeApp(options)

    val token =
        "cJcBhr8uQiytTL9TX-lATu:APA91bHriOWxOvtfPRgNjHSs4t9lTVLxM7_TNBt9hVsJCYUsEmzHUdCHCE_zb0zO0Oob0ZNwBLweJbrYITTQXpCVmcx2sHJh6tOTpzQ74DnRCqf4-n_PqMo"
    val actionType = "NEW_POST"

    when (actionType) {
        "LIKE" -> sendLike(token)
        "NEW_POST" -> sendNewPost(token)
        else -> return
    }
}

fun sendLike(token: String) {
    val message = Message.builder()
        .putData("action", "LIKE")
        .putData(
            "content", """{
                "userId": 1,
                "userName": "Vasiliy",
                "postId": 2,
                "postAuthor": "Netology"
            }""".trimIndent()
        )
        .setToken(token)
        .build()

    FirebaseMessaging.getInstance().send(message)
}

fun sendNewPost(token: String) {
    val message = Message.builder()
        .putData("action", "NEW_POST")
        .putData(
            "content", """{
                "userId": 5,
                "userName": "Odysseus",
                "content": "Some Content"
            }""".trimIndent()
        )
        .setToken(token)
        .build()

    FirebaseMessaging.getInstance().send(message)
}