package edu.hkbu.comp.androidhw.ui.coins

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.data.Coupon
import edu.hkbu.comp.androidhw.databinding.FragmentCoinItemBinding
import edu.hkbu.comp.androidhw.databinding.FragmentMallItemBinding
import edu.hkbu.comp.androidhw.ui.malls.RestByMallRecyclerViewAdapter

class RestByCoinRecyclerViewAdapter(
    private val values: List<Coupon>
): RecyclerView.Adapter<RestByCoinRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCoinItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coupon = values[position]
        holder.restNameView.text = coupon.name
        holder.mallNameView.text = coupon.mall

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCoinItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val restNameView: TextView = binding.restName
        val mallNameView: TextView = binding.mallName

        init {
            binding.root.setOnClickListener{
                val coupon = values[bindingAdapterPosition]

                it.findNavController().navigate(
                    R.id.action_coinFragment_to_couponDetailFragment,
                    bundleOf(
                        Pair("image", coupon.image),
                        Pair("name", coupon.name),
                        Pair("title", coupon.title),
                        Pair("mall", coupon.mall),
                        Pair("coins", coupon.coins),
                        Pair("validtill", coupon.validtill)
                    )
                )
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + restNameView.text + "'"
        }
    }

}