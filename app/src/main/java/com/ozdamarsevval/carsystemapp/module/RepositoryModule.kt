package com.ozdamarsevval.carsystemapp.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ozdamarsevval.carsystemapp.repository.AuthRepository
import com.ozdamarsevval.carsystemapp.repository.AuthRepositoryImlp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
    ): AuthRepository{
        return AuthRepositoryImlp(auth, db)
    }



}