package com.example.kothakhali.activity.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.kothakhali.R

class PermissionActivity : AppCompatActivity() {

    private val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
    )
    private val REQUEST: Int = 112
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        // Check for permission
        if (!haspermission()) {
            requestpermission()
        }
    }
    private fun requestpermission() {
        ActivityCompat.requestPermissions(
                this@PermissionActivity,
                permissions, 112
        )
    }
    private fun haspermission(): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            permission
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false;
            }
        }
        return true
    }
    private fun callnextactivity(){
        startActivity(Intent(this@PermissionActivity, DashboardActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        when (requestCode) {
            REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callnextactivity()
                } else {
                    Toast.makeText(this, "Grant Access to use the app", Toast.LENGTH_LONG).show()
                    requestpermission()
                }
            }
        }
    }
}