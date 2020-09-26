package ru.mrfrozzen.qrcode.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import ru.mrfrozzen.qrcode.R
import ru.mrfrozzen.qrcode.ui.generate.QRGenerateFragment
import ru.mrfrozzen.qrcode.ui.qrscanner.QRScannerFragment
import ru.mrfrozzen.qrcode.ui.scanner_history.ScannedHistoryFragment

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
