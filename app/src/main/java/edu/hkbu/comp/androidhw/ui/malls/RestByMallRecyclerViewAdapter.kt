package edu.hkbu.comp.androidhw.ui.malls

import android.view.LayoutInflater
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import edu.hkbu.comp.androidhw.data.Coupon
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.databinding.FragmentMallItemBinding

class RestByMallRecyclerViewAdapter (
    private val values: List<Coupon>
): RecyclerView.Adapter<RestByMallRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentMallItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coupon = values[position]
        holder.mallView.text = coupon.name

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMallItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mallView: TextView = binding.MallName

        init {
            binding.root.setOnClickListener{
                val coupon = values[bindingAdapterPosition]

                it.findNavController().navigate(
                    R.id.action_mallFragment_to_couponDetailFragment,
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
            return super.toString() + " '" + mallView.text + "'"
        }
    }

}