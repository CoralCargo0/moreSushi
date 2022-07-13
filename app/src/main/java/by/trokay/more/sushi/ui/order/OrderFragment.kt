package by.trokay.more.sushi.ui.order

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.trokay.more.sushi.R
import by.trokay.more.sushi.common.Constants.dateFormatForAPI
import by.trokay.more.sushi.common.Constants.nameKey
import by.trokay.more.sushi.common.Constants.personalPrefs
import by.trokay.more.sushi.common.Constants.phoneKey
import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.data.remote.OrderDto
import by.trokay.more.sushi.databinding.FragmentOrderBinding
import by.trokay.more.sushi.ui.menu.MenuViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    var totalCost = 0.0
    private val customAdapter = OrderListAdapter {
        calculateAmountAndPrint()
    }

    private val viewModel by activityViewModels<MenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        customAdapter.submitList(viewModel.state.value.menu?.filter { it.amount > 0 })
        calculateAmountAndPrint()

        binding.items.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customAdapter
        }

        val prefs =
            activity?.getSharedPreferences(personalPrefs, Context.MODE_PRIVATE)
        binding.etNameLayout.setText(prefs?.getString(nameKey, ""))
        binding.etNumberLayout.setText(prefs?.getString(phoneKey, ""))

        binding.button.setOnClickListener {
            if (totalCost > 0) {
                if (binding.etNameLayout.text.toString().isNotEmpty()) {
                    if (binding.etNumberLayout.text.toString()
                            .isNotEmpty() && binding.etNumberLayout.text.toString()
                            .startsWith("+375")
                    ) {
                        val tmp = OrderDto()
                        totalCost = 0.0
                        viewModel.state.value.menu?.filter { it.amount > 0 }?.forEach {
                            totalCost += it.amount * it.price
                            tmp._1order.add(
                                resources.getString(
                                    R.string.product_template,
                                    it.title,
                                    it.price,
                                    it.amount,
                                    it.amount * it.price
                                )
                            )
                        }

                        tmp._3clientWishes = binding.etCommentLayout.text.toString()
                        tmp._4name = binding.etNameLayout.text.toString()
                        tmp._5phone = binding.etNumberLayout.text.toString()
                        tmp._6time = dateFormatForAPI.format(Calendar.getInstance().time)

//                        var resultString = ""
//                        resultString += resources.getString(R.string.delimiter_template)
//                        viewModel.state.value.menu?.filter { it.amount > 0 }?.forEach {
//                            totalCost += it.amount * it.price
//                                resultString += resources.getString(
//                                R.string.product_template,
//                                it.title,
//                                it.price,
//                                it.amount,
//                                it.amount * it.price
//                            )
//                        }
//                        resultString += resources.getString(
//                            R.string.total_cost_template,
//                            "",
//                            totalCost
//                        )
//                        tmp._2totalCost = resources.getString(
//                            R.string.total_cost_template,
//                            "",
//                            totalCost
//                        )
//                        resultString += resources.getString(R.string.delimiter_template)
//                        resultString += resources.getString(
//                            R.string.customer_template,
//                            binding.etNameLayout.text,
//                            binding.etNumberLayout.text
//                        )
//                        resultString += resources.getString(R.string.delimiter_template)
//                        resultString += binding.etCommentLayout.text
//                        resultString += resources.getString(R.string.delimiter_template)
//                        println(resultString)


                        viewModel.sendOrder(tmp).onEach { resource ->
                            when (resource) {
                                is Resource.Loading -> {
                                    activity?.runOnUiThread {
                                        Snackbar.make(
                                            it,
                                            getString(R.string.orger_loading),
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                is Resource.Success -> {
                                    activity?.runOnUiThread {
                                        viewModel.state.value.menu?.forEach {
                                            it.amount = 0
                                        }

                                        Snackbar.make(
                                            it,
                                            getString(R.string.orger_sended),
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        calculateAmountAndPrint()
                                    }
                                }
                                is Resource.Error -> {
                                    activity?.runOnUiThread {
                                        Snackbar.make(
                                            it,
                                            getString(R.string.error_sending_order),
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        }.launchIn(CoroutineScope(Dispatchers.IO))
                        val prefs =
                            activity?.getSharedPreferences(personalPrefs, Context.MODE_PRIVATE)
                                ?.edit()
                        prefs?.putString(nameKey, binding.etNameLayout.text.toString())
                        prefs?.putString(phoneKey, binding.etNumberLayout.text.toString())

                        prefs?.apply()
                    } else {
                        binding.etNumberLayout.error = getString(R.string.enter_phone)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.enter_phone),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    binding.etNameLayout.error = getString(R.string.enter_name)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.enter_name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, getString(R.string.ypu_have_no_products), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun calculateAmountAndPrint() {
        totalCost = 0.0

        viewModel.state.value.menu?.filter { it.amount > 0 }?.forEach {
            totalCost += it.amount * it.price
        }

        if (totalCost == 0.0) {
            binding.mainViewGroup.visibility = View.GONE
            binding.defaultViewGroup.visibility = View.VISIBLE
        }

        binding.totalCost.text = resources.getString(R.string.total_cost, totalCost)
        customAdapter.submitList(viewModel.state.value.menu?.filter { it.amount > 0 })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//                    val snackbar = Snackbar.make(
//                        it,
//                        context.resources.getString(
//                            R.string.product_added_to_cart_template,
//                            item.title,
//                            item.amount,
//                            item.price
//                        ),
//                        Snackbar.LENGTH_SHORT
//                    )
//                    val snackbarView = snackbar.view
//                    val params = snackbarView.layoutParams as FrameLayout.LayoutParams
//                    params.gravity = Gravity.TOP
//                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                    snackbarView.layoutParams = params
//                    snackbarView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                    snackbar.show()