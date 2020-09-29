package ru.mrfrozzen.qrcode.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import ru.mrfrozzen.qrcode.R
import ru.mrfrozzen.qrcode.ui.generate.QRGenerateFragment
import ru.mrfrozzen.qrcode.ui.qrscanner.QRScannerFragment
import ru.mrfrozzen.qrcode.ui.scanner_history.ScannedHistoryFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdView : AdView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)

            // TestDevice
        val testDeviceIds = listOf("ECB43FF4FEC70E712026992CA3A91CB4")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val bottom_nav: ChipNavigationBar = findViewById(R.id.bottom_navigation)

        openFragment(QRScannerFragment())
        bottom_nav.setItemSelected(R.id.scaner)

        bottom_nav.setOnItemSelectedListener { id :Int->
            when (id) {
                R.id.scaner -> {
                    val fragment =
                        QRScannerFragment()
                    openFragment(fragment)
                }
                R.id.history -> {
                    val fragment =
                        ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultListType.ALL_RESULT)
                    openFragment(fragment)
                }
                R.id.favorites -> {
                    val fragment =
                        ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultListType.FAVOURITE_RESULT)
                    openFragment(fragment)
                }
                R.id.generate -> {
                    val fragment =
                        QRGenerateFragment()
                    openFragment(fragment)
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
