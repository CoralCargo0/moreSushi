package by.trokay.more.sushi.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.trokay.more.sushi.R
import by.trokay.more.sushi.databinding.ProductItemBinding
import by.trokay.more.sushi.domain.product.Product
import coil.load

class MenuListAdapter(
    private val makeToast: (String, Int) -> Unit,
    private val onItemClicked: (Product) -> Unit
) :
    ListAdapter<Product, MenuListAdapter.MenuViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            ),
            onItemClicked,
            makeToast
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class MenuViewHolder(
        private var binding: ProductItemBinding,
        private val onItemClicked: (Product) -> Unit,
        private val makeToast: (String, Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            itemView.setOnClickListener {
                onItemClicked(item)
            }
            binding.apply {
                productTitle.text = item.title
                productPrice.text = item.price.toString() + " BYN"

                addToCartButton.setOnClickListener {
                    item.amount++
                    makeToast(
                        item.title,
                        item.amount
                    )
                }

                productImage.load(item.image) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder)
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