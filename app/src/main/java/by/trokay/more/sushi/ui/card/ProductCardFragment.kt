package by.trokay.more.sushi.ui.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.trokay.more.sushi.R
import by.trokay.more.sushi.databinding.ProductCardFragmentBinding
import by.trokay.more.sushi.databinding.ProductItemBinding
import by.trokay.more.sushi.domain.product.Product
import coil.load
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductCardFragment : Fragment() {

    private var _binding: ProductCardFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CardViewModel>()

    val product by lazy { viewModel.getProduct(ProductCardFragmentArgs.fromBundle(arguments!!).itemId) }
    private var prodAmount = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductCardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        binding.apply {

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            product?.let { it ->
                productImage.load(it.image) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder)
                }

                title.text = it.title
                productDescription.text = it.description
                productWeight.text = it.constitution.joinToString(", ")
                productPrice.text = resources.getString(R.string.product_price_template, it.price)
                amount.text = prodAmount.toString()

                changeButtons(prodAmount, binding)

                setListeners()
            }
        }
    }

    private fun setListeners() {
        binding.apply {

            incrementAmount.setOnClickListener { _ ->
                prodAmount++
                changeButtons(prodAmount, binding)
                amount.text = prodAmount.toString()
            }

            decrementAmount.setOnClickListener { _ ->
                prodAmount--
                changeButtons(prodAmount, binding)
                amount.text = prodAmount.toString()
            }

            addToCartButton.setOnClickListener { _ ->
                product?.let { it ->
                    it.amount += prodAmount
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun changeButtons(amount: Int, binding: ProductCardFragmentBinding) {
        when {
            amount <= 0 -> {
                binding.decrementAmount.isEnabled = false
                binding.incrementAmount.isEnabled = true
            }
            amount + (product?.amount ?: 0) >= 99 -> {
                binding.incrementAmount.isEnabled = false
                binding.decrementAmount.isEnabled = true
            }
            else -> {
                binding.incrementAmount.isEnabled = true
                binding.decrementAmount.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}