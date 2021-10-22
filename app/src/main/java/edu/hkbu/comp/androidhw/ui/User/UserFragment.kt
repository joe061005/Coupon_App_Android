package edu.hkbu.comp.androidhw.ui.User

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
import edu.hkbu.comp.androidhw.databinding.FragmentUserBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL

//import edu.hkbu.comp.androidhw.user

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var logout = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        println("UserFragment")

        val nav =  arguments?.getBoolean("Nav")

        if(nav == true){
            findNavController().popBackStack()
        }

        val settings = context?.getSharedPreferences("userData",0)

        var user: User = Gson().fromJson(settings?.getString("DefaultUser", ""), User::class.java)

        if (settings != null) {
            if(settings.getBoolean("isLogin", false)) {
                user = Gson().fromJson(settings.getString("currentUser", ""), User::class.java)
            }
        }

        _binding = FragmentUserBinding.inflate(inflater, container, false)

        Picasso.get().load("https://support.logmeininc.com/assets/images/care/topnav/default-user-avatar.jpg").into(binding.userImageView)
        binding.userNameView.text = user.username
        binding.loginTextView.text = "Logoff / Login"
        binding.couponTextView.text = "My Redeemed Coupons"

        binding.couponTextView.setOnClickListener{
            if(user.id == -1){
                AlertDialog.Builder(context)
                    .setTitle("This function is not available for guest ")
                    .setMessage("You need to login first!")
                    .setPositiveButton("OK", null)
                    .show()
            }else{
               findNavController().navigate(
                   R.id.action_userFragment_to_redeemedcouponfragment
               )
            }
        }

        binding.loginTextView.setOnClickListener {
            if(user.id == -1){
                findNavController().navigate(
                    R.id.action_userFragment_to_loginFragment
                )
            }else{
                Logout()
                if(logout){
                    println("LOGOUT")
                    findNavController().navigate(
                        R.id.action_userFragment_self,
                        bundleOf(Pair("Nav", true))
                    )
                }
            }
        }

        return binding.root
    }

    fun Logout(){
        val settings = context?.getSharedPreferences("userData",0)

        var cookie: String? = settings?.getString("cookie", "")

        val client = OkHttpClient()
        val url = URL("${resources.getString(R.string.baseURL)}/logout")

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = "".toRequestBody(mediaType)

        val request = cookie?.let {
            Request.Builder()
                .url(url)
                .post(body)
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

        if(code == 200){
            settings?.edit()?.putString("cookie", "")?.putBoolean("isLogin", false)
                ?.putString("currentUser", "")?.apply()

            AlertDialog.Builder(context)
                .setTitle("Logout successfully")
                .setMessage("See you next time")
                .setPositiveButton("OK", null)
                .show()

            logout = true

        }else{
            AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("please try to logout later")
                .setPositiveButton("OK", null)
                .show()
        }

    }
}