package edu.hkbu.comp.androidhw.ui.coins

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.databinding.FragmentCoinRangeBinding

import edu.hkbu.comp.androidhw.ui.coins.placeholder.PlaceholderContent.PlaceholderItem
//import edu.hkbu.comp.androidhw.ui.coins.databinding.FragmentCoinItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class CoinRecyclerViewAdapter(
    private val values: List<String>
) : RecyclerView.Adapter<CoinRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCoinRangeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.coinRangeView.text = values[position]
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCoinRangeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val coinRangeView: TextView = binding.coinRange

        init {
            binding.root.setOnClickListener {
                it.findNavController().navigate(
                    R.id.action_coinFragment_self,
                    bundleOf(Pair("coinRange", coinRangeView.text))
                )
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + coinRangeView.text + "'"
        }
    }

}