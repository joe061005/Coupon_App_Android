package edu.hkbu.comp.androidhw

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import edu.hkbu.comp.androidhw.data.User
import edu.hkbu.comp.androidhw.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_coupon_detail.view.*
import kotlinx.android.synthetic.main.fragment_coupon_item.view.*

var user = User(-1, "Guest", "Guest", -1)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.couponFragment,R.id.mallFragment,R.id.coinFragment,R.id.userFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController(R.id.nav_host_fragment_activity_main).popBackStack()
        return true
    }

    /*

    fun onRedeemButtonClick(view: View){
        println("Redeem clicked")


    }

    fun onAddressButtonClick(view: View){
        println("address clicked")

        var mall = view.coinTextView.text

        println(mall)

        findNavController(view.id).navigate(R.id.action_couponDetailFragment_to_mapsFragment,
            bundleOf(
                Pair("latitude", 1.1),
                Pair("longitude", 1.2)

            )
        )
    }

     */
}