package edu.hkbu.comp.androidhw.ui.redeemedCoupons

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.hkbu.comp.androidhw.Network
import edu.hkbu.comp.androidhw.R
//import edu.hkbu.comp.androidhw.cookie
import edu.hkbu.comp.androidhw.data.Coupon
import edu.hkbu.comp.androidhw.data.User
import edu.hkbu.comp.androidhw.ui.coins.CoinRecyclerViewAdapter
import edu.hkbu.comp.androidhw.ui.coins.RestByCoinRecyclerViewAdapter
import edu.hkbu.comp.androidhw.ui.coupons.CouponRecyclerViewAdapter
import edu.hkbu.comp.androidhw.ui.redeemedCoupons.placeholder.PlaceholderContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

/**
 * A fragment representing a list of Items.
 */
class redeemedcouponfragment : Fragment() {

    private var columnCount = 1
    private var currentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (currentView == null) {
            val recyclerView = inflater.inflate(
                R.layout.fragment_redeemedcoupon_list,
                container,
                false
            ) as RecyclerView
            relaodData(recyclerView)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                true
            )
            currentView = recyclerView
        }

        return currentView
    }

    private fun relaodData(recyclerView: RecyclerView) {
        val settings = context?.getSharedPreferences("userData",0)

        var cookie: String? = settings?.getString("cookie", "")

        println("Redeened Cookie: $cookie")

        val client = OkHttpClient()
        val url = URL("${resources.getString(R.string.baseURL)}/redeem")

        val request = cookie?.let {
            Request.Builder()
                .url(url)
                .get()
                .addHeader("Cookie", cookie)
                .build()
        }

        val thread = Thread {
            val response = request?.let { client.newCall(it).execute() }

            val responseBody = response?.body!!.string()

          //  val ClientObj = JSONObject(responseBody)

         //   val CouponArray = ClientObj.getJSONArray("clients")

            val mapperAll = ObjectMapper()

            val objData = mapperAll.readTree(responseBody)["clients"].toString()

            val coupons = Gson().fromJson<List<Coupon>>(objData, object: TypeToken<List<Coupon>>(){}.type)

            println("coupons: $coupons")

            CoroutineScope(Main).launch {
                recyclerView.adapter = redeemedCouponRecyclerViewAdapter(coupons)
            }

        }

        thread.start()

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            redeemedcouponfragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}