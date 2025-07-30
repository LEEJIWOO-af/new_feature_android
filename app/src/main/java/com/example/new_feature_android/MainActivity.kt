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
import com.example.new_feature_android.ui.theme.New_feature_androidTheme
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class MainActivity : ComponentActivity() {
    companion object {
        private const val ENGINE_ID = "new_feature_flutter_module"
        private const val CHANNEL_NAME = "com.example.module-test/custom1"
    }

    private var methodChannel: MethodChannel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

        // Dart run
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Store the FlutterEngine in the cache
        FlutterEngineCache.getInstance().put(ENGINE_ID, flutterEngine)

        // MethodChannel setup
        methodChannel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL_NAME
        )

        return flutterEngine
    }

    private fun sendMessageToFlutter(message: String) {
        methodChannel?.invokeMethod(
            "getUserToken",
            message,
            object : MethodChannel.Result {
                override fun success(result: Any?) {
                    println("Flutter Response: $result")
                }

                override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                    println("Flutter Error: $errorCode - $errorMessage")
                }

                override fun notImplemented() {
                    println("Flutter not existed")
                }
            }
        )
    }

    private fun openFlutterScreen() {
        createFreshFlutterEngine()

        android.os.Handler(mainLooper).postDelayed({
            sendMessageToFlutter("Android sent data: ${System.currentTimeMillis()}")
        }, 500)

        val intent = FlutterActivity
            .withNewEngine()
            .initialRoute("/custom1")
            .build(this)

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        FlutterEngineCache.getInstance().get(ENGINE_ID)?.destroy()
        FlutterEngineCache.getInstance().remove(ENGINE_ID)
        methodChannel = null
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
                        sendMessageToFlutter("Data from button: ${System.currentTimeMillis()}")
                    }
                ) {
                    Text("動作")
                }

                Button(
                    onClick = {
                        openFlutterScreen()
                    }
                ) {
                    Text("Flutterへ移動")
                }
            }
        }
    }
}
