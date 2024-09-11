package com.rspl.rojgaarrakshak.core.interfaces

import com.google.android.gms.maps.model.LatLng

interface LocationListener {
    fun onAddressFetched(address: String )
    fun onLatLngFetched(latLng: LatLng)
}