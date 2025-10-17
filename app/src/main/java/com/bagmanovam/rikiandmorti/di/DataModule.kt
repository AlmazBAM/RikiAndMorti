package com.bagmanovam.rikiandmorti.di

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bagmanovam.rikiandmorti.data.db.RikMortiDatabase
import com.bagmanovam.rikiandmorti.data.internet.RikMortiApi
import com.bagmanovam.rikiandmorti.data.repository.RikMortiHeroesDbRepositoryImpl
import com.bagmanovam.rikiandmorti.data.repository.SearchRikMortiHeroesRepositoryImpl
import com.bagmanovam.rikiandmorti.domain.interactor.GetRikMoritHeroesDbInteractor
import com.bagmanovam.rikiandmorti.domain.interactor.GetRikMortiHeroDbInteractor
import com.bagmanovam.rikiandmorti.domain.interactor.RequestRikMortiHeroesInteractor
import com.bagmanovam.rikiandmorti.domain.interactor.SaveRikMortiHeroesDbInteractor
import com.bagmanovam.rikiandmorti.domain.repository.RikMortiHeroesDbRepository
import com.bagmanovam.rikiandmorti.domain.repository.SearchRikMortiHeroesRepository
import com.bagmanovam.rikiandmorti.domain.useCase.GetRikMoritHeroesDbUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.GetRikMortiHeroDbUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroesUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.SaveRikMoritHeroesDbUseCase
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://rickandmortyapi.com/")
            .build()
            .create(RikMortiApi::class.java)
    }


    single<SearchRikMortiHeroesRepository> { SearchRikMortiHeroesRepositoryImpl(get()) }
    single<RikMortiHeroesDbRepository> { RikMortiHeroesDbRepositoryImpl(get()) }

    factory<RequestRikMortiHeroesUseCase> { RequestRikMortiHeroesInteractor(get()) }
    factory<GetRikMoritHeroesDbUseCase> { GetRikMoritHeroesDbInteractor(get()) }
    factory<SaveRikMoritHeroesDbUseCase> { SaveRikMortiHeroesDbInteractor(get()) }
    factory<GetRikMortiHeroDbUseCase> { GetRikMortiHeroDbInteractor(get()) }


    single<RikMortiDatabase> {
        Room.databaseBuilder(get(), RikMortiDatabase::class.java, "space_db")
            .fallbackToDestructiveMigration(true)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.i("Room", "onCreate: ")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.i("Room", "onOpen: ${db.path}")
                }

                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    super.onDestructiveMigration(db)
                    Log.i("Room", "onDestructiveMigration: ${db.version}")
                }
            })
            .build()
    }

    single {
        get<RikMortiDatabase>().getDao()
    }
}