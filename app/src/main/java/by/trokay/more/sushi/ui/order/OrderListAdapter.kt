package by.trokay.more.sushi.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.trokay.more.sushi.R
import by.trokay.more.sushi.domain.product.Product
import by.trokay.more.sushi.databinding.ProductItemOrderBinding
import coil.load

class OrderListAdapter(private val onItemClicked: (Product) -> Unit) :
    ListAdapter<Product, OrderListAdapter.OrderViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ProductItemOrderBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            ),
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class OrderViewHolder(
        private var binding: ProductItemOrderBinding,
        private val onItemClicked: (Product) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {

            binding.apply {
                productTitle.text = item.title
                amount.text = item.amount.toString()
                productPrice.text = item.price.toString() + " BYN"
                productDescription.text = item.description
                incrementAmount.setOnClickListener {
                    if (item.amount < 99) {
                        item.amount++
                        amount.text = item.amount.toString()
                    }
                    onItemClicked(item)
                }
                decrementAmount.setOnClickListener {
                    if (item.amount > 0) {
                        item.amount--
                        amount.text = item.amount.toString()
                    }
                    onItemClicked(item)
                }

                imageView.load(item.image) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}