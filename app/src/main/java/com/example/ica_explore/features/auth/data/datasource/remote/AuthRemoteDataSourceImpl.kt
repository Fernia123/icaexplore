package com.example.ica_explore.features.auth.data.datasource.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo

class AuthRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : AuthRemoteDataSource {

    override suspend fun signUp(email: String, password: String): UserInfo? {
        val user = supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        return user
    }

    override suspend fun signIn(email: String, password: String) {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signOut() {
        supabaseClient.auth.signOut()
    }

    override suspend fun getCurrentUser(): UserInfo? {
        val session = supabaseClient.auth.currentSessionOrNull()
        return session?.user
    }
}
