package by.trokay.more.sushi.ui.order

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.trokay.more.sushi.R
import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.data.remote.OrderDto
import by.trokay.more.sushi.databinding.FragmentOrderBinding
import by.trokay.more.sushi.ui.menu.MenuViewModel
import com.google.firestore.v1.StructuredQuery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.DateFormat
import java.text.SimpleDateFormat

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SimpleDateFormat")
    val dateFormatForAPI: DateFormat = SimpleDateFormat("yyyy.MM.dd - hh.mm.ss")

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
        lifecycleScope.launchWhenResumed {
            viewModel.state.collect {
                when {
                    it.menu != null -> {
                        bindUi()

                    }
                }

            }
        }
    }

    private fun bindUi() {
        customAdapter.submitList(viewModel.state.value.menu?.filter { it.amount > 0 })
        calculateAmountAndPrint()

        binding.items.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customAdapter
        }

        binding.button.setOnClickListener {
            var resultString = ""
            val tmp: OrderDto = OrderDto()
            totalCost = 0.0
            resultString += resources.getString(R.string.delimiter_template)
            viewModel.state.value.menu?.filter { it.amount > 0 }?.forEach {
                totalCost += it.amount * it.price
                resultString += resources.getString(
                    R.string.product_template,
                    it.title,
                    it.price,
                    it.amount,
                    it.amount * it.price
                )
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
            resultString += resources.getString(
                R.string.total_cost_template,
                "",
                totalCost
            )
            tmp._2totalCost = resources.getString(
                R.string.total_cost_template,
                "",
                totalCost
            )
            tmp._3clientWishes = binding.etCommentLayout.text.toString()
            tmp._4name = binding.etNameLayout.text.toString()
            tmp._5phone = binding.etNumberLayout.text.toString()
            tmp._6time = dateFormatForAPI.format(Calendar.getInstance().time)
            resultString += resources.getString(R.string.delimiter_template)
            resultString += resources.getString(
                R.string.customer_template,
                binding.etNameLayout.text,
                binding.etNumberLayout.text
            )
            resultString += resources.getString(R.string.delimiter_template)
            resultString += binding.etCommentLayout.text
            resultString += resources.getString(R.string.delimiter_template)
            println(resultString)
            viewModel.sendOrder(tmp).onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Loading!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    is Resource.Success -> {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "NICE!", Toast.LENGTH_LONG).show()
                            viewModel.state.value.menu?.forEach {
                                it.amount = 0
                            }
                            calculateAmountAndPrint()
                        }
                    }
                    is Resource.Error -> {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "BAD!", Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }.launchIn(CoroutineScope(Dispatchers.IO))
        }
    }

    private fun calculateAmountAndPrint() {
        totalCost = 0.0
        viewModel.state.value.menu?.filter { it.amount > 0 }?.forEach {
            totalCost += it.amount * it.price
        }
        binding.totalCost.text = resources.getString(R.string.total_cost, totalCost)
        customAdapter.submitList(viewModel.state.value.menu?.filter { it.amount > 0 })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}