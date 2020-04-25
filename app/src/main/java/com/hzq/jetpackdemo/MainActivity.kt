package com.hzq.jetpackdemo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)
        viewModel = ViewModelProviders.of(this, MainVIewModeFactory(countReserved))
            .get(MainViewModel::class.java)

        lifecycle.addObserver(Myobserver())

        plusOneBtn.setOnClickListener {
            viewModel.plusOne()
        }

        clearBtn.setOnClickListener {
            viewModel.clear()
        }

        viewModel.counter.observe(this, Observer { count ->
            infoText.text = count.toString()
        })

        getUserBtn.setOnClickListener {
            val userId = (0..10000).random().toString()
            viewModel.getUser(userId)
        }

        viewModel.user.observe(this, Observer { user ->
            infoText.text = user.firstName
        })

        val userDao = AppDataBase.getDataBase(this).userDao()
        val user1 = User("tom", "brady", 40)
        val user2 = User("tom", "hanks", 50)

        addDataBtn.setOnClickListener {
            thread {
                user1.id = userDao.insertUser(user1)
                user2.id = userDao.insertUser(user2)
            }
        }
        updateDataBtn.setOnClickListener {
            thread {
                user1.age = 42
                userDao.updataUser(user1)
            }
        }

        deleteDataBtn.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("hanks")
            }
        }

        queryDataBtn.setOnClickListener {
            thread {
                for (user in userDao.loadAllUsers()) {
                    Log.d("MainActivity", user.toString())
                }
            }
        }

        doWorkBtn.setOnClickListener {
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()
            WorkManager.getInstance(this).enqueue(request)
        }
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value ?: 0)
        }
    }
}
