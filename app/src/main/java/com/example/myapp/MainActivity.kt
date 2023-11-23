
package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp.databinding.ActivityMainBinding
import android.content.Intent
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapp.databinding.ActivitySettingsBinding
import com.example.myapp.R
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.material3.Text
import android.graphics.Typeface
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.*
import com.example.myapp.theme.MyAppTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import co.yml.charts.common.model.Point
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.LineChart
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
/*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jaikeerthick.composable_graphs.composables.LineGraph
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.style.*
import com.jaikeerthick.composable_graphs.color.*
*/


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
        var jakhir = -1.901923f
        var raj = 1.94272f
        var sya = 2.589f
        var ram = 1.7293f
        var syw = 3.1839f
        var zqh = 2.74628f
        var zhj = -1.3791f
        
        
        // ycharts
        val pointsData: List<Point> =
            listOf(
                Point(0f, 0f),
                Point(1f, muh),
                Point(2f, saf),
                Point(3f, rawal),
                Point(4f, rakhir),
                Point(5f, jawal),
                Point(6f, jakhir),
                Point(7f, raj),
                Point(8f, sya),
                Point(9f, ram),
                Point(10f, syw),
                Point(11f, zqh),
                Point(12f, zhj),
                )
        
           
           
        /* LineGraph
        val style2 = LineGraphStyle(
                    visibility = LinearGraphVisibility(
                        isHeaderVisible = true,
                        isYAxisLabelVisible = true,
                        isCrossHairVisible = true,
                        isGridVisible = true
                    ),
                    colors = LinearGraphColors(
                        lineColor = GraphAccent2,
                        pointColor = GraphAccent2,
                        clickHighlightColor = PointHighlight2,
                        fillGradient = Brush.verticalGradient(
                            listOf(Gradient3, Gradient2)
                        )
                    ),
                    yAxisLabelPosition = LabelPosition.LEFT
                )
                */
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyAppTheme {
                    /* composable graphs
                val clickedValue: MutableState<Pair<Any, Any>?> =
                    remember { mutableStateOf(null) }
                    
                LineGraph(
                    xAxisData = listOf("Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat").map {
                    GraphData.String(it)
                    }, // xAxisData : List<GraphData>, and GraphData accepts both Number and String types
                    yAxisData = listOf(200, 40, 60, 450, 700, 30, 50),
                    style = style2,
                    onPointClicked = {
                        clickedValue.value = it
                    },
                )
                clickedValue.value?.let {
                    Row(
                        modifier = Modifier
                            .padding(all = 25.dp)
                    ) {
                        Text(text = "Value: ", color = Color.Gray)
                        Text(
                            text = "${it.first}, ${it.second}",
                            color = GraphAccent2,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                */
                val steps = 12
        val xAxisData = AxisData.Builder()
            .axisStepSize(50.dp)
            .axisLabelColor(color = MaterialTheme.colorScheme.primary)
            .axisLineColor(lineColor = MaterialTheme.colorScheme.primary)
            .backgroundColor(Color.Transparent)
            .steps(pointsData.size - 1)
            .labelData { i -> i.toString() }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(steps)
            .backgroundColor(Color.Transparent)
            .labelAndAxisLinePadding(20.dp)
            .axisLabelColor(color = MaterialTheme.colorScheme.primary)
            .axisLineColor(lineColor = MaterialTheme.colorScheme.primary)
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
                           color = MaterialTheme.colorScheme.primary
                       ),
                       IntersectionPoint(
                           color = MaterialTheme.colorScheme.primary
                       ),
                       SelectionHighlightPoint(
                           color = MaterialTheme.colorScheme.primary
                       ),
                       ShadowUnderLine(
                           brush = Brush.verticalGradient(
                               listOf(
                                   MaterialTheme.colorScheme.primary,
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
                
                Text(text = "hai from compose", color = MaterialTheme.colorScheme.onSurface)
                LineChart(
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                    lineChartData = lineChartData,
                    )
                }
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
