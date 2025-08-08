package com.example.new_feature_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.new_feature_android.models.UserInfo
import com.example.new_feature_android.ui.theme.New_feature_androidTheme
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class MainActivity : ComponentActivity() {
    companion object {
        private const val ENGINE_ID = "new_feature_flutter_module"
        private const val CHANNEL = "com.example.new_feature_android/custom1"
    }

    private var methodChannel: MethodChannel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 앱을 시작하자마자 플러터 엔진 초기화
        createFreshFlutterEngine()

        setContent {
            New_feature_androidTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CenterScreen()
                }
            }
        }
    }

    private fun createFreshFlutterEngine(): FlutterEngine {
        // delete previous FlutterEngine
        FlutterEngineCache.getInstance().get(ENGINE_ID)?.destroy()
        FlutterEngineCache.getInstance().remove(ENGINE_ID)

        // create a new FlutterEngine
        val flutterEngine = FlutterEngine(this)

        // 플러터 화면의 경로를 네비게이션으로 지정하도록 변경
        flutterEngine.navigationChannel.setInitialRoute("/custom1")

        // MethodChannel 설정
        setupMethodChannel(flutterEngine)

        // Dart run
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Store the FlutterEngine in the cache
        FlutterEngineCache.getInstance().put(ENGINE_ID, flutterEngine)

        return flutterEngine
    }

    private fun setupMethodChannel(flutterEngine: FlutterEngine) {
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)

        methodChannel?.setMethodCallHandler { call, result ->
            // 2개의 함수로 나눠서 서로 다른 데이터를 보내보도록 한다.
            when (call.method) {
                "getUserInfo" -> {
                    try {
                        // 더미 사용자 정보 생성
                        val userList = UserInfo.getDummyUserList()
                        val userMaps = userList.map { it.toMap() }
                        result.success(userMaps)
                    } catch (e: Exception) {
                        result.error("ERROR", "Failed to get user info", e.message)
                    }
                }
                "getCurrentUser" -> {
                    try {
                        // 현재 사용자 정보 (첫 번째 사용자를 현재 사용자로 가정)
                        val currentUser = UserInfo.getDummyUserList().first()
                        result.success(currentUser.toMap())
                    } catch (e: Exception) {
                        result.error("ERROR", "Failed to get current user", e.message)
                    }
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun openFlutterScreen() {
        // 캐싱된 플러터 엔진을 사용하도록 변경
        val intent = FlutterActivity
            .withCachedEngine(ENGINE_ID)
            .build(this)

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        FlutterEngineCache.getInstance().get(ENGINE_ID)?.destroy()
        FlutterEngineCache.getInstance().remove(ENGINE_ID)
    }

    @Composable
    fun CenterScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Android",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                Button(
                    onClick = {
                        // Flutter 화면을 열기 전에 사용자 정보를 전송
                        openFlutterScreen()
                    }
                ) {
                    Text("Move to Flutter")
                }
            }
        }
    }
}
