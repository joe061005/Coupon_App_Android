package edu.hkbu.comp.androidhw.ui.User

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import edu.hkbu.comp.androidhw.R
//import edu.hkbu.comp.androidhw.cookie
import edu.hkbu.comp.androidhw.data.User
import edu.hkbu.comp.androidhw.databinding.FragmentLoginBinding
//import edu.hkbu.comp.androidhw.user
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.CookieManager
import java.net.URL

class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var user: User = User(-1, "Guest", "Guest", -1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

       binding.login.setOnClickListener {
           val username = binding.username.text.toString()
           val password = binding.password.text.toString()

           if(username == "") {
               binding.username.error = "Username required"
               binding.username.requestFocus()
           }else if (password == ""){
               binding.password.error = "Password required"
               binding.password.requestFocus()
           }else{
               if(!post(username,password)){

                   AlertDialog.Builder(context)
                       .setTitle("Error")
                       .setMessage("Invalid username or password")
                       .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                           binding.username.getText().clear()
                           binding.password.getText().clear()
                       })
                       .show()
               }else{
                   AlertDialog.Builder(context)
                       .setTitle("Login successfully")
                       .setMessage("Welcome back, ${user.username}")
                       .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                           findNavController().popBackStack()
                       })
                       .show()



               }
           }

       }
        return view
    }

    fun post(username: String, password: String): Boolean{
        val settings = context?.getSharedPreferences("userData",0)

        val client = OkHttpClient()
        val url = URL("${resources.getString(R.string.baseURL)}/login")

        // var jsonString = "{\"username\": \"${username}\", \"password\": \"${password}\"}"

        val mapperAll = ObjectMapper()
        val jacksonObj = mapperAll.createObjectNode()
        jacksonObj.put("username", username)
        jacksonObj.put("password", password)
        val jacksonString = jacksonObj.toString()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jacksonString.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        var code = 0;

        val thread = Thread{
            val response = client.newCall(request).execute()

            code = response.code

            println("CODEL $code")

            if(code != 401) {

                val responseBody = response.body!!.string()

                val result = Gson().fromJson(responseBody, User::class.java)

                user = result

                println("COOKIE ${response.headers["set-cookie"].toString().split(";")[0]}")

                settings?.edit()?.putString("currentUser", Gson().toJson(result))
                    ?.putString("cookie", response.headers["set-cookie"].toString())
                    ?.putBoolean("isLogin", true)
                    ?.apply()

            }

        }
        thread.start()
        thread.join()

        return code != 401

    }

}