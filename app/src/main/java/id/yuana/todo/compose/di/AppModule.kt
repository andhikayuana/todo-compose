package id.yuana.todo.compose.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.yuana.todo.compose.data.local.TodoDatabase
import id.yuana.todo.compose.data.repository.AuthRepository
import id.yuana.todo.compose.data.repository.TodoRepository
import id.yuana.todo.compose.util.EmailValidator
import id.yuana.todo.compose.util.PasswordConfirmValidator
import id.yuana.todo.compose.util.PasswordValidator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DATABASE_NAME = "todo.db"

    @Provides
    @Singleton
    fun provideTodoDatabase(application: Application): TodoDatabase {
        return Room.databaseBuilder(
            application,
            TodoDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(database: TodoDatabase): TodoRepository {
        return TodoRepository.Impl(database.todoDao)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepository.Impl(auth)

    @Provides
    fun provideEmailValidator(): EmailValidator = EmailValidator()

    @Provides
    fun providePasswordValidator(): PasswordValidator = PasswordValidator()

    @Provides
    fun providePasswordConfirmValidator(): PasswordConfirmValidator = PasswordConfirmValidator()
}