package com.example.vinciandroidv2.ui.main.clinics

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.vinciandroidv2.R
import com.example.vinciandroidv2.helper.RealmHelper
import com.example.vinciandroidv2.helper.showBaseAlert
import com.example.vinciandroidv2.model.NetworkStateEvent
import com.example.vinciandroidv2.network.respons.Clinic
import com.example.vinciandroidv2.ui.Screens
import com.example.vinciandroidv2.ui.global.BaseFragment
import com.example.vinciandroidv2.ui.main.MainActivity
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationTokenSource
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_clinic_map.*
import kotlinx.android.synthetic.main.view_card_selected_item_on_map.view.*

class ClinicMapFragment : BaseFragment(), OnMapReadyCallback, LocationListener {
    override val layoutRes = R.layout.fragment_clinic_map
    override val TAG = "ClinicMapFragment"

    private var googleMap: GoogleMap? = null
    private var cancellationTokenSource = CancellationTokenSource()

    private val clinicList = RealmHelper.realm?.where(Clinic::class.java)?.findAll() ?: RealmList()
    private val markerClinicList = ArrayList<Marker>()

    var selectedClinicId: Int? = null

    private var selectedMarker: Marker? = null
        set(value) {
            field = value
            updateIconForSelectedMarker((selectedMarker?.tag as? Int) ?: -1)
            moveCameraToSelectedMarker(selectedMarker?.position, 5f)
            showSelectedClinicCard(null != selectedMarker, (selectedMarker?.tag as? Int) ?: -1)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        closeButton?.setOnClickListener { activity?.onBackPressed() }
        (childFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment)
            ?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        showAllClinicsOnMap()
        selectedMarker = markerClinicList.firstOrNull { it.tag == selectedClinicId }

        getCurrentLocation()

        googleMap?.setOnMarkerClickListener { marker ->
            selectedMarker = marker
            true
        }
    }

    private fun showAllClinicsOnMap() {
        clinicList.forEach { clinic ->
            googleMap?.addMarker(
                MarkerOptions()
                    .title(clinic.name)
                    .icon(bitmapFromVector(context, R.drawable.ic_pin_on_map_unselected_57))
                    .position(LatLng(clinic.lat ?: 0.0, clinic.lng ?: 0.0))
            )?.apply {
                tag = clinic.id
                markerClinicList.add(this)
            }
        }
    }


    private fun updateIconForSelectedMarker(selectedMarkerId: Int) {
        markerClinicList.forEach { marker ->
            marker.setIcon(
                bitmapFromVector(
                    context,
                    if (marker.tag == selectedMarkerId) R.drawable.ic_pin_on_map_selected_57
                    else R.drawable.ic_pin_on_map_unselected_57
                )
            )
        }
    }

    private fun moveCameraToSelectedMarker(latLng: LatLng?, zoomTo: Float) {
        if (null != latLng) {
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap?.moveCamera(CameraUpdateFactory.zoomTo(zoomTo))
        }
    }

    private fun showSelectedClinicCard(isVisible: Boolean, clinicId: Int) {
        includeSelectedItemCard?.isVisible = isVisible

        if (!isVisible) return

        RealmHelper.realm?.where(Clinic::class.java)
            ?.equalTo("id", clinicId)
            ?.findFirst()
            ?.let { clinic ->
                includeSelectedItemCard?.apply {
                    Glide.with(this).load(clinic.urlImage).into(this.image)
                    textFieldOne?.text = clinic.name
                    textFieldTwo?.text = clinic.address
                    directionsButton?.apply {
                        direectionsTextField?.text =
                            RealmHelper.getLocalizedText("clinics.map.card.button.directions.text")
                        setOnClickListener {
                            val uri =
                                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${clinic.lat},${clinic.lng}")
                            startActivity(Intent(Intent.ACTION_VIEW, uri))
                        }
                    }
                    contactButton?.apply {
                        contactTextField?.text =
                            RealmHelper.getLocalizedText("clinics.map.card.button.contact.text")
                        setOnClickListener {
                            Screens.Clinics.getContactBottomSheet(clinic.id)
                                .show(childFragmentManager, ContactBottomSheet().TAG)
                        }
                    }
                    this.setOnClickListener {
                        add(
                            R.id.content,
                            Screens.Clinics.getExpandedClinicFragment(clinicId)
                        )
                    }
                    this.cardCloseButton?.setOnClickListener { selectedMarker = null }
                }
            }
    }

    private fun bitmapFromVector(context: Context?, vectorResId: Int): BitmapDescriptor? {

        if (null == context) return null

        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)
        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)
        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onLocationChanged(location: Location) {
        showMyLocationOnMap(location)
    }

    private val resultPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { list ->
        list?.forEach { if (!it.value) return@registerForActivityResult }
        getLocationRequest()
    }

    private fun getCurrentLocation() {
        if (permissionListApproved(context)) getLocationRequest()
        else resultPermissionLauncher.launch(permissionListToCheck)
    }

    @SuppressLint("MissingPermission")
    private fun getLocationRequest() {

        if (!isGPSEnabled(context)) return

        googleMap?.isMyLocationEnabled = true

        (activity as? MainActivity)?.fusedLocationClient
            ?.lastLocation
            ?.addOnSuccessListener { location -> showMyLocationOnMap(location) }
            ?.addOnCanceledListener { }

        (activity as? MainActivity)?.fusedLocationClient
            ?.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
            ?.addOnSuccessListener { location -> showMyLocationOnMap(location) }
            ?.addOnCanceledListener { }
    }

    private fun showMyLocationOnMap(location: Location?) {
        val currentLocation = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        googleMap?.addMarker(
            MarkerOptions()
                .icon(bitmapFromVector(context, R.drawable.ic_current_location_blue_134))
                .anchor(0.5f, 0.5f)
                .position(currentLocation)
        )
        if (null == selectedMarker) moveCameraToSelectedMarker(currentLocation, 5f)
    }

    private fun isGPSEnabled(context: Context?): Boolean {
        val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var internetEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        } catch (ex: Exception) {
        }
        try {
            internetEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
        } catch (ex: Exception) {
        }
        return if (!gpsEnabled && !internetEnabled) {
            showBaseAlert(
                context,
                title = RealmHelper.getLocalizedText("alerts.turn.on.locationservices"),
                positiveText = getString(R.string.ok_label)
            )
            false
        } else true
    }

    /** Permissions **/

    private fun permissionListApproved(context: Context?): Boolean {
        permissionListToCheck.forEach { permission ->
            if (PackageManager.PERMISSION_GRANTED !=
                context?.let { checkSelfPermission(it, permission) }
            ) return false
        }
        return true
    }

    private val permissionListToCheck = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource.cancel()
    }


//    override fun geocodeListLoaded(baseList: BaseList) {
//        if (baseList.plusCode != null) {
//            activity?.supportFragmentManager?.popBackStack()
//            baseList.locationModelList?.firstOrNull()?.let { lm ->
//                callback?.selectedLocationCallback(Address().apply {
//                    placeId = lm.placeId
//                    address = lm.formattedAddress
//                    city = lm.addressComponentList
//                        ?.firstOrNull { it.types?.contains("locality") == true }
//                        ?.longName ?: ""
//                    region = lm.addressComponentList
//                        ?.firstOrNull { it.types?.contains("administrative_area_level_1") == true }
//                        ?.longName ?: ""
//                    lat = lm.geometry?.location?.lat
//                    lng = lm.geometry?.location?.lng
//                }, lm.addressComponentList
//                    ?.firstOrNull { it.types?.contains("locality") == true }
//                    ?.longName ?: "")
//            }
//        }
//    }


    override fun onMessageEvent(event: NetworkStateEvent) {
        super.onMessageEvent(event)
        event
    }
}