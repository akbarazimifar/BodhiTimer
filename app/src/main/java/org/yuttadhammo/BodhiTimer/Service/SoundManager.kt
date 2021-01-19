package org.yuttadhammo.BodhiTimer.Service

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import androidx.preference.PreferenceManager

class SoundManager(val mContext: Context) {

    private val TAG: String = "SoundManager"
    private var player: MediaPlayer = MediaPlayer()

    private fun play(mUri: Uri) {

        if (mUri != null) {

            try {
                player.reset()
                val currVolume: Int = PreferenceManager.getDefaultSharedPreferences(mContext).getInt("tone_volume", 0)
                if (currVolume != 0) {
                    val log1 = (Math.log((100 - currVolume).toDouble()) / Math.log(100.0)).toFloat()
                    player.setVolume(1 - log1, 1 - log1)
                }
                player.setDataSource(mContext, mUri)
                player.prepare()
                getWakeLock(mContext, player.duration)
                player.isLooping = false
                player.setOnCompletionListener(OnCompletionListener { mp ->
                    Log.v(TAG, "Resetting media player...")
                    mp.reset()
                    mp.release()
                })
                player.start()
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    fun play(mUri: String) {
        play(Uri.parse(mUri))
    }

    private fun getWakeLock(context: Context, dur: Int): WakeLock? {
        // Make sure we can play the sound until it's finished
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wL = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Bodhi:Alarm")
        Log.v(TAG, "Acquiring Wake Lock for $dur")
        wL.acquire((dur + 1000).toLong())
        return wL
    }
}