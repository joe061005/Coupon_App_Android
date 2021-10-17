package edu.hkbu.comp.androidhw.ui.malls

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import edu.hkbu.comp.androidhw.R
import edu.hkbu.comp.androidhw.databinding.FragmentMallItemBinding

import edu.hkbu.comp.androidhw.ui.malls.placeholder.PlaceholderContent.PlaceholderItem
import org.w3c.dom.Text

//import edu.hkbu.comp.androidhw.ui.malls.databinding.FragmentMallItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MallRecyclerViewAdapter(
    private val values: List<String>
) : RecyclerView.Adapter<MallRecyclerViewAdapter.ViewHolder>() {

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
        holder.mallView.text = values[position]

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMallItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val mallView: TextView = binding.MallName

        init{
            binding.root.setOnClickListener{
                it.findNavController().navigate(
                    R.id.action_mallFragment_self,
                    bundleOf(Pair("mall", mallView.text))
                )
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + mallView.text + "'"
        }
    }

}