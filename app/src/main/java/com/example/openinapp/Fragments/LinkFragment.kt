package com.example.openinapp.Fragments

import Data
import ObjectClasses
import RecentLink
import TopLink
import android.app.VoiceInteractor.Request
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.openinapp.Adapters.Recent_linksAdapter
import com.example.openinapp.Adapters.TopLinkRvAdapter
import com.example.openinapp.R
import com.example.openinapp.databinding.FragmentLinkBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.security.KeyStore
import java.util.Calendar


class LinkFragment : Fragment() {

private lateinit var chart: LineChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLinkBinding.inflate(inflater, container, false)
        val cal = Calendar.getInstance()
        val timeOfDay = cal.get(Calendar.HOUR_OF_DAY)
        when (timeOfDay){
            in 0..11 -> {binding.greetings.text = "Good Morning"}
            in 12..16 -> {binding.greetings.text = "Good Afternoon"}
            in 17..20 -> {binding.greetings.text = "Good Evening"}
            in 21..23 -> {binding.greetings.text = "Good Night"}
        }
        val xvalue = arrayOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
        chart = binding.chart
        chart.description.setPosition(150f,15f);
        chart.description.text = "Overview"
        chart.axisRight.setDrawLabels(false)
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xvalue)
        xAxis.setLabelCount(8)
        xAxis.granularity = 1f

        val yAxis = chart.axisLeft
        yAxis.axisMaximum = 100f
        yAxis.axisMinimum = 0f
       yAxis.axisLineColor = Color.BLACK
        yAxis.axisLineWidth = 0f
        yAxis.setLabelCount(5)

        val entries = ArrayList<Entry>()
        entries.add(Entry(0f,20f))
        entries.add(Entry(1f,25f))
        entries.add(Entry(2f,30f))
        entries.add(Entry(3f,50f))
        entries.add(Entry(4f,80f))
        entries.add(Entry(5f,75f))
        entries.add(Entry(6f,100f))
        entries.add(Entry(7f,50f))
        entries.add(Entry(8f,25f))
        entries.add(Entry(9f,100f))

        val lineDataSet = LineDataSet(entries,"Touch counts")
//        lineDataSet.color = R.color.blue
        lineDataSet.lineWidth = 2f

        lineDataSet.color = Color.BLUE
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawValues(false)
        val LineData = LineData(lineDataSet)
        chart.data = LineData
        chart.invalidate()
        val datalist = ArrayList<TopLink>()

        val url = "https://api.inopenapp.com/api/v1/dashboardNew"
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("https://api.inopenapp.com/api/v1/dashboardNew")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI")
            .addHeader("Cookie", "connect.sid=s%3AjhoOf9sm8GMX_-fdXkb8oIngXkLNb4xh.bdHGcGZ98S7py1g6sQ0CkxFhEdzBxdqeMP8Ca4eCP74")
            .build()
        var stringRequest = object:StringRequest(com.android.volley.Request.Method.GET,url, Response.Listener{
            var gson = GsonBuilder().create()

            var data = gson.fromJson(it,ObjectClasses::class.java)
            val topLinks = data.data.top_links
            binding.countsvalue.text = data.today_clicks.toString()
            binding.cityvalue.text = data.top_location.toString()
            binding.communityvalue.text = data.top_source.toString()
            binding.timevalue.text = data.startTime.toString()
            if(binding.cityvalue.text.isNullOrEmpty()){
                binding.cityvalue.text = "No Data"
            }
            if(binding.communityvalue.text.isNullOrEmpty()){
                binding.communityvalue.text = "No Data"
            }
            if(binding.timevalue.text.isNullOrEmpty()){
                binding.timevalue.text = "No Data"
            }

            val recentlinks:List<RecentLink> = data.data.recent_links
            var boolea  = true;
            binding.recentLinksbutn.setOnClickListener{
                binding.recentLinksbutn.setBackgroundColor(Color.parseColor("#0E6FFF"))
                binding.recentLinksbutn.setTextColor(Color.WHITE)
                binding.toplinkbutn.setBackgroundColor(Color.TRANSPARENT)
                binding.toplinkbutn.setTextColor(Color.GRAY)
                val adapter = Recent_linksAdapter(requireContext(),recentlinks)
                binding.linksdatarv.layoutManager = LinearLayoutManager(requireContext())
                binding.linksdatarv.adapter = adapter
                boolea = false
            }
            binding.toplinkbutn.setOnClickListener {
                binding.toplinkbutn.setBackgroundColor(Color.parseColor("#0E6FFF"))
                binding.toplinkbutn.setTextColor(Color.WHITE)
                binding.recentLinksbutn.setBackgroundColor(Color.TRANSPARENT)
                binding.recentLinksbutn.setTextColor(Color.GRAY)
                val adapter = TopLinkRvAdapter(requireContext(),topLinks)
                binding.linksdatarv.layoutManager = LinearLayoutManager(requireContext())
                binding.linksdatarv.adapter = adapter
                boolea = true
            }
            binding.progressBar.isVisible = false
            if(boolea){
                val adapter = TopLinkRvAdapter(requireContext(), topLinks)
                binding.linksdatarv.layoutManager = LinearLayoutManager(requireContext())
                binding.linksdatarv.adapter = adapter
            }

        },
            {it->

                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                val code = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
                headers["Authorization"] = "Bearer $code"
                // Log headers for debugging
                Log.d("Headers", headers.toString())
                return headers

            }
        }
        var volley = Volley.newRequestQueue(requireContext())
        volley.add(stringRequest)
        return binding.root
    }

}