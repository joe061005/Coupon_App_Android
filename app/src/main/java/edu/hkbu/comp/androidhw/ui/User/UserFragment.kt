package edu.hkbu.comp.androidhw.ui.User

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.databinding.FragmentUserBinding
import edu.hkbu.comp.androidhw.user

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

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

            }
        }

        binding.loginTextView.setOnClickListener {
            if(user.id == -1){
                findNavController().navigate(
                    R.id.action_userFragment_to_loginFragment
                )
            }else{

            }
        }

        return view
    }
}