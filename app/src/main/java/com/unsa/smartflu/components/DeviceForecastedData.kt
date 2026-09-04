package com.unsa.smartflu.components

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unsa.smartflu.R

class DeviceForecastedData : AppCompatActivity() {

    private lateinit var devName: String
    private lateinit var devId: String

    private lateinit var database: DatabaseReference
    private lateinit var lineChart: LineChart
    private lateinit var lineData: ArrayList<Entry>
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineDataChart: LineData
    private var xValue: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_device_forecasted_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        devName = intent.getStringExtra("nombre") ?: ""
        devId = intent.getStringExtra("id") ?: ""

        val title = findViewById<TextView>(R.id.upper_text)
        title.text = devName

        databaseListener()

        lineData = arrayListOf()

        lineDataSet = LineDataSet(lineData, "Caudal (m3/s)")
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.valueTextSize = 15f

        lineDataChart = LineData(lineDataSet)
        lineChart = findViewById(R.id.line_chart)
        lineChart.data = lineDataChart
        lineChart.description.text = "Datos predecidos"

        lineChart.animateY(2000)
    }

    private fun addNewEntry(yValue: Float) {
        xValue += 1
        lineData.add(Entry(xValue, yValue))

        lineDataSet.notifyDataSetChanged()
        lineDataChart.notifyDataChanged()
        lineChart.notifyDataSetChanged()

        lineChart.invalidate()
    }

    private fun databaseListener() {
        database = FirebaseDatabase.getInstance().getReference()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.child(devId).child("value").getValue(Float::class.java)
                value?.let {
                    addNewEntry(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DeviceForecastedData, "Failed to read value.", Toast.LENGTH_SHORT).show()
            }
        }
        database.addValueEventListener(postListener)
    }
}
