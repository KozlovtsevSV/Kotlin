package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.view.forEach as forEach1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClick = findViewById<Button>(R.id.click)
        val buttonView = findViewById<Button>(R.id.view)

        var listMyDataClass: ArrayList<MyDataClass> = ArrayList()

        if (buttonClick != null) {
            buttonClick.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    for (i in 1..10) {
                        listMyDataClass.add(createMyDataClass("MyDataClass_" + i, i))
                    }
                }
            })
        }


        if (buttonView != null) {
            buttonView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    for (i in 0..listMyDataClass.size-1) {
                        println(listMyDataClass.get(i).toString())
                    }
                }
            })
        }
    }

    fun createMyDataClass(name: String, number: Int): MyDataClass{
        return MyDataClass(name, number)
    }

    data class MyDataClass(var name: String, val number: Int){

    }

}

