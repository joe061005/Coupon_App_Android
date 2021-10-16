package edu.hkbu.comp.androidhw.ui.coupons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import edu.hkbu.comp.androidhw.R
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

        val Addressbutton = binding.AddressButton

        Addressbutton.setOnClickListener{
            var mallArray = resources.getStringArray(R.array.mall)
            var index = 0

            for(i in 0..(mallArray.size-1)){
                if(mallArray[i].equals(mall)){
                    index = i
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

        return view
    }

}