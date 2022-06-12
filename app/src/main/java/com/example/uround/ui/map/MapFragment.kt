package com.example.uround.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.uround.databinding.FragmentMapBinding

import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentActivity
import com.example.uround.MainActivity
import com.example.uround.R
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapBinding? = null
    private lateinit var mMap: GoogleMap
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val namePage = activity?.findViewById<View>(R.id.namePage) as TextView
        namePage.setText("Map")

        //val mapFragment = (activity as FragmentActivity).supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //val textView: TextView = binding.textMap
        //dashboardViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = it
        //}
        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(6.5f)
        val adelaideBounds = LatLngBounds(
            LatLng(57.61543315501259, 21.24351523584895),  // SW bounds
            LatLng(59.733994495189506, 28.1987837809604) // NE bounds
        )

        // Add a marker in Sydney and move the camera
        var ivanHouse = LatLng(59.390403,  27.794942)

        mMap.addMarker(MarkerOptions()
            .position(ivanHouse)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .title("Тут живёт Иван Ниуканен"))?.tag = 0

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ivanHouse))

        var ran1 = LatLng(59.0178399414698, 25.186073876393408)
        mMap.addMarker(MarkerOptions()
            .position(ran1)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .title("ran1"))?.tag = 0

        var ran2 = LatLng(58.504764826288884, 25.319972094374165)
        mMap.addMarker(MarkerOptions()
            .position(ran2)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .title("ran2"))?.tag = 0

        var ran3 = LatLng(58.92581453239329, 26.532494846088778)
        mMap.addMarker(MarkerOptions()
            .position(ran3)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .title("ran3"))?.tag = 0

        mMap.setOnMarkerClickListener(this)
        mMap.setLatLngBoundsForCameraTarget(adelaideBounds)
    }
    override fun onMarkerClick(marker: Marker): Boolean {

        // Retrieve the data from the marker.
        val clickCount = marker.tag as? Int
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15F));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14F), 2000, null);




        val b = Bundle()
        //b.putString("id", response.userInfo!!._id)
        b.putString("id", marker.tag as? String)

        val msgIntent = Intent(context, EventPage::class.java)

        msgIntent.putExtras(b)
        msgIntent.putExtra(EventPage.DATA_L, b)
        startActivity(msgIntent)

        // Check if a click count was set, then display the click count.
        clickCount?.let {
            val newClickCount = it + 1
            marker.tag = newClickCount
            Toast.makeText(
                activity,
                "${marker.title} has been clicked $newClickCount times.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}