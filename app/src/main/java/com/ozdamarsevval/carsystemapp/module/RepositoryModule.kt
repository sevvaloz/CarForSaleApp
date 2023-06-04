package com.ozdamarsevval.carsystemapp.module

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.ozdamarsevval.carsystemapp.repository.*
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
        appPref: SharedPreferences,
        gson: Gson
    ): AuthRepository{
        return AuthRepositoryImlp(auth, db, appPref, gson)
    }

    @Provides
    @Singleton
    fun provideCarRepository(
        db: FirebaseFirestore,
        storageReference: StorageReference
    ): CarRepository {
        return CarRepositoryImpl(db, storageReference)
    }

    @Provides
    @Singleton
    fun provideCarSpecialityRepository(
        db: FirebaseFirestore
    ): CarSpecialityRepository {
        return CarSpecialityRepositoryImlp(db)
    }



}