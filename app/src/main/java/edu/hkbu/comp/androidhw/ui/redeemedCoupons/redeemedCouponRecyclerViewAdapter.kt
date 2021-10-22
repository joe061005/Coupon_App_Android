package edu.hkbu.comp.androidhw.ui.redeemedCoupons

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.data.Coupon
import edu.hkbu.comp.androidhw.databinding.FragmentRedeemedcouponBinding

import edu.hkbu.comp.androidhw.ui.redeemedCoupons.placeholder.PlaceholderContent.PlaceholderItem
// import edu.hkbu.comp.androidhw.ui.redeemedCoupons.databinding.FragmentRedeemedcouponBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class redeemedCouponRecyclerViewAdapter(
    private val values: List<Coupon>
) : RecyclerView.Adapter<redeemedCouponRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentRedeemedcouponBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.restNameView.text = item.name
        holder.mallNameView.text = item.mall
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentRedeemedcouponBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val restNameView: TextView = binding.redeemedRestName
        val mallNameView: TextView = binding.redeemedMallName

        init {
            binding.root.setOnClickListener{
                val coupon = values[bindingAdapterPosition]

                it.findNavController().navigate(
                    R.id.action_redeemedcouponfragment_to_couponDetailFragment,
                    bundleOf(
                        Pair("image", coupon.image),
                        Pair("name", coupon.name),
                        Pair("title", coupon.title),
                        Pair("mall", coupon.mall),
                        Pair("coins", coupon.coins),
                        Pair("validtill", coupon.validtill),
                        Pair("id", coupon.id),
                        Pair("quota", coupon.quota)
                    )
                )
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + restNameView.text + "'"
        }
    }

}