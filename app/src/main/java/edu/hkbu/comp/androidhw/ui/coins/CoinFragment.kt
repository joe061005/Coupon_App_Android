package edu.hkbu.comp.androidhw.ui.coins

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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.hkbu.comp.androidhw.Network
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.data.Coupon
import edu.hkbu.comp.androidhw.ui.coins.placeholder.PlaceholderContent
import edu.hkbu.comp.androidhw.ui.malls.RestByMallRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * A fragment representing a list of Items.
 */
class CoinFragment : Fragment() {

    private var columnCount = 1
    private var currentView: View? = null
    private var showBackButton = false

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
        if(currentView == null) {
            val view = inflater.inflate(R.layout.fragment_coin_list, container, false)

            // Set the adapter
            if (view is RecyclerView) {
                with(view) {
                    layoutManager = when {
                        columnCount <= 1 -> LinearLayoutManager(context)
                        else -> GridLayoutManager(context, columnCount)
                    }
                    val coinRange = arguments?.getString("coinRange")
                    if (coinRange == null) {
                        adapter = CoinRecyclerViewAdapter(
                            resources.getStringArray(R.array.coinRange).toList()
                        )
                        showBackButton = false
                    } else {

                        val URL = "${resources.getString(R.string.baseURL)}/Coins/$coinRange"

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val json = Network.getTextFromNetwork(URL)
                                val coupons = Gson().fromJson<List<Coupon>>(
                                    json,
                                    object : TypeToken<List<Coupon>>() {}.type
                                )
                                println(json)
                                CoroutineScope(Dispatchers.Main).launch {
                                    adapter = RestByCoinRecyclerViewAdapter(coupons)
                                }


                            } catch (e: Exception) {
                                Log.d("MallFragment", "loadData: ${e}")
                            }
                        }
                        showBackButton = true

                    }
                }
            }
            currentView = view
        }
        if (showBackButton) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                true
            )
        }
        return currentView
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CoinFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}