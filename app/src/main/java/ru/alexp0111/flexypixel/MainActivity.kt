package ru.alexp0111.flexypixel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}