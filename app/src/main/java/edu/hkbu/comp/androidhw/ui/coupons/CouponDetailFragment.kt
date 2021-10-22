package edu.hkbu.comp.androidhw.ui.coupons

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.data.User
import edu.hkbu.comp.androidhw.databinding.FragmentCouponDetailBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL

class CouponDetailFragment : Fragment(){

    private var _binding: FragmentCouponDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settings = context?.getSharedPreferences("userData",0)

        val isLogin = settings?.getBoolean("isLogin", false)

        var user: User = Gson().fromJson(settings?.getString("DefaultUser", ""), User::class.java)

        if (settings != null) {
            if(settings.getBoolean("isLogin", false)) {
                user = Gson().fromJson(settings.getString("currentUser", ""), User::class.java)
            }
        }

        _binding = FragmentCouponDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        val image= arguments?.getString("image")
        val name= arguments?.getString("name")
        val title= arguments?.getString("title")
        val validtill= arguments?.getString("validtill")
        val mall= arguments?.getString("mall")
        val coins= arguments?.getInt("coins")
        val id = arguments?.getInt("id")
        val quota = arguments?.getInt("quota")

        Picasso.get().load(image).into(binding.RestImageView)
        binding.RestNameTextView.text = name
        binding.DescTextView.text = title
        binding.DateTextView.text = "Expiry Date: $validtill"
        binding.MallCoinTextView.text = "Mall: $mall, Coins: $coins,"

        val addressButton = binding.AddressButton

        addressButton.setOnClickListener{
            var mallArray = resources.getStringArray(R.array.mall)
            var index = 0

            for(i in 0..(mallArray.size-1)){
                if(mallArray[i].equals(mall)){
                    index = i
                    break
                }
            }

           val LatArray = resources.getStringArray(R.array.latitude)
           val LongArray = resources.getStringArray(R.array.longitude)

            findNavController().navigate(
                R.id.action_couponDetailFragment_to_mapsFragment,
                bundleOf(
                    Pair("latitude", LatArray[index]),
                    Pair("longitude", LongArray[index]),
                    Pair("mall", mall)

                )
            )
        }

        val redeemButton = binding.RedeemButton

        redeemButton.setOnClickListener {
             if(!isLogin!!){
                 AlertDialog.Builder(context)
                     .setTitle("This function is not available for guest")
                     .setMessage("You need to login first!")
                     .setPositiveButton("OK", null)
                     .show()
             }else{
                 AlertDialog.Builder(context)
                     .setTitle("Are you sure?")
                     .setMessage("To redeem this coupon?")
                     .setPositiveButton("Yes", DialogInterface.OnClickListener {dialogInterface, i ->
                         if(isRedeemed(id)){
                             AlertDialog.Builder(context)
                                 .setTitle("cannot redeem")
                                 .setMessage("You have redeemed this coupon!")
                                 .setPositiveButton("OK", null)
                                 .show()
                         }else{
                                 if(user.coins < coins!! || quota == 0){
                                     AlertDialog.Builder(context)
                                         .setTitle("cannot redeeem")
                                         .setMessage("No quota/ Not enough coins")
                                         .setPositiveButton("OK", null)
                                         .show()
                                 }else {
                                     if(redeem(id)){
                                         val updatedUser = User(user.id,user.username,user.role,user.coins-coins)

                                         settings.edit().putString("currentUser", Gson().toJson(updatedUser)).apply()

                                         AlertDialog.Builder(context)
                                             .setTitle("redeem successfully")
                                             .setMessage("please use it before the expiry date")
                                             .setPositiveButton("OK", null)
                                             .show()
                                     }else{
                                         AlertDialog.Builder(context)
                                             .setTitle("cannot redeem")
                                             .setMessage("Server error")
                                             .setPositiveButton("OK", null)
                                             .show()
                                     }
                                 }
                         }
                     })
                     .setNeutralButton("No", null)
                     .show()
             }
        }

        return view
    }

    fun isRedeemed(CouponID: Int?): Boolean{
        val settings = context?.getSharedPreferences("userData",0)

        var cookie: String? = settings?.getString("cookie", "")

        val client = OkHttpClient()
        val url = URL("${resources.getString(R.string.baseURL)}/check/$CouponID")

        val request = cookie?.let{
            Request.Builder()
                .url(url)
                .get()
                .addHeader("Cookie", cookie)
                .build()
        }

        var code = 0

        val thread = Thread{
            val response = request?.let { client.newCall(request).execute() }

            if (response != null) {
                code = response.code
            }

        }
        thread.start()
        thread.join()

        if(code == 409){
            return true
        }
        return false

    }

    fun redeem(CouponID: Int?): Boolean {

        val settings = context?.getSharedPreferences("userData",0)
        var cookie: String? = settings?.getString("cookie", "")

        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = "".toRequestBody(mediaType)

        val addRecordURL = URL("${resources.getString(R.string.baseURL)}/user/clients/add/$CouponID")

        val addRecordRequest = cookie?.let{
            Request.Builder()
                .url(addRecordURL)
                .post(body)
                .addHeader("Cookie", cookie)
                .build()
        }

        var addRecordCode = 0

        val addRecordThread = Thread{
            val response = addRecordRequest?.let { client.newCall(addRecordRequest).execute() }

            if (response != null) {
                addRecordCode = response.code
            }

        }



        val updateRecordURL = URL("${resources.getString(R.string.baseURL)}/user/update/$CouponID")

        val updateRecordRequest = cookie?.let{
            Request.Builder()
                .url(updateRecordURL)
                .put(body)
                .addHeader("Cookie", cookie)
                .build()
        }

        var updateRecordCode = 0

        val updateRecordThread = Thread{
            val response = updateRecordRequest?.let { client.newCall(updateRecordRequest).execute() }

            if (response != null) {
                updateRecordCode = response.code
            }

        }

        addRecordThread.start()
        updateRecordThread.start()
        addRecordThread.join()
        updateRecordThread.join()

        if(addRecordCode == 200 && updateRecordCode == 200){
            return true
        }
        return false
    }

}