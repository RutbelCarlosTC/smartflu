package com.unsa.smartflu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import com.unsa.smartflu.components.ConnectDevice
import com.unsa.smartflu.components.DeviceAdapter
import com.unsa.smartflu.databinding.ActivityMainBinding
import com.unsa.smartflu.models.Device

class MainActivity : AppCompatActivity() {

    private lateinit var deviceList: ArrayList<Device>
    private lateinit var deviceAdapter: DeviceAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        deviceList = arrayListOf()
        deviceAdapter = DeviceAdapter(deviceList)
        val recyclerView : RecyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = deviceAdapter
        val fabAddDevice: FloatingActionButton = findViewById(R.id.fab_add_device)
        fabAddDevice.setOnClickListener {
            startActivity(Intent(this, ConnectDevice::class.java))
        }
        EventChangeListener()
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email

            db.collection("users")
                .document(userEmail!!)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val deviceIds = snapshot.get("devs") as? ArrayList<String> ?: arrayListOf()

                        db.collection("devices")
                            .whereIn("id", deviceIds)
                            .addSnapshotListener { value, error ->
                                if (error != null) {
                                    Log.e("Firestore error", error.message.toString())
                                    return@addSnapshotListener
                                }

                                val updatedDeviceList = ArrayList<Device>()

                                for (doc in value!!.documents) {
                                    val device = Device(
                                        doc.get("email_creator") as? String ?: "",
                                        doc.get("id") as? String ?: "",
                                        doc.get("last_value") as? Int ?: 0,
                                        doc.get("name") as? String ?: "",
                                        doc.get("tag_description") as? String ?: "",
                                        doc.get("size") as? Double ?: 0.0
                                    )
                                    updatedDeviceList.add(device)
                                }

                                deviceList.clear()
                                deviceList.addAll(updatedDeviceList)
                                deviceAdapter.notifyDataSetChanged()
                            }
                    } else {
                        Log.d("Firestore", "Document does not exist")
                    }
                }
        } else {
            Log.d("Firebase Auth", "User not authenticated")
        }
    }

}