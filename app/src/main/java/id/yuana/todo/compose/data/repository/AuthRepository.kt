package id.yuana.todo.compose.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface AuthRepository {

    fun isAuthenticated(): Boolean

    suspend fun signUp(email: String, password: String): AuthResult

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signOut()

    class Impl(
        private val auth: FirebaseAuth
    ) : AuthRepository {

        override fun isAuthenticated(): Boolean = auth.currentUser != null

        override suspend fun signUp(email: String, password: String): AuthResult {
            return suspendCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        }

        override suspend fun signIn(email: String, password: String): AuthResult {
            return suspendCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
        }

        override suspend fun signOut() {
            auth.signOut()
        }

    }
}