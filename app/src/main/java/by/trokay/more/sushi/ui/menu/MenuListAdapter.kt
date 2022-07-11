package by.trokay.more.sushi.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.trokay.more.sushi.domain.product.Product
import by.trokay.more.sushi.R
import by.trokay.more.sushi.databinding.ProductItemBinding
import coil.load
import coil.transform.CircleCropTransformation

class MenuListAdapter(private val onItemClicked: (Product) -> Unit) :
    ListAdapter<Product, MenuListAdapter.MenuViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            ),
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class MenuViewHolder(
        private var binding: ProductItemBinding,
        private val onItemClicked: (Product) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
//            itemView.setOnClickListener {
//                onItemClicked(item)
//            }
            binding.apply {
                productTitle.text = item.title
                amount.text = item.amount.toString()
                productPrice.text = item.price.toString() + " BYN"
                incrementAmount.setOnClickListener {
                    if (item.amount < 10) {
                        item.amount++
                        amount.text = item.amount.toString()
                    }
                }
                decrementAmount.setOnClickListener {
                    if (item.amount > 0) {
                        item.amount--
                        amount.text = item.amount.toString()
                    }
                }

                productImage.load(item.image) {
                    crossfade(true)
                    placeholder(R.drawable.progress)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}