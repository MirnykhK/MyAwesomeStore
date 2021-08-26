package ru.adminmk.myawesomestore.view.main_page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.adminmk.myawesomestore.R
import ru.adminmk.myawesomestore.databinding.FragmentMainBinding
import ru.adminmk.myawesomestore.view.main_page.adapter_delegates.productDelegate
import ru.adminmk.myawesomestore.view.main_page.adapter_delegates.saleDelegate
import ru.adminmk.myawesomestore.view.HostContract
import ru.adminmk.myawesomestore.view.ParentFragment
import ru.adminmk.myawesomestore.viewmodel.main_page.MainFragmentViewModel
import ru.adminmk.myawesomestore.viewmodel.main_page.MainPageViewModelSavedStateFactory
import javax.inject.Inject

class MainFragment : ParentFragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private val saleDelegateAdapter by lazy { ListDelegationAdapter(saleDelegate()) }
    private val newDelegateAdapter by lazy { ListDelegationAdapter(productDelegate()) }

    private var bigBannerHeight: Int? = null

    @Inject
    lateinit var viewModelSavedStateProviderFactory: MainPageViewModelSavedStateFactory.ProviderFactory

    override val localViewModel: MainFragmentViewModel by viewModels {
        viewModelSavedStateProviderFactory.create(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onResume() {
        super.onResume()

        localViewModel.loadRemoteData()
    }

    override fun onPause() {
        super.onPause()

        localViewModel.cancelJob()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saleContainer.headerTextView.text = resources.getString(R.string.sale_header_main_page)
        initSaleRecycler()

        binding.newContainer.headerTextView.text = resources.getString(R.string.new_product_main_page)
        initNewRecycler()

        binding.toolbar.doOnPreDraw {
            bigBannerHeight = binding.toolbar.height
        }

        binding.scrollContainer.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            bigBannerHeight?.let {
                if (oldScrollY <= it && scrollY > it) {
                    setupHostStatusBarTheme(true)
                }
                if (oldScrollY >= it && scrollY < it) {
                    setupHostStatusBarTheme(localViewModel.getBigBanner().isLightTheme)
                }
            }

            collapseBottomToolbarOnFirstScroll(oldScrollY)
        }

        initMainData()
    }

    @SuppressLint("NotifyDataSetChanged") // for sketch only
    private fun initMainData() {
        localViewModel.isDataAvailable.observe(
            viewLifecycleOwner,
            Observer
            { isDataAvailable ->
                if (isDataAvailable) {
                    val bigBanner = localViewModel.getBigBanner()

                    binding.bigBannerImageView.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), bigBanner.pic)!!
                    )
                    binding.bigBannerTextView.text = bigBanner.text

                    binding.saleContainer.contentTextView.text = localViewModel.getSaleContentString()
                    saleDelegateAdapter.items = localViewModel.getListOSaleItems()
                    saleDelegateAdapter.notifyDataSetChanged()
                    binding.saleContainer.productRecycler.isVisible = saleDelegateAdapter.items.size > 0

                    newDelegateAdapter.items = localViewModel.getListONewItems()
                    newDelegateAdapter.notifyDataSetChanged()
                    binding.newContainer.productRecycler.isVisible = newDelegateAdapter.items.size > 0
                    binding.newContainer.contentTextView.text = localViewModel.getNewContentString()

                    if (true) {
                        // backstack is not clear - it is not launch
                        setupHostStatusBarTheme(bigBanner.isLightTheme)
                    }

                    activity?.let {
                        with(it as HostContract) {
                            performAnimation()
                        }
                    }
                }
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        localViewModel.saveCache(outState)
    }

    private fun initSaleRecycler() {
        binding.saleContainer.productRecycler.adapter = saleDelegateAdapter
        binding.saleContainer.productRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.saleContainer.productRecycler.setupOnScrollListener()
    }

    private fun initNewRecycler() {
        binding.newContainer.productRecycler.adapter = newDelegateAdapter
        binding.newContainer.productRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.newContainer.productRecycler.setupOnScrollListener()
    }

    private fun setupHostStatusBarTheme(isLight: Boolean) {
        activity?.let {
            (it as HostContract).setupHostStatusBarTheme(isLight)
        }
    }

    override fun selectBottomToolbarNavCommand(fragmentDestinationId: Int): NavDirections? {
        return when (fragmentDestinationId) {
            R.id.categoriesFragment -> MainFragmentDirections.actionMainFragmentToCategoriesFragment()
            else -> null
        }
    }
}
