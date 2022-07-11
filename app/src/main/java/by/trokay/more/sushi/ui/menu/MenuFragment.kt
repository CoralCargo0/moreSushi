package by.trokay.more.sushi.ui.menu

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.trokay.more.sushi.R
import by.trokay.more.sushi.databinding.FragmentMenuBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private var customAdapter = MenuListAdapter {
        TODO("OnClick fun")
    }

    val viewModel by activityViewModels<MenuViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
//        setHasOptionsMenu(true)
        viewModel.loadProducts()
        return binding.root
    }

//    val types: HashMap<String, Int> = hashMapOf(
//        Pair("Всё", 0),
//        Pair("Боксы", 1),
//        Pair("Бургеры", 2),
//        Pair("Роллы", 3),
//        Pair("Картофель", 4),
//        Pair("Десерты", 5),
//        Pair("Напитки", 6),
//        Pair("Кофе и чай", 7),
//        Pair("Соусы", 8),
//        Pair("Хэппи Мил™", 9),
//    )

    var types: HashMap<String, Int> = hashMapOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        binding.items.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = customAdapter
        }

        lifecycleScope.launchWhenResumed {
            viewModel.state.collect {
                when {
                    it.isLoading -> {
                        binding.apply {
                            loadingGroup.visibility = View.VISIBLE
                            viewGroup.visibility = View.INVISIBLE
                        }

                    }
                    !it.error.isNullOrBlank() -> {
                        Toast.makeText(
                            requireContext(),
                            resources.getText(R.string.error_menu),
                            Toast.LENGTH_LONG
                        ).show()
                        binding.apply {
                            loadingGroup.visibility = View.VISIBLE
                            viewGroup.visibility = View.INVISIBLE
                            loadingTv.text = resources.getText(R.string.error_menu)
                        }

                    }
                    else -> {
                        binding.apply {
                            loadingGroup.visibility = View.GONE
                            viewGroup.visibility = View.VISIBLE
                        }
                        bindTabLayoutAndRecycler()
                    }
                }
            }
        }
    }


    private fun bindTabLayoutAndRecycler() {
        types = viewModel.productsTypes
        binding.categoryChooser.apply {
            types.toSortedMap(compareBy { types[it] }).forEach { (t, u) ->
                val tmpTab = newTab().setText(t)
                addTab(tmpTab)
                if (u == viewModel.selectedTab) {
                    selectTab(tmpTab)
                    if (viewModel.selectedTab != 0) {
                        customAdapter.submitList(viewModel.state.value.menu?.filter {
                            it.type == u
                        })
                    } else {
                        customAdapter.submitList(viewModel.state.value.menu)
                    }
                }
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewModel.selectedTab = types[tab!!.text]!!
                    if (viewModel.selectedTab == 0) {
                        customAdapter.submitList(viewModel.state.value.menu)
                    } else {
                        customAdapter.submitList(viewModel.state.value.menu?.filter {
                            it.type == viewModel.selectedTab
                        })

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Nothing to do
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Nothing to do
                }
            })
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.navigation_settings -> {
//                findNavController().navigate(R.id.navigation_settings)
//            }
//        }
//        return true
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.settings_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}