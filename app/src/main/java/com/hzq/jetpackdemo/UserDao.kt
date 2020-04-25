package com.hzq.jetpackdemo

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User): Long

    @Update
    fun updataUser(user: User)

    @Query("select*from User")
    fun loadAllUsers(): List<User>

    @Query("select * from User where age > :age")
    fun loadUsersOlderThan(age: Int): List<User>

    @Delete
    fun deleteUser(user: User)

    @Query("delete from User where lastName = :lastName")
    fun deleteUserByLastName(lastName: String): Int

}