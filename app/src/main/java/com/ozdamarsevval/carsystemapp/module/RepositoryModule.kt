package com.ozdamarsevval.carsystemapp.module

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ozdamarsevval.carsystemapp.repository.AuthRepository
import com.ozdamarsevval.carsystemapp.repository.AuthRepositoryImlp
import com.ozdamarsevval.carsystemapp.repository.CarRepository
import com.ozdamarsevval.carsystemapp.repository.CarRepositoryImpl
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
        db: FirebaseFirestore
    ): CarRepository {
        return CarRepositoryImpl(db)
    }



}