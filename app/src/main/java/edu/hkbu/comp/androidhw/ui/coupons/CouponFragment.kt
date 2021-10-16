package edu.hkbu.comp.androidhw.ui.coupons

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.hkbu.comp.androidhw.Network
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.data.Coupon
import edu.hkbu.comp.androidhw.ui.coupons.placeholder.PlaceholderContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class CouponFragment : Fragment() {

    private var columnCount = 1

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
        val recyclerView = inflater.inflate(R.layout.fragment_coupon_list, container, false) as RecyclerView


        reloadData(recyclerView)
        return recyclerView
    }

    private fun reloadData(recyclerView: RecyclerView){
        /*val image = resources.getStringArray(R.array.Image)
        val name = resources.getStringArray(R.array.name)
        val title = resources.getStringArray(R.array.title)
        val coin = resources.getIntArray(R.array.coin)
        val coupon = mutableListOf<Coupon>()
        for(i in 0..(image.size-1))
            coupon.add(Coupon(image[i],name[i],title[i],coin[i]))
        recyclerView.adapter = CouponRecyclerViewAdapter(coupon)*/
        val COUPON_URL = resources.getString(R.string.baseURL)
        CoroutineScope(IO).launch{
            try{
                val json = Network.getTextFromNetwork(COUPON_URL)
                // convert the string json into List<Coupon>
                val coupons = Gson().fromJson<List<Coupon>>(json, object: TypeToken<List<Coupon>>(){}.type)
                CoroutineScope(Main).launch {
                    recyclerView.adapter = CouponRecyclerViewAdapter(coupons)
                }
            } catch(e: Exception){
                Log.d("CouponListFragment", "reloadData: ${e}")
            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CouponFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}