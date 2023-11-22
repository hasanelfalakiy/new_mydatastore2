
package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp.databinding.ActivityMainBinding
import android.content.Intent
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapp.databinding.ActivitySettingsBinding
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.material.Text
import androidx.compose.runtime.*


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")

public class MainActivity : AppCompatActivity() {

    private lateinit var bindingSettings: ActivitySettingsBinding

    private var _binding: ActivityMainBinding? = null
    
    private val binding: ActivityMainBinding
      get() = checkNotNull(_binding) { "Activity has been destroyed" }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate and get instance of binding
        _binding = ActivityMainBinding.inflate(layoutInflater)
        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        // set content view to binding's root
        setContentView(binding.root)
        
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Text(text = "hai from compose")
            }
        }
        
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSetting().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    bindingSettings.switchTemagelap.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    bindingSettings.switchTemagelap.isChecked = false
                }
            })
            
        
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
