
package {{ cookiecutter.full_package_namespace }}

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    // Switch to AppTheme for displaying the activity
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

  }
}
