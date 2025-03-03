package leakcanary

import android.app.Activity
import android.app.Application
import leakcanary.internal.friendly.noOpDelegate

/**
 * Expects activities to become weakly reachable soon after they receive the [Activity.onDestroy]
 * callback.
 */
class ActivityWatcher(
  private val application: Application,
  private val deletableObjectReporter: DeletableObjectReporter
) : InstallableWatcher {

  // Kept for backward compatibility.
  constructor(
    application: Application,
    reachabilityWatcher: ReachabilityWatcher
  ) : this(application, reachabilityWatcher.asDeletableObjectReporter())

  private val lifecycleCallbacks =
    object : Application.ActivityLifecycleCallbacks by noOpDelegate() {
      override fun onActivityDestroyed(activity: Activity) {
        deletableObjectReporter.expectDeletionFor(
          activity, "${activity::class.java.name} received Activity#onDestroy() callback"
        )
      }
    }

  override fun install() {
    application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
  }

  override fun uninstall() {
    application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
  }
}
