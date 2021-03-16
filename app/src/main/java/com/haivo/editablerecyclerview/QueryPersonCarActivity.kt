package com.haivo.editablerecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haivo.editablerecyclerview.databinding.ActivityMoreAppsBinding
import com.haivo.editablerecyclerview.databinding.ActivityQueryPersonCarBinding


/**
 * @description:
 **/
class QueryPersonCarActivity : AppCompatActivity() {
    private val TAG = "QueryPersonCarActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityQueryPersonCarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}