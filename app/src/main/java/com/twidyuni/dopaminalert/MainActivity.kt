package com.twidyuni.dopaminalert

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminalert.AppAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var appList: List<AppInfo>
    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Overlay 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Overlay 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_CODE_OVERLAY_PERMISSION = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkOverlayPermission()

        sharedPreferences = getSharedPreferences("AppUsagePreferences", MODE_PRIVATE)

        appList = getInstalledApps()
        setupRecyclerView()

        saveButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            saveMaxUsageTime()
        }
    }

    private fun saveMaxUsageTime() {
        val editor = sharedPreferences.edit()

        (recyclerView as AppAdapter).appList.forEach { app ->
            val maxUsageTime = app.maxUsageTime
        }

        editor.apply()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AppAdapter(appList)
    }

    private fun getInstalledApps(): List<AppInfo> {
        val packageManager = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        // Intent에 맞는 앱 목록 쿼리
        val resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        return resolveInfos.map { resolveInfo ->
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val packageName = resolveInfo.activityInfo.packageName
            AppInfo(appName, packageName)
        }
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            overlayPermissionLauncher.launch(intent)
        }
    }

}