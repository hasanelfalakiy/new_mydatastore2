
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Colors
import androidx.compose.ui.unit.dp
import co.yml.charts.ui.linechart.LineChart
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import co.yml.charts.common.model.Point
//import co.yml.charts.common.model.LegendLabel
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.common.extensions.formatToSinglePrecision
import android.graphics.Typeface
import androidx.compose.ui.graphics.drawscope.Stroke


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
        
        // tinggi hilal mar'i low setiap bulan
        var muh = 2.3263f
        var saf = 3f
        var rawal = 1f
        var rakhir = 1.75262f
        var jawal = 0.94261f
        var jakhir = 4f
        var raj = 1.94272f
        
        // val legendData: List<LegendLabel> = listOf(LegendLabel(name = ""), LegendLabel(name = "Muharam"), LegendLabel(name = "Safar"), LegendLabel(name = "R. awal"), LegendLabel(name = "R. akhir"))
        val pointsData: List<Point> =
            listOf(Point(0f, 0f), Point(1f, muh), Point(2f, saf), Point(3f, rawal), Point(4f, rakhir), Point(5f, jawal), Point(6f, jakhir), Point(7f, raj))
        val steps = 12
        val xAxisData = AxisData.Builder()
            .axisStepSize(50.dp)
            .axisLabelColor(Color.Green)
            .axisLineColor(Color.Green)
            .backgroundColor(Color.Transparent)
            .steps(pointsData.size - 1)
            .labelData { i -> i.toString() }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(steps)
            .backgroundColor(Color.Transparent)
            .labelAndAxisLinePadding(20.dp)
            .axisLabelColor(Color.Green)
            .axisLineColor(Color.Green)
            .labelData { i ->
                // Add yMin to get the negative axis values to the scale
                val yMin = pointsData.minOf { it.y }
                val yMax = pointsData.maxOf { it.y }
                val yScale = (yMax - yMin) / steps
                ((i * yScale) + yMin).formatToSinglePrecision()
            }.build()
                
       val lineChartData = LineChartData(
           linePlotData = LinePlotData(
               lines = listOf(
                   Line(
                       dataPoints = pointsData,
                       LineStyle(
                           color = Color.Green
                       ),
                       IntersectionPoint(
                           color = Color.Green
                       ),
                       SelectionHighlightPoint(
                           color = Color.Green
                       ),
                       ShadowUnderLine(
                           brush = Brush.verticalGradient(
                               listOf(
                                   Color.Green,
                                   Color.Transparent
                               )
                           ), alpha = 0.3f
                       ),
                       SelectionHighlightPopUp()
                   )
               ),
           ),
           xAxisData = xAxisData,
           yAxisData = yAxisData,
           gridLines = GridLines(),
           backgroundColor = Color.Transparent
           )
        
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Text(text = "hai from compose", color = MaterialTheme.colors.onSurface)
                LineChart(
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                    lineChartData = lineChartData,
                    )
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
