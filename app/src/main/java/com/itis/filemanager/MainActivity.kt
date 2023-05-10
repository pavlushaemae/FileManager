package com.itis.filemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itis.filemanager.presentation.fragments.list.ListOfFilesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ListOfFilesFragment(), ListOfFilesFragment::class.simpleName)
            .commit()
    }
}
