package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override val isUserLoggedIn: Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override suspend fun signInAnonymously() {
        firebaseAuth.signInAnonymously().await()
    }

    override suspend fun signInWithEmail(email: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).await()
    }

    override suspend fun signUpWithEmail(email: String, pass: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}
