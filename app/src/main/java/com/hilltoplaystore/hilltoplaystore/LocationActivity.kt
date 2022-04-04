package com.hilltoplaystore.hilltoplaystore

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.hilltoplaystore.hilltoplaystore.ui.theme.HillToPlaystoreTheme

class LocationActivity : ComponentActivity() {

    // 위치 추적 클라이언트
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // TODO 위치 추적 권한 요청
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // 정확한 위치 권한을 부여 받았을 때
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // 대략적인 위치 권한을 부여 받았을 때
                }
                else -> {
                    // 권한을 부여 받지 않았을 때
                }
            }
        }

        // 요청할 권한 목록
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        // 위치 추적 클라이언트 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 마지막 알려진 위치 받기
        fusedLocationClient.lastLocation  // 혹은 .currentLocation (배터리 사용량은 lastLocation이 더 적음. 정확도는 떨어짐)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // TODO 화면 세팅
                    setContent {
                        HillToPlaystoreTheme {
                            // A surface container using the 'background' color from the theme
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background,
                            ) {

                                Location(this, LatLng(location.latitude, location.longitude))

                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "위치 정보 수신 불가", Toast.LENGTH_SHORT).show()
                }
            }
    }


}


@Composable
fun Location(context: Context, lastLocation: LatLng) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(lastLocation, 10f)
        }
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                position = lastLocation,
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 12.dp, 0.dp, 0.dp),
            onClick = {
                // TODO 위치 전송
                Toast.makeText(context, "클릭", Toast.LENGTH_SHORT).show()
            },
        ) {
            Text(context.getString(R.string.location_send))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview5() {
    HillToPlaystoreTheme {
        Location(LocalContext.current, LatLng(1.35, 103.87))
    }
}