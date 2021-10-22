package edu.hkbu.comp.androidhw.ui.coupons

import android.content.Context
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.data.Coupon
import edu.hkbu.comp.androidhw.databinding.FragmentCouponItemBinding

import edu.hkbu.comp.androidhw.ui.coupons.placeholder.PlaceholderContent.PlaceholderItem
//import edu.hkbu.comp.androidhw.ui.coupons.databinding.FragmentCouponItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class CouponRecyclerViewAdapter(
    private val values: List<Coupon>
) : RecyclerView.Adapter<CouponRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCouponItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.restaurantTextView.text = item.name
        holder.detailTextView.text = item.title
        holder.coinTextView.text = "coins: ${item.coins}"
        //Picasso.get().setLoggingEnabled(true)
        Picasso.get().load(item.image).into(holder.couponImageView)

    }

    override fun getItemCount(): Int = values.size
    inner class ViewHolder(binding: FragmentCouponItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val couponImageView: ImageView = binding.couponImageView
        val restaurantTextView: TextView = binding.restaurantTextView
        val detailTextView: TextView = binding.detailTextView
        val coinTextView: TextView = binding.coinTextView

        init {
            binding.root.setOnClickListener{
                val coupon = values[bindingAdapterPosition]

                it.findNavController().navigate(
                    R.id.action_couponFragment_to_couponDetailFragment,
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
            return super.toString() + " '" + detailTextView.text + "'"
        }
    }

}