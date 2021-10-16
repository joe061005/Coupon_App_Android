package edu.hkbu.comp.androidhw.ui.coupons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import edu.hkbu.comp.androidhw.databinding.FragmentCouponDetailBinding

class CouponDetailFragment : Fragment(){

    private var _binding: FragmentCouponDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCouponDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        val image= arguments?.getString("image")
        val name= arguments?.getString("name")
        val title= arguments?.getString("title")
        val validtill= arguments?.getString("validtill")
        val mall= arguments?.getString("mall")
        val coins= arguments?.getInt("coins")

        Picasso.get().load(image).into(binding.RestImageView)
        binding.RestNameTextView.text = name
        binding.DescTextView.text = title
        binding.DateTextView.text = "Expiry Date: " + validtill
        binding.MallCoinTextView.text = "Mall: " + mall + ", Coins: " + coins +","

        return view
    }
}